package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureJobScheduled extends DataCaptureJob {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="indicates if the job will be send to a scheduler", required=true)
    private JobSchedule schedule;

    public DataCaptureJobScheduled() {
    }

    @JsonCreator
    public DataCaptureJobScheduled(
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
            @JsonProperty("converterArguments") ConverterArgumentsAbstract converterArguments,
            @JsonProperty("schedule") JobSchedule schedule) {
        super(id, datasourceID, datasetID, schema, schemaKey, confParameters, dateAdded, dateFinished, status, batchSize, connectorArguments, converterArguments);
        this.schedule = schedule;
    }

    public JobSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(JobSchedule schedule) {
        this.schedule = schedule;
    }
    
    
}
