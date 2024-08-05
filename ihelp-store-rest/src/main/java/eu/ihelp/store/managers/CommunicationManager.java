package eu.ihelp.store.managers;

import eu.ihelp.store.model.Message;
import eu.ihelp.store.model.MessageBuilder;
import eu.ihelp.store.server.exceptions.DataStoreException;
import eu.ihelp.store.server.utils.DBAHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class CommunicationManager {
    private static final Logger Log = LoggerFactory.getLogger(CommunicationManager.class);
    private static final String QUERY_GET_MESSAGES = "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES ";
    private static final String DATE_FIELD_NAME = "CREATIONTIME";
    
    public Message getMessage(int id, String sth) {
        Message message = null;
        
        return message;
    }
    
    public static Map<String, List<Message>> getMessagesBySubjectID(List<String> dates) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getFilterQuery(dates));
            ResultSet resultSet = statement.executeQuery();) {
            return MessageBuilder.createInstance(resultSet).buildAll();
        } catch(SQLException ex) {
            Log.error("Could not get communication messages for {}. {}", dates, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static String getFilterQuery(List<String> dates) {
        if(dates.isEmpty()) {
            return QUERY_GET_MESSAGES;
        }
        
        String result = QUERY_GET_MESSAGES + " WHERE ";
        
        boolean isFirstCondition = true;
        for(String dateStr : dates) {
            if(!isFirstCondition) {
                result+= " AND ";
            } else {
                isFirstCondition = false;
            }
            
            if((dateStr.startsWith("e")) && (Character.isDigit(dateStr.charAt(1)))) {
                result+= DATE_FIELD_NAME + "='" + dateStr.substring(1) + "' ";
            } else if((dateStr.startsWith("g")) && (Character.isDigit(dateStr.charAt(1)))) {
                result+= DATE_FIELD_NAME + ">'" + dateStr.substring(1) + "' ";
            } else if((dateStr.startsWith("l")) && (Character.isDigit(dateStr.charAt(1)))) {
                result+= DATE_FIELD_NAME + "<'" + dateStr.substring(1) + "' ";
            } else if(dateStr.startsWith("ge") && (Character.isDigit(dateStr.charAt(2)))) {
                result+= DATE_FIELD_NAME + ">='" + dateStr.substring(2) + "' ";
            } else if(dateStr.startsWith("le") && (Character.isDigit(dateStr.charAt(2)))) {
                result+= DATE_FIELD_NAME + "<='" + dateStr.substring(2) + "' ";
            } else {
                throw new RuntimeException("Unsupported date filter operation '" + dateStr + "'");
            }
        }
        
        return result;
    }
}
