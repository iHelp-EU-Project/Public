package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobElement;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobScheduled;
import eu.ihelp.datacapture.commons.model.JobSchedule;
import eu.ihelp.datacapture.commons.model.JobScheduleFuture;
import eu.ihelp.datacapture.commons.model.JobSchedulePeriodic;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.DataCaptureThreadFactory;
import java.io.Closeable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ScheduledComboJob implements Closeable {
    private static final Logger Log = LoggerFactory.getLogger(ScheduledComboJob.class);
    
    private final String id;
    private final String name;
    private final Date dateAdded;
    private List<DataCaptureComboJobElement> jobs;
    private Date dateFinished;
    private JobStatusEnum status;
    private final JobScheduleFuture future;
    private final JobSchedulePeriodic periodic;
    private final ScheduledExecutorService scheduler;

    public ScheduledComboJob(String id, String name, Date dateAdded, List<DataCaptureComboJobElement> jobs, JobScheduleFuture future, JobSchedulePeriodic periodic) {
        this.id = id;
        this.name = name;
        this.dateAdded = dateAdded;
        this.jobs = jobs;
        this.future = future;
        this.periodic = periodic;
        
        this.scheduler = Executors.newScheduledThreadPool(1, new DataCaptureThreadFactory("scheduled-combo-job-" + id, Thread.MIN_PRIORITY));
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

    public JobScheduleFuture getFuture() {
        return future;
    }

    public JobSchedulePeriodic getPeriodic() {
        return periodic;
    }
    
    public Date getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(Date dateFinished) {
        this.dateFinished = dateFinished;
    }

    public JobStatusEnum getStatus() {
        return status;
    }

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.name);
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
        final ScheduledComboJob other = (ScheduledComboJob) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "ScheduledComboJob{" + "id=" + id + ", name=" + name + ", status=" + status + '}';
    }
    
    public void init() {
        if((this.future==null) && (this.periodic==null)) {
            throw new IllegalArgumentException("future and periodic parameters cannot be both null");
        }
        
        ScheduledComboJobRunnable scheduledComboJobRunnable = new ScheduledComboJobRunnable(createDataCaptureComboJob());
        
        if(this.future==null) {
            this.scheduler.scheduleAtFixedRate(scheduledComboJobRunnable, 0, this.periodic.getTime(), TimeUnit.valueOf(this.periodic.getUnit()));
        } else if(this.periodic==null) {
            this.scheduler.schedule(scheduledComboJobRunnable, this.future.getTime(), TimeUnit.valueOf(this.future.getUnit()));
        } else {
            if(!future.getUnit().equals(periodic.getUnit())) {
                throw new IllegalArgumentException("future and periodic time units must be the same");
            }
            this.scheduler.scheduleAtFixedRate(scheduledComboJobRunnable, this.future.getTime(), this.periodic.getTime(), TimeUnit.valueOf(this.periodic.getUnit()));
        }
    }
    
    @Override
    public void close() {
        this.scheduler.shutdown();
    }
    
    private DataCaptureComboJob createDataCaptureComboJob() {
        return new DataCaptureComboJob(id, name, null, null, null, jobs);
    }
    
    public DataCaptureComboJobScheduled toDataCaptureComboJobScheduled() {
        return new DataCaptureComboJobScheduled
                (new JobSchedule(future, periodic), 
                id, 
                name, 
                status.name(), 
                DataCaptureDateFormater.getStringFromDate(dateAdded), 
                DataCaptureDateFormater.getStringFromDate(dateFinished), 
                jobs);
    }
    
}
