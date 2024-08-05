package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ScheduledComboJobRunnable implements Runnable {
    private static final Logger Log = LoggerFactory.getLogger(ScheduledComboJobRunnable.class);
    private final DataCaptureComboJob dataCaptureComboJob;

    protected ScheduledComboJobRunnable(DataCaptureComboJob dataCaptureComboJob) {
        this.dataCaptureComboJob = dataCaptureComboJob;
    }
    
    

    @Override
    public void run() {
        Log.info("Periodically adding combo job {}-{}", 
                dataCaptureComboJob.getId(),
                dataCaptureComboJob.getName());
        
        try {
            ComboJobManager.getInstance().addDataCaptureComboJob(dataCaptureComboJob);
        } catch(Exception ex) {
            Log.error("Error adding periodic job: {}/{}. {}", dataCaptureComboJob.getId(), dataCaptureComboJob.getName(), ex);
        }
    }
    
}
