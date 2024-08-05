package eu.ihelp.datacapture.functions.connectors.utils;

import eu.ihelp.datacapture.functions.DataRowItem;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ParseCSVLine {
    private static final Logger Log = LoggerFactory.getLogger(ParseCSVLine.class);
    private final List<Schema.Field> fields;
    private final List<Schema.Field> fieldsKey;
    private final String delimiter;
    private final String nullString;
    private final DateFormat dateTimeFormatter;
    private final TimeZone TIME_ZONE = TimeZone.getDefault();
    private final DateTimeFormatter timeFormatter;

    private ParseCSVLine(Schema schema, Schema schemaKey, String delimiter, DateFormat dateTimeFormatter, String nullString, DateTimeFormatter timeFormatter) {
        this.fields = schema.getFields();
        this.fieldsKey = schemaKey.getFields();
        this.delimiter = delimiter;
        this.nullString = nullString;
        this.dateTimeFormatter = dateTimeFormatter;
        this.timeFormatter = timeFormatter;
    }
    
    public static ParseCSVLine createInstance(Schema schema,  Schema schemaKey, String delimiter, String datePattern, String nullString, String timeFormatter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        DateTimeFormatter _timeFormatter = null;
        if((timeFormatter==null) || (timeFormatter.equals(""))) {
            _timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        } else {
            _timeFormatter = DateTimeFormatter.ofPattern(timeFormatter);
        }
        return new ParseCSVLine(schema, schemaKey, delimiter, dateFormat, nullString, _timeFormatter);
    }
    
    public DataRowItem createDataRowItem(String line) {
        if(line.isEmpty()) {
            return null;
        }
        
        DataRowItem dataRowItem = new DataRowItem(this.fields.size() + this.fieldsKey.size());
        //DataRowItem dataRowItem = new DataRowItem(this.fields.size());
        
        
        //replaces empty values with null string i.e. ,,, will be ,NULL,NULL,
        line = line.replace(this.delimiter+this.delimiter, this.delimiter + this.nullString + this.delimiter)
                .replace(this.delimiter+this.delimiter, this.delimiter + this.nullString + this.delimiter);
        
        String [] values = line.split(this.delimiter);
        
        for(Schema.Field field : this.fieldsKey) {
            try {
                dataRowItem.getRow()[field.pos()] = generateValue(values[field.pos()], field.schema(), field.schema().getLogicalType());
            } catch(IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                Log.warn(ex.getMessage());
                dataRowItem.getRow()[field.pos()] = null;
            } catch(Exception ex) {
                Log.error(ex.getMessage());
                return null;
            }
        }
        
        for(Schema.Field field : this.fields) {
            try {
                dataRowItem.getRow()[this.fieldsKey.size() + field.pos()] = generateValue(values[this.fieldsKey.size() + field.pos()], field.schema(), field.schema().getLogicalType());
            } catch(IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                Log.warn("Cannot parse {} for value {}. {}", field.name(), values[this.fieldsKey.size() + field.pos()],  ex.getMessage());
                dataRowItem.getRow()[this.fieldsKey.size() + field.pos()] = null;
            } catch(Exception ex) {
                Log.error(ex.getMessage());
                return null;
            }
        }
        
        
        return dataRowItem;
    }
    
    private Object generateValue(String value, Schema schema, LogicalType logicalType) {
        if(value.isEmpty()) {
            return null;
        }
        
        if(nullString!=null) {
            if(value.equalsIgnoreCase(nullString)) {
                return null;
            }
        }
        
        Schema.Type type = schema.getType();
        
        if(type==Schema.Type.UNION) {
            type = schema.getTypes().get(0).getType();
            
        }
        
        switch(type) {
            case BOOLEAN:
                return Boolean.valueOf(value);
            case DOUBLE:
                return Double.valueOf(value);
            case FLOAT:
                return Float.valueOf(value);
            case INT:
                    if(logicalType==null) {
                    return Integer.valueOf(value);
                } else {
                    if(logicalType instanceof LogicalTypes.Date) {
                        try {
                            long timestamp = this.dateTimeFormatter.parse(value).getTime();
                            return timestamp + TIME_ZONE.getOffset(timestamp);
                        } catch(ParseException ex) {
                            Log.warn("Couild not parse date value: " + value);
                            return null;
                        }
                    } else if(logicalType instanceof LogicalTypes.TimeMillis) {
                        LocalTime time = LocalTime.parse(value, timeFormatter);
                        return time.toSecondOfDay();
                    }
                }
            case LONG:
                if(logicalType==null) {
                    return Long.valueOf(value);
                } else {
                    if(logicalType instanceof LogicalTypes.TimestampMillis) {
                        try {
                            long timestamp = this.dateTimeFormatter.parse(value).getTime();
                            return timestamp + TIME_ZONE.getOffset(timestamp);
                        } catch(ParseException ex) {
                            Log.warn("Couild not parse date value: " + value);
                            return null;
                        }
                    } else {
                        throw new IllegalArgumentException("Schema loigical type '" + logicalType.getName() + "' is not supported");
                    }
                }
            case ARRAY:
                String [] _values = value.split(",");
                List<Object> list = new ArrayList<>(_values.length);
                for(String _value : _values) {
                    list.add(generateValue(_value, schema.getElementType(), logicalType));
                }
                return list;
            case STRING:
                return value;
            default:
                throw new IllegalArgumentException("Schema type '" + type.getName() + "' is not supported");
        }
    }
}
