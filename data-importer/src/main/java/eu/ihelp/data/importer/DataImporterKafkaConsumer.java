package eu.ihelp.data.importer;

import eu.ihelp.data.importer.utils.HHRImportParser;
import eu.ihelp.data.importer.utils.MessageToKafkaProducer;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataImporterKafkaConsumer implements Closeable, Runnable {
    private static final Logger Log = LoggerFactory.getLogger(DataImporterKafkaConsumer.class);
    
    private final KafkaConsumer kafkaConsumer;
    private final DataImporterKafkaProducer dataImporterKafkaProducer;

    protected DataImporterKafkaConsumer(KafkaConsumer kafkaConsumer, DataImporterKafkaProducer dataImporterKafkaProducer) {
        this.kafkaConsumer = kafkaConsumer;
        this.dataImporterKafkaProducer = dataImporterKafkaProducer;
    }
    

    @Override
    public void run() {
        while(true) {
            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String, String> consumerRecord : records) {
                try {
                    String input = consumerRecord.value();
                    System.out.println(" I got this:\n" + input);
                    processRecord(input);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            if(Thread.currentThread().isInterrupted()) {
                try {
                    close();
                } catch(IOException ex) {/*do nothing*/}
                
                break;
            }
        }
    }
     
    @Override
    public void close() throws IOException {
        try (
           this.kafkaConsumer; 
           this.dataImporterKafkaProducer) {
            Log.info("Will close the consumer and producer");
        }
    }
    
    
    private void processRecord(String message) {
        try {
            MessageToKafkaProducer inputToSend = HHRImportParser.createInstance(message).process();
            this.dataImporterKafkaProducer.process(inputToSend);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
