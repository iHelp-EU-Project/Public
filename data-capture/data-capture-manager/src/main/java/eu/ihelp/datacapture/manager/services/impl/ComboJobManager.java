package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobElement;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.exceptions.InvalidComboJobException;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureComboService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ComboJobManager implements DataCaptureComboService {
    private static final Logger Log = LoggerFactory.getLogger(ComboJobManager.class);
    
    private final HashMap<String, ComboJob> cache;

    private ComboJobManager(HashMap<String, ComboJob> cache) {
        this.cache = cache;
    }
    
    private static final class ComboJobManagerHolder {
        private static final ComboJobManager INSTANCE = new ComboJobManager(new HashMap<>());
    }
    
    public static ComboJobManager getInstance() {
        return ComboJobManagerHolder.INSTANCE;
    }

    @Override
    public synchronized List<DataCaptureComboJob> getAllDataCaptureComboJobs() {
        List<DataCaptureComboJob> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, ComboJob>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue().toDataCaptureComboJob());
        }
        return list;
    }

    @Override
    public synchronized DataCaptureComboJob getDataCaptureComboJob(String id) throws JobNotFoundException {
        ComboJob comboJob = this.cache.get(id);
        if(comboJob==null) {
            throw new JobNotFoundException(id);
        }
        
        return comboJob.toDataCaptureComboJob();
    }

    @Override
    public synchronized DataCaptureComboJob addDataCaptureComboJob(DataCaptureComboJob dataCaptureJobCombo) throws InvalidComboJobException {
        if((dataCaptureJobCombo.getJobs()==null) || (dataCaptureJobCombo.getJobs().isEmpty())) {
            throw new InvalidComboJobException(dataCaptureJobCombo.getName());
        }
        
        TreeMap<Integer, Job> orderedJobs = new TreeMap<>();
        for(DataCaptureComboJobElement element : dataCaptureJobCombo.getJobs()) {
            DataCaptureJob dataCaptureJob = JobManager.getInstance().addJob(element.getJob());
            orderedJobs.put(element.getOrder(), JobManager.getInstance().getJob(dataCaptureJob.getId()));
        }
        
        ComboJob comboJob = new ComboJob(
                UniqueIDManager.getInstance().getNewID(), 
                dataCaptureJobCombo.getName(), 
                new Date(System.currentTimeMillis()), 
                orderedJobs);
        comboJob.setStatus(JobStatusEnum.SUBMITTED);
        this.cache.put(comboJob.getId(), comboJob);
        
        Log.info("Added new combo job: {}", comboJob);
        
        try {//now start the job
            comboJob.init();
        } catch(Exception ex) {
            Log.error("Error submitting job: {}. {}", comboJob, ex);
            comboJob.setDateFinished(new Date(System.currentTimeMillis()));
            comboJob.setStatus(JobStatusEnum.FAILED);
        }
        
        return comboJob.toDataCaptureComboJob();
    }
    
    
    public synchronized void setComboJobAsFailed(String id)  {
        setComboJobAsStartedFinishedFailed(id, ComboJobManager.SET_JOB_STATUS.FAILED);
    }
    
    public synchronized void setComboJobAsFinished(String id) {
        setComboJobAsStartedFinishedFailed(id, ComboJobManager.SET_JOB_STATUS.FINISHED);
    }
    
    public synchronized void seCombotJobAsStarted(String id) {
        setComboJobAsStartedFinishedFailed(id, ComboJobManager.SET_JOB_STATUS.STARTED);
    }
    
    private void setComboJobAsStartedFinishedFailed(String id, SET_JOB_STATUS flag) {
        ComboJob comboJob = this.cache.get(id);
        
        if(comboJob!=null) {
            comboJob.setDateFinished(new Date(System.currentTimeMillis()));
            if(flag==SET_JOB_STATUS.FAILED) {
                comboJob.close();
                comboJob.setStatus(JobStatusEnum.FAILED);
            } 
            if(flag==SET_JOB_STATUS.FINISHED) {
                comboJob.close();
                comboJob.setStatus(JobStatusEnum.FINISHED_CAPTURE);
            }
            if(flag==SET_JOB_STATUS.STARTED) {
                comboJob.setStatus(JobStatusEnum.STARTED);
            }
        }
    }
    
    private enum SET_JOB_STATUS { FAILED, FINISHED, STARTED }
}
