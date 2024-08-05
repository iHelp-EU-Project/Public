package eu.ihelp.data.importer;

import eu.ihelp.data.importer.logging.LoggingMessage;
import eu.ihelp.data.importer.logging.LoggingMessageBuilder;
import eu.ihelp.data.importer.utils.HHRRecordToImport;
import eu.ihelp.data.importer.utils.MessageToKafkaProducer;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataImporterKafkaProducer implements Closeable {
    private static final Logger Log = LoggerFactory.getLogger(DataImporterKafkaProducer.class);
    
    private final KafkaProducer producer;
    protected final ArrayBlockingQueue<LoggingMessage> queue;

    protected DataImporterKafkaProducer(KafkaProducer producer, ArrayBlockingQueue<LoggingMessage> queue) {
        this.producer = producer;
        this.queue = queue;
    }
    
    
    public void process(MessageToKafkaProducer input) {
        for(HHRRecordToImport hHRRecordToImport : input.gethHRRecordToImportList()) {
            process(hHRRecordToImport);
        }
        
        try {
            this.queue.put(LoggingMessageBuilder.createLoggingMessage(input));
        } catch(InterruptedException ex) {
            //interruption will have been thrown and the object will have been closed 
        }
        
    }
    
    /*
    private void process(HHRRecordToImport hHRRecordToImport) {
        for(HHRRecordToImport.KeyValueRecord keyValueRecord : hHRRecordToImport.getKeyValueRecords()) {
            //do nothing
        }
    }
    */
    
    private void process(HHRRecordToImport hHRRecordToImport) {
        for(HHRRecordToImport.KeyValueRecord keyValueRecord : hHRRecordToImport.getKeyValueRecords()) {
            try {
                ProducerRecord<Object, Object> record = new ProducerRecord<>(hHRRecordToImport.getTopic(), keyValueRecord.getKeyRecord(), keyValueRecord.getValueRecord());
                RecordMetadata res = (RecordMetadata) producer.send(record).get(); // Send the record to Kafka server
                System.out.println(res.toString());
            }  catch(SerializationException | InterruptedException | ExecutionException e) {
                Log.error("Could not send message {}", e);
                e.printStackTrace();
            } 
        }
    }
    

    @Override
    public void close() throws IOException {
        try (this.producer) {
            this.producer.flush();
            Log.info("Will close the kafka producer...");
        }
    }    
}
