package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JobSchedule {
    @ApiModelProperty(value="indicates if the job will be schedule to start in the future", required=true)
    private JobScheduleFuture future;
    @ApiModelProperty(value="indicates if the job will be schedule to start periodically", required=true)
    private JobSchedulePeriodic  periodic;

    @JsonCreator
    public JobSchedule(
            @JsonProperty("future") JobScheduleFuture future,
            @JsonProperty("periodic") JobSchedulePeriodic periodic) {
        this.future = future;
        this.periodic = periodic;
    }

    public JobScheduleFuture getFuture() {
        return future;
    }

    public void setFuture(JobScheduleFuture future) {
        this.future = future;
    }

    public JobSchedulePeriodic getPeriodic() {
        return periodic;
    }

    public void setPeriodic(JobSchedulePeriodic periodic) {
        this.periodic = periodic;
    }

    
}
