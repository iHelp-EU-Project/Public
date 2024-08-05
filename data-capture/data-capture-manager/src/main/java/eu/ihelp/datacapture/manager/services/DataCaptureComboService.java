package eu.ihelp.datacapture.manager.services;

import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import eu.ihelp.datacapture.manager.exceptions.InvalidComboJobException;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface DataCaptureComboService {
    
    public List<DataCaptureComboJob> getAllDataCaptureComboJobs();
    
    public DataCaptureComboJob getDataCaptureComboJob(String id) throws JobNotFoundException;
    
    public DataCaptureComboJob addDataCaptureComboJob(DataCaptureComboJob dataCaptureJobCombo) throws InvalidComboJobException; 
}
