package eu.ihelp.datacapture.functions.connectors.utils;

import eu.ihelp.datacapture.functions.DataRowItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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
public class RowToJsonConverter {
    private static final Logger Log = LoggerFactory.getLogger(RowToJsonConverter.class);
    private final List<Schema.Field> fields;
    private final List<Schema.Field> fieldsKey;
    private final DateFormat dateTimeFormatter;

    public RowToJsonConverter(Schema schema, Schema schemaKey, DateFormat datePattern) {
        this.fields = schema.getFields();
        this.fieldsKey = schemaKey.getFields();
        this.dateTimeFormatter = datePattern;
    }
    
    public static RowToJsonConverter createInstance(Schema schema, Schema schemaKey, String datePattern) {
        DateFormat dateTimeFormatter = null;
        if(datePattern!=null) {
            dateTimeFormatter = new SimpleDateFormat(datePattern);
        }
            
        return new RowToJsonConverter(schema, schemaKey, dateTimeFormatter);
    }
    
    
    public JSONObject convert(DataRowItem row) {
        JSONObject jSONObject = new JSONObject();
        
        for(Schema.Field field : this.fieldsKey) {
            jSONObject.put(field.name(), generateValue(row.getRow()[field.pos()], field.schema().getLogicalType()));
        }
        
        for(Schema.Field field : this.fields) {
            jSONObject.put(field.name(), generateValue(row.getRow()[this.fieldsKey.size() + field.pos()], field.schema().getLogicalType()));
        }
        return jSONObject;
    }
    
    private Object generateValue(Object value, LogicalType logicalType) {
        if(value==null) {
            return null;
        }
        
        if(logicalType==null) {
            return value;
        } else {
            if(logicalType instanceof LogicalTypes.TimestampMillis) {
                return value;
            } else if(logicalType instanceof LogicalTypes.Date) {
                return value;
            } else if(logicalType instanceof LogicalTypes.TimeMillis) {
                return value;
            }
            return null;
        }
    }
}
