package eu.ihelp.store.server.utils;

import java.util.HashMap;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Consts {
    public static final String HEALTHENTIA_PROVIDER = "HEALTHENTIA";
    
    public static class MonitoringMessages {
    
        public static final String IDENTIFIER_TRIGGERID = "https://fhir.ihelp-project.eu/comm_trigger_id";
        public static final String IDENTIFIER_LOINC = "LOINC/78200-3";
        public static final String IDENTIFIER_DIALOGUEID = "https://fhir.ihelp-project.eu/dialogue_id";
        public static final String EXTENSION_NOTIFICATION_MESSAGE = "https://fhir.ihelp-project.eu/StructureDefinition/comm_notification_msg";
        public static final String EXTENSION_CREATION_TIME = "https://fhir.ihelp-project.eu/StructureDefinition/comm_creation_time";
        public static final String EXTENSION_COACH_DOMAIN = "https://fhir.ihelp-project.eu/StructureDefinition/coach_domain";
        public static final String EXTENSION_COACH_ELEMENT = "https://fhir.ihelp-project.eu/StructureDefinition/coach_element";
        public static final String EXTENSION_COACH_INTENT = "https://fhir.ihelp-project.eu/StructureDefinition/coach_intent";
        public static final String EXTENSION_HEALTH_OUTCOME = "https://fhir.ihelp-project.eu/StructureDefinition/health_outcome";
        public static final String EXTENSION_MESSAGE_FRAMING = "https://fhir.ihelp-project.eu/StructureDefinition/msg_framing";
        public static final String EXTENSION_SUBJECT_ABILITY = "https://fhir.ihelp-project.eu/StructureDefinition/subj_ability";
        public static final String EXTENSION_HEALTH_ORIENTATION = "https://fhir.ihelp-project.eu/StructureDefinition/health_orientation";

        public static final HashMap<String, String> MESSAGE_STATUS_HEALTHENTIA_TO_FHIR = buildMessageStatusHealthentiaToFhir();
        public static final HashMap<String, String> MESSAGE_STATUS_FHIR_TO_HEALTHENTIA = buildMessageStatusFhirToHealthentia();

        public static enum HealthentiaStatus {
            proposed, automatic, approved, rejected
        }

        public static enum FhirStatus {
            preparation, inprogress, completed, stopped
        }
    
        static HashMap<String, String> buildMessageStatusFhirToHealthentia() {
            HashMap<String, String> map = new HashMap<>(4);
            map.put(FhirStatus.preparation.name().toUpperCase(), HealthentiaStatus.proposed.name().toUpperCase());
            map.put(FhirStatus.inprogress.name().toUpperCase(), HealthentiaStatus.automatic.name().toUpperCase());
            map.put(FhirStatus.completed.name().toUpperCase(), HealthentiaStatus.approved.name().toUpperCase());
            map.put(FhirStatus.stopped.name().toUpperCase(), HealthentiaStatus.rejected.name().toUpperCase());
            return map;
        }

        static HashMap<String, String> buildMessageStatusHealthentiaToFhir() {
            HashMap<String, String> map = new HashMap<>(4);
            map.put(HealthentiaStatus.proposed.name().toUpperCase(), FhirStatus.preparation.name().toUpperCase());
            map.put(HealthentiaStatus.automatic.name().toUpperCase(), FhirStatus.inprogress.name().toUpperCase());
            map.put(HealthentiaStatus.approved.name().toUpperCase(), FhirStatus.completed.name().toUpperCase());
            map.put(HealthentiaStatus.rejected.name().toUpperCase(), FhirStatus.stopped.name().toUpperCase());
            return map;
        }
    }
}

