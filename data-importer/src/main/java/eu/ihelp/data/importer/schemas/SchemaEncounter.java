package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaEncounter implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaEncounter(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaEncounter(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("encounterPK").namespace("eu.ihelp.hhr").fields();
            assember.name("encounterid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("encounter").namespace("eu.ihelp.hhr").fields();
            assember.name("status").type().nullable().stringType().noDefault();
            assember.name("periodstart").type(tsType).noDefault();
            assember.name("periodend").type(tsType).noDefault();
            assember.name("typesystem").type().nullable().stringType().noDefault();
            assember.name("typecode").type().nullable().stringType().noDefault();
            assember.name("typedisplay").type().nullable().stringType().noDefault();
            assember.name("serviceproviderid").type().nullable().intType().noDefault();
            assember.name("practitionerid").type().nullable().intType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaEncounter getInstance() {
        return (SchemaEncounter) HOLDER.INSTANCE;
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
