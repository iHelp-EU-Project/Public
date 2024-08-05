package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import eu.ihelp.enrollment.utils.IhelpDateFormatter;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MessageSmallDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="subjectID", required=true)
    protected String triggerID;
    @ApiModelProperty(value="studyID", required=true)
    protected String studyID;
    @ApiModelProperty(value="subjectID", required=true)
    protected String dialogueID;
    @ApiModelProperty(value="subjectID", required=true)
    protected String subjectID;
    @ApiModelProperty(value="notificationMessage", required=false)
    protected String notificationMessage;
    @ApiModelProperty(value="status", required=false)
    protected String status;
    @ApiModelProperty(value="creationTime", required=false)
    protected Date creationTime;

    public MessageSmallDTO() {
    }

    @JsonCreator
    public MessageSmallDTO(
            @JsonProperty("triggerID") String triggerID, 
            @JsonProperty("studyID") String studyID, 
            @JsonProperty("dialogueID") String dialogueID, 
            @JsonProperty("subjectID") String subjectID, 
            @JsonProperty("subjectID") String notificationMessage, 
            @JsonProperty("status") String status,
            @JsonProperty("creationTime") Date creationTime) {
        this.triggerID = triggerID;
        this.studyID = studyID;
        this.dialogueID = dialogueID;
        this.subjectID = subjectID;
        this.notificationMessage = notificationMessage;
        this.status = status;
        this.creationTime = creationTime;
    }

    public String getTriggerID() {
        return triggerID;
    }

    public void setTriggerID(String triggerID) {
        this.triggerID = triggerID;
    }

    public String getStudyID() {
        return studyID;
    }

    public void setStudyID(String studyID) {
        this.studyID = studyID;
    }

    public String getDialogueID() {
        return dialogueID;
    }

    public void setDialogueID(String dialogueID) {
        this.dialogueID = dialogueID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    
    public String getCreationTimeStr() {
        return IhelpDateFormatter.getStringFromDate(this.creationTime);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.triggerID);
        hash = 59 * hash + Objects.hashCode(this.studyID);
        hash = 59 * hash + Objects.hashCode(this.dialogueID);
        hash = 59 * hash + Objects.hashCode(this.subjectID);
        hash = 59 * hash + Objects.hashCode(this.status);
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
        final MessageSmallDTO other = (MessageSmallDTO) obj;
        if (!Objects.equals(this.triggerID, other.triggerID)) {
            return false;
        }
        if (!Objects.equals(this.studyID, other.studyID)) {
            return false;
        }
        if (!Objects.equals(this.dialogueID, other.dialogueID)) {
            return false;
        }
        if (!Objects.equals(this.subjectID, other.subjectID)) {
            return false;
        }
        return Objects.equals(this.status, other.status);
    }

    @Override
    public String toString() {
        return "MessageSmallDTO{" + "triggerID=" + triggerID + ", studyID=" + studyID + ", dialogueID=" + dialogueID + ", subjectID=" + subjectID + ", notificationMessage=" + notificationMessage + ", status=" + status + '}';
    }
    
}
