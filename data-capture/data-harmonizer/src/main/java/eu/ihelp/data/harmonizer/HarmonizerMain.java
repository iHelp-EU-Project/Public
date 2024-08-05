package eu.ihelp.data.harmonizer;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HarmonizerMain implements Closeable{
    private static final Logger Log = LoggerFactory.getLogger(HarmonizerMain.class);
    
    private final HarmonizerKafkaConsumer client;
    private final ExecutorService executor;

    private HarmonizerMain(ExecutorService executor, HarmonizerKafkaConsumer client) {
        this.executor = executor;
        this.client = client;
    }
    
    
    public static void main(String [] args) {
        Log.info("Starting the HHR Harmonizer....");
        HarmonizerMain.getInstance();
    }

    
    public static final class HarmonizerMainHolder {
        private static final HarmonizerMain INSTANCE = buildInstance();
                
        static HarmonizerMain buildInstance() {
            try {
                HarmonizerKafkaProducer producer = HarmonizerKafkaBuilder.createInstance(
                        HarmonizerProperties.getInstance().getKafkaURL(), 
                        HarmonizerProperties.getInstance().getKafkaTopicOutput())
                        .createProducer();
                
                HarmonizerKafkaConsumer client = HarmonizerKafkaBuilder.createInstance(
                        HarmonizerProperties.getInstance().getKafkaURL(), 
                        HarmonizerProperties.getInstance().getKafkaTopicInput())
                        .createConsumer(producer);
                
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                HarmonizerMain harmonizerMain = new HarmonizerMain(executorService, client);
                
                Runtime runtime = Runtime.getRuntime();
                runtime.addShutdownHook(new ShutDownHook(harmonizerMain));
                
                executorService.submit(client);
                
                return harmonizerMain;
            } catch(Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
            
            return null;
        }
    }
    
    public static HarmonizerMain getInstance() {
        return HarmonizerMainHolder.INSTANCE;
    }
    
    @Override
    public void close()  {
        this.executor.shutdownNow();
    }
}
