package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaPatient;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.joda.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserPatientUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserPatientUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputPatient();
    
    private final String EXTENSION_HEIGHT = STRUCTURE_DEFINITION + "height";
    private final String EXTENSION_WEIGHT = STRUCTURE_DEFINITION + "weight";
    private final String EXTENSION_TIMEZONE = STRUCTURE_DEFINITION + "timezone";
    private final String EXTENSION_BASELINEDATE = STRUCTURE_DEFINITION + "baselineDate";
    
    private HHRParserPatientUtils() {}

    private static final class HHRParserPatientUtilsHolder {
        private static final HHRParserPatientUtils INSTANCE = new HHRParserPatientUtils();
    }
    
    public static HHRParserPatientUtils getInstance() {
        return HHRParserPatientUtilsHolder.INSTANCE;
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
                Patient patient = parser.parseResource(Patient.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, patient));
            } catch(Exception ex) {
                Log.error("Error parsing patient {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        Patient patient = (Patient) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaPatient.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaPatient.getInstance().getSchemaValue());

        //parse the key
        if((!patient.hasIdentifier()) && (!patient.hasId()))  {
            throw new IllegalArgumentException("Patient has no identifier");
        }

        if(patient.hasIdentifier()) {
            avroKeyRecord.put("patientid", patient.getIdentifierFirstRep().getValue());
        } else if (patient.hasId()) {
            String patientID = patient.getId();
            if(patientID.startsWith("Patient/")) {//usually the id comes like "Patient/XXXXXXX only need the XXXXXXX
                patientID = patientID.substring(patientID.indexOf("Patient/") + "Patient/".length());
            }
            avroKeyRecord.put("patientid", patientID);
        }
        
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(patient.hasGender()) {
            avroValueRecord.put("gender", patient.getGender().toCode());
        }
        if(patient.hasActive()) {
            avroValueRecord.put("active", patient.getActive());
        }
        if(patient.hasBirthDate()) {
            //avroValueRecord.put("birthdate", patient.getBirthDate().getTime());
            avroValueRecord.put("birthdate", patient.getBirthDate().getTime() + TIME_ZONE.getOffset(patient.getBirthDate().getTime()));
        }
        
        avroValueRecord.put("baselinedate", 0L);
        if((patient.hasExtension() && (!patient.getExtension().isEmpty()))) {
            for(Extension extension : patient.getExtension().get(0).getExtension()) {
                if(extension.getUrl()!=null) {
                    if((extension.getUrl().equalsIgnoreCase(EXTENSION_BASELINEDATE)) && (extension.getValue() instanceof DateTimeType)) {
                        avroValueRecord.put("baselinedate", ((DateTimeType) extension.getValue()).getValue().getTime() + TIME_ZONE.getOffset(patient.getBirthDate().getTime()));
                    } else if((extension.getUrl().equalsIgnoreCase(EXTENSION_TIMEZONE)) && (extension.getValue() instanceof StringType)) {
                        avroValueRecord.put("timezone", ((StringType) extension.getValue()).getValueAsString());
                    } else if((extension.getUrl().equalsIgnoreCase(EXTENSION_HEIGHT)) && (extension.getValue() instanceof DecimalType)) {
                        avroValueRecord.put("height", ((DecimalType) extension.getValue()).getValue().doubleValue());
                    } else if((extension.getUrl().equalsIgnoreCase(EXTENSION_WEIGHT)) && (extension.getValue() instanceof DecimalType)) {
                        avroValueRecord.put("weight", ((DecimalType) extension.getValue()).getValue().doubleValue());
                    } 
                }
            }
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
}
