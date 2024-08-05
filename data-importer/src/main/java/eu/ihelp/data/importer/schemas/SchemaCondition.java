package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaCondition implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaCondition(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaCondition(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("conditionPK").namespace("eu.ihelp.hhr").fields();
            assember.name("conditionid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("condition").namespace("eu.ihelp.hhr").fields();
            assember.name("abatementdatetime").type(tsType).noDefault();
            assember.name("onsetdate").type(tsType).noDefault();
            assember.name("categorysystem").type().nullable().stringType().noDefault();
            assember.name("categorycode").type().nullable().stringType().noDefault();
            assember.name("categorydisplay").type().nullable().stringType().noDefault();
            assember.name("codesystem").type().nullable().stringType().noDefault();
            assember.name("codecode").type().nullable().stringType().noDefault();
            assember.name("codedisplay").type().nullable().stringType().noDefault();
            assember.name("encounterid").type().nullable().intType().noDefault();
            assember.name("practitionerid").type().nullable().intType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaCondition getInstance() {
        return (SchemaCondition) HOLDER.INSTANCE;
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
