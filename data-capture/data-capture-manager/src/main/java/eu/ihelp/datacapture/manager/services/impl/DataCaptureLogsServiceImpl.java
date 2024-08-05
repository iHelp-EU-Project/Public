package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureJobLogging;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureLogsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureLogsServiceImpl implements DataCaptureLogsService {
    private final HashMap<String, List<DataCaptureJobLogging>> cache;
    
    private DataCaptureLogsServiceImpl(HashMap<String, List<DataCaptureJobLogging>> cache) {
        this.cache = cache;
    }

        
    private static final class DataCaptureLogsServiceImplHOLDER {
        private static final DataCaptureLogsServiceImpl INSTANCE = new DataCaptureLogsServiceImpl(new HashMap<>());
    }
    
    public static DataCaptureLogsServiceImpl getInstance() {
        return DataCaptureLogsServiceImplHOLDER.INSTANCE;
    }
    
    @Override
    public List<DataCaptureJobLogging> getAllDataCaptureLogs(String id) throws JobNotFoundException {
        if(this.cache.containsKey(id)) {
            return this.cache.get(id);
        } else {
            throw new JobNotFoundException(id);
        }
    }

    @Override
    public DataCaptureJobLogging insertDataCaptureLog(DataCaptureJobLogging log) {
        if((log.getLogID()==null) || (log.getLogID().isEmpty())) {
            log.setLogID(UUID.randomUUID().toString());
        }
        
        if(this.cache.containsKey(log.getJobID())) {
            this.cache.get(log.getJobID()).add(log);
        } else {
            List<DataCaptureJobLogging> loggings = new ArrayList<>();
            loggings.add(log);
            this.cache.put(log.getJobID(), loggings);
        }
        
        return log;
    }
}
