package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaMedicationAdministration;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserMedicationAdministrationUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserMeasurementUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputMedicationAdministration();
    
    private final String EXTENSION_CYCLES = "https://fhir.ihelp-project.eu/StructureDefinition/cycles";
    private final String EXTENSION_SUSPENSION = "https://fhir.ihelp-project.eu/StructureDefinition/suspension";
    private final String EXTENSION_SUSPENSION_DAYS = "suspension-days";
    private final String EXTENSION_SUSPENSION_REASON = "suspension-reason";
    
    private HHRParserMedicationAdministrationUtils() {}

    private static final class HHRParserMedicationAdministrationUtilsUtilsHolder {
        private static final HHRParserMedicationAdministrationUtils INSTANCE = new HHRParserMedicationAdministrationUtils();
    }
    
    public static HHRParserMedicationAdministrationUtils getInstance() {
        return HHRParserMedicationAdministrationUtilsUtilsHolder.INSTANCE;
    }
    
    @Override
    public String getTopic() {
        return topic;
    }
    
    @Override
    public HHRRecordToImport parseValues(String dataSourceID, JSONArray values) {
        HHRRecordToImport hHRRecordToImport = new HHRRecordToImport(topic, values.length());
        for(int i=0; i<values.length(); i++) {
            JSONObject item = null;
            if(values.get(i) instanceof JSONObject) {
                item = values.getJSONObject(i);
            } else if(values.get(i) instanceof String) {
                item = new JSONObject(values.getString(i));
            } else {
                throw new IllegalArgumentException("values is not JSONObject or serialized (String) JSONObject");
            }
            
            try {
                MedicationAdministration medicationAdministration = parser.parseResource(MedicationAdministration.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, medicationAdministration));
            } catch(Exception ex) {
                Log.error("Error parsing medicationAdministration {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        MedicationAdministration medicationAdministration = (MedicationAdministration) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaMedicationAdministration.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaMedicationAdministration.getInstance().getSchemaValue());

        //parse the key                
        if(!medicationAdministration.hasId()) {
            avroKeyRecord.put("medicationadministrationid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("medicationadministrationid", medicationAdministration.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(medicationAdministration.hasStatus()) {
            avroValueRecord.put("status", medicationAdministration.getStatus().toCode());
        }

        if(medicationAdministration.hasCategory() && medicationAdministration.getCategory().hasCoding()) {
            avroValueRecord.put("categorysystem", medicationAdministration.getCategory().getCodingFirstRep().getSystem());
            avroValueRecord.put("categorycode", medicationAdministration.getCategory().getCodingFirstRep().getCode());
            avroValueRecord.put("categorydisplay", medicationAdministration.getCategory().getCodingFirstRep().getDisplay());
        }

        if(medicationAdministration.hasMedicationCodeableConcept() && medicationAdministration.getMedicationCodeableConcept().hasCoding()) {
            avroValueRecord.put("medicationcodesystem", medicationAdministration.getMedicationCodeableConcept().getCodingFirstRep().getSystem());
            avroValueRecord.put("medicationcodecode", medicationAdministration.getMedicationCodeableConcept().getCodingFirstRep().getCode());
            avroValueRecord.put("medicationcodedisplay", medicationAdministration.getMedicationCodeableConcept().getCodingFirstRep().getDisplay());
        }
        
        if(medicationAdministration.hasReasonCode() && medicationAdministration.getReasonCodeFirstRep().hasCoding()) {
            avroValueRecord.put("reasoncodesystem", medicationAdministration.getReasonCodeFirstRep().getCodingFirstRep().getSystem());
            avroValueRecord.put("reasoncodecode", medicationAdministration.getReasonCodeFirstRep().getCodingFirstRep().getCode());
            avroValueRecord.put("reasoncodedisplay", medicationAdministration.getReasonCodeFirstRep().getCodingFirstRep().getDisplay());
        }
        
        if(medicationAdministration.hasExtension(EXTENSION_CYCLES)) {
            Extension extension = medicationAdministration.getExtensionByUrl(EXTENSION_CYCLES);
            if(extension.getValue() instanceof StringType) {
                avroValueRecord.put("cycles", ((StringType) extension.getValue()).toString());
            }
        }
        
        if(medicationAdministration.hasExtension(EXTENSION_SUSPENSION)) {
            Extension extensionSuspension = medicationAdministration.getExtensionByUrl(EXTENSION_SUSPENSION);
            if(extensionSuspension.hasExtension(EXTENSION_SUSPENSION_DAYS)) {
                Extension extension = extensionSuspension.getExtensionByUrl(EXTENSION_SUSPENSION_DAYS);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("suspensionreasondays", ((StringType) extension.getValue()).toString());
                }
            }
            if(extensionSuspension.hasExtension(EXTENSION_SUSPENSION_REASON)) {
                Extension extension = extensionSuspension.getExtensionByUrl(EXTENSION_SUSPENSION_REASON);
                if(extension.getValue() instanceof CodeableConcept) {
                    avroValueRecord.put("suspensionreasonsystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                    avroValueRecord.put("suspensionreasoncode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                    avroValueRecord.put("suspensionreasondisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
                }
            }
        }


        if(medicationAdministration.hasSubject() && medicationAdministration.getSubject().hasReferenceElement()) {
            avroValueRecord.put("patientid", medicationAdministration.getSubject().getReferenceElement().getIdPart());
        }
        
        if(medicationAdministration.hasPartOf()) {
            avroValueRecord.put("partofid", medicationAdministration.getPartOfFirstRep().getReferenceElement().getIdPart());
            avroValueRecord.put("partofprocider", dataSourceID);
        }
        
        

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
}
