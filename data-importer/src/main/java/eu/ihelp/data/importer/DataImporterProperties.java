package eu.ihelp.data.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataImporterProperties {
    private static final Logger Log = LoggerFactory.getLogger(DataImporterProperties.class);
    private static final String PROPERTIES_FILE = "importer.properties";
    
    private static final String KAFKA_URL = "kafka_url";
    private static final String KAFKA_PORT = "kafka.port";
    private static final String KAFKA_AVRO_PORT = "kafka.avro.port";
    private static final String KAFKA_TOPIC_INPUT = "kafka.topic.input";
    private static final String KAFKA_TOPIC_OUTPUT_PATIENT = "kafka.topic.output.patient";
    private static final String KAFKA_TOPIC_OUTPUT_CONDITION = "kafka.topic.output.condition";
    private static final String KAFKA_TOPIC_OUTPUT_MEDICATION = "kafka.topic.output.medication";
    private static final String KAFKA_TOPIC_OUTPUT_MEDICATIONADMINISTRATION = "kafka.topic.output.medicationadministration";
    private static final String KAFKA_TOPIC_OUTPUT_OBSERVATIOKN = "kafka.topic.output.observation";
    private static final String KAFKA_TOPIC_OUTPUT_ENCOUNTER = "kafka.topic.output.encounter";
    private static final String KAFKA_TOPIC_OUTPUT_PROCEDURE = "kafka.topic.output.procedure";
    private static final String KAFKA_TOPIC_OUTPUT_MEASUREMENT = "kafka.topic.output.measurement";
    private static final String KAFKA_TOPIC_OUTPUT_FAMILYMEMBERHISTORY = "kafka.topic.output.familymemberhistory";
    private static final String KAFKA_TOPIC_OUTPUT_ANSWER = "kafka.topic.output.answer";
    private static final String DATACAPTURE_LOGGING_INFOALL = "datacapture.logging.infoall";
    
    private final String kafkaURL;
    private final String kafkaPort;
    private final String kafkaAvroPort;
    private final String kafkaTopicInput;
    private final String kafkaTopicOutputPatient;
    private final String kafkaTopicOutputCondition;
    private final String kafkaTopicOutputMedication;
    private final String kafkaTopicOutputMedicationAdministration;
    private final String kafkaTopicOutputObservation;
    private final String kafkaTopicOutputEncounter;
    private final String kafkaTopicOutputProcedure;
    private final String kafkaTopicOutputMeasurement;
    private final String kafkaTopicOutputFamilyMemberHistory;
    private final String kafkaTopicOutputAnswer;
    private final boolean dataCaptureLoggingInfoAll;

    private DataImporterProperties(String kafkaURL, String kafkaPort, String kafkaAvroPort, String kafkaTopicInput, String kafkaTopicOutputPatient, String kafkaTopicOutputConditiont, String kafkaTopicOutputMedication, String kafkaTopicOutputMedicationAdministration, String kafkaTopicOutputObservation, String kafkaTopicOutputEncounter, String kafkaTopicOutputProcedure, String kafkaTopicOutputMeasurement, String kafkaTopicOutputFamilyMemberHistory, String kafkaTopicOutputAnswer, boolean dataCaptureLoggingInfoAll) {
        this.kafkaURL = kafkaURL;
        this.kafkaPort = kafkaPort;
        this.kafkaAvroPort = kafkaAvroPort;
        this.kafkaTopicInput = kafkaTopicInput;
        this.kafkaTopicOutputPatient = kafkaTopicOutputPatient;
        this.kafkaTopicOutputCondition = kafkaTopicOutputConditiont;
        this.kafkaTopicOutputMedication = kafkaTopicOutputMedication;
        this.kafkaTopicOutputMedicationAdministration = kafkaTopicOutputMedicationAdministration;
        this.kafkaTopicOutputObservation = kafkaTopicOutputObservation;
        this.kafkaTopicOutputEncounter = kafkaTopicOutputEncounter;
        this.kafkaTopicOutputProcedure = kafkaTopicOutputProcedure;
        this.kafkaTopicOutputMeasurement = kafkaTopicOutputMeasurement;
        this.kafkaTopicOutputFamilyMemberHistory = kafkaTopicOutputFamilyMemberHistory;
        this.kafkaTopicOutputAnswer = kafkaTopicOutputAnswer;
        this.dataCaptureLoggingInfoAll = dataCaptureLoggingInfoAll;
    }
    
    

    
    private static final class HarmonizerPropertiesHolder {
        private static final DataImporterProperties INSTANCE = buildInstance();
        
        static DataImporterProperties buildInstance() {
            try {
                Properties properties = new Properties();
                ClassLoader classLoader = DataImporterProperties.class.getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
                properties.load(inStream);
                
                String _kafkaUrl = getProperty(properties, KAFKA_URL);
                String _kafkaPort = getProperty(properties, KAFKA_PORT);
                String _kafkaAvroPort = getProperty(properties, KAFKA_AVRO_PORT);
                String _kafkaInput = getProperty(properties, KAFKA_TOPIC_INPUT);
                String _kafkaOutputPatient = getProperty(properties, KAFKA_TOPIC_OUTPUT_PATIENT);
                String _kafkaOutputCondition = getProperty(properties, KAFKA_TOPIC_OUTPUT_CONDITION);
                String _kafkaOutputMedication = getProperty(properties, KAFKA_TOPIC_OUTPUT_MEDICATION);
                String _kafkaOutputMedicationAdministration = getProperty(properties, KAFKA_TOPIC_OUTPUT_MEDICATIONADMINISTRATION);
                String _kafkaOutputObservation = getProperty(properties, KAFKA_TOPIC_OUTPUT_OBSERVATIOKN);
                String _kafkaOutputEncouter = getProperty(properties, KAFKA_TOPIC_OUTPUT_ENCOUNTER);
                String _kafkaOutputProcedure = getProperty(properties, KAFKA_TOPIC_OUTPUT_PROCEDURE);
                String _kafkaOutputMeasurement = getProperty(properties, KAFKA_TOPIC_OUTPUT_MEASUREMENT);
                String _kafkaOutputFamilyMemberHistory = getProperty(properties, KAFKA_TOPIC_OUTPUT_FAMILYMEMBERHISTORY);
                String _kafkaTopicOutputAnswer = getProperty(properties, KAFKA_TOPIC_OUTPUT_ANSWER);
                String _dataCaptureLoggingInfoAll = getProperty(properties, DATACAPTURE_LOGGING_INFOALL, "false");
            
                return new DataImporterProperties(
                        _kafkaUrl, 
                        _kafkaPort,
                        _kafkaAvroPort,
                        _kafkaInput, 
                        _kafkaOutputPatient, 
                        _kafkaOutputCondition, 
                        _kafkaOutputMedication, 
                        _kafkaOutputMedicationAdministration,
                        _kafkaOutputObservation, 
                        _kafkaOutputEncouter,
                        _kafkaOutputProcedure,
                        _kafkaOutputMeasurement,
                        _kafkaOutputFamilyMemberHistory,
                        _kafkaTopicOutputAnswer,
                        Boolean.valueOf(_dataCaptureLoggingInfoAll));
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
        
        static String getProperty(Properties properties, String propertyKey, String defaultValue) {
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
            
            return defaultValue;
        }
    
    }    
    
      
    
    public static DataImporterProperties getInstance() {
        return HarmonizerPropertiesHolder.INSTANCE;
    }
    
    
    public String getKafkaURL() {
        return kafkaURL;
    }

    public String getKafkaPort() {
        return kafkaPort;
    }

    public String getKafkaAvroPort() {
        return kafkaAvroPort;
    }
    
    public String getKafkaTopicInput() {
        return kafkaTopicInput;
    }

    public String getKafkaTopicOutputPatient() {
        return kafkaTopicOutputPatient;
    }

    public String getKafkaTopicOutputCondition() {
        return kafkaTopicOutputCondition;
    }

    public String getKafkaTopicOutputMedication() {
        return kafkaTopicOutputMedication;
    }

    public String getKafkaTopicOutputMedicationAdministration() {
        return kafkaTopicOutputMedicationAdministration;
    }
    
    public String getKafkaTopicOutputObservation() {
        return kafkaTopicOutputObservation;
    }

    public String getKafkaTopicOutputEncounter() {
        return kafkaTopicOutputEncounter;
    }

    public String getKafkaTopicOutputProcedure() {
        return kafkaTopicOutputProcedure;
    }

    public String getKafkaTopicOutputMeasurement() {
        return kafkaTopicOutputMeasurement;
    }

    public String getKafkaTopicOutputFamilyMemberHistory() {
        return kafkaTopicOutputFamilyMemberHistory;
    }

    public String getKafkaTopicOutputAnswer() {
        return kafkaTopicOutputAnswer;
    }
    
    public boolean isDataCaptureLoggingInfoAll() {
        return dataCaptureLoggingInfoAll;
    }
}
