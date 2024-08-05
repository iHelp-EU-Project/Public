package eu.ihelp.datacapture.manager.services;

import eu.ihelp.datacapture.commons.model.DataCaptureJobScheduled;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface DataCaptureScheduledService {
    
    public List<DataCaptureJobScheduled> getAllDataCaptureScheduleJobs();    
    
    public DataCaptureJobScheduled getDataCaptureScheduleJob(String id) throws JobNotFoundException;
    
    public DataCaptureJobScheduled addDataCaptureScheduleJob(DataCaptureJobScheduled dataCaptureJobScheduled);
    
    public DataCaptureJobScheduled stopDataCaptureScheduleJob(String id) throws JobNotFoundException;
}
