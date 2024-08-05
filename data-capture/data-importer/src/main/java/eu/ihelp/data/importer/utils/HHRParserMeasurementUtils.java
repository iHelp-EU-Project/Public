package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaMeasurement;
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
public class HHRParserMeasurementUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserMeasurementUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputMeasurement();
    
    private HHRParserMeasurementUtils() {}

    private static final class HHRParserMeasurementUtilsHolder {
        private static final HHRParserMeasurementUtils INSTANCE = new HHRParserMeasurementUtils();
    }
    
    public static HHRParserMeasurementUtils getInstance() {
        return HHRParserMeasurementUtilsHolder.INSTANCE;
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
                Observation measurement = parser.parseResource(Observation.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, measurement));
            } catch(Exception ex) {
                Log.error("Error parsing Measurement {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        Observation measurement = (Observation) resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaMeasurement.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaMeasurement.getInstance().getSchemaValue());

        //parse the key                
        if(!measurement.hasId()) {
            avroKeyRecord.put("measurementid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("measurementid", measurement.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(measurement.hasStatus()) {
            avroValueRecord.put("status", measurement.getStatus().toCode());
        }

        if(measurement.hasCategory()&& measurement.getCategoryFirstRep().hasCoding()) {
            avroValueRecord.put("measurementcategorysystem", measurement.getCategoryFirstRep().getCodingFirstRep().getSystem());
            avroValueRecord.put("measurementcategorycode", measurement.getCategoryFirstRep().getCodingFirstRep().getCode());
            avroValueRecord.put("measurementcategorydisplay", measurement.getCategoryFirstRep().getCodingFirstRep().getDisplay());
        }

        if(measurement.hasCode()&& measurement.getCode().hasCoding()) {
            avroValueRecord.put("measurementsystem", measurement.getCode().getCodingFirstRep().getSystem());
            avroValueRecord.put("measurementcode", measurement.getCode().getCodingFirstRep().getCode());
            avroValueRecord.put("measurementdisplay", measurement.getCode().getCodingFirstRep().getDisplay());
        }


        if(measurement.hasEffectiveDateTimeType()) {
            long timestampValue = measurement.getEffectiveDateTimeType().getValueAsCalendar().getTimeInMillis();
            avroValueRecord.put("effectivedatetime", timestampValue + TIME_ZONE.getOffset(timestampValue));
        } else {
            avroValueRecord.put("effectivedatetime", 0L);
        }

        if(measurement.hasSubject() && measurement.getSubject().hasReferenceElement()) {
            //avroValueRecord.put("patientid", measurement.getSubject().getReferenceElement().getIdPartAsLong().intValue());
            avroValueRecord.put("patientid", measurement.getSubject().getReferenceElement().getIdPart());
        }


        if(measurement.hasPerformer()&& measurement.getPerformerFirstRep().hasReference()) {
            avroValueRecord.put("practitionerid", measurement.getPerformerFirstRep().getReferenceElement().getIdPartAsLong().intValue());
        }


        if(measurement.hasEncounter()&& measurement.getEncounter().hasReference()) {
            avroValueRecord.put("encounterid", measurement.getEncounter().getReferenceElement().getIdPartAsLong().intValue());
        }


        if(measurement.hasValueQuantity()) {
            avroValueRecord.put("valuesystem", measurement.getValueQuantity().getSystem());
            avroValueRecord.put("valuecode", measurement.getValueQuantity().getCode());
            avroValueRecord.put("valueunit", measurement.getValueQuantity().getUnit());
            avroValueRecord.put("valuequantity", measurement.getValueQuantity().getValue().doubleValue());                    
        }

        if(measurement.hasReferenceRange()) {
            if(measurement.getReferenceRangeFirstRep().hasLow()) {
                avroValueRecord.put("valuesystemlow", measurement.getReferenceRangeFirstRep().getLow().getSystem());
                avroValueRecord.put("valuecodelow", measurement.getReferenceRangeFirstRep().getLow().getCode());
                avroValueRecord.put("valueunitlow", measurement.getReferenceRangeFirstRep().getLow().getUnit());
                avroValueRecord.put("valuequantitylow", measurement.getReferenceRangeFirstRep().getLow().getValue().doubleValue());                    
            }
            if(measurement.getReferenceRangeFirstRep().hasHigh()) {
                avroValueRecord.put("valuesystemhigh", measurement.getReferenceRangeFirstRep().getHigh().getSystem());
                avroValueRecord.put("valuecodehigh", measurement.getReferenceRangeFirstRep().getHigh().getCode());
                avroValueRecord.put("valueunithigh", measurement.getReferenceRangeFirstRep().getHigh().getUnit());
                avroValueRecord.put("valuequantityhigh", measurement.getReferenceRangeFirstRep().getHigh().getValue().doubleValue());                    
            }
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
    
}
