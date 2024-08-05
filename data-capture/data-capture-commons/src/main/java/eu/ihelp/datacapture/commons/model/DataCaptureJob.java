package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataCaptureJob implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the job's id", required=false)
    private String id;
    @ApiModelProperty(value="the job's target dataset's data provider (i.e. FPG, HDM, etc)", required=true)
    private String datasourceID;
    @ApiModelProperty(value="the job's target dataset name to capture", required=true)
    private String datasetID;
    @ApiModelProperty(value="the job's target dataset schema of the value", required=true)
    private Object schema;
    @ApiModelProperty(value="the job's target dataset schema of the key", required=true)
    private Object schemaKey;
    @ApiModelProperty(value="the configuration parameters for the functions inolved in the data ingestion pipeline", required=true)
    private Object confParameters;
    @ApiModelProperty(value="the job's date that has been added", required=false)
    private String dateAdded;
    @ApiModelProperty(value="the job's date that has been finished/failed", required=false)
    private String dateFinished;
    @ApiModelProperty(value="the job's status", required=false)
    private String status;
    @ApiModelProperty(value="the job's arguments for its specific connectors", required=true)
    private ConnectorArgumentsAbstract connectorArguments;
    @ApiModelProperty(value="the job's arguments for its specific converter", required=true)
    private ConverterArgumentsAbstract converterArguments;
    @ApiModelProperty(value="the size of the batch to be sent each time", required=false)
    private Integer batchSize;

    public DataCaptureJob() {
    }

    @JsonCreator
    public DataCaptureJob(
            @JsonProperty("id") String id, 
            @JsonProperty("datasourceID") String datasourceID, 
            @JsonProperty("datasetID") String datasetID, 
            @JsonProperty("schema") Object schema, 
            @JsonProperty("schemaKey") Object schemaKey, 
            @JsonProperty("confParameters") Object confParameters, 
            @JsonProperty("dateAdded") String dateAdded, 
            @JsonProperty("dateFinished") String dateFinished, 
            @JsonProperty("status") String status, 
            @JsonProperty("batchSize") Integer batchSize, 
            @JsonProperty("connectorArguments") ConnectorArgumentsAbstract connectorArguments,
            @JsonProperty("converterArguments") ConverterArgumentsAbstract converterArguments) {
        this.id = id;
        this.datasourceID = datasourceID;
        this.datasetID = datasetID;
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.confParameters = confParameters;
        this.dateAdded = dateAdded;
        this.dateFinished = dateFinished;
        this.status = status;
        this.batchSize = batchSize;
        this.connectorArguments = connectorArguments;
        this.converterArguments = converterArguments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatasetID() {
        return datasetID;
    }

    public String getDatasourceID() {
        return datasourceID;
    }

    public void setDatasourceID(String datasourceID) {
        this.datasourceID = datasourceID;
    }
    
    public void setDatasetID(String datasetID) {
        this.datasetID = datasetID;
    }
    
    @JsonIgnore
    public Schema getAvroSchema() {
        if((schema!=null) && (schema instanceof Schema)) {
            return (Schema) schema;
        }
        return null;
    }
    
    public Object getSchema() {
        if(schema instanceof Schema) {
            return new JSONObject(((Schema) schema).toString()).toMap();
        } else {
            return schema;
        }
    }

    public void setSchema(Object schema) {
        this.schema = schema;
    }
    
    @JsonIgnore
    public Schema getAvroSchemaKey() {
        if((schemaKey!=null) && (schemaKey instanceof Schema)) {
            return (Schema) schemaKey;
        }
        return null;
    }

    public Object getSchemaKey() {
        if(schemaKey instanceof Schema) {
            return new JSONObject(((Schema) schemaKey).toString()).toMap();
        } else {
            return schemaKey;
        }
    }

    public void setSchemaKey(Object schemaKey) {
        this.schemaKey = schemaKey;
    }

    public Object getConfParameters() {
        if(confParameters instanceof JSONObject) {
            return ((JSONObject) confParameters).toMap();
        }
        return confParameters;
    }

    public void setConfParameters(Object confParameters) {
        this.confParameters = confParameters;
    }
    
    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public ConnectorArgumentsAbstract getConnectorArguments() {
        return connectorArguments;
    }

    public void setConnectorArguments(ConnectorArgumentsAbstract connectorArguments) {
        this.connectorArguments = connectorArguments;
    }

    public ConverterArgumentsAbstract getConverterArguments() {
        return converterArguments;
    }

    public void setConverterArguments(ConverterArgumentsAbstract converterArguments) {
        this.converterArguments = converterArguments;
    }
    
    
}
