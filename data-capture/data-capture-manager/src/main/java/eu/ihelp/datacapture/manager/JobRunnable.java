package eu.ihelp.datacapture.manager;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.utils.DataCaptureThreadFactory;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.DataRowItemEnd;
import eu.ihelp.datacapture.functions.connectors.ConnectorAbstract;
import eu.ihelp.datacapture.functions.converters.ConverterAbstract;
import eu.ihelp.datacapture.manager.services.impl.JobManager;
import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JobRunnable implements Runnable, Closeable {
    private static final Logger Log = LoggerFactory.getLogger(JobRunnable.class);
    
    private final ConnectorAbstract connector;
    private final ConverterAbstract converter;
    private final String jobID;
    private final ArrayBlockingQueue<DataRowItem> queue;
    private final ExecutorService executor;

    public JobRunnable(ConnectorAbstract connector, ConverterAbstract converter, String jobID, ArrayBlockingQueue<DataRowItem> queue) {
        this.connector = connector;
        this.converter = converter;
        this.jobID = jobID;
        this.queue = queue;
        
        this.executor = Executors.newSingleThreadExecutor(new DataCaptureThreadFactory(this.connector.getClass().getSimpleName()));
    }

    public void init() throws DataCaptureInitException {
        try {
            this.connector.init();
        } catch(DataCaptureInitException ex) {
            Log.error("Cound not initialize connector {}. {}", this.connector.getClass().getSimpleName(), ex);
            JobManager.getInstance().setJobAsFailed(jobID);
            throw ex;
        }
        
        this.executor.submit(this.connector);
        
        try { 
            this.converter.init();
            JobManager.getInstance().setJobAsStarted(jobID);
        } catch(Exception ex) {
            Log.error("Cound not initialize converter {}. {}", this.converter.getClass().getSimpleName(), ex);
            this.connector.close();
            JobManager.getInstance().setJobAsFailed(jobID);
        }
        
    }
    
    @Override
    public void close() {
        this.executor.shutdownNow();
        this.converter.close();
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                DataRowItem dataRowItem = this.queue.take();
                
                
                if(dataRowItem instanceof DataRowItemEnd) {
                    close();
                    JobManager.getInstance().setJobAsFinishedCaptured(jobID);
                    break;
                }
                
                this.converter.process(dataRowItem);
                
            } catch (InterruptedException ex) {
                close();
            } catch (Exception ex) {
                ex.printStackTrace();
                close();
            }
        }
    }
    
}
