package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataCaptureComboJobScheduled extends DataCaptureComboJob {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="indicates if the job will be send to a scheduler", required=true)
    private JobSchedule schedule;

    public DataCaptureComboJobScheduled() {
    }

    @JsonCreator
    public DataCaptureComboJobScheduled(
            @JsonProperty("schedule") JobSchedule schedule, 
            @JsonProperty("id") String id, 
            @JsonProperty("name") String name, 
            @JsonProperty("status") String status, 
            @JsonProperty("dateAdded") String dateAdded, 
            @JsonProperty("dateFinished") String dateFinished, 
            @JsonProperty("jobs") List<DataCaptureComboJobElement> jobs) {
        super(id, name, status, dateAdded, dateFinished, jobs);
        this.schedule = schedule;
    }

    public JobSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(JobSchedule schedule) {
        this.schedule = schedule;
    }
    
    
}
