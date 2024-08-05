package eu.ihelp.store.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MessageBuilder {
    private final ResultSet resultSet;
    protected String triggerID;
    protected String studyID;
    protected String dialogueID;
    protected String subjectID;
    protected String notificationMessage;
    protected String status;
    protected String notes;
    protected String createdBY;
    protected Date creationTime;
    protected Date sentDate;
    protected String topicCode;
    protected String topicSystem;
    protected String topicDisplay;
    protected String coachingElement;
    protected String coachingDomain;
    protected String coachingIntent;
    protected String healthOutcome;
    protected String messageFraming;
    protected String subjectAbility;
    protected String healthOrientation;

    private MessageBuilder(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    public static MessageBuilder createInstance(ResultSet resultSet) {
        return new MessageBuilder(resultSet);
    }
    
    public Map<String, List<Message>> buildAll() throws SQLException {
        List<Message> messages = new ArrayList<>();
        Message message = null;
        while((message = buildNext())!=null) {
            messages.add(message);
        }
        
        return messages.stream().collect(Collectors.groupingBy(Message::getSubjectID));
    }
    
    public Message buildNext() throws SQLException {
        if(resultSet.next()) {
            _restoreValues();
            _setValues();
            return _buildMessage();
        } else {
            return null;
        }
    }

    private void _setValues() throws SQLException {
        this.triggerID = resultSet.getString("TRIGGERID");
        this.studyID = resultSet.getString("STUDYID");
        this.dialogueID = resultSet.getString("DIALOGUEID");
        this.subjectID = resultSet.getString("SUBJECTID");
        this.notificationMessage = resultSet.getString("NOTIFICATION_MESSAGE");
        this.status = resultSet.getString("STATUS");
        this.notes = resultSet.getString("NOTES");
        this.createdBY = resultSet.getString("CREATEDBY");
        this.topicCode = resultSet.getString("TOPICCODE");
        this.topicSystem = resultSet.getString("TOPICSYSTEM");
        this.topicDisplay = resultSet.getString("TOPICDISPLAY");
        this.coachingElement = resultSet.getString("COACHING_ELEMENT");
        this.coachingDomain = resultSet.getString("COACHING_DOMAIN");
        this.coachingIntent = resultSet.getString("COACHING_INTENT");
        this.healthOutcome = resultSet.getString("HEALTH_OUTCOME");
        this.messageFraming = resultSet.getString("MESSAGE_FRAMING");
        this.subjectAbility = resultSet.getString("SUBJECT_ABILITY");
        this.healthOrientation = resultSet.getString("HEALTH_ORIENTATION");
        
        Timestamp timestamp = resultSet.getTimestamp("CREATIONTIME");
        if(!resultSet.wasNull()) {
            this.creationTime = new Date(timestamp.getTime());
        }
        timestamp = resultSet.getTimestamp("SENTTIME");
        if(!resultSet.wasNull()) {
            this.sentDate = new Date(timestamp.getTime());
        }
    }

    private void _restoreValues() {
        this.triggerID = null;
        this.studyID = null;
        this.dialogueID = null;
        this.subjectID = null;
        this.notificationMessage = null;
        this.status = null;
        this.notes = null;
        this.createdBY = null;
        this.creationTime = null;
        this.sentDate = null;
        this.topicCode = null;
        this.topicSystem = null;
        this.topicDisplay = null;
        this.coachingElement = null;
        this.coachingDomain = null;
        this.coachingIntent = null;
        this.healthOutcome = null;
        this.messageFraming = null;
        this.subjectAbility = null;
        this.healthOrientation = null;
    }
    
    private Message _buildMessage() {
        return new Message(triggerID, studyID, dialogueID, subjectID, notificationMessage, status, notes, createdBY, creationTime, sentDate, topicCode, topicSystem, topicDisplay, coachingElement, coachingDomain, coachingIntent, healthOutcome, messageFraming, subjectAbility, healthOrientation);
    }
}