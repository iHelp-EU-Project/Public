package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaProcedure;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Procedure;
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
public class HHRParserProcedureUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserProcedureUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputProcedure();
    
    private final String EXTENSION_CYCLES = "https://fhir.ihelp-project.eu/StructureDefinition/cycles";
    private final String EXTENSION_DURATION = "https://fhir.ihelp-project.eu/StructureDefinition/duration";
    private final String EXTENSION_SURGERY_APPROACH = "https://fhir.ihelp-project.eu/StructureDefinition/surgery-approach";
    private final String EXTENSION_GRADING = "https://fhir.ihelp-project.eu/StructureDefinition/grading";
    private final String EXTENSION_RADIOTHERAPY_SETTING = "https://fhir.ihelp-project.eu/StructureDefinition/radiotherapy-setting";
    private final String EXTENSION_RADIOTHERAPY_TIMING = "https://fhir.ihelp-project.eu/StructureDefinition/radiotherapy-timing";
    private final String EXTENSION_RADIOTHERAPY_TECHNIQUE = "https://fhir.ihelp-project.eu/StructureDefinition/radiotherapy-technique";
    private final String EXTENSION_RADIOTHERAPY_CTV1 = "https://fhir.ihelp-project.eu/StructureDefinition/radiotherapy-ctv1";
    private final String EXTENSION_RADIOTHERAPY_CTV2 = "https://fhir.ihelp-project.eu/StructureDefinition/radiotherapy-ctv2";
    private final String EXTENSION_RADIOTHERAPY_CTV3 = "https://fhir.ihelp-project.eu/StructureDefinition/radiotherapy-ctv3";
    private final String EXTENSION_DOSE = "dose";
    private final String EXTENSION_FRACTIONATION = "fractionation";
    private final String EXTENSION_CLOSURE_REASON = "https://fhir.ihelp-project.eu/StructureDefinition/closure-reason";
    private final String EXTENSION_SUSPENSION = "https://fhir.ihelp-project.eu/StructureDefinition/suspension";
    private final String EXTENSION_SUSPENSION_DAYS = "suspension-days";
    private final String EXTENSION_SUSPENSION_REASON = "suspension-reason";
    private final String CODING_SNOMED = "SNOMED";
    private final String CODING_SNOMED_67600007 = "67600007";
    private final String CODING_SNOMED_25803005 = "25803005";
    
    private HHRParserProcedureUtils() {}

    private static final class HHRParserProcedureUtilsHolder {
        private static final HHRParserProcedureUtils INSTANCE = new HHRParserProcedureUtils();
    }
    
    public static HHRParserProcedureUtils getInstance() {
        return HHRParserProcedureUtilsHolder.INSTANCE;
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
                Procedure procedure = parser.parseResource(Procedure.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, procedure));
            } catch(Exception ex) {
                Log.error("Error parsing patient {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        Procedure procedure = (Procedure) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaProcedure.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaProcedure.getInstance().getSchemaValue());

        //parse the key                
        if(!procedure.hasId()) {
            avroKeyRecord.put("procedureid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("procedureid", procedure.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(procedure.hasStatus()) {
            avroValueRecord.put("status", procedure.getStatus().toCode());
        }
        
        if(procedure.hasPerformedPeriod()) {
            if(procedure.getPerformedPeriod().hasStart()) {
                long timestampValue = procedure.getPerformedPeriod().getStart().getTime();
                avroValueRecord.put("performedperiodstart", timestampValue + TIME_ZONE.getOffset(timestampValue));
            } else {
                avroValueRecord.put("performedperiodstart", 0L);
            }
            if(procedure.getPerformedPeriod().hasEnd()) {
                long timestampValue = procedure.getPerformedPeriod().getEnd().getTime();
                avroValueRecord.put("performedperiodend", timestampValue + TIME_ZONE.getOffset(timestampValue));
            } else {
                avroValueRecord.put("performedperiodend", 0L);
            }
        } else {
            avroValueRecord.put("performedperiodstart", 0L);
            avroValueRecord.put("performedperiodend", 0L);
        }

        if(procedure.hasCategory()&& procedure.getCategory().hasCoding()) {
            avroValueRecord.put("procedurecategorysystem", procedure.getCategory().getCodingFirstRep().getSystem());
            avroValueRecord.put("procedurecategorycode", procedure.getCategory().getCodingFirstRep().getCode());
            avroValueRecord.put("procedurecategorydisplay", procedure.getCategory().getCodingFirstRep().getDisplay());
        }

        if(procedure.hasCode()&& procedure.getCode().hasCoding()) {
            avroValueRecord.put("proceduresystem", procedure.getCode().getCodingFirstRep().getSystem());
            avroValueRecord.put("procedurecode", procedure.getCode().getCodingFirstRep().getCode());
            avroValueRecord.put("proceduredisplay", procedure.getCode().getCodingFirstRep().getDisplay());
        }
        
        if(procedure.hasExtension(EXTENSION_CYCLES)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_CYCLES);
            if(extension.getValue() instanceof StringType) {
                avroValueRecord.put("cycles", ((StringType) extension.getValue()).toString());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_DURATION)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_DURATION);
            if(extension.getValue() instanceof StringType) {
                avroValueRecord.put("duration", ((StringType) extension.getValue()).toString());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_SURGERY_APPROACH)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_SURGERY_APPROACH);
            if(extension.getValue() instanceof CodeableConcept) {
                avroValueRecord.put("surgeryapproachsystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                avroValueRecord.put("surgeryapproachcode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                avroValueRecord.put("surgeryapproachdisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_GRADING)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_GRADING);
            if(extension.getValue() instanceof CodeableConcept) {
                avroValueRecord.put("gradingsystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                avroValueRecord.put("gradingcode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                avroValueRecord.put("gradingdisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_RADIOTHERAPY_TIMING)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_RADIOTHERAPY_TIMING);
            if(extension.getValue() instanceof CodeableConcept) {
                avroValueRecord.put("radiotherapytimingsystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                avroValueRecord.put("radiotherapytimingcode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                avroValueRecord.put("radiotherapytimingdisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_RADIOTHERAPY_SETTING)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_RADIOTHERAPY_SETTING);
            if(extension.getValue() instanceof CodeableConcept) {
                avroValueRecord.put("radiotherapysettingsystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                avroValueRecord.put("radiotherapysettingcode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                avroValueRecord.put("radiotherapysettingdisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_RADIOTHERAPY_TECHNIQUE)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_RADIOTHERAPY_TECHNIQUE);
            if(extension.getValue() instanceof CodeableConcept) {
                avroValueRecord.put("radiotherapytechniquesystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                avroValueRecord.put("radiotherapytechniquecode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                avroValueRecord.put("radiotherapytechniquedisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_CLOSURE_REASON)) {
            Extension extension = procedure.getExtensionByUrl(EXTENSION_CLOSURE_REASON);
            if(extension.getValue() instanceof CodeableConcept) {
                avroValueRecord.put("closurereasonsystem", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getSystem());
                avroValueRecord.put("closurereasoncode", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getCode());
                avroValueRecord.put("closurereasondisplay", ((CodeableConcept) extension.getValue()).getCodingFirstRep().getDisplay());
            }
        }
        
        if(procedure.hasExtension(EXTENSION_SUSPENSION)) {
            Extension extensionSuspension = procedure.getExtensionByUrl(EXTENSION_SUSPENSION);
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
        
        if(procedure.hasExtension(EXTENSION_RADIOTHERAPY_CTV1)) {
            Extension radiotherapyExtension = procedure.getExtensionByUrl(EXTENSION_RADIOTHERAPY_CTV1);
            if(radiotherapyExtension.hasExtension(EXTENSION_DOSE)) {
                Extension extension = radiotherapyExtension.getExtensionByUrl(EXTENSION_DOSE);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("radiotherapyctv1dose", ((StringType) extension.getValue()).toString());
                }
            }
            if(radiotherapyExtension.hasExtension(EXTENSION_FRACTIONATION)) {
                Extension extension = radiotherapyExtension.getExtensionByUrl(EXTENSION_FRACTIONATION);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("radiotherapyctv1fractionation", ((StringType) extension.getValue()).toString());
                }
            }
        }
        
        if(procedure.hasExtension(EXTENSION_RADIOTHERAPY_CTV2)) {
            Extension radiotherapyExtension = procedure.getExtensionByUrl(EXTENSION_RADIOTHERAPY_CTV2);
            if(radiotherapyExtension.hasExtension(EXTENSION_DOSE)) {
                Extension extension = radiotherapyExtension.getExtensionByUrl(EXTENSION_DOSE);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("radiotherapyctv2dose", ((StringType) extension.getValue()).toString());
                }
            }
            if(radiotherapyExtension.hasExtension(EXTENSION_FRACTIONATION)) {
                Extension extension = radiotherapyExtension.getExtensionByUrl(EXTENSION_FRACTIONATION);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("radiotherapyctv2fractionation", ((StringType) extension.getValue()).toString());
                }
            }
        }
        
        if(procedure.hasExtension(EXTENSION_RADIOTHERAPY_CTV3)) {
            Extension radiotherapyExtension = procedure.getExtensionByUrl(EXTENSION_RADIOTHERAPY_CTV3);
            if(radiotherapyExtension.hasExtension(EXTENSION_DOSE)) {
                Extension extension = radiotherapyExtension.getExtensionByUrl(EXTENSION_DOSE);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("radiotherapyctv3dose", ((StringType) extension.getValue()).toString());
                }
            }
            if(radiotherapyExtension.hasExtension(EXTENSION_FRACTIONATION)) {
                Extension extension = radiotherapyExtension.getExtensionByUrl(EXTENSION_FRACTIONATION);
                if(extension.getValue() instanceof StringType) {
                    avroValueRecord.put("radiotherapyctv3fractionation", ((StringType) extension.getValue()).toString());
                }
            }
        }
        
        if(procedure.hasComplication()) {
            for(CodeableConcept codeableConcept : procedure.getComplication()) {
                if((codeableConcept.getCodingFirstRep().getSystem().equalsIgnoreCase(CODING_SNOMED)) && (codeableConcept.getCodingFirstRep().getCode().equalsIgnoreCase(CODING_SNOMED_67600007))) {
                    if(codeableConcept.getText().equalsIgnoreCase("true")) {
                        avroValueRecord.put("biliaryfistula", true);
                    } else if(codeableConcept.getText().equalsIgnoreCase("false")) {
                        avroValueRecord.put("biliaryfistula", false);
                    } 
                }
                if((codeableConcept.getCodingFirstRep().getSystem().equalsIgnoreCase(CODING_SNOMED)) && (codeableConcept.getCodingFirstRep().getCode().equalsIgnoreCase(CODING_SNOMED_25803005))) {
                    if(codeableConcept.getText().equalsIgnoreCase("true")) {
                        avroValueRecord.put("pancreaticfistula", true);
                    } else if(codeableConcept.getText().equalsIgnoreCase("false")) {
                        avroValueRecord.put("pancreaticfistula", false);
                    } 
                }
            }
        }
        
        if(procedure.hasFollowUp()) {
            avroValueRecord.put("resurgery", true);
        }
        

        if(procedure.hasSubject() && procedure.getSubject().hasReferenceElement()) {
            avroValueRecord.put("patientid", procedure.getSubject().getReferenceElement().getIdPart());
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
    
}
