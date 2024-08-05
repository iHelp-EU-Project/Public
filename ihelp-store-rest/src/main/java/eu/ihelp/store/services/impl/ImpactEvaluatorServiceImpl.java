package eu.ihelp.store.services.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.ihelp.store.managers.CommunicationManager;
import eu.ihelp.store.managers.MeasurementManager;
import eu.ihelp.store.managers.ObservationManager;
import eu.ihelp.store.managers.PatientManager;
import eu.ihelp.store.managers.QuestionnaireResponseManager;
import eu.ihelp.store.model.Measurement;
import eu.ihelp.store.model.Message;
import eu.ihelp.store.model.Observation;
import eu.ihelp.store.model.Patient;
import eu.ihelp.store.model.QuestionnaireResponse;
import eu.ihelp.store.server.exceptions.DataStoreException;
import eu.ihelp.store.server.utils.Consts;
import eu.ihelp.store.server.utils.TomcatServerProperties;
import eu.ihelp.store.services.ImpactEvaluatorService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ImpactEvaluatorServiceImpl implements ImpactEvaluatorService {
    private static final Logger Log = LoggerFactory.getLogger(ImpactEvaluatorServiceImpl.class);
    
    static final FhirContext ctx = FhirContext.forR4();
    static final IParser parser = ctx.newJsonParser();
    static final String METADATA_FILE = "hapi-metainfo.json";
    
    
    public static void main(String [] args) {
        org.hl7.fhir.r4.model.Observation observation = new org.hl7.fhir.r4.model.Observation();
        observation.setValue(new Quantity(null, 3, null, null, "cicles"));
        
        
        
        org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
        
        patient.setId("OT1IP");
        patient.setBirthDate(new Date());
        patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        patient.setActive(true);
        
        //new Extension(METADATA_FILE, new IBaseDecimalDatatype)
        Extension extension = new Extension("https://fhir.ihelp-project.eu/StructureDefinition/");
        patient.setExtension(Arrays.asList(extension));
        extension.getExtension().add(new Extension("https://fhir.ihelp-project.eu/StructureDefinition/height", new DecimalType(180.0)));
        extension.getExtension().add(new Extension("https://fhir.ihelp-project.eu/StructureDefinition/weight", new DecimalType(68.0 )));
        extension.getExtension().add(new Extension("https://fhir.ihelp-project.eu/StructureDefinition/timezone", new StringType("Europe/Rome")));
        extension.getExtension().add(new Extension("https://fhir.ihelp-project.eu/StructureDefinition/baselineDate", new DateTimeType(new Date())));
        
        IParser parser = ctx.newJsonParser();
        String string = parser.encodeResourceToString(patient);
        System.out.println(string);
        
        string = parser.encodeResourceToString(observation);
        System.out.println(string);
        
    }

    @Override
    public JSONObject getMetaData() throws IOException {
        
        
        try {/*
            List<Patient> patients = Collections.EMPTY_LIST;
            final Bundle bundle = new Bundle();
            bundle.setType(Bundle.BundleType.SEARCHSET);
            bundle.setId(UUID.randomUUID().toString());
            Meta meta = new Meta();
            meta.setLastUpdated(new Date(LocalDate.now().toEpochDay()));
            bundle.setMeta(meta);

            final Bundle.BundleEntrySearchComponent bundleEntrySearchComponent = new Bundle.BundleEntrySearchComponent();
            bundleEntrySearchComponent.setMode(Bundle.SearchEntryMode.MATCH);

            patients.stream().forEach((Patient p) -> {            
                bundle.addEntry()
                        .setResource(getFHIRPatient(p))
                        .setSearch(bundleEntrySearchComponent)
                        .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/Patient/");
            });           
            
            IParser parser = ctx.newJsonParser();
            String string = parser.encodeResourceToString(bundle);
            System.out.println(string);
            */
            
            String json = readJsonFromFile(METADATA_FILE);
            return new JSONObject(json);
        } catch(IOException ex) {
            Log.error("No valid path to retrieve the metadata: {}", METADATA_FILE);
            throw ex;
        }
    }
    
    @Override
    public String getQuestionnaireResponses(List<String> date, String subject, String questionnaireID) throws DataStoreException {
        List<Patient> patients = PatientManager.getPatients(Arrays.asList(subject));
        List<QuestionnaireResponse> questionnaireResponses = QuestionnaireResponseManager.getQuestionnaireResponses(date, patients, questionnaireID);
        
        Map<String, Map<Date, List<QuestionnaireResponse>>> groupedQuestionnaires = questionnaireResponses.stream()
                .collect(groupingBy(QuestionnaireResponse::getQuestionnaireID, 
                         groupingBy(QuestionnaireResponse::getAuthoredDate)));
        
        final Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(UUID.randomUUID().toString());
        Meta meta = new Meta();
        meta.setLastUpdated(new Date(LocalDate.now().toEpochDay()));
        bundle.setMeta(meta);
        
        final Bundle.BundleEntrySearchComponent bundleEntrySearchComponentMatch = new Bundle.BundleEntrySearchComponent();
        bundleEntrySearchComponentMatch.setMode(Bundle.SearchEntryMode.MATCH);
        
        groupedQuestionnaires.values().stream().forEach((Map<Date, List<QuestionnaireResponse>> entry) -> {
            entry.values().stream().forEach((List<QuestionnaireResponse> responses) -> {
                bundle.addEntry()
                        .setResource(getFHIRQuestionnaireResponse(responses))
                        .setSearch(bundleEntrySearchComponentMatch)
                        .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/QuestionnaireResponse/" + responses.get(0).getQuestionnaireResponseID());
            });
        });
        
        /*
        questionnaireResponses.stream().forEach((QuestionnaireResponse questionnaireResponse) -> {
            bundle.addEntry()
                    .setResource(getFHIRQuestionnaireResponse(questionnaireResponse))
                    .setSearch(bundleEntrySearchComponentMatch)
                    .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/QuestionnaireResponse/" + questionnaireResponse.getQuestionnaireResponseID());
        });
        */
        
        String result = parser.encodeResourceToString(bundle);
        System.out.println(result);
        
        return result;
    }
    
    @Override
    public String getObservations(List<String> date, String subject, String combocode) throws DataStoreException {
        List<Patient> patients = PatientManager.getPatients(Arrays.asList(subject));
        //patients = Arrays.asList(PatientBuilder.createTestInstance("207779", "HDM"));
        //patients = Arrays.asList(PatientBuilder.createTestInstance("BFK58166", "FPG"));
        
        List<Observation> observations = ObservationManager.getObservations(date, patients, combocode);
        List<Measurement> measurements = MeasurementManager.getMeasurements(date, patients, combocode);
        
        final Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(UUID.randomUUID().toString());
        Meta meta = new Meta();
        meta.setLastUpdated(new Date(LocalDate.now().toEpochDay()));
        bundle.setMeta(meta);
        
        final Bundle.BundleEntrySearchComponent bundleEntrySearchComponentMatch = new Bundle.BundleEntrySearchComponent();
        bundleEntrySearchComponentMatch.setMode(Bundle.SearchEntryMode.MATCH);
        
        observations.stream().forEach((Observation observation) -> {
            bundle.addEntry()
                    .setResource(getFHIRObservation(observation))
                    .setSearch(bundleEntrySearchComponentMatch)
                    .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/Observation/" + observation.getObservationID());
        });
        
        measurements.stream().forEach((Measurement measurement) -> {
            bundle.addEntry()
                    .setResource(getFHIRObservation(measurement))
                    .setSearch(bundleEntrySearchComponentMatch)
                    .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/Measurement/" + measurement.getMeasurementID());
        });
        
        String result = parser.encodeResourceToString(bundle);
        System.out.println(result);
        
        return result;
    }
    

    @Override
    public String getCommunications(List<String> sent, List<String> _include) throws DataStoreException {
        Map<String, List<Message>> messagesGroupByPatients = CommunicationManager.getMessagesBySubjectID(sent);
        
        List<Message> messages = messagesGroupByPatients.values().stream().flatMap(List::stream).collect(toList());
        List<Patient> patients = PatientManager.getPatients(messagesGroupByPatients.keySet().stream().collect(toList()));
        Map<String, List<Patient>> groupedPatients = patients.stream().collect(groupingBy(Patient::getiHelpID));
        
        //patients = PatientManager.getPatients(Arrays.asList("08931057-6a91-449a-a252-ccf9facb77fd", "0b5e9735-5fb5-491b-acbe-8d22bde679f0"));
        
        
        final Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(UUID.randomUUID().toString());
        Meta meta = new Meta();
        meta.setLastUpdated(new Date(LocalDate.now().toEpochDay()));
        bundle.setMeta(meta);
        
        
        messages.stream().forEach((Message message) -> {
            final Bundle.BundleEntrySearchComponent bundleEntrySearchComponentMatch = new Bundle.BundleEntrySearchComponent();
            bundleEntrySearchComponentMatch.setMode(Bundle.SearchEntryMode.MATCH);
            bundle.addEntry()
                    .setResource(getFHIRCommunication(message))
                    .setSearch(bundleEntrySearchComponentMatch)
                    .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/Commmunication/" + message.getTriggerID());
        });
        
        groupedPatients.values().stream().forEach((List<Patient> p) -> {            
            final Bundle.BundleEntrySearchComponent bundleEntrySearchComponentInclude = new Bundle.BundleEntrySearchComponent();
            bundleEntrySearchComponentInclude.setMode(Bundle.SearchEntryMode.INCLUDE);
            bundle.addEntry()
                    .setResource(getFHIRPatient(p))
                    .setSearch(bundleEntrySearchComponentInclude)
                    .setFullUrl(TomcatServerProperties.getInstance().getBaseURL() + "baseR4/Patient/" + p.get(0).getiHelpID());
        });
        
        String result = parser.encodeResourceToString(bundle);
        System.out.println(result);
        
        return result;
    }
    
    private org.hl7.fhir.r4.model.Observation getFHIRObservation(Measurement input) {
        org.hl7.fhir.r4.model.Observation observation = new org.hl7.fhir.r4.model.Observation();
        
        observation.setId(input.getMeasurementID());
        observation.addIdentifier().setSystem(input.getProviderID()).setValue(input.getMeasurementID());
        if(isStringValid(input.getStatus())) {
            try {
                observation.setStatus(org.hl7.fhir.r4.model.Observation.ObservationStatus.valueOf(input.getStatus().toUpperCase()));
            } catch(Exception ex) {
                Log.warn("No valid status {}", input.getStatus());
            }
        }
        
        if(isStringValid(input.getMeasurementCode()) && isStringValid(input.getMeasurementCategoryDisplay()) && isStringValid(input.getMeasurementCategorySystem())) {
            CodeableConcept codeableConcept = new CodeableConcept(new Coding(input.getMeasurementCategorySystem(), input.getMeasurementCategoryCode(), input.getMeasurementCategoryDisplay()));
            observation.setCategory(Arrays.asList(codeableConcept));
        }
        
        if(isStringValid(input.getMeasurementCode()) && isStringValid(input.getMeasurementDisplay()) && isStringValid(input.getMeasurementDisplay())) {
            observation.setCode(new CodeableConcept(new Coding(input.getMeasurementSystem(), input.getMeasurementCode(), input.getMeasurementDisplay())));
        }
        
        if((input.getEffectiveDate()!=null) && (input.getEffectiveDate().getTime()!=0)) {
            observation.setEffective(new DateTimeType(input.getEffectiveDate()));
        }
        
        if(isStringValid(input.getPatientID())) {
            org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
            patient.setId(input.getPatientID());
            observation.setSubject(new Reference(patient));
            //observation.setSubject(new Reference(input.getPatientID()));
        }
        
        if(input.getPractitionerID()!=null) {
            observation.setPerformer(Arrays.asList(new Reference(input.getPractitionerID().toString())));
        }
        
        if(input.getEncounterID()!=null) {
            Encounter encounter = new Encounter();
            encounter.setId(input.getEncounterID().toString());
            observation.setEncounter(new Reference(encounter));
            //observation.setEncounter(new Reference(input.getEncounterID().toString()));
        }
        
        if(observation.getValueQuantity()!=null) {
            observation.setValue(new Quantity(null, input.getValueQuantity(), input.getValueSystem(), input.getValueCode(), input.getValueUnit()));
        }
        
        if(input.getValueQuantityLow()!=null || input.getValueQuantityHigh()!=null) {
            org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent rangeComponent = new org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent();
            if(input.getValueQuantityLow()!=null) {
                rangeComponent.setLow(new Quantity(null, input.getValueQuantityLow(), input.getValueSystemLow(), input.getValueCodeLow(), input.getValueUnitLow()));
            }
            if(input.getValueQuantityHigh()!=null) {
                rangeComponent.setHigh(new Quantity(null, input.getValueQuantityHigh(), input.getValueSystemHigh(), input.getValueCodeHigh(), input.getValueUnitHigh()));
            }
            observation.setReferenceRange(Arrays.asList(rangeComponent));
        }
        
        return observation;
    }
    
    private org.hl7.fhir.r4.model.QuestionnaireResponse getFHIRQuestionnaireResponse(List<QuestionnaireResponse> list) {
        org.hl7.fhir.r4.model.QuestionnaireResponse questionnaireResponse = new org.hl7.fhir.r4.model.QuestionnaireResponse();
        
        QuestionnaireResponse inputQuestionnaire = list.get(0);
        
        questionnaireResponse.setId(inputQuestionnaire.getQuestionnaireResponseID());
        if(isStringValid(inputQuestionnaire.getPatientID())) {
            org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
            patient.setId(inputQuestionnaire.getPatientID());
            questionnaireResponse.setSubject(new Reference(patient));
        }
        if(isStringValid(inputQuestionnaire.getStatus())) {
            try {
                questionnaireResponse.setStatus(org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseStatus.valueOf(inputQuestionnaire.getStatus().toUpperCase()));
            } catch(Exception ex) {
                Log.warn("No valid status {}", inputQuestionnaire.getStatus());
            }
        }
        
        questionnaireResponse.setQuestionnaireElement(new CanonicalType(inputQuestionnaire.getQuestionnaireID()));
        
        if((inputQuestionnaire.getAuthoredDate()!=null) && (inputQuestionnaire.getAuthoredDate().getTime()!=0)) {
            questionnaireResponse.setAuthoredElement(new DateTimeType(inputQuestionnaire.getAuthoredDate()));
        }
        
        list.stream().forEach((QuestionnaireResponse inputQuestionnaireResponse) -> {
            questionnaireResponse
                .addItem()
                .setLinkId(inputQuestionnaireResponse.getLinkedID())
                .setText(inputQuestionnaireResponse.getQuestion())
                .addAnswer()
                .setValue(
                        inputQuestionnaireResponse.getQuestionnaireScore()!=null //it might be that the (numeric/double) score is 'null' so it cannot be converted to String
                              ? new Coding(inputQuestionnaireResponse.getLinkedID(), inputQuestionnaireResponse.getQuestionnaireScore().toString(), inputQuestionnaireResponse.getQuestionnaireResponse()) //score is not null, so convert to (String) code
                              : new Coding(inputQuestionnaireResponse.getLinkedID(), null, inputQuestionnaireResponse.getQuestionnaireResponse())); //score is null, do not add the code
        });
        
        return questionnaireResponse;
    }
    
    private org.hl7.fhir.r4.model.QuestionnaireResponse getFHIRQuestionnaireResponse(QuestionnaireResponse input) {
        org.hl7.fhir.r4.model.QuestionnaireResponse questionnaireResponse = new org.hl7.fhir.r4.model.QuestionnaireResponse();
        
        questionnaireResponse.setId(input.getQuestionnaireResponseID());
        if(isStringValid(input.getPatientID())) {
            org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
            patient.setId(input.getPatientID());
            questionnaireResponse.setSubject(new Reference(patient));
        }
        if(isStringValid(input.getStatus())) {
            try {
                questionnaireResponse.setStatus(org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseStatus.valueOf(input.getStatus().toUpperCase()));
            } catch(Exception ex) {
                Log.warn("No valid status {}", input.getStatus());
            }
        }
        questionnaireResponse.setQuestionnaireElement(new CanonicalType(input.getQuestionnaireID()));
        if((input.getAuthoredDate()!=null) && (input.getAuthoredDate().getTime()!=0)) {
            questionnaireResponse.setAuthoredElement(new DateTimeType(input.getAuthoredDate()));
        }
        
        questionnaireResponse
                .addItem()
                .setLinkId(input.getLinkedID())
                .setText(input.getQuestion())
                .addAnswer()
                .setValue(
                        input.getQuestionnaireScore()!=null //it might be that the (numeric/double) score is 'null' so it cannot be converted to String
                              ? new Coding(input.getLinkedID(), input.getQuestionnaireScore().toString(), input.getQuestionnaireResponse()) //score is not null, so convert to (String) code
                              : new Coding(input.getLinkedID(), null, input.getQuestionnaireResponse())); //score is null, do not add the code
        
        return questionnaireResponse;
    }
    
    private org.hl7.fhir.r4.model.Observation getFHIRObservation(Observation input) {
        org.hl7.fhir.r4.model.Observation observation = new org.hl7.fhir.r4.model.Observation();
        
        observation.setId(input.getObservationID());
        observation.addIdentifier().setSystem(input.getProviderID()).setValue(input.getObservationID());
        if(isStringValid(input.getStatus())) {
            try {
                observation.setStatus(org.hl7.fhir.r4.model.Observation.ObservationStatus.valueOf(input.getStatus().toUpperCase()));
            } catch(Exception ex) {
                Log.warn("No valid status {}", input.getStatus());
            }
        }
        
        if(isStringValid(input.getObservationCategoryCode()) && isStringValid(input.getObservationCategoryDisplay()) && isStringValid(input.getObservationCategorySystem())) {
            CodeableConcept codeableConcept = new CodeableConcept(new Coding(input.getObservationCategorySystem(), input.getObservationCategoryCode(), input.getObservationCategoryDisplay()));
            observation.setCategory(Arrays.asList(codeableConcept));
        }
        
        if(isStringValid(input.getObservationCode()) && isStringValid(input.getObservationDisplay()) && isStringValid(input.getObservationSystem())) {
            observation.setCode(new CodeableConcept(new Coding(input.getObservationSystem(), input.getObservationCode(), input.getObservationDisplay())));
        }
        
        if((input.getEffectiveDate()!=null) && (input.getEffectiveDate().getTime()!=0)) {
            observation.setEffective(new DateTimeType(input.getEffectiveDate()));
        }
        
        if(isStringValid(input.getPatientID())) {
            org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
            patient.setId(input.getPatientID());
            observation.setSubject(new Reference(patient));
            //observation.setSubject(new Reference(input.getPatientID()));
        }
        
        if(input.getPractitionerID()!=null) {
            observation.setPerformer(Arrays.asList(new Reference(input.getPractitionerID().toString())));
        }
        
        if(input.getEncounterID()!=null) {
            Encounter encounter = new Encounter();
            encounter.setId(input.getEncounterID().toString());
            observation.setEncounter(new Reference(encounter));
            //observation.setEncounter(new Reference(input.getEncounterID().toString()));
        }
        
        if(input.getValueBoolean()!=null) {
          observation.setValue(new BooleanType(input.getValueBoolean())); 
        } else if(isStringValid(input.getValueString())) {
            observation.setValue(new StringType(input.getValueString()));
        } else if(input.getValueQuantity()!=null) {
            observation.setValue(new Quantity(null, input.getValueQuantity(), input.getValueSystem(), input.getValueCode(), input.getValueUnit()));
        }
        
        if(input.getValueQuantityLow()!=null || input.getValueQuantityHigh()!=null) {
            org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent rangeComponent = new org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent();
            if(input.getValueQuantityLow()!=null) {
                rangeComponent.setLow(new Quantity(null, input.getValueQuantityLow(), input.getValueSystemLow(), input.getValueCodeLow(), input.getValueUnitLow()));
            }
            if(input.getValueQuantityHigh()!=null) {
                rangeComponent.setHigh(new Quantity(null, input.getValueQuantityHigh(), input.getValueSystemHigh(), input.getValueCodeHigh(), input.getValueUnitHigh()));
            }
            observation.setReferenceRange(Arrays.asList(rangeComponent));
        }
        
        if(isStringValid(input.getBodysiteCode()) && isStringValid(input.getBodysiteDisplay()) && isStringValid(input.getBodysiteSystem())) {
            observation.setBodySite(new CodeableConcept(new Coding(input.getBodysiteSystem(), input.getBodysiteCode(), input.getBodysiteDisplay())));
        }
        
        
        return observation;
    }
    
    
    
    private org.hl7.fhir.r4.model.Communication getFHIRCommunication(Message message) {
        org.hl7.fhir.r4.model.Communication communication = new org.hl7.fhir.r4.model.Communication();
        
        communication.setId(message.getTriggerID());
        
        //communication.setSubject(new Reference(message.getSubjectID()));
        
        org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
        patient.setId(message.getSubjectID());        
        communication.setSubject(new Reference(patient));
        
        Identifier triggerID = new Identifier();
        triggerID.setSystem(Consts.MonitoringMessages.IDENTIFIER_TRIGGERID);
        triggerID.setValue(message.getTriggerID());        
        Identifier studyID = new Identifier();
        studyID.setSystem(Consts.MonitoringMessages.IDENTIFIER_LOINC);
        studyID.setValue(message.getStudyID());
        Identifier dialogueID = new Identifier();
        dialogueID.setSystem(Consts.MonitoringMessages.IDENTIFIER_DIALOGUEID);
        dialogueID.setValue(message.getDialogueID());
        communication.setIdentifier(Arrays.asList(triggerID, studyID, dialogueID));        
        
        if(message.getSentDate()!=null) {
            communication.setSent(message.getSentDate());
        }
        
        if(isStringValid(message.getNotes())) {
            Annotation annotation = new Annotation();
            annotation.setText(message.getNotes());
            communication.setNote(Arrays.asList(annotation));
        }
        
        if(isStringValid(message.getTopicCode()) && isStringValid(message.getTopicDisplay()) && isStringValid(message.getTopicSystem())) {
            communication.setTopic(new CodeableConcept(new Coding(message.getTopicSystem(), message.getTopicCode(), message.getTopicDisplay())));
        }
        
        if(isStringValid(message.getStatus())) {
            try {
                String status = Consts.MonitoringMessages.MESSAGE_STATUS_HEALTHENTIA_TO_FHIR.get(message.getStatus().toUpperCase());
                communication.setStatus(Communication.CommunicationStatus.valueOf(status.toUpperCase()));
            } catch(Exception ex) {
                Log.warn("No valid status {}", message.getStatus());
            }
        }
        
        Extension extension = new Extension("https://fhir.ihelp-project.eu/StructureDefinition/");
        communication.setExtension(Arrays.asList(extension));
        if(isStringValid(message.getNotificationMessage())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_NOTIFICATION_MESSAGE, new StringType(message.getNotificationMessage())));
        }
        if(isStringValid(message.getCoachingDomain())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_COACH_DOMAIN, new StringType(message.getCoachingDomain())));
        }
        if(isStringValid(message.getCoachingElement())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_COACH_ELEMENT, new StringType(message.getCoachingElement())));
        }
        if(isStringValid(message.getCoachingIntent())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_COACH_INTENT, new StringType(message.getCoachingIntent())));
        }
        if(isStringValid(message.getHealthOrientation())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_HEALTH_ORIENTATION, new StringType(message.getHealthOrientation())));
        }
        if(isStringValid(message.getHealthOutcome())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_HEALTH_OUTCOME, new StringType(message.getHealthOutcome())));
        }
        if(isStringValid(message.getMessageFraming())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_MESSAGE_FRAMING, new StringType(message.getMessageFraming())));
        }
        if(isStringValid(message.getSubjectAbility())) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_SUBJECT_ABILITY, new StringType(message.getSubjectAbility())));
        }
        if(message.getCreationTime()!=null) {
            extension.getExtension().add(new Extension(Consts.MonitoringMessages.EXTENSION_CREATION_TIME, new DateTimeType(message.getCreationTime())));
        }
        
        return communication;
    }
    
    
    
    
    private org.hl7.fhir.r4.model.Patient getFHIRPatient(List<Patient> iHelpGroupedPatient) {
        org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
        patient.setId(iHelpGroupedPatient.get(0).getiHelpID());
        
        iHelpGroupedPatient.stream().forEach((Patient p) -> {
            patient.addIdentifier().setValue(p.getPatientID()).setSystem(p.getProviderID());
        });
        
        iHelpGroupedPatient.stream().forEach((Patient p) -> {
            if(p.isActive()!=null) {
                patient.setActive(p.isActive());
            }
        });
        
        iHelpGroupedPatient.stream().forEach((Patient p) -> {
            if(p.getBirthDate()!=null) {
                patient.setBirthDate(p.getBirthDate());
            }
        });
        
        iHelpGroupedPatient.stream().forEach((Patient p) -> {
            if((p.getGender()!=null) && (!p.getGender().isEmpty())) {
                try {
                    patient.setGender(Enumerations.AdministrativeGender.valueOf(p.getGender().toUpperCase()));
                } catch(Exception ex) {
                    Log.warn("Not valid gender '{}' for patient {}/{}", p.getGender(), p.getProviderID(), p.getPatientID());
                }
            }
        });
        
        return patient;
    }
    

    
    
    
    private static String readJsonFromFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
            String line;
            while((line = br.readLine())!=null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
    
    private boolean isStringValid(String str) {
        return (str!=null)&&(!str.isEmpty());
    }

    
}
