package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsAbstract;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsAbstract;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.DataCaptureThreadFactory;
import eu.ihelp.datacapture.manager.JobRunnable;
import eu.ihelp.datacapture.manager.JobRunnableFactory;
import java.io.Closeable;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Job implements Closeable {
    private final String id;
    private final String dataProvider;
    private final String dataset;
    private final Schema schema;
    private final Schema schemaKey;
    private final JSONObject confParameters;
    private final Date dateAdded;
    private final ConnectorArgumentsAbstract connectorArguments;
    private final ConverterArgumentsAbstract converterArguments;
    private Date dateFinished;
    private JobStatusEnum status;
    private final Integer batchSize;
    private ExecutorService executor;    

    protected Job(String id, String dataProvider, String dataset, Schema schema, Schema schemaKey, JSONObject confParameters, Date dateAdded, ConnectorArgumentsAbstract connectorArguments, ConverterArgumentsAbstract converterArguments, Integer batchSize) {
        this.id = id;
        this.dataProvider = dataProvider;
        this.dataset = dataset;
        this.schema = schema;
        this.confParameters = confParameters;
        this.schemaKey = schemaKey;
        this.dateAdded = dateAdded;
        this.connectorArguments = connectorArguments;
        this.converterArguments = converterArguments;
        this.batchSize = batchSize;
        
        this.executor = Executors.newSingleThreadExecutor(new DataCaptureThreadFactory("job-" + this.id, Thread.NORM_PRIORITY));
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

    public Schema getSchema() {
        return schema;
    }

    public Schema getSchemaKey() {
        return schemaKey;
    }

    public JSONObject getConfParameters() {
        return confParameters;
    }
    
    protected Date getDateAdded() {
        return dateAdded;
    }

    public ConnectorArgumentsAbstract getConnectorArguments() {
        return connectorArguments;
    }

    public ConverterArgumentsAbstract getConverterArguments() {
        return converterArguments;
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

    protected void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    public Integer getBatchSize() {
        return batchSize;
    }
    
    protected DataCaptureJob toDataCaptureJob() {
        return new DataCaptureJob(id, dataProvider, dataset, schema, schemaKey, confParameters, DataCaptureDateFormater.getStringFromDate(dateAdded), DataCaptureDateFormater.getStringFromDate(dateFinished), status.name(), batchSize, connectorArguments, converterArguments);
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Job other = (Job) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Job{" + "id=" + id + ", dataset=" + dataset + ", dateAdded=" + DataCaptureDateFormater.getStringFromDate(dateAdded) + ", status=" + status + '}';
    }
    
    public Future init() throws DataCaptureInitException {
        JobRunnable jobRunnable = JobRunnableFactory.createInstance(id, dataProvider, dataset, schema, schemaKey, confParameters, batchSize, connectorArguments, converterArguments);
        jobRunnable.init();
        //this.executor.submit(jobRunnable);
        return this.executor.submit(jobRunnable);
    }

    @Override
    public void close() {
        this.executor.shutdownNow();
        this.executor = null;
    }
    
    
}
