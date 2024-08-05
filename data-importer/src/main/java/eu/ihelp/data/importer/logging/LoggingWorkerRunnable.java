package eu.ihelp.data.importer.logging;

import eu.ihelp.data.importer.DataImporterProperties;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class LoggingWorkerRunnable implements  Runnable {
    private static final Logger Log = LoggerFactory.getLogger(LoggingWorkerRunnable.class);
    
    protected final ArrayBlockingQueue<LoggingMessage> queue;

    public LoggingWorkerRunnable(ArrayBlockingQueue<LoggingMessage> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                if(Thread.currentThread().isInterrupted()) {
                    sendClosingMessage();
                    break;
                }
                
                LoggingMessage loggingMessage = this.queue.take();
                
                try {
                    if(DataImporterProperties.getInstance().isDataCaptureLoggingInfoAll()) {
                        HttpClient.executePOST(loggingMessage.getDataCaptureUrl(), loggingMessage.createMessage());
                    }
                } catch (Exception ex) {
                    Log.error("Error while trying to send an info message {} to {}. {}:{}", loggingMessage.createMessage(), loggingMessage.getDataCaptureUrl(), ex.getClass().getName(), ex.getMessage());
                }
                
                if(loggingMessage.isLastBatch()) {
                    try {
                        String dataCaptureURL = loggingMessage.getDataCaptureUrl();
                        dataCaptureURL = dataCaptureURL.replace("logs/", "");
                        HttpClient.executeGET(dataCaptureURL + "finished/" + loggingMessage.getJobID());
                    } catch (Exception ex) {
                    Log.error("Error while trying to set job status to finished for jobID {} {}:{}", loggingMessage.getJobID(), ex.getClass().getName(), ex.getMessage());
                }
                }
                
            } catch (InterruptedException ex) {
                sendClosingMessage();
            }
        }
    }
    
    private void sendClosingMessage() {
        Log.warn("Interruption has been sent, will close the {}", this.getClass().getName());
    }
}
