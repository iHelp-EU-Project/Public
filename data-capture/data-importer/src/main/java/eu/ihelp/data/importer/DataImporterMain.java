package eu.ihelp.data.importer;

import eu.ihelp.data.importer.logging.LoggingMessage;
import eu.ihelp.data.importer.logging.LoggingUtils;
import eu.ihelp.data.importer.logging.LoggingWorkerRunnable;
import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataImporterMain implements Closeable{
    private static final Logger Log = LoggerFactory.getLogger(DataImporterMain.class);
    
    private final ExecutorService executor;
    private final ExecutorService logginExecutor;

    private DataImporterMain(ExecutorService executor, ExecutorService logginExecutor) {
        this.executor = executor;
        this.logginExecutor = logginExecutor;
    }
    
    
    public static void main(String [] args) {
        Log.info("Starting the HHR Harmonizer....");
        DataImporterMain.getInstance();
    }

    
    public static final class HarmonizerMainHolder {
        private static final DataImporterMain INSTANCE = buildInstance();
                
        static DataImporterMain buildInstance() {
            try {
                LoggingUtils loggingUtils = LoggingUtils.getInstance(); //this LoggingUtils also initiates the BlockingQueue to send/receive logging messages
                
                DataImporterKafkaProducer producer = DataImporterKafkaBuilder.createInstance()
                        .createProducer(loggingUtils.getQueue());
                
                DataImporterKafkaConsumer client = DataImporterKafkaBuilder.createInstance()
                        .createConsumer(producer);
                
                LoggingWorkerRunnable loggingRunnable = new LoggingWorkerRunnable(loggingUtils.getQueue());
                
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                ExecutorService logginExecutorService = Executors.newSingleThreadExecutor();
                DataImporterMain harmonizerMain = new DataImporterMain(executorService, logginExecutorService);
                
                Runtime runtime = Runtime.getRuntime();
                runtime.addShutdownHook(new ShutDownHook(harmonizerMain));
                
                executorService.submit(client);
                logginExecutorService.submit(loggingRunnable);
                
                return harmonizerMain;
            } catch(Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
            
            return null;
        }
    }
    
    public static DataImporterMain getInstance() {
        return HarmonizerMainHolder.INSTANCE;
    }
    
    @Override
    public void close()  {
        this.executor.shutdownNow();
        this.logginExecutor.shutdownNow();
    }
}
