package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaCondition;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserConditionUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserConditionUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputCondition();
    
    private HHRParserConditionUtils() {}

    private static final class HHRParserConditionUtilsHolder {
        private static final HHRParserConditionUtils INSTANCE = new HHRParserConditionUtils();
    }
    
    public static HHRParserConditionUtils getInstance() {
        return HHRParserConditionUtilsHolder.INSTANCE;
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
                Condition condition = parser.parseResource(Condition.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, condition));
            } catch(Exception ex) {
                Log.error("Error parsing condition {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        Condition condition = (Condition)resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaCondition.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaCondition.getInstance().getSchemaValue());

        //parse the key                
        if(!condition.hasId()) {
            avroKeyRecord.put("conditionid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("conditionid", condition.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(condition.hasAbatementDateTimeType()) {
            long timestampValue = condition.getAbatementDateTimeType().toCalendar().getTimeInMillis();
            avroValueRecord.put("abatementdatetime", timestampValue + TIME_ZONE.getOffset(timestampValue));
        } else {
            avroValueRecord.put("abatementdatetime", 0L);
        }
        if(condition.hasOnsetDateTimeType()) {
            long timestampValue = condition.getOnsetDateTimeType().toCalendar().getTimeInMillis();
            avroValueRecord.put("onsetdate", timestampValue + TIME_ZONE.getOffset(timestampValue));
        } else {
            avroValueRecord.put("onsetdate", 0L);
        }
        if(condition.hasCategory()) {
            avroValueRecord.put("categorysystem", condition.getCategoryFirstRep().getCodingFirstRep().getSystem());
            avroValueRecord.put("categorycode", condition.getCategoryFirstRep().getCodingFirstRep().getCode());
            avroValueRecord.put("categorydisplay", condition.getCategoryFirstRep().getCodingFirstRep().getDisplay());
        }        
        if(condition.hasCode()) {
            avroValueRecord.put("codesystem", condition.getCode().getCodingFirstRep().getSystem());
            avroValueRecord.put("codecode", condition.getCode().getCodingFirstRep().getCode());
            avroValueRecord.put("codedisplay", condition.getCode().getCodingFirstRep().getDisplay());
        }
        if(condition.hasSubject() && condition.getSubject().hasReferenceElement()) {
            //avroValueRecord.put("patientid", condition.getSubject().getReferenceElement().getIdPartAsLong().intValue());
            avroValueRecord.put("patientid", condition.getSubject().getReferenceElement().getIdPart());
        }

        if(condition.hasAsserter() && condition.getAsserter().hasReferenceElement()) {
            avroValueRecord.put("practitionerid", condition.getAsserter().getReferenceElement().getIdPartAsLong().intValue());
        }

        if(condition.hasEncounter() && condition.getEncounter().hasReferenceElement()) {
            avroValueRecord.put("encounterid", condition.getEncounter().getReferenceElement().getIdPartAsLong().intValue());
        }                
        
        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
}
