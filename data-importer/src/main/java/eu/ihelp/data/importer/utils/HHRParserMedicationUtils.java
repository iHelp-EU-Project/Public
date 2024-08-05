package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaMedicationStatement;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserMedicationUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserMedicationUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputMedication();
    
    private HHRParserMedicationUtils() {}

    private static final class HHRParserMedicationUtilsHOLDER {
        private static final HHRParserMedicationUtils INSTANCE = new HHRParserMedicationUtils();
    }
    
    public static HHRParserMedicationUtils getInstance() {
        return HHRParserMedicationUtilsHOLDER.INSTANCE;
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
                MedicationStatement medicationStatement = parser.parseResource(MedicationStatement.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, medicationStatement));
            } catch(Exception ex) {
                Log.error("Error parsing Medication Statement {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        MedicationStatement medicationStatement = (MedicationStatement) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaMedicationStatement.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaMedicationStatement.getInstance().getSchemaValue());

        //parse the key                
        if(!medicationStatement.hasId()) {
            avroKeyRecord.put("medicationid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("medicationid", medicationStatement.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(medicationStatement.hasStatus()) {
            avroValueRecord.put("status", medicationStatement.getStatus().toCode());
        }

        if(medicationStatement.hasMedicationCodeableConcept() && medicationStatement.getMedicationCodeableConcept().hasCoding()) {
            avroValueRecord.put("medicationsystem", medicationStatement.getMedicationCodeableConcept().getCodingFirstRep().getSystem());
            avroValueRecord.put("medicationcode", medicationStatement.getMedicationCodeableConcept().getCodingFirstRep().getCode());
            avroValueRecord.put("medicationdisplay", medicationStatement.getMedicationCodeableConcept().getCodingFirstRep().getDisplay());
        }

        if(medicationStatement.hasEffectivePeriod()) {
            if(medicationStatement.getEffectivePeriod().hasStart()) {
                long timestampValue = medicationStatement.getEffectivePeriod().getStart().getTime();
                avroValueRecord.put("effectiveperiodstart", timestampValue + TIME_ZONE.getOffset(timestampValue));
            } else {
                avroValueRecord.put("effectiveperiodstart", 0L);
            }
            if(medicationStatement.getEffectivePeriod().hasEnd()) {
                long timestampValue = medicationStatement.getEffectivePeriod().getEnd().getTime();
                avroValueRecord.put("effectiveperiodend", timestampValue + TIME_ZONE.getOffset(timestampValue));
            } else {
                avroValueRecord.put("effectiveperiodend", 0L);
            }
        } else {
            avroValueRecord.put("effectiveperiodstart", 0L);
            avroValueRecord.put("effectiveperiodend", 0L);
        }


        if(medicationStatement.hasSubject() && medicationStatement.getSubject().hasReferenceElement()) {
            //avroValueRecord.put("patientid", medicationStatement.getSubject().getReferenceElement().getIdPartAsLong().intValue());
            avroValueRecord.put("patientid", medicationStatement.getSubject().getReferenceElement().getIdPart());
        }

        if(medicationStatement.hasInformationSource() && medicationStatement.getInformationSource().hasReference()) {
            avroValueRecord.put("practitionerid", medicationStatement.getInformationSource().getReferenceElement().getIdPartAsLong().intValue());
        }

        if(medicationStatement.hasContext() && medicationStatement.getContext().hasReference()) {
            avroValueRecord.put("encounterid", medicationStatement.getContext().getReferenceElement().getIdPartAsLong().intValue());
        }

        if(medicationStatement.hasDosage() && medicationStatement.getDosageFirstRep().hasDoseAndRate()) {
            avroValueRecord.put("dosage", medicationStatement.getDosageFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue().doubleValue());
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
}
