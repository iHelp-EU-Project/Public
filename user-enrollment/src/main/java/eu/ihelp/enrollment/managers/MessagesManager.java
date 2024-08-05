package eu.ihelp.enrollment.managers;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.model.MessageDTO;
import eu.ihelp.enrollment.utils.DBAHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MessagesManager {
    private static final Logger Log = LoggerFactory.getLogger(MessagesManager.class);
    private static final String qInsertMessage = "INSERT INTO MESSAGES (TRIGGERID,\n" +
                                            "STUDYID,\n" +
                                            "DIALOGUEID,\n" +
                                            "SUBJECTID,\n" +
                                            "NOTES,\n" +
                                            "CREATEDBY,\n" +
                                            "CREATIONTIME,\n" +
                                            "SENTTIME,\n" +
                                            "STATUS,\n" +
                                            "TOPICCODE,\n" +
                                            "TOPICSYSTEM,\n" +
                                            "TOPICDISPLAY,\n" +
                                            "NOTIFICATION_MESSAGE,\n" +
                                            "COACHING_ELEMENT,\n" +
                                            "COACHING_DOMAIN,\n" +
                                            "COACHING_INTENT,\n" +
                                            "HEALTH_OUTCOME,\n" +
                                            "MESSAGE_FRAMING,\n" +
                                            "SUBJECT_ABILITY,\n" +
                                            "HEALTH_ORIENTATION) VALUES (\n" +
                                            "?, ?, ?, ?, ?, \n" +
                                            "?, ?, ?, ?, ?, \n" +
                                            "?, ?, ?, ?, ?, \n" +
                                            "?, ?, ?, ?, ? )";
    
    private static final String qGetMessages = "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES ORDER BY CREATIONTIME DESC ";
    private static final String qGetMessagesByPatientID = "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES WHERE SUBJECTID = ? ORDER BY CREATIONTIME DESC ";
    private static final String qGetMessagesByStatus = "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES WHERE STATUS = ? ORDER BY CREATIONTIME DESC ";
    private static final String qGetMessagesByPatientIDByStatus = "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES WHERE SUBJECTID = ? AND STATUS = ? ORDER BY CREATIONTIME DESC ";
    private static final String qGetMessagesByID = "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES \n" +                                        "WHERE TRIGGERID = ? AND  STUDYID = ? AND  DIALOGUEID = ? AND  SUBJECTID = ?";
    private static final String qUpdateMessages = "UPDATE APP.MESSAGES SET STATUS = ? WHERE TRIGGERID = ? AND  STUDYID = ? AND  DIALOGUEID = ? AND  SUBJECTID = ?"; 
    
    public static MessageDTO getMessage(String studyID, String subjectId, String dialogueId, String triggeredId) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(qGetMessagesByID)) {
            preparedStatement.setString(1, triggeredId);
            preparedStatement.setString(2, studyID);
            preparedStatement.setString(3, dialogueId);
            preparedStatement.setString(4, subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return buildMessage(resultSet);
            } else {
                return null;
            }
            
        } catch(SQLException ex) {
            Log.error("Could not get messages", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static void updateMessage(String studyID, String subjectId, String dialogueId, String triggeredId, String status) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(qUpdateMessages)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, triggeredId);
            preparedStatement.setString(3, studyID);
            preparedStatement.setString(4, dialogueId);
            preparedStatement.setString(5, subjectId);
            preparedStatement.executeUpdate();            
        } catch(SQLException ex) {
            Log.error("Could not get messages", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<MessageDTO> getMessages(String patientID, String status) throws DataStoreException {
        if((patientID!=null) && (patientID.isEmpty())) {
            patientID = null;
        }
        if((status!=null) && (status.isEmpty())) {
            status = null;
        }
        
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            if((patientID==null) && (status==null)) {
                return _getMessages(connection);
            }
            
            else if((patientID!=null) && (status==null)) {
                return _getMessagesByPatientID(connection, patientID);
            }
            
            else if((patientID==null) && (status!=null)) {
                return _getMessagesByStatus(connection, status);
            }
            
            else {
                return _getMessagesByPatientIDByStatus(connection, patientID, status);
            }
            
        } catch(SQLException ex) {
            Log.error("Could not get messages", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static List<MessageDTO> _getMessages(Connection conn) throws SQLException {
        List<MessageDTO> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement(qGetMessages)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(buildMessage(resultSet));
            }
        }
        return list;
    }
    
    private static List<MessageDTO> _getMessagesByPatientID(Connection conn, String patientID) throws SQLException {
        List<MessageDTO> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement(qGetMessagesByPatientID)) {
            preparedStatement.setString(1, patientID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(buildMessage(resultSet));
            }
        }
        return list;
    }
    
    private static List<MessageDTO> _getMessagesByStatus(Connection conn, String status) throws SQLException {
        List<MessageDTO> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement(qGetMessagesByStatus)) {
            preparedStatement.setString(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(buildMessage(resultSet));
            }
        }
        return list;
    }
    
    private static List<MessageDTO> _getMessagesByPatientIDByStatus(Connection conn, String patientID, String status) throws SQLException {
        List<MessageDTO> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement(qGetMessagesByPatientIDByStatus)) {
            preparedStatement.setString(1, patientID);
            preparedStatement.setString(2, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(buildMessage(resultSet));
            }
        }
        return list;
    }
    
    public static void insertMessage(List<MessageDTO> messages) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            for(MessageDTO message : messages) {
                try {
                    _insertMessage(connection, message);
                } catch(Exception ex) { /*do nothing*/}
            }            
        } catch(SQLException ex) {
            Log.error("Could not add messages of size {}", messages.size(), ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static void insertMessage(MessageDTO message) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            _insertMessage(connection, message);     
        } catch(SQLException ex) {
            Log.error("Could not add message {}", message, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }   
    
    
    public static void _insertMessage(Connection connection, MessageDTO message) throws DataStoreException {
        try(PreparedStatement statement = connection.prepareStatement(qInsertMessage)) {
            
            statement.setString(1, message.getTriggerID());
            statement.setString(2, message.getStudyID());
            statement.setString(3, message.getDialogueID());
            statement.setString(4, message.getSubjectID());
            statement.setString(5, message.getNotes());
            statement.setString(6, message.getCreatedBY());
            
            if(message.getCreationTime()!=null)  {
                statement.setTimestamp(7, new Timestamp(message.getCreationTime().getTime()));
            } else {
                statement.setNull(7, java.sql.Types.TIMESTAMP);
            }
            
            if(message.getSentDate()!=null)  {
                statement.setTimestamp(8, new Timestamp(message.getSentDate().getTime()));
            } else {
                statement.setNull(8, java.sql.Types.TIMESTAMP);
            }
            
            statement.setString(9, message.getStatus());
            statement.setString(10, message.getTopicCode());
            statement.setString(11, message.getTopicSystem());
            statement.setString(12, message.getTopicDisplay());
            statement.setString(13, message.getNotificationMessage());
            statement.setString(14, message.getCoachingElement());
            statement.setString(15, message.getCoachingDomain());
            statement.setString(16, message.getCoachingIntent());
            statement.setString(17, message.getHealthOutcome());
            statement.setString(18, message.getMessageFraming());
            statement.setString(19, message.getSubjectAbility());
            statement.setString(20, message.getHealthOrientation());
            
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            Log.error("Could not add message {}. {}", message, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static MessageDTO buildMessage(ResultSet resultSet) throws SQLException {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTriggerID(resultSet.getString("TRIGGERID"));
        messageDTO.setStudyID(resultSet.getString("STUDYID"));
        messageDTO.setDialogueID(resultSet.getString("DIALOGUEID"));
        messageDTO.setSubjectID(resultSet.getString("SUBJECTID"));
        messageDTO.setNotes(resultSet.getString("NOTES"));
        messageDTO.setCreatedBY(resultSet.getString("CREATEDBY"));
        messageDTO.setStatus(resultSet.getString("STATUS"));
        messageDTO.setTopicCode(resultSet.getString("TOPICCODE"));
        messageDTO.setTopicSystem(resultSet.getString("TOPICSYSTEM"));
        messageDTO.setTopicDisplay(resultSet.getString("TOPICDISPLAY"));
        messageDTO.setNotificationMessage(resultSet.getString("NOTIFICATION_MESSAGE"));
        messageDTO.setCoachingElement(resultSet.getString("COACHING_ELEMENT"));
        messageDTO.setCoachingDomain(resultSet.getString("COACHING_DOMAIN"));
        messageDTO.setCoachingIntent(resultSet.getString("COACHING_INTENT"));
        messageDTO.setHealthOutcome(resultSet.getString("HEALTH_OUTCOME"));
        messageDTO.setMessageFraming(resultSet.getString("MESSAGE_FRAMING"));
        messageDTO.setSubjectAbility(resultSet.getString("SUBJECT_ABILITY"));
        messageDTO.setHealthOrientation(resultSet.getString("HEALTH_ORIENTATION"));
        
        Timestamp date = resultSet.getTimestamp("CREATIONTIME");
        if(!resultSet.wasNull()) {
            messageDTO.setCreationTime(date);
        }
        
        date = resultSet.getTimestamp("SENTTIME");
        if(!resultSet.wasNull()) {
            messageDTO.setSentDate(date);
        }
        
        return messageDTO;
    }
}
