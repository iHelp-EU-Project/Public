package eu.ihelp.enrollment.services.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.MessageNotFoundException;
import eu.ihelp.enrollment.exceptions.MessageNotProposedException;
import eu.ihelp.enrollment.exceptions.MonitoringMessageTransformException;
import eu.ihelp.enrollment.managers.MessagesManager;
import eu.ihelp.enrollment.model.MessageDTO;
import eu.ihelp.enrollment.model.MessageSmallDTO;
import eu.ihelp.enrollment.services.MonitoringMessagesService;
import eu.ihelp.enrollment.utils.Consts;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MonitoringMessagesServiceImpl implements MonitoringMessagesService {
    private static final Logger Log = LoggerFactory.getLogger(MonitoringMessagesServiceImpl.class);
    private static final String PATIENT_REFERENCE = "Patient/";
    
    final IParser parser = FhirContext.forR4().newJsonParser();
    
    private MonitoringMessagesServiceImpl() {}

    
    private static final class MonitoringMessagesServiceImplHolder {
        private static final MonitoringMessagesServiceImpl INSTANCE = new MonitoringMessagesServiceImpl();
    }
    
    public static MonitoringMessagesServiceImpl getInstance() {
        return MonitoringMessagesServiceImplHolder.INSTANCE;
    }
    
    
    @Override
    public void updateMessageStatus(String studyID, String subjectId, String dialogueId, String triggeredId, Consts.MonitoringMessages.HealthentiaStatus status) throws DataStoreException, MessageNotProposedException, MessageNotFoundException {
        MessageDTO message = MessagesManager.getMessage(studyID, subjectId, dialogueId, triggeredId);
        if(message==null) {
            throw new MessageNotFoundException(new MessageSmallDTO(triggeredId, studyID, dialogueId, subjectId, null, null, null));
        }
        
        String messageCurrentStatus = message.getStatus();
        if(messageCurrentStatus!=null) {
            if(!Consts.MonitoringMessages.HealthentiaStatus.proposed.name().equals(messageCurrentStatus.toLowerCase())) {
                throw new MessageNotProposedException(studyID, subjectId, dialogueId, triggeredId);
            }
        }
        
        MessagesManager.updateMessage(studyID, subjectId, dialogueId, triggeredId, status.name().toUpperCase());
    }
    @Override
    public List<MessageSmallDTO> getMessages(String ihelpID, String status) throws DataStoreException {
        List<MessageDTO> list = MessagesManager.getMessages(ihelpID, status);
        List<MessageSmallDTO> result = new ArrayList<>(list.size());
        for(MessageDTO messageDTO : list) {
            result.add(messageDTO.toMessageSmallDTO());
        }
        return result;
    }
    
    @Override
    public void insertMonitoringMessages(String input) throws MonitoringMessageTransformException, DataStoreException {
        try {
            Communication communication = parser.parseResource(Communication.class, input);
            MessageDTO message = transformToMessage(communication);
            MessagesManager.insertMessage(message);
            System.out.println(communication);
        } catch(MonitoringMessageTransformException | DataStoreException ex) {
            Log.error("Error while trying to add message. {}: {}", ex.getClass().getName(), ex.getMessage());
            throw ex;
        }
    }
    
    private MessageDTO transformToMessage(Communication communication) throws MonitoringMessageTransformException {
        MessageDTO message = new MessageDTO();
        
        if(communication.hasIdentifier()) {
            for(Identifier identifier : communication.getIdentifier()) {
                if(identifier.getSystem().equalsIgnoreCase(Consts.MonitoringMessages.IDENTIFIER_TRIGGERID)) {
                    message.setTriggerID(identifier.getValue());
                } else if(identifier.getSystem().equalsIgnoreCase(Consts.MonitoringMessages.IDENTIFIER_LOINC)) {
                    message.setStudyID(identifier.getValue());
                } else if(identifier.getSystem().equalsIgnoreCase(Consts.MonitoringMessages.IDENTIFIER_DIALOGUEID)) {
                    message.setDialogueID(identifier.getValue());
                } 
            }
        } else {
            throw new MonitoringMessageTransformException("No identifier can be found");
        }
        
        if(communication.hasSubject()) {
            String referenceSubject = communication.getSubject().getReference();
            if(referenceSubject.startsWith(PATIENT_REFERENCE)) { //transforms 'Patient/ihelpAccount' --> 'ihelpAccount'
                referenceSubject = referenceSubject.substring(PATIENT_REFERENCE.length());
            }
            message.setSubjectID(referenceSubject);
        } else {
            throw new MonitoringMessageTransformException("No subject can be found");
        }
        
        if(communication.hasStatus()) {
            String status = communication.getStatus().name();
            status = Consts.MonitoringMessages.MESSAGE_STATUS_FHIR_TO_HEALTHENTIA.get(status);
            message.setStatus(Consts.MonitoringMessages.MESSAGE_STATUS_FHIR_TO_HEALTHENTIA.get(communication.getStatus().name()));
        } else {
            throw new MonitoringMessageTransformException("No status can be found");
        }
        
        if((communication.hasTopic()) && (communication.getTopic().hasCoding())) {
            message.setTopicCode(communication.getTopic().getCodingFirstRep().getCode());
            message.setTopicSystem(communication.getTopic().getCodingFirstRep().getSystem());
            message.setTopicDisplay(communication.getTopic().getCodingFirstRep().getDisplay());
        }
        
        if(communication.hasSent()) {
            message.setSentDate(communication.getSent());
        }
        
        if(communication.hasNote()) {
            message.setNotes(communication.getNoteFirstRep().getText());
        }
        
        if((communication.hasExtension() && (!communication.getExtension().isEmpty()))) {
            for(Extension extension : communication.getExtension().get(0).getExtension()) {
                if(extension.getUrl()!=null) {
                    if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_CREATION_TIME)) && (extension.getValue() instanceof DateTimeType)) {
                        message.setCreationTime(((DateTimeType) extension.getValue()).getValue());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_COACH_DOMAIN)) && (extension.getValue() instanceof StringType)) {
                        message.setCoachingDomain(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_COACH_ELEMENT)) && (extension.getValue() instanceof StringType)) {
                        message.setCoachingElement(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_COACH_INTENT)) && (extension.getValue() instanceof StringType)) {
                        message.setCoachingIntent(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_HEALTH_ORIENTATION)) && (extension.getValue() instanceof StringType)) {
                        message.setHealthOrientation(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_HEALTH_OUTCOME)) && (extension.getValue() instanceof StringType)) {
                        message.setHealthOutcome(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_MESSAGE_FRAMING)) && (extension.getValue() instanceof StringType)) {
                        message.setMessageFraming(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_NOTIFICATION_MESSAGE)) && (extension.getValue() instanceof StringType)) {
                        message.setNotificationMessage(((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(Consts.MonitoringMessages.EXTENSION_SUBJECT_ABILITY)) && (extension.getValue() instanceof StringType)) {
                        message.setSubjectAbility(((StringType) extension.getValue()).getValueAsString());
                    }
                }
            }
        }
        return message;
    }
}
