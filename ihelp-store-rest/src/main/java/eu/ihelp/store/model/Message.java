package eu.ihelp.store.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected final String triggerID;
    protected final String studyID;
    protected final String dialogueID;
    protected final String subjectID;
    protected final String notificationMessage;
    protected final String status;
    protected final String notes;
    protected final String createdBY;
    protected final Date creationTime;
    protected final Date sentDate;
    protected final String topicCode;
    protected final String topicSystem;
    protected final String topicDisplay;
    protected final String coachingElement;
    protected final String coachingDomain;
    protected final String coachingIntent;
    protected final String healthOutcome;
    protected final String messageFraming;
    protected final String subjectAbility;
    protected final String healthOrientation;

    protected Message(String triggerID, String studyID, String dialogueID, String subjectID, String notificationMessage, String status, String notes, String createdBY, Date creationTime, Date sentDate, String topicCode, String topicSystem, String topicDisplay, String coachingElement, String coachingDomain, String coachingIntent, String healthOutcome, String messageFraming, String subjectAbility, String healthOrientation) {
        this.triggerID = triggerID;
        this.studyID = studyID;
        this.dialogueID = dialogueID;
        this.subjectID = subjectID;
        this.notificationMessage = notificationMessage;
        this.status = status;
        this.notes = notes;
        this.createdBY = createdBY;
        this.creationTime = creationTime;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTriggerID() {
        return triggerID;
    }

    public String getStudyID() {
        return studyID;
    }

    public String getDialogueID() {
        return dialogueID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedBY() {
        return createdBY;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public String getTopicSystem() {
        return topicSystem;
    }

    public String getTopicDisplay() {
        return topicDisplay;
    }

    public String getCoachingElement() {
        return coachingElement;
    }

    public String getCoachingDomain() {
        return coachingDomain;
    }

    public String getCoachingIntent() {
        return coachingIntent;
    }

    public String getHealthOutcome() {
        return healthOutcome;
    }

    public String getMessageFraming() {
        return messageFraming;
    }

    public String getSubjectAbility() {
        return subjectAbility;
    }

    public String getHealthOrientation() {
        return healthOrientation;
    }

    @Override
    public String toString() {
        return "Message{" + "triggerID=" + triggerID + ", studyID=" + studyID + ", dialogueID=" + dialogueID + ", subjectID=" + subjectID + ", notificationMessage=" + notificationMessage + ", status=" + status + '}';
    }
    
    
    
}
