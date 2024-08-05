package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaObservation implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaObservation(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaObservation(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("observationID").namespace("eu.ihelp.hhr").fields();
            assember.name("observationid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("observation").namespace("eu.ihelp.hhr").fields();
            assember.name("status").type().nullable().stringType().noDefault();
            assember.name("observationcode").type().nullable().stringType().noDefault();
            assember.name("observationsystem").type().nullable().stringType().noDefault();
            assember.name("observationdisplay").type().nullable().stringType().noDefault();
            assember.name("effectivedatetime").type(tsType).noDefault();
            assember.name("observationcategorycode").type().nullable().stringType().noDefault();
            assember.name("observationcategorysystem").type().nullable().stringType().noDefault();
            assember.name("observationcategorydisplay").type().nullable().stringType().noDefault();
            assember.name("valuesystem").type().nullable().stringType().noDefault();
            assember.name("valuecode").type().nullable().stringType().noDefault();
            assember.name("valueunit").type().nullable().stringType().noDefault();
            assember.name("valuequantity").type().nullable().doubleType().noDefault();
            assember.name("valuesystemlow").type().nullable().stringType().noDefault();
            assember.name("valuecodelow").type().nullable().stringType().noDefault();
            assember.name("valueunitlow").type().nullable().stringType().noDefault();
            assember.name("valuequantitylow").type().nullable().doubleType().noDefault();
            assember.name("valuesystemhigh").type().nullable().stringType().noDefault();
            assember.name("valuecodehigh").type().nullable().stringType().noDefault();
            assember.name("valueunithigh").type().nullable().stringType().noDefault();
            assember.name("valuequantityhigh").type().nullable().doubleType().noDefault();
            assember.name("valuestring").type().nullable().stringType().noDefault();
            assember.name("valueboolean").type().nullable().booleanType().noDefault();
            assember.name("bodySiteSystem").type().nullable().stringType().noDefault();
            assember.name("bodySiteCode").type().nullable().stringType().noDefault();
            assember.name("bodySiteDisplay").type().nullable().stringType().noDefault();
            assember.name("encounterid").type().nullable().intType().noDefault();
            assember.name("practitionerid").type().nullable().intType().noDefault();
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaObservation getInstance() {
        return (SchemaObservation) HOLDER.INSTANCE;
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
