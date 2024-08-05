package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaEncounter;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserEncounterUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserEncounterUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputEncounter();
    
    private HHRParserEncounterUtils() {}

    private static final class HHRParserEncounterUtilsHOLDER {
        private static final HHRParserEncounterUtils INSTANCE = new HHRParserEncounterUtils();
    }
    
    public static HHRParserEncounterUtils getInstance() {
        return HHRParserEncounterUtilsHOLDER.INSTANCE;
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
                Encounter encounter = parser.parseResource(Encounter.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, encounter));
            } catch(Exception ex) {
                Log.error("Error parsing encounter {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        Encounter encounter = (Encounter) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaEncounter.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaEncounter.getInstance().getSchemaValue());

        //parse the key                
        if(!encounter.hasId()) {
            avroKeyRecord.put("encounterid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("encounterid", encounter.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(encounter.hasStatus()) {
            avroValueRecord.put("status", encounter.getStatus().toCode());
        }
        if(encounter.hasPeriod()) {
            if(encounter.getPeriod().hasStart()) {
                long timestampValue = encounter.getPeriod().getStart().getTime();
                avroValueRecord.put("periodstart", timestampValue + TIME_ZONE.getOffset(timestampValue));
            } else {
                avroValueRecord.put("periodstart", 0L);
            }
            if(encounter.getPeriod().hasEnd()) {
                long timestampValue = encounter.getPeriod().getEnd().getTime();
                avroValueRecord.put("periodend", timestampValue + TIME_ZONE.getOffset(timestampValue));
            } else {
                avroValueRecord.put("periodend", 0L);
            }
        } else {
            avroValueRecord.put("periodstart", 0L);
            avroValueRecord.put("periodend", 0L);
        }
        
        if(encounter.hasClass_()) {
            avroValueRecord.put("typesystem", encounter.getClass_().getSystem());
            avroValueRecord.put("typecode", encounter.getClass_().getCode());
            avroValueRecord.put("typedisplay", encounter.getClass_().getDisplay());
        }

        if(encounter.hasSubject() && encounter.getSubject().hasReferenceElement()) {
            //avroValueRecord.put("patientid", encounter.getSubject().getReferenceElement().getIdPartAsLong().intValue());
            avroValueRecord.put("patientid", encounter.getSubject().getReferenceElement().getIdPart());
        }
        if(encounter.hasServiceProvider()&& encounter.getServiceProvider().hasReferenceElement()) {
            avroValueRecord.put("serviceproviderid", encounter.getServiceProvider().getReferenceElement().getIdPartAsLong().intValue());
        }
        if(encounter.hasParticipant() && encounter.getParticipantFirstRep().hasIndividual()) {
            avroValueRecord.put("practitionerid", encounter.getParticipantFirstRep().getIndividual().getReferenceElement().getIdPartAsLong().intValue());
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
}
