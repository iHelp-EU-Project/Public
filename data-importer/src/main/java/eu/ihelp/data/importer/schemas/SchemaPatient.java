package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaPatient implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaPatient(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaPatient(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("patientPK").namespace("eu.ihelp.hhr").fields();
            assember.name("patientid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("patient").namespace("eu.ihelp.hhr").fields();
            assember.name("gender").type().nullable().stringType().noDefault();
            assember.name("active").type().nullable().booleanType().noDefault();
            assember.name("birthdate").type(tsType).noDefault();
            assember.name("height").type().nullable().doubleType().noDefault();
            assember.name("weight").type().nullable().doubleType().noDefault();
            assember.name("timezone").type().nullable().stringType().noDefault();
            assember.name("baselinedate").type(tsType).noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaPatient getInstance() {
        return (SchemaPatient) HOLDER.INSTANCE;
    }

    @Override
    public Schema getSchemaKey() {
        return schemaKey;
    }

    @Override
    public Schema getSchemaValue() {
        return schemaValue;
    }
    
    
}
