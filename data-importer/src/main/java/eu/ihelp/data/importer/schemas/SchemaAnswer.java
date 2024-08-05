package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaAnswer implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaAnswer(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaAnswer(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("answerPK").namespace("eu.ihelp.hhr").fields();
            assember.name("answerid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("answer").namespace("eu.ihelp.hhr").fields();
            assember.name("status").type().nullable().stringType().noDefault();
            assember.name("authored").type(tsType).noDefault();
            assember.name("questionnaireid").type().nullable().stringType().noDefault();
            assember.name("questionnairename").type().nullable().stringType().noDefault();
            assember.name("questionnairetitle").type().nullable().stringType().noDefault();
            assember.name("linkId").type().nullable().stringType().noDefault();
            assember.name("questionid").type().nullable().stringType().noDefault();
            assember.name("question").type().nullable().stringType().noDefault();
            assember.name("answersystem").type().nullable().stringType().noDefault();
            assember.name("answer").type().nullable().stringType().noDefault();
            assember.name("score").type().nullable().doubleType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaAnswer getInstance() {
        return (SchemaAnswer) HOLDER.INSTANCE;
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
