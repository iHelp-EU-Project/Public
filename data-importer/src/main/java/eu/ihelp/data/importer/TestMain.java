package eu.ihelp.data.importer;

import eu.ihelp.data.importer.logging.LoggingMessage;
import eu.ihelp.data.importer.schemas.SchemaMeasurement;
import eu.ihelp.data.importer.utils.HHRImportParser;
import eu.ihelp.data.importer.utils.HHRRecordToImport;
import eu.ihelp.data.importer.utils.MessageToKafkaProducer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema.Field;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TestMain {
    
    static ArrayBlockingQueue<LoggingMessage> queue = new ArrayBlockingQueue<>(200);
    
    
    
    public static void main(String [] args) throws IOException {
        //String serializedMessage = readJsonFromFile("kafka-hhr-output/KafkaConditions.json");
        //String serializedMessage = readJsonFromFile("kafka-hhr-output/fpg/blood_test.json");
        //String serializedMessage = readJsonFromFile("kafka-hhr-output/mup/mup_final.json");
        String serializedMessage = readJsonFromFile("kafka-hhr-output/healthentia/patient_test_20240116.json");
        //String serializedMessage = readJsonFromFile("kafka-hhr-output/healthentia/physiological.json");
        //String serializedMessage = readJsonFromFile("kafka-hhr-output/healthentia/physiological.json");
        //String serializedMessage = readJsonFromFile("kafka-hhr-output/mup/pencho.json");
        
        
        
        try(
            DataImporterKafkaProducer dataImporterProducer = DataImporterKafkaBuilder.createInstance().createProducer(queue);) {
            MessageToKafkaProducer inputToSend  = HHRImportParser.createInstance(serializedMessage).process();
            dataImporterProducer.process(inputToSend);
        }
        
    }
    
    
    private static String readJsonFromFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
            String line;
            while((line = br.readLine())!=null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
