package eu.ihelp.datacapture.functions.connectors.utils;

import eu.ihelp.datacapture.functions.DataRowItem;
import java.math.BigInteger;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is not thread safe. Each thread must have its own instance
 * 
 * @author Pavlos Kranas (LeanXcale)
 */
public class RowToKafkaAvroConverter {
    private static final Logger Log = LoggerFactory.getLogger(RowToKafkaAvroConverter.class);
    private final TimeZone TIME_ZONE = TimeZone.getDefault();
    private final Schema schemaKey;
    private final Schema schemaValue;
    private final DateFormat dateTimeFormatter;
    
    GenericRecord avroKeyRecord;
    GenericRecord avroValueRecord;

    private RowToKafkaAvroConverter(Schema schemaKey, Schema schemaValue, DateFormat dateTimeFormatter) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
        this.dateTimeFormatter = dateTimeFormatter;
    }
    
    

    public static RowToKafkaAvroConverter createInstance(Schema schema, Schema schemaKey, String datePattern) {
        DateFormat dateTimeFormatter = null;
        if(datePattern!=null) {
            dateTimeFormatter = new SimpleDateFormat(datePattern);
        }
        return new RowToKafkaAvroConverter(schemaKey, schema, dateTimeFormatter);
    }
    
    
    public void convert(DataRowItem row) {
        this.avroKeyRecord = new GenericData.Record(schemaKey);
        this.avroValueRecord = new GenericData.Record(schemaValue);
        
        
        int index=0;
        //generate key record
        for(Field field : this.schemaKey.getFields()) {
            try {
                Object value = generateValue(row.getRow()[index], field.schema().getLogicalType(), field.defaultVal());
                this.avroKeyRecord.put(field.name(), value);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            index++;
        }
        
        //generate value record
        for(Field field : this.schemaValue.getFields()) {
            try {
                Object value = generateValue(row.getRow()[index], field.schema().getLogicalType(), field.defaultVal());
                if(value!=null) {
                    this.avroValueRecord.put(field.name(), value);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            index++;
        }
        
    }
     
    private Object generateValue(Object value, LogicalType logicalType, Object defaultValue) {
        if(value==null) {
            return defaultValue;
        }
        
        
        if(logicalType==null) {
            return value;
        } else {
            if(logicalType instanceof LogicalTypes.TimestampMillis) {
                if(value instanceof Integer) {
                    return new BigInteger(String.valueOf((Integer) value)).longValue();
                }
                
                /*
                return new Date((long) value).getTime();
            } else if(logicalType instanceof LogicalTypes.Date) {
                return value;
            } else if(logicalType instanceof LogicalTypes.TimeMillis) {
                return value;
                //Time t = Time.valueOf((String) value);
                //Time time = new Time(t.getTime() + TIME_ZONE.getOffset(t.getTime()));
                //return time;
            }
            return null;
            */
            
            }
        }
        
            
        return value;
    }
    

    public GenericRecord getAvroKeyRecord() {
        return avroKeyRecord;
    }

    public GenericRecord getAvroValueRecord() {
        return avroValueRecord;
    }
    
}
