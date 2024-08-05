package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaObservation;
import eu.ihelp.datacapture.commons.DataProviders;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserObservationUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserObservationUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputObservation();
    
    private HHRParserObservationUtils() {}

    private static final class HHRParserObservationUtilsHOLDER {
        private static final HHRParserObservationUtils INSTANCE = new HHRParserObservationUtils();
    }
    
    public static HHRParserObservationUtils getInstance() {
        return HHRParserObservationUtilsHOLDER.INSTANCE;
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
                Observation observation = parser.parseResource(Observation.class, item.toString());
                
                
                
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, observation));
            } catch(Exception ex) {
                Log.error("Error parsing Observation {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        Observation observation = (Observation) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaObservation.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaObservation.getInstance().getSchemaValue());

        //parse the key                
        if(!observation.hasId()) {
            avroKeyRecord.put("observationid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("observationid", observation.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(observation.hasStatus()) {
            avroValueRecord.put("status", observation.getStatus().toCode());
        }

        if(observation.hasCategory()&& observation.getCategoryFirstRep().hasCoding()) {
            avroValueRecord.put("observationcategorysystem", observation.getCategoryFirstRep().getCodingFirstRep().getSystem());
            avroValueRecord.put("observationcategorycode", observation.getCategoryFirstRep().getCodingFirstRep().getCode());
            avroValueRecord.put("observationcategorydisplay", observation.getCategoryFirstRep().getCodingFirstRep().getDisplay());
        }

        if(observation.hasCode()&& observation.getCode().hasCoding()) {
            avroValueRecord.put("observationsystem", observation.getCode().getCodingFirstRep().getSystem());
            avroValueRecord.put("observationcode", observation.getCode().getCodingFirstRep().getCode());
            avroValueRecord.put("observationdisplay", observation.getCode().getCodingFirstRep().getDisplay());
            
            if(DataProviders.SecondaryDataProviders.HEALTHENTIA.name().equalsIgnoreCase(dataSourceID)) {
                //hack for healthentia to update the PK (many records from healthentia dataset is transformed to various observations with the same id of the original record
                avroKeyRecord.put("observationid", observation.getIdElement().getIdPart() + "-" + observation.getCode().getCodingFirstRep().getCode());
            }
        }


        if(observation.hasEffectiveDateTimeType()) {
            long timetsampValue = observation.getEffectiveDateTimeType().getValueAsCalendar().getTimeInMillis();
            avroValueRecord.put("effectivedatetime", timetsampValue + TIME_ZONE.getOffset(timetsampValue));
        } else {
            avroValueRecord.put("effectivedatetime", 0L);
        }

        if(observation.hasSubject() && observation.getSubject().hasReferenceElement()) {
            //avroValueRecord.put("patientid", observation.getSubject().getReferenceElement().getIdPartAsLong().intValue());
            avroValueRecord.put("patientid", observation.getSubject().getReferenceElement().getIdPart());
        }


        if(observation.hasPerformer()&& observation.getPerformerFirstRep().hasReference()) {
            avroValueRecord.put("practitionerid", observation.getPerformerFirstRep().getReferenceElement().getIdPartAsLong().intValue());
        }


        if(observation.hasEncounter()&& observation.getEncounter().hasReference()) {
            avroValueRecord.put("encounterid", observation.getEncounter().getReferenceElement().getIdPartAsLong().intValue());
        }


        if(observation.hasValueQuantity()) {
            avroValueRecord.put("valuesystem", observation.getValueQuantity().getSystem());
            avroValueRecord.put("valuecode", observation.getValueQuantity().getCode());
            avroValueRecord.put("valueunit", observation.getValueQuantity().getUnit());
            avroValueRecord.put("valuequantity", observation.getValueQuantity().getValue().doubleValue());                    
        }
        
        if(observation.hasValueStringType()) {
            avroValueRecord.put("valuestring", observation.getValueStringType().getValue());
        }
        
        if(observation.hasValueBooleanType()) {
            avroValueRecord.put("valueboolean", observation.getValueBooleanType().getValue());
        }

        if(observation.hasReferenceRange()) {
            if(observation.getReferenceRangeFirstRep().hasLow()) {
                avroValueRecord.put("valuesystemlow", observation.getReferenceRangeFirstRep().getLow().getSystem());
                avroValueRecord.put("valuecodelow", observation.getReferenceRangeFirstRep().getLow().getCode());
                avroValueRecord.put("valueunitlow", observation.getReferenceRangeFirstRep().getLow().getUnit());
                avroValueRecord.put("valuequantitylow", observation.getReferenceRangeFirstRep().getLow().getValue().doubleValue());                    
            }
            if(observation.getReferenceRangeFirstRep().hasHigh()) {
                avroValueRecord.put("valuesystemhigh", observation.getReferenceRangeFirstRep().getHigh().getSystem());
                avroValueRecord.put("valuecodehigh", observation.getReferenceRangeFirstRep().getHigh().getCode());
                avroValueRecord.put("valueunithigh", observation.getReferenceRangeFirstRep().getHigh().getUnit());
                avroValueRecord.put("valuequantityhigh", observation.getReferenceRangeFirstRep().getHigh().getValue().doubleValue());                    
            }
        }
        
        if(observation.hasBodySite()) {
            avroValueRecord.put("bodySiteSystem", observation.getBodySite().getCodingFirstRep().getSystem());
            avroValueRecord.put("bodySiteCode", observation.getBodySite().getCodingFirstRep().getCode());
            avroValueRecord.put("bodySiteDisplay", observation.getBodySite().getCodingFirstRep().getDisplay());
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
}
