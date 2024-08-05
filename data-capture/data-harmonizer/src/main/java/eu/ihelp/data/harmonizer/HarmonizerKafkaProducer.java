package eu.ihelp.data.harmonizer;

import java.io.Closeable;
import java.io.IOException;
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
public class HarmonizerKafkaProducer implements Closeable {
    private static final Logger Log = LoggerFactory.getLogger(HarmonizerKafkaProducer.class);
    
    private final KafkaProducer producer;
    private final String topic;

    protected HarmonizerKafkaProducer(KafkaProducer producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }
    
    
    public void write(String serializedMessage) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, null, serializedMessage);
            RecordMetadata res = (RecordMetadata)producer.send(record).get(); // Send the record to Kafka server
            System.out.println(res.toString());
        }  catch(SerializationException | InterruptedException | ExecutionException e) {
            Log.error("Could not send message {}", e);
            e.printStackTrace();
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
