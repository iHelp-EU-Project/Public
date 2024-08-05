package eu.ihelp.datacapture.manager;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.manager.services.impl.ComboJobManager;
import eu.ihelp.datacapture.manager.services.impl.Job;
import eu.ihelp.datacapture.manager.services.impl.JobManager;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ComboJobRunnable implements Runnable {
    private static final Logger Log = LoggerFactory.getLogger(ComboJobRunnable.class);
    
    private final String comboJobID;
    private final TreeMap<Integer, Job> orderedJobs;

    public ComboJobRunnable(String comboJobID, TreeMap<Integer, Job> orderedJobs) {
        this.comboJobID = comboJobID;
        this.orderedJobs = orderedJobs;
    }
    
    @Override
    public void run() {
        ComboJobManager.getInstance().seCombotJobAsStarted(comboJobID);
        Iterator<Map.Entry<Integer, Job>> it = this.orderedJobs.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, Job> entry = it.next();
            Integer order = entry.getKey();
            Job job = entry.getValue();
            Log.info("Will submit job {} for provider {} and dataset {}", job.getId(), job.getDataProvider(), job.getDataset());
            
            try {
                if(Thread.currentThread().isInterrupted()) {
                    Log.warn("Thread {} for comboJobID has been interrupted", Thread.currentThread().getName(), comboJobID);
                    throw new InterruptedException();
                }  
                
                Future future = job.init();
                
                //wait until job has been finished
                future.get();
                
            } catch (DataCaptureInitException ex) {
                Log.error("Error initializing job {}. {}", job.getId(), ex);
                JobManager.getInstance().setJobAsFailed(job.getId());
            } catch (ExecutionException ex) {
                Log.warn("Job {} has been failed upon its execution.", job.getId(), ex);
                JobManager.getInstance().setJobAsFailed(job.getId());
            } catch (InterruptedException ex) {
                Log.warn("Job {} has been interrupted.");
                setRemainingJobsAsFailed(order);
            } 
        }
        
        ComboJobManager.getInstance().setComboJobAsFinished(comboJobID);
    }
    
    private void setRemainingJobsAsFailed(Integer order) {
        Iterator<Map.Entry<Integer, Job>> it = this.orderedJobs.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, Job> entry = it.next();
            Integer currentOrder = entry.getKey();
            if(currentOrder<=order) {
                continue;
            }
            
            JobManager.getInstance().setJobAsFailed(entry.getValue().getId());
        }
    }
    
}
