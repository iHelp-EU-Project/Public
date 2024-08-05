package eu.ihelp.datacapture.manager.services;

import eu.ihelp.datacapture.commons.model.DataCaptureComboJobScheduled;
import eu.ihelp.datacapture.manager.exceptions.InvalidComboJobException;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface DataCaptureComboScheduledService {
    
    public List<DataCaptureComboJobScheduled> getAllDataCaptureScheduleComboJobs();    
    
    public DataCaptureComboJobScheduled getDataCaptureScheduleComboJob(String id) throws JobNotFoundException;
    
    public DataCaptureComboJobScheduled addDataCaptureScheduleComboJob(DataCaptureComboJobScheduled dataCaptureJobScheduled) throws InvalidComboJobException;
    
    public DataCaptureComboJobScheduled stopDataCaptureScheduleComboJob(String id) throws JobNotFoundException;
}
