package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.model.ConnectorArgumentsAbstract;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsAbstract;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.DataCaptureJobScheduled;
import eu.ihelp.datacapture.commons.model.JobSchedule;
import eu.ihelp.datacapture.commons.model.JobScheduleFuture;
import eu.ihelp.datacapture.commons.model.JobSchedulePeriodic;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.DataCaptureThreadFactory;
import java.io.Closeable;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ScheduledJob implements Closeable {
    private final String id;
    private final String dataProvider;
    private final String dataset;
    private final Object schema;
    private final Object schemaKey;
    private final JSONObject confParameters;
    private final Date dateAdded;
    private final ConnectorArgumentsAbstract connectorArguments;
    private final ConverterArgumentsAbstract converterArguments;
    private final JobScheduleFuture future;
    private final JobSchedulePeriodic periodic;
    private Date dateFinished;
    private JobStatusEnum status;
    private final Integer batchSize;
    private final ScheduledExecutorService scheduler;

    protected ScheduledJob(String id, String dataProvider, String dataset, Schema schema, Schema schemaKey, JSONObject confParameters, Date dateAdded, ConnectorArgumentsAbstract connectorArguments, ConverterArgumentsAbstract converterArguments, JobScheduleFuture future, JobSchedulePeriodic periodic, Integer batchSize) {
        this.id = id;
        this.dataProvider = dataProvider;
        this.dataset = dataset;
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.confParameters = confParameters;
        this.dateAdded = dateAdded;
        this.connectorArguments = connectorArguments;
        this.converterArguments = converterArguments;
        this.future = future;
        this.periodic = periodic;
        this.batchSize = batchSize;
        
        this.scheduler = Executors.newScheduledThreadPool(1, new DataCaptureThreadFactory("scheduled-job-" + id, Thread.MIN_PRIORITY));
    }

    public String getId() {
        return id;
    }

    public String getDataProvider() {
        return dataProvider;
    }
    
    public String getDataset() {
        return dataset;
    }

    public Object getSchema() {
        return schema;
    }

    public Object getSchemaKey() {
        return schemaKey;
    }

    public JSONObject getConfParameters() {
        return confParameters;
    }
    
    public Date getDateAdded() {
        return dateAdded;
    }

    public ConnectorArgumentsAbstract getConnectorArguments() {
        return connectorArguments;
    }

    public ConverterArgumentsAbstract getConverterArguments() {
        return converterArguments;
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

    public Integer getBatchSize() {
        return batchSize;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final ScheduledJob other = (ScheduledJob) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ScheduledJob{" + "id=" + id + ", dataset=" + dataset + ", status=" + status + '}';
    }
    
    protected void init() {
        if((this.future==null) && (this.periodic==null)) {
            throw new IllegalArgumentException("future and periodic parameters cannot be both null");
        }
        
        ScheduledJobRunnable scheduledJobRunnable = new ScheduledJobRunnable(createDataCaptureJob());
        
        if(this.future==null) {
            this.scheduler.scheduleAtFixedRate(scheduledJobRunnable, 0, this.periodic.getTime(), TimeUnit.valueOf(this.periodic.getUnit()));
        } else if(this.periodic==null) {
            this.scheduler.schedule(scheduledJobRunnable, this.future.getTime(), TimeUnit.valueOf(this.future.getUnit()));
        } else {
            if(!future.getUnit().equals(periodic.getUnit())) {
                throw new IllegalArgumentException("future and periodic time units must be the same");
            }
            this.scheduler.scheduleAtFixedRate(scheduledJobRunnable, this.future.getTime(), this.periodic.getTime(), TimeUnit.valueOf(this.periodic.getUnit()));
        }
    }
    
    @Override
    public void close() {
        this.scheduler.shutdown();
    }
    
    private DataCaptureJob createDataCaptureJob() {
        return new DataCaptureJob(id, dataProvider, dataset, schema, schemaKey, confParameters, null, null, null, batchSize, connectorArguments, converterArguments);
    }
    
    public DataCaptureJobScheduled toDataCaptureScheduledJob() {
        return new DataCaptureJobScheduled(
                id, 
                dataProvider,
                dataset, 
                schema, 
                schemaKey, 
                confParameters,
                DataCaptureDateFormater.getStringFromDate(dateAdded), 
                DataCaptureDateFormater.getStringFromDate(dateFinished), 
                status.name(), 
                batchSize,
                connectorArguments,
                converterArguments, 
                new JobSchedule(future, periodic));
    }    
}
