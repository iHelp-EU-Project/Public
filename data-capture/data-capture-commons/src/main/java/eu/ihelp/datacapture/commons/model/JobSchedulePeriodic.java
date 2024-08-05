package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JobSchedulePeriodic {
    @ApiModelProperty(value="the schedule job's time (of the unit)", required=true)
    private int time;
    @ApiModelProperty(value="the schedule job's time unit", required=true)
    private String unit;

    @JsonCreator
    public JobSchedulePeriodic(
            @JsonProperty("time") int time,
            @JsonProperty("unit") String unit) {
        this.time = time;
        this.unit = unit;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
