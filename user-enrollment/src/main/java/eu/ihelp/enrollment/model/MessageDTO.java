package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO extends MessageSmallDTO {
    @ApiModelProperty(value="notes", required=false)
    protected String notes;
    @ApiModelProperty(value="createdBY", required=false)
    protected String createdBY;
    @ApiModelProperty(value="sentDate", required=false)
    protected Date sentDate;
    @ApiModelProperty(value="topicCode", required=false)
    protected String topicCode;
    @ApiModelProperty(value="topicSystem", required=false)
    protected String topicSystem;
    @ApiModelProperty(value="topicDisplay", required=false)
    protected String topicDisplay;
    @ApiModelProperty(value="coachingElement", required=false)
    protected String coachingElement;
    @ApiModelProperty(value="coachingDomain", required=false)
    protected String coachingDomain;
    @ApiModelProperty(value="coachingIntent", required=false)
    protected String coachingIntent;
    @ApiModelProperty(value="healthOutcome", required=false)
    protected String healthOutcome;
    @ApiModelProperty(value="messageFraming", required=false)
    protected String messageFraming;
    @ApiModelProperty(value="subjectAbility", required=false)
    protected String subjectAbility;
    @ApiModelProperty(value="healthOrientation", required=false)
    protected String healthOrientation;

    public MessageDTO() {
    }

    @JsonCreator
    public MessageDTO(
            @JsonProperty("triggerID") String triggerID, 
            @JsonProperty("studyID") String studyID, 
            @JsonProperty("dialogueID") String dialogueID, 
            @JsonProperty("subjectID") String subjectID, 
            @JsonProperty("notes") String notes, 
            @JsonProperty("createdBY") String createdBY, 
            @JsonProperty("creationTime") Date creationTime, 
            @JsonProperty("sentDate") Date sentDate, 
            @JsonProperty("status") String status, 
            @JsonProperty("topicCode") String topicCode, 
            @JsonProperty("topicSystem") String topicSystem, 
            @JsonProperty("topicDisplay") String topicDisplay, 
            @JsonProperty("notificationMessage") String notificationMessage, 
            @JsonProperty("coachingElement") String coachingElement, 
            @JsonProperty("coachingDomain") String coachingDomain, 
            @JsonProperty("coachingIntent") String coachingIntent, 
            @JsonProperty("healthOutcome") String healthOutcome, 
            @JsonProperty("messageFraming") String messageFraming, 
            @JsonProperty("subjectAbility") String subjectAbility, 
            @JsonProperty("healthOrientation") String healthOrientation) {
        super(triggerID, studyID, dialogueID, subjectID, notificationMessage, status, creationTime);
        this.notes = notes;
        this.createdBY = createdBY;
        this.sentDate = sentDate;
        this.topicCode = topicCode;
        this.topicSystem = topicSystem;
        this.topicDisplay = topicDisplay;
        this.coachingElement = coachingElement;
        this.coachingDomain = coachingDomain;
        this.coachingIntent = coachingIntent;
        this.healthOutcome = healthOutcome;
        this.messageFraming = messageFraming;
        this.subjectAbility = subjectAbility;
        this.healthOrientation = healthOrientation;
    }

    

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(String createdBY) {
        this.createdBY = createdBY;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    public String getTopicSystem() {
        return topicSystem;
    }

    public void setTopicSystem(String topicSystem) {
        this.topicSystem = topicSystem;
    }

    public String getTopicDisplay() {
        return topicDisplay;
    }

    public void setTopicDisplay(String topicDisplay) {
        this.topicDisplay = topicDisplay;
    }

    public String getCoachingElement() {
        return coachingElement;
    }

    public void setCoachingElement(String coachingElement) {
        this.coachingElement = coachingElement;
    }

    public String getCoachingDomain() {
        return coachingDomain;
    }

    public void setCoachingDomain(String coachingDomain) {
        this.coachingDomain = coachingDomain;
    }

    public String getCoachingIntent() {
        return coachingIntent;
    }

    public void setCoachingIntent(String coachingIntent) {
        this.coachingIntent = coachingIntent;
    }

    public String getHealthOutcome() {
        return healthOutcome;
    }

    public void setHealthOutcome(String healthOutcome) {
        this.healthOutcome = healthOutcome;
    }

    public String getMessageFraming() {
        return messageFraming;
    }

    public void setMessageFraming(String messageFraming) {
        this.messageFraming = messageFraming;
    }

    public String getSubjectAbility() {
        return subjectAbility;
    }

    public void setSubjectAbility(String subjectAbility) {
        this.subjectAbility = subjectAbility;
    }

    public String getHealthOrientation() {
        return healthOrientation;
    }

    public void setHealthOrientation(String healthOrientation) {
        this.healthOrientation = healthOrientation;
    }

    public MessageSmallDTO toMessageSmallDTO() {
        return new MessageSmallDTO(triggerID, studyID, dialogueID, subjectID, notificationMessage, status, creationTime);
    }
    
}
