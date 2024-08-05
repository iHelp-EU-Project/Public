package eu.ihelp.datacapture.functions.connectors.utils;

import eu.ihelp.datacapture.functions.DataRowItem;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class GenerateRandomValues {
    private static final Logger Log = LoggerFactory.getLogger(GenerateRandomValues.class);
    private final Random rd = new Random();    
    private final List<Schema.Field> fields;
    
    private GenerateRandomValues(Schema schema) {
        this.fields = schema.getFields();
    }
    
    public static GenerateRandomValues createInstance(Schema schema) {
        return new GenerateRandomValues(schema);
    }
    
    public DataRowItem generateRandom() {
        DataRowItem dataRowItem = new DataRowItem(this.fields.size());
        for(Schema.Field field : this.fields) {            
            try {
                dataRowItem.getRow()[field.pos()] = generateValue(field.schema().getType(), field.schema().getLogicalType());
            } catch(IllegalArgumentException ex) {
                Log.warn(ex.getMessage());
                dataRowItem.getRow()[field.pos()] = null;
            }
        }
        
        return dataRowItem;
    }
    
    private Object generateValue(Type type, LogicalType logicalType) {
        switch(type) {
            case BOOLEAN:
                return rd.nextBoolean();
            case DOUBLE:
                return rd.nextDouble();
            case FLOAT:
                return rd.nextFloat();
            case INT:
                return rd.nextInt();
            case LONG:
                if(logicalType==null) {
                    return rd.nextLong();
                } else {
                    if(logicalType instanceof LogicalTypes.TimestampMillis) {
                        return System.currentTimeMillis();
                    } else {
                        throw new IllegalArgumentException("Schema loigical type '" + logicalType.getName() + "' is not supported");
                    }
                }
            case STRING:
                return UUID.randomUUID().toString();
            default:
                throw new IllegalArgumentException("Schema type '" + type.getName() + "' is not supported");
        }
    }
}
