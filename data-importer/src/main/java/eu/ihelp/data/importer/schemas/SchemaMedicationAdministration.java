package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaMedicationAdministration implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaMedicationAdministration(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaMedicationAdministration(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("medicationAdministrationID").namespace("eu.ihelp.hhr").fields();
            assember.name("medicationadministrationid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("medicationAdministration").namespace("eu.ihelp.hhr").fields();
            assember.name("status").type().nullable().stringType().noDefault();
            assember.name("categorysystem").type().nullable().stringType().noDefault();
            assember.name("categorycode").type().nullable().stringType().noDefault();
            assember.name("categorydisplay").type().nullable().stringType().noDefault();
            assember.name("medicationcodesystem").type().nullable().stringType().noDefault();
            assember.name("medicationcodecode").type().nullable().stringType().noDefault();
            assember.name("medicationcodedisplay").type().nullable().stringType().noDefault();
            assember.name("reasoncodesystem").type().nullable().stringType().noDefault();
            assember.name("reasoncodecode").type().nullable().stringType().noDefault();
            assember.name("reasoncodedisplay").type().nullable().stringType().noDefault();
            assember.name("cycles").type().nullable().stringType().noDefault();
            assember.name("suspensionreasonsystem").type().nullable().stringType().noDefault();
            assember.name("suspensionreasoncode").type().nullable().stringType().noDefault();
            assember.name("suspensionreasondisplay").type().nullable().stringType().noDefault();
            assember.name("suspensionreasondays").type().nullable().stringType().noDefault();
            assember.name("partofid").type().nullable().stringType().noDefault();
            assember.name("partofprocider").type().nullable().stringType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaMedicationAdministration getInstance() {
        return (SchemaMedicationAdministration) HOLDER.INSTANCE;
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
