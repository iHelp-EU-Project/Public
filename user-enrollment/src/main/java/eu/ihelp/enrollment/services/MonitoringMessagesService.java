package eu.ihelp.enrollment.services;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.MessageNotFoundException;
import eu.ihelp.enrollment.exceptions.MessageNotProposedException;
import eu.ihelp.enrollment.exceptions.MonitoringMessageTransformException;
import eu.ihelp.enrollment.model.MessageSmallDTO;
import eu.ihelp.enrollment.utils.Consts;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface MonitoringMessagesService {
    
    public void insertMonitoringMessages(String input) throws MonitoringMessageTransformException, DataStoreException;
    
    public void updateMessageStatus(String studyID, String subjectId, String dialogueId, String triggeredId, Consts.MonitoringMessages.HealthentiaStatus status) throws DataStoreException, MessageNotProposedException, MessageNotFoundException;
    
    public List<MessageSmallDTO> getMessages(String ihelpID, String status) throws DataStoreException;
    
}
