package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataCaptureComboJob implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the combo job's id", required=false)
    private String id;
    @ApiModelProperty(value="the combo job's name", required=true)
    private String name;
    @ApiModelProperty(value="the combo job's status", required=false)
    private String status;
    @ApiModelProperty(value="the combo job's date that has been added", required=false)
    private String dateAdded;
    @ApiModelProperty(value="the combo job's date that has been finished", required=false)
    private String dateFinished;
    @ApiModelProperty(value="the list of the jobs for this combo", required=true)
    private List<DataCaptureComboJobElement> jobs;

    public DataCaptureComboJob() {
        this.jobs = new ArrayList<>();
    }
    

    @JsonCreator
    public DataCaptureComboJob(
            @JsonProperty("id") String id, 
            @JsonProperty("name") String name, 
            @JsonProperty("status") String status, 
            @JsonProperty("dateAdded") String dateAdded, 
            @JsonProperty("dateFinished") String dateFinished, 
            @JsonProperty("jobs") List<DataCaptureComboJobElement> jobs) {
        this.id = id;
        this.name = name;
        this.jobs = jobs;
        this.dateAdded = dateAdded;
        this.dateFinished = dateFinished;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    
    public List<DataCaptureComboJobElement> getJobs() {
        return jobs;
    }

    public void setJobs(List<DataCaptureComboJobElement> jobs) {
        this.jobs = jobs;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.name);
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
        final DataCaptureComboJob other = (DataCaptureComboJob) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "DataCaptureJobCombo{" + "id=" + id + ", name=" + name + ", jobs=" + jobs + '}';
    }
    
}
