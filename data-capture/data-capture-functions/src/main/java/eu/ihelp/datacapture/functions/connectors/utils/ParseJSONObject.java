package eu.ihelp.datacapture.functions.connectors.utils;

import eu.ihelp.datacapture.functions.DataRowItem;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ParseJSONObject {
    private static final Logger Log = LoggerFactory.getLogger(ParseCSVLine.class);
    private final List<Schema.Field> fields;
    private final List<Schema.Field> fieldsKey;
    private final DateFormat dateTimeFormatter;
    protected final TimeZone TIME_ZONE = TimeZone.getDefault();
    
    private ParseJSONObject(Schema schema, Schema schemaKey, DateFormat dateTimeFormatter) {
        this.fields = schema.getFields();
        this.fieldsKey = schemaKey.getFields();
        this.dateTimeFormatter = dateTimeFormatter;
    }
    
    public static ParseJSONObject createInstance(Schema schema,  Schema schemaKey,  String datePattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        return new ParseJSONObject(schema, schemaKey, dateFormat);
    }
    
    public DataRowItem createDataRowItem(JSONObject jSONObject) {
        DataRowItem dataRowItem = new DataRowItem(this.fields.size() + this.fieldsKey.size());
        
        for(Schema.Field field : this.fieldsKey) {
            try {
                dataRowItem.getRow()[field.pos()] = generateValue(jSONObject, field.name(), field.schema(), field.schema().getLogicalType());
            } catch(IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                Log.warn(ex.getMessage());
                dataRowItem.getRow()[this.fieldsKey.size() + field.pos()] = null;
            } catch(Exception ex) {
                Log.error(ex.getMessage());
                return null;
            }
        }
        
        for(Schema.Field field : this.fields) {
            try {
                dataRowItem.getRow()[this.fieldsKey.size() + field.pos()] = generateValue(jSONObject, field.name(), field.schema(), field.schema().getLogicalType());
            } catch(IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                Log.warn(ex.getMessage());
                dataRowItem.getRow()[this.fieldsKey.size() + field.pos()] = null;
            } catch(Exception ex) {
                Log.error(ex.getMessage());
                return null;
            }
        }
        
        return dataRowItem;
    }
    
    
    private Object generateValue(JSONObject jSONObject, String keyName, Schema schema, LogicalType logicalType) {
        if(jSONObject.has(keyName)) {
            if(jSONObject.isNull(keyName)) {
                if(logicalType==null) {
                    return null;
                } else {//logical type is a Date/Timestamp
                    return 0; //Date/Timestamps cannot be null in Avro Schema, so return 0 instead
                }
            }
            
            Schema.Type type = schema.getType();
        
            if(type==Schema.Type.UNION) {
                type = schema.getTypes().get(0).getType();
            }
            
            switch(type) {
                case BOOLEAN:
                    return jSONObject.getBoolean(keyName);
                case DOUBLE:
                    return jSONObject.getDouble(keyName);
                case FLOAT:
                    return jSONObject.getFloat(keyName);
                case INT:
                    if(logicalType==null) {
                        return jSONObject.getInt(keyName);
                    } else {
                        if(logicalType instanceof LogicalTypes.Date) {
                            try {
                                return this.dateTimeFormatter.parse(jSONObject.getString(keyName)).getTime();
                            } catch(ParseException ex) {
                                Log.warn("Couild not parse date value: " + jSONObject.getString(keyName));
                                return null;
                            }
                        }
                    }
                case LONG:
                    if(logicalType==null) {
                        return jSONObject.getLong(keyName);
                    } else {
                        if(logicalType instanceof LogicalTypes.TimestampMillis) {
                            try {
                                long timestampMilis = this.dateTimeFormatter.parse(jSONObject.getString(keyName)).getTime();
                                return timestampMilis + TIME_ZONE.getOffset(timestampMilis);
                            } catch(ParseException ex) {
                                Log.warn("Couild not parse date value: " + jSONObject.getString(keyName));
                                return null;
                            }
                        } else {
                            throw new IllegalArgumentException("Schema loigical type '" + logicalType.getName() + "' is not supported");
                        }
                    }
                case STRING:
                    return jSONObject.getString(keyName);
                default:
                    throw new IllegalArgumentException("Schema type '" + type.getName() + "' is not supported");
            }
        }
        
        return null;
    }
}
