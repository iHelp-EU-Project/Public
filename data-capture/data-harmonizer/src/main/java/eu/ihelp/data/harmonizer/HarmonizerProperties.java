package eu.ihelp.data.harmonizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HarmonizerProperties {
    private static final Logger Log = LoggerFactory.getLogger(HarmonizerProperties.class);
    private static final String PROPERTIES_FILE = "harmomizer.properties";
    
    private static final String KAFKA_URL = "kafka.url";
    private static final String KAFKA_TOPIC_INPUT = "kafka.topic.input";
    private static final String KAFKA_TOPIC_OUTPUT = "kafka.topic.output";
    private static final String HHR_MAPPER_URL = "hhr.mapper.url";
    
    private final String kafkaURL;
    private final String kafkaTopicInput;
    private final String kafkaTopicOutput;
    private final String hhrMaperURL;
    
    private HarmonizerProperties(String kafkaURL, String kafkaTopicInput, String kafkaTopicOutput, String hhrMaperURL) {
        this.kafkaURL = kafkaURL;
        this.kafkaTopicInput = kafkaTopicInput;
        this.kafkaTopicOutput = kafkaTopicOutput;
        this.hhrMaperURL = hhrMaperURL;
    }

    
    private static final class HarmonizerPropertiesHolder {
        private static final HarmonizerProperties INSTANCE = buildInstance();
        
        static HarmonizerProperties buildInstance() {
            try {
                Properties properties = new Properties();
                ClassLoader classLoader = HarmonizerProperties.class.getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
                properties.load(inStream);
                
                String _kafkaUrl = getProperty(properties, KAFKA_URL);
                String _kafkaInput = getProperty(properties, KAFKA_TOPIC_INPUT);
                String _kafkaOutput = getProperty(properties, KAFKA_TOPIC_OUTPUT);
                String _hhrMaperURL = getProperty(properties, HHR_MAPPER_URL);
            
                return new HarmonizerProperties(_kafkaUrl, _kafkaInput, _kafkaOutput, _hhrMaperURL);
            } catch(IOException ex) {
                Log.error("Could not load ingestion properties file. {}: {}", ex.getClass().getName(), ex.getMessage());
                System.exit(-1);
            }
            
            return null;
        }
        
        
        static String getProperty(Properties properties, String propertyKey) {
            String result = System.getenv(propertyKey);
            if((result!=null) && (!result.isEmpty())) {
                return result;
            }
            
            result = System.getProperty(propertyKey);
            if((result!=null) && (!result.isEmpty())) {
                return result;
            }
            
            result = properties.getProperty(propertyKey);
            if((result!=null) && (!result.isEmpty())) {
                return result;
            }
            
            throw new IllegalArgumentException(propertyKey + " has not been provided");
        }
    }    
    
    public static HarmonizerProperties getInstance() {
        return HarmonizerPropertiesHolder.INSTANCE;
    }
    
    
    public String getKafkaURL() {
        return kafkaURL;
    }

    public String getKafkaTopicInput() {
        return kafkaTopicInput;
    }

    public String getKafkaTopicOutput() {
        return kafkaTopicOutput;
    }

    public String getHhrMaperURL() {
        return hhrMaperURL;
    }
    
}
