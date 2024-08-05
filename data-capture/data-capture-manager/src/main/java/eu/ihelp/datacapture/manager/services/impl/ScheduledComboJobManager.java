package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureComboJobScheduled;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.exceptions.InvalidComboJobException;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureComboScheduledService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ScheduledComboJobManager implements DataCaptureComboScheduledService {
    private static final Logger Log = LoggerFactory.getLogger(ScheduledComboJobManager.class);
    
    private final HashMap<String, ScheduledComboJob> cache;

    private ScheduledComboJobManager(HashMap<String, ScheduledComboJob> cache) {
        this.cache = cache;
    }
    
    private static final class ScheduledComboJobManagerHolder {
        private static final ScheduledComboJobManager INSTANCE = new ScheduledComboJobManager(new HashMap<>());
    }
    
    public static ScheduledComboJobManager getInstance() {
        return ScheduledComboJobManagerHolder.INSTANCE;
    }

    @Override
    public List<DataCaptureComboJobScheduled> getAllDataCaptureScheduleComboJobs() {
        List<DataCaptureComboJobScheduled> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, ScheduledComboJob>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue().toDataCaptureComboJobScheduled());
        }
        return list;
    }

    @Override
    public DataCaptureComboJobScheduled getDataCaptureScheduleComboJob(String id) throws JobNotFoundException {
        ScheduledComboJob job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        return job.toDataCaptureComboJobScheduled();
    }

    @Override
    public DataCaptureComboJobScheduled stopDataCaptureScheduleComboJob(String id) throws JobNotFoundException {
        ScheduledComboJob job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        Log.info("Will close scheduled job: {}", job.getId());
        job.close();
        job.setDateFinished(new Date(System.currentTimeMillis()));
        job.setStatus(JobStatusEnum.STOPPED);
        
        return job.toDataCaptureComboJobScheduled();
    }
    
    
    @Override
    public DataCaptureComboJobScheduled addDataCaptureScheduleComboJob(DataCaptureComboJobScheduled dataCaptureJobScheduled) throws InvalidComboJobException {
        if((dataCaptureJobScheduled.getJobs()==null) || (dataCaptureJobScheduled.getJobs().isEmpty())) {
            throw new InvalidComboJobException(dataCaptureJobScheduled.getName());
        }
        
        ScheduledComboJob scheduledComboJob = new ScheduledComboJob(
                UniqueIDManager.getInstance().getNewID(), 
                dataCaptureJobScheduled.getName(), 
                new Date(System.currentTimeMillis()), 
                dataCaptureJobScheduled.getJobs(), 
                dataCaptureJobScheduled.getSchedule().getFuture(), 
                dataCaptureJobScheduled.getSchedule().getPeriodic());
        
        scheduledComboJob.setStatus(JobStatusEnum.STARTED);
        this.cache.put(scheduledComboJob.getId(), scheduledComboJob);
        
        try {
            Log.info("Initializing scheduled combo job: {}", scheduledComboJob);
            scheduledComboJob.init();
        } catch(Exception ex) {
            Log.error("Error initializing scheduled job {}. {}", scheduledComboJob.getId(), ex);
            scheduledComboJob.setDateFinished(new Date(System.currentTimeMillis()));
            scheduledComboJob.setStatus(JobStatusEnum.FAILED);
        }        
        
        return scheduledComboJob.toDataCaptureComboJobScheduled();
        
    }
    
}
