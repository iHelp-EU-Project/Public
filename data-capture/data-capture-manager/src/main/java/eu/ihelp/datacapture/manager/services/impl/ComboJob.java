package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobElement;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.DataCaptureThreadFactory;
import eu.ihelp.datacapture.manager.ComboJobRunnable;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ComboJob implements Closeable  {
    private static final Logger Log = LoggerFactory.getLogger(ComboJob.class);
    
    private final String id;
    private final String name;
    private final Date dateAdded;
    private Date dateFinished;
    private JobStatusEnum status;
    private final TreeMap<Integer, Job> comboJobs;
    
    private ExecutorService executor;    

    public ComboJob(String id, String name, Date dateAdded, TreeMap<Integer, Job> comboJobs) {
        this.id = id;
        this.name = name;
        this.dateAdded = dateAdded;
        this.comboJobs = comboJobs;
        
        this.executor = Executors.newSingleThreadExecutor(new DataCaptureThreadFactory("combojob-" + this.id, Thread.NORM_PRIORITY));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(Date dateFinished) {
        this.dateFinished = dateFinished;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
    
    public JobStatusEnum getStatus() {
        return status;
    }

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }
    
    public TreeMap<Integer, Job> getComboJobs() {
        return comboJobs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComboJob other = (ComboJob) obj;
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public String toString() {
        return "ComboJob{" + "id=" + id + ", name=" + name + ", dateAdded=" + DataCaptureDateFormater.getStringFromDate(dateAdded) + ", status=" + status + '}';
    }
    
    protected DataCaptureComboJob toDataCaptureComboJob() {
        List<DataCaptureComboJobElement> list = new ArrayList<>(this.comboJobs.size());
        Iterator<Map.Entry<Integer, Job>> it = this.comboJobs.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, Job> entry = it.next();
            list.add(new DataCaptureComboJobElement(entry.getKey(), entry.getValue().toDataCaptureJob()));
        }
        
        return new DataCaptureComboJob(id, name, status.name(), DataCaptureDateFormater.getStringFromDate(dateAdded), DataCaptureDateFormater.getStringFromDate(dateFinished), list);
    }
    
    protected void init() throws DataCaptureInitException {
        Log.info("{}-{} is being initialized...", getClass().getSimpleName(), this.id);
        ComboJobRunnable comboJobRunnable = new ComboJobRunnable(id, comboJobs);
        this.executor.submit(comboJobRunnable);
    }
    
    @Override
    public void close() {
        this.executor.shutdownNow();
        this.executor = null;
    }
}
