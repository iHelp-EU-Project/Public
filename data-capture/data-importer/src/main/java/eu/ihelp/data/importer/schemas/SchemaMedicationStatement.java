package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaMedicationStatement implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaMedicationStatement(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaMedicationStatement(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("medicationID").namespace("eu.ihelp.hhr").fields();
            assember.name("medicationid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("medication").namespace("eu.ihelp.hhr").fields();
            assember.name("status").type().nullable().stringType().noDefault();
            assember.name("medicationsystem").type().nullable().stringType().noDefault();
            assember.name("medicationcode").type().nullable().stringType().noDefault();
            assember.name("medicationdisplay").type().nullable().stringType().noDefault();
            assember.name("effectiveperiodstart").type(tsType).noDefault();
            assember.name("effectiveperiodend").type(tsType).noDefault();
            assember.name("dosage").type().nullable().doubleType().noDefault();
            assember.name("encounterid").type().nullable().intType().noDefault();
            assember.name("practitionerid").type().nullable().intType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaMedicationStatement getInstance() {
        return (SchemaMedicationStatement) HOLDER.INSTANCE;
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
