package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaFamilyMemberHistory implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaFamilyMemberHistory(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaFamilyMemberHistory(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("familymemberhistoryPK").namespace("eu.ihelp.hhr").fields();
            assember.name("familymemberhistoryid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("familymemberhistory").namespace("eu.ihelp.hhr").fields();
            assember.name("conditionsystem").type().nullable().stringType().noDefault();
            assember.name("conditioncode").type().nullable().stringType().noDefault();
            assember.name("conditiondisplay").type().nullable().stringType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaFamilyMemberHistory getInstance() {
        return (SchemaFamilyMemberHistory) HOLDER.INSTANCE;
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
