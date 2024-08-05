package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureComboJobElement implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the order of this job to be executed in the combo", required=true)
    private int order;
    @ApiModelProperty(value="the job to be executed in the combo", required=true)
    private DataCaptureJob job;

    public DataCaptureComboJobElement() {}
    

    @JsonCreator
    public DataCaptureComboJobElement(
            @JsonProperty("order") int order,
            @JsonProperty("job") DataCaptureJob job) {
        this.order = order;
        this.job = job;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public DataCaptureJob getJob() {
        return job;
    }

    public void setJob(DataCaptureJob job) {
        this.job = job;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.order;
        hash = 29 * hash + Objects.hashCode(this.job);
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
        final DataCaptureComboJobElement other = (DataCaptureComboJobElement) obj;
        if (this.order != other.order) {
            return false;
        }
        return Objects.equals(this.job, other.job);
    }

    @Override
    public String toString() {
        return "DataCaptureJobComboElement{" + "order=" + order + ", job=" + job + '}';
    }
    
}
