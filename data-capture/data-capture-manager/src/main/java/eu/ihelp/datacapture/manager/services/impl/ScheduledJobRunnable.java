package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ScheduledJobRunnable implements Runnable {
    private static final Logger Log = LoggerFactory.getLogger(ScheduledJobRunnable.class);
    private final DataCaptureJob dataCaptureJob;

    protected ScheduledJobRunnable(DataCaptureJob dataCaptureJob) {
        this.dataCaptureJob = dataCaptureJob;
    }

    @Override
    public void run() {
        Log.info("Periodically adding job from dataset {} and connector {} to converter {}", 
                dataCaptureJob.getDatasetID(), 
                dataCaptureJob.getConnectorArguments().getClass().getSimpleName(), 
                dataCaptureJob.getConverterArguments().getClass().getSimpleName());
        
        try {
            JobManager.getInstance().addDataCaptureJob(dataCaptureJob);
        } catch(Exception ex) {
            Log.error("Error adding periodic job: {}. {}", dataCaptureJob.getId(), ex);
        }
    }
}
