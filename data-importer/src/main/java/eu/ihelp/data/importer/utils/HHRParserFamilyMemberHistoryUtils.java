package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaFamilyMemberHistory;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserFamilyMemberHistoryUtils extends HHRParserAbstractUtils  {
     private static final Logger Log = LoggerFactory.getLogger(HHRParserFamilyMemberHistoryUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputFamilyMemberHistory();
    
    private HHRParserFamilyMemberHistoryUtils() {}

    private static final class HHRParserFamilyMemberHistoryUtilsHolder {
        private static final HHRParserFamilyMemberHistoryUtils INSTANCE = new HHRParserFamilyMemberHistoryUtils();
    }
    
    public static HHRParserFamilyMemberHistoryUtils getInstance() {
        return HHRParserFamilyMemberHistoryUtilsHolder.INSTANCE;
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
                FamilyMemberHistory familyMemberHisotry = parser.parseResource(FamilyMemberHistory.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, familyMemberHisotry));
            } catch(Exception ex) {
                Log.error("Error parsing familyMemberHisotry {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        FamilyMemberHistory familyMemberHistory = (FamilyMemberHistory) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaFamilyMemberHistory.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaFamilyMemberHistory.getInstance().getSchemaValue());

        //parse the key                
        if(!familyMemberHistory.hasId()) {
            avroKeyRecord.put("familymemberhistoryid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("familymemberhistoryid", familyMemberHistory.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(familyMemberHistory.hasCondition()&& familyMemberHistory.getConditionFirstRep().hasCode() && familyMemberHistory.getConditionFirstRep().getCode().hasCoding()) {
            avroValueRecord.put("conditionsystem", familyMemberHistory.getConditionFirstRep().getCode().getCodingFirstRep().getSystem());
            avroValueRecord.put("conditioncode", familyMemberHistory.getConditionFirstRep().getCode().getCodingFirstRep().getCode());
            avroValueRecord.put("conditiondisplay", familyMemberHistory.getConditionFirstRep().getCode().getCodingFirstRep().getDisplay());
        }

        if(familyMemberHistory.hasPatient()&& familyMemberHistory.getPatient().hasReferenceElement()) {
            avroValueRecord.put("patientid", familyMemberHistory.getPatient().getReferenceElement().getIdPart());
        }
        
        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
    
}
