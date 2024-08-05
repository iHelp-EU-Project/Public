package eu.ihelp.datacapture.functions.converters;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import eu.ihelp.datacapture.commons.DataCaptureDatasets;
import eu.ihelp.datacapture.commons.exceptions.JsonSerializationException;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafka;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import eu.ihelp.datacapture.functions.BatchConverterOutput;
import eu.ihelp.datacapture.functions.BatchConverterOutputBuilder;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.converters.transform.HHRMapper;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterKafka extends ConverterAbstract {
    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(ConverterKafkaAvro.class);
    protected final String datasourceID;
    protected final String datasetID;
    protected final ConverterArgumentsKafka params;
    protected final JSONObject confParameters;
    protected final int batchSize;
    protected final KafkaProducer producer;
    protected int count = 0;
    protected BatchConverterOutputBuilder messageBuilder;
    
    protected volatile BatchConverterOutput messageToSend = null;
    
    public ConverterKafka(String jobID, String datasourceID, String datasetID, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue, ConverterArgumentsKafka params, JSONObject confParameters, int batchSize) {
        super(jobID, schema, schemaKey, queue);
        this.datasourceID = datasourceID;
        this.datasetID = datasetID;
        this.params = params;
        this.confParameters = confParameters;
        this.batchSize = batchSize;
        
        this.messageBuilder = BatchConverterOutputBuilder.newInstance(datasourceID, datasetID, schema, schemaKey, confParameters, 1, batchSize, this.jobID);

        // Create producer configuration
        Properties props = new Properties(); 
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://" + this.params.getUrl() + ":" + this.params.getPort()); // Set Kafka server ip:port
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Set record key serializer to String
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Set record value serializer to String
        this.producer = new KafkaProducer(props); // Create producer
    }

    @Override
    public void init() {
        System.out.println("I am in the kafka converter now...");
        System.out.println("URL: " + this.params.getUrl());
        System.out.println("Topic: " + this.params.getTopic());
    }

    @Override
    public void close() {
        try (this.producer) {
            if(!this.messageBuilder.isEmpty()) { 
                addMessageToSend(this.messageBuilder.build());
            } 
            
            sendMessage(messageToSend.toLastBatchMessage());
            
            this.producer.flush();
            System.out.println("Closing the kafka thing now....");
        }
    }

    @Override
    public void process(DataRowItem dataRowItem) {
        if(this.messageToSend!=null) {
            sendMessage(this.messageToSend);
            this.messageToSend = null;
        }
        
        count++;
        
        this.messageBuilder.addRow(dataRowItem);
        
        
        //check here
        //HHRMapper.testMultiple(schemaKey, schema, dataRowItem, DataCaptureDatasets.valueOf(datasetID));
        
        if(count%batchSize==0) {
            addMessageToSend(this.messageBuilder.build());
            this.messageBuilder = BatchConverterOutputBuilder.newInstance(datasourceID, datasetID, schema, schemaKey, confParameters, count+1, batchSize, this.jobID);
        }
    }
    
    private void addMessageToSend(BatchConverterOutput message) {    
        this.messageToSend = message;
    }
    
    private void sendMessage(BatchConverterOutput message) {    
        try {
            String serializedMessage = JsonSerializers.serialize(message, PropertyAccessor.GETTER);
            ProducerRecord<String, String> record = new ProducerRecord<>(this.params.getTopic(), null, serializedMessage);
            RecordMetadata res = (RecordMetadata)producer.send(record).get(); // Send the record to Kafka server
            System.out.println(res.toString());
        } catch (JsonSerializationException ex) {
            Log.error("Could not serialize message {}", ex);
            ex.printStackTrace();
        }  catch(SerializationException | InterruptedException | ExecutionException e) {
            Log.error("Could not send message {}", e);
            e.printStackTrace();
        }
    }
    
    
    
}
