package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureJobScheduled;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.Consts;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.services.DataCaptureScheduledService;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.avro.Schema;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ScheduledJobManager implements DataCaptureScheduledService {
    private static final Logger Log = LoggerFactory.getLogger(ScheduledJobManager.class);
    
    private final HashMap<String, ScheduledJob> cache;

    private ScheduledJobManager(HashMap<String, ScheduledJob> cache) {
        this.cache = cache;
    }

    private static final class ScheduledJobManagerHOLDER {
        private static final ScheduledJobManager INSTANCE = new ScheduledJobManager(new HashMap<>());
    }

    public static ScheduledJobManager getInstance() {
        return ScheduledJobManagerHOLDER.INSTANCE;
    }

    @Override
    public synchronized List<DataCaptureJobScheduled> getAllDataCaptureScheduleJobs() {
        List<DataCaptureJobScheduled> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, ScheduledJob>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue().toDataCaptureScheduledJob());
        }
        return list;
    }

    @Override
    public synchronized DataCaptureJobScheduled getDataCaptureScheduleJob(String id) throws JobNotFoundException {
        ScheduledJob job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        return job.toDataCaptureScheduledJob();
    }

    @Override
    public synchronized DataCaptureJobScheduled addDataCaptureScheduleJob(DataCaptureJobScheduled dataCaptureJobScheduled) {
        Schema schema;
        if(dataCaptureJobScheduled.getAvroSchema()!=null) {
            schema = dataCaptureJobScheduled.getAvroSchema();
        } else {
            if(dataCaptureJobScheduled.getSchema()==null) {
                schema = null;
            } else {
                if(dataCaptureJobScheduled.getSchema() instanceof Schema) {
                    schema = (Schema) dataCaptureJobScheduled.getSchema();
                } else {
                    java.util.LinkedHashMap object = (java.util.LinkedHashMap) dataCaptureJobScheduled.getSchema();
                    JSONObject json = new JSONObject(object);
                    Schema.Parser parser = new Schema.Parser();
                    schema = parser.parse(json.toString());
                }
            }
        }
        
        Schema schemaKey;
        if(dataCaptureJobScheduled.getAvroSchemaKey()!=null) {
            schemaKey = dataCaptureJobScheduled.getAvroSchema();
        } else {
            if(dataCaptureJobScheduled.getSchemaKey()==null) {
                schemaKey = null;
            } else {
                if(dataCaptureJobScheduled.getSchemaKey() instanceof Schema) {
                    schemaKey = (Schema) dataCaptureJobScheduled.getSchemaKey();
                } else {
                    java.util.LinkedHashMap object = (java.util.LinkedHashMap) dataCaptureJobScheduled.getSchemaKey();
                    JSONObject json = new JSONObject(object);
                    Schema.Parser parser = new Schema.Parser();
                    schemaKey = parser.parse(json.toString());
                }
            }
        }
        
        JSONObject confParameters = (dataCaptureJobScheduled.getConfParameters()==null) 
                ? new JSONObject() 
                : new JSONObject((LinkedHashMap) dataCaptureJobScheduled.getConfParameters());
        
        int batchSize = (dataCaptureJobScheduled.getBatchSize()==null) ? Consts.MAX_BATCH_SIZE : dataCaptureJobScheduled.getBatchSize();
        
        ScheduledJob scheduled = new ScheduledJob(
                UniqueIDManager.getInstance().getNewID(), 
                dataCaptureJobScheduled.getDatasourceID(),
                dataCaptureJobScheduled.getDatasetID(), 
                schema,
                schemaKey,
                confParameters,
                new Date(System.currentTimeMillis()), 
                dataCaptureJobScheduled.getConnectorArguments(), 
                dataCaptureJobScheduled.getConverterArguments(),
                dataCaptureJobScheduled.getSchedule().getFuture(),
                dataCaptureJobScheduled.getSchedule().getPeriodic(),
                batchSize);
        scheduled.setStatus(JobStatusEnum.STARTED);
        this.cache.put(scheduled.getId(), scheduled);
        
        try {
            Log.info("Initializing scheduled job: {}", scheduled);
            scheduled.init();
        } catch(Exception ex) {
            Log.error("Error initializing scheduled job {}. {}", scheduled.getId(), ex);
            scheduled.setDateFinished(new Date(System.currentTimeMillis()));
            scheduled.setStatus(JobStatusEnum.FAILED);
        }        
        
        return scheduled.toDataCaptureScheduledJob();
    }
    
    @Override
    public DataCaptureJobScheduled stopDataCaptureScheduleJob(String id) throws JobNotFoundException {
        ScheduledJob job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        Log.info("Will close scheduled job: {}", job.getId());
        job.close();
        job.setDateFinished(new Date(System.currentTimeMillis()));
        job.setStatus(JobStatusEnum.STOPPED);
        
        return job.toDataCaptureScheduledJob();
        
        
    }
    
}
