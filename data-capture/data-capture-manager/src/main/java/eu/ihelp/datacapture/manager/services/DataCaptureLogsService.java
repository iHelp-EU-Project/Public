package eu.ihelp.datacapture.manager.services;

import eu.ihelp.datacapture.commons.model.DataCaptureJobLogging;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface DataCaptureLogsService {
    
    public List<DataCaptureJobLogging> getAllDataCaptureLogs(String id) throws JobNotFoundException;
    
    public DataCaptureJobLogging insertDataCaptureLog(DataCaptureJobLogging log);
}
