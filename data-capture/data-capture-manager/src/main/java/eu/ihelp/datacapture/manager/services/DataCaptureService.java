package eu.ihelp.datacapture.manager.services;

import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.List;


/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface DataCaptureService {
    
    public List<DataCaptureJob> getAllDataCaptureJobs();    
    
    public DataCaptureJob getDataCaptureJob(String id) throws JobNotFoundException;
    
    public DataCaptureJob addDataCaptureJob(DataCaptureJob dataCaptureJob);
    
    public DataCaptureJob setDataCaptureJobFinished(String id) throws JobNotFoundException;
}
