package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.Consts;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.services.DataCaptureService;
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
public class JobManager implements DataCaptureService {
    private static final Logger Log = LoggerFactory.getLogger(JobManager.class);
    
    private final HashMap<String, Job> cache;

    private JobManager(HashMap<String, Job> cache) {
        this.cache = cache;
    }

    private static final class JobManagerHOLDER {
        private static final JobManager INSTANCE = new JobManager(new HashMap<>());
    }

    public static JobManager getInstance() {
        return JobManagerHOLDER.INSTANCE;
    }
    
    @Override
    public synchronized List<DataCaptureJob> getAllDataCaptureJobs() {
        List<DataCaptureJob> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, Job>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue().toDataCaptureJob());
        }
        return list;
    }

    @Override
    public synchronized DataCaptureJob getDataCaptureJob(String id) throws JobNotFoundException {
        Job job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        return job.toDataCaptureJob();
    }

    @Override
    public synchronized DataCaptureJob addDataCaptureJob(DataCaptureJob dataCaptureJob) {
        
        dataCaptureJob = addJob(dataCaptureJob);
        Job job = this.cache.get(dataCaptureJob.getId());
        
        try {//now start the job
            job.init();
        } catch(Exception ex) {
            Log.error("Error submitting job: {}. {}", job, ex);
            job.setDateFinished(new Date(System.currentTimeMillis()));
            job.setStatus(JobStatusEnum.FAILED);
        }
        
        return job.toDataCaptureJob();
    }
    
    @Override
    public synchronized DataCaptureJob setDataCaptureJobFinished(String id) throws JobNotFoundException {
        Job job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        setJobAsFinished(id);
        
        return job.toDataCaptureJob();
    }
    
    protected synchronized Job getJob(String id) {
        Job job = this.cache.get(id);
        return job;
    }
    
    protected synchronized DataCaptureJob addJob(DataCaptureJob dataCaptureJob) {
        Schema schema;
        if(dataCaptureJob.getAvroSchema()!=null) {
            schema = dataCaptureJob.getAvroSchema();
        } else {
            if(dataCaptureJob.getSchema()==null) {
                schema = null;
            } else {
                if(dataCaptureJob.getSchema() instanceof Schema) {
                    schema = (Schema) dataCaptureJob.getSchema();
                } else {
                    java.util.LinkedHashMap object = (java.util.LinkedHashMap) dataCaptureJob.getSchema();
                    JSONObject json = new JSONObject(object);
                    Schema.Parser parser = new Schema.Parser();
                    schema = parser.parse(json.toString());
                }
            }
        }
        
        Schema schemaKey;
        if(dataCaptureJob.getAvroSchemaKey()!=null) {
            schemaKey = dataCaptureJob.getAvroSchema();
        } else {
            if(dataCaptureJob.getSchemaKey()==null) {
                schemaKey = null;
            } else {
                if(dataCaptureJob.getSchemaKey() instanceof Schema) {
                    schemaKey = (Schema) dataCaptureJob.getSchemaKey();
                } else {
                    java.util.LinkedHashMap object = (java.util.LinkedHashMap) dataCaptureJob.getSchemaKey();
                    JSONObject json = new JSONObject(object);
                    Schema.Parser parser = new Schema.Parser();
                    schemaKey = parser.parse(json.toString());
                }
            }
        }
        
        JSONObject confParameters = (dataCaptureJob.getConfParameters()==null) 
                ? new JSONObject() 
                : new JSONObject((LinkedHashMap) dataCaptureJob.getConfParameters());
        
        int batchSize = (dataCaptureJob.getBatchSize()==null) ? Consts.MAX_BATCH_SIZE : dataCaptureJob.getBatchSize();
        
        Job job = new Job(
                UniqueIDManager.getInstance().getNewID(), 
                dataCaptureJob.getDatasourceID(),
                dataCaptureJob.getDatasetID(), 
                schema, 
                schemaKey,
                confParameters,
                new Date(System.currentTimeMillis()), 
                dataCaptureJob.getConnectorArguments(), 
                dataCaptureJob.getConverterArguments(),
                batchSize);
        job.setStatus(JobStatusEnum.SUBMITTED);
        this.cache.put(job.getId(), job);
        
        Log.info("Added new job: {}", job);
        
        return job.toDataCaptureJob();
    }
    
    
    public synchronized void setJobAsFailed(String id)  {
        setJobAsStartedFinishedFailed(id, SET_JOB_STATUS.FAILED);
    }
    
    public synchronized void setJobAsFinishedCaptured(String id) {
        setJobAsStartedFinishedFailed(id, SET_JOB_STATUS.FINISHED_CAPTURE);
    }
    
    public synchronized void setJobAsStarted(String id) {
        setJobAsStartedFinishedFailed(id, SET_JOB_STATUS.STARTED);
    }
    
    public synchronized void setJobAsFinished(String id) {
        setJobAsStartedFinishedFailed(id, SET_JOB_STATUS.FINISHED);
    }
    
    private void setJobAsStartedFinishedFailed(String id, SET_JOB_STATUS flag) {
        Job job = this.cache.get(id);
        
        if(job!=null) {
            job.setDateFinished(new Date(System.currentTimeMillis()));
            if(flag==SET_JOB_STATUS.FAILED) {
                job.close();
                job.setStatus(JobStatusEnum.FAILED);
            } 
            if(flag==SET_JOB_STATUS.FINISHED_CAPTURE) {
                job.close();
                if(job.getStatus()!=JobStatusEnum.FINISHED) { //it might have been already finished ingesting
                    job.setStatus(JobStatusEnum.FINISHED_CAPTURE);
                }
            }
            if(flag==SET_JOB_STATUS.STARTED) {
                job.setStatus(JobStatusEnum.STARTED);
            }
            
            if(flag==SET_JOB_STATUS.FINISHED) {
                job.setStatus(JobStatusEnum.FINISHED);
            }
        }
    }
    
    private enum SET_JOB_STATUS { FAILED, FINISHED_CAPTURE, STARTED, FINISHED }
    
}
