package eu.ihelp.datacapture.functions.converters;

import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafkaAvro;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.connectors.utils.RowToKafkaAvroConverter;
import eu.ihelp.datacapture.functions.connectors.utils.SetJobFinishedUtil;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterKafkaAvro extends ConverterAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConverterKafkaAvro.class);
    protected final ConverterArgumentsKafkaAvro params;
    protected final RowToKafkaAvroConverter converter;
    protected final KafkaProducer producer;
    
    
    public ConverterKafkaAvro(String jobID, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue, ConverterArgumentsKafkaAvro params) {
        super(jobID, schema, schemaKey, queue);
        this.params = params;
        this.converter = RowToKafkaAvroConverter.createInstance(schema, schemaKey, params.getDatePattern());
        
        
        // Create producer configuration
        Properties props = new Properties(); 
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://" + this.params.getUrl() + ":" + this.params.getPort()); // Set Kafka server ip:port
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class); // Set record key serializer to Avro
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class); // Set record value serializer to Avro
        props.put("schema.registry.url", "http://" + this.params.getUrl() + ":" + this.params.getAvroPort());
        //props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 300000);
        this.producer = new KafkaProducer(props); // Create producer
    }

    @Override
    public void init() {
        System.out.println("I am in the avro kafka now...");
        System.out.println("URL: " + this.params.getUrl());
        System.out.println("Topic: " + this.params.getTopic());
    }

    @Override
    public void close() {
        try (this.producer) {
            this.producer.flush();
            System.out.println("Closing the kafka thing now....");
        }
        
        try {
            SetJobFinishedUtil.setJobAsFinished(jobID);
        } catch(IOException ex) {
            Log.warn("Could not set job:{} as finished. {}:{}", this.jobID, ex.getClass().getName(), ex.getMessage());
        }
    }
    
    @Override
    public void process(DataRowItem dataRowItem) {
        this.converter.convert(dataRowItem);
        
        GenericRecord avroKeyRecord = converter.getAvroKeyRecord();
        GenericRecord avroValueRecord = converter.getAvroValueRecord();
        
        ProducerRecord<Object, Object> record = new ProducerRecord<>(this.params.getTopic(), avroKeyRecord, avroValueRecord);
        try {
            producer.send(record, new Callback() { // Send the record to Kafka server
                @Override //callback after to be called after completion of sending the record
                public void onCompletion(RecordMetadata rm, Exception excptn) {
                    if(excptn==null) {
                        Log.info("sent to topic: {} partition: {} offset: {} at {}", rm.topic(), rm.partition(), rm.offset(), rm.timestamp());
                    } else {
                        Log.error("Error while producing", excptn);
                    }
                }
            }).get(); 
            
            //System.out.println(res.toString());
            
        } catch(SerializationException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
}
