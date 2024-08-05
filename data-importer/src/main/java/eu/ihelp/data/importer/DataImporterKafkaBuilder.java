package eu.ihelp.data.importer;

import eu.ihelp.data.importer.logging.LoggingMessage;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataImporterKafkaBuilder {
    
    private final String kafkaUrl = DataImporterProperties.getInstance().getKafkaURL();
    private final String kafkaPort = DataImporterProperties.getInstance().getKafkaPort();
    private final String kafkaAvroPort = DataImporterProperties.getInstance().getKafkaAvroPort();

    private DataImporterKafkaBuilder() {
    }
    
    public static DataImporterKafkaBuilder createInstance() {
        return new DataImporterKafkaBuilder();
    }
    
    public DataImporterKafkaProducer createProducer(ArrayBlockingQueue<LoggingMessage> queue) {
        // Create producer configuration
        Properties props = new Properties(); 
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://" + this.kafkaUrl + ":" + this.kafkaPort); // Set Kafka server ip:port
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class); // Set record key serializer to Avro
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class); // Set record value serializer to Avro
        props.put("schema.registry.url", "http://" + this.kafkaUrl + ":" + this.kafkaAvroPort);
        
        KafkaProducer kafkaProducer =  new KafkaProducer(props); // Create producer
        return new DataImporterKafkaProducer(kafkaProducer, queue);
    }
    
    
    public DataImporterKafkaConsumer createConsumer(DataImporterKafkaProducer dataImporterKafkaProducer) {
        
        // Create producer configuration
        Properties props = new Properties(); 
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://" + this.kafkaUrl + ":" + this.kafkaPort); // Set Kafka server ip:port
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // Set record key serializer to String
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // Set record value serializer to String
        
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //to read from the benning
        
        KafkaConsumer kafkaConsumer = new KafkaConsumer(props); // Create consumer
        kafkaConsumer.subscribe(Collections.singletonList(DataImporterProperties.getInstance().getKafkaTopicInput())); //subscribe
        
        return new  DataImporterKafkaConsumer(kafkaConsumer, dataImporterKafkaProducer);
    }
}
