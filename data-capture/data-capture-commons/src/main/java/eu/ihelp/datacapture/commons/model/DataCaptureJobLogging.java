package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataCaptureJobLogging implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the log's id", required=false)
    private String logID;
    @ApiModelProperty(value="the job's id", required=true)
    private String jobID;
    @ApiModelProperty(value="the name of the function that produced the log", required=true)
    private String functionName;
    @ApiModelProperty(value="the log's status", required=true)
    private String status;
    @ApiModelProperty(value="the log's message", required=true)
    private String message;
    @ApiModelProperty(value="the log's datetime", required=true)
    private String timestamp;

    public DataCaptureJobLogging() {
    }

    @JsonCreator
    public DataCaptureJobLogging(
            @JsonProperty("logID") String logID, 
            @JsonProperty("jobID") String jobID, 
            @JsonProperty("functionName") String functionName, 
            @JsonProperty("status") String status, 
            @JsonProperty("message") String message,
            @JsonProperty("timestamp") String timestamp) {
        this.logID = logID;
        this.jobID = jobID;
        this.functionName = functionName;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.logID);
        hash = 59 * hash + Objects.hashCode(this.jobID);
        hash = 59 * hash + Objects.hashCode(this.functionName);
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
        final DataCaptureJobLogging other = (DataCaptureJobLogging) obj;
        if (!Objects.equals(this.logID, other.logID)) {
            return false;
        }
        if (!Objects.equals(this.jobID, other.jobID)) {
            return false;
        }
        return Objects.equals(this.functionName, other.functionName);
    }

    @Override
    public String toString() {
        return "DataCaptureJobLogging{" + "logID=" + logID + ", jobID=" + jobID + ", functionName=" + functionName + ", status=" + status + ", message=" + message + ", timestamp=" + timestamp + '}';
    }
}
