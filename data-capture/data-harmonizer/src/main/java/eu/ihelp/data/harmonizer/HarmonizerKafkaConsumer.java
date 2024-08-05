package eu.ihelp.data.harmonizer;

import eu.ihelp.datacapture.commons.DataCaptureDatasets;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import org.apache.avro.Schema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HarmonizerKafkaConsumer implements Closeable, Runnable {
    private static final Logger Log = LoggerFactory.getLogger(HarmonizerKafkaConsumer.class);
    
    private final KafkaConsumer kafkaConsumer;
    private final String topic;
    private final HarmonizerKafkaProducer harmonizerKafkaProducer;

    protected HarmonizerKafkaConsumer(KafkaConsumer kafkaConsumer, String topic, HarmonizerKafkaProducer harmonizerKafkaProducer) {
        this.kafkaConsumer = kafkaConsumer;
        this.topic = topic;
        this.harmonizerKafkaProducer = harmonizerKafkaProducer;
    }
    

    @Override
    public void run() {
        while(true) {
            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String, String> consumerRecord : records) {
                try {
                    String input = consumerRecord.value();
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
           this.harmonizerKafkaProducer) {
            Log.info("Will close the consumer and producer");
        }
    }
    
    
    private void processRecord(String message) {
        try {
            JSONObject jSONObject = new JSONObject(message);
            String datasetID = jSONObject.getString("datasetID");
            JSONArray values = jSONObject.getJSONArray("values");
            
            Schema.Parser parser = new Schema.Parser();
            Schema schemaKey = parser.parse(jSONObject.getJSONObject("schemaKey").toString());
            Schema schemaValue = parser.parse(jSONObject.getJSONObject("schema").toString());
            
            JSONArray hhrEntities = HHRMapper.mapToHHR(schemaKey, schemaValue, values, DataCaptureDatasets.valueOf(datasetID));
            
            jSONObject.put("values", hhrEntities);
            this.harmonizerKafkaProducer.write(jSONObject.toString());
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
