package eu.ihelp.data.importer.schemas;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SchemaProcedure implements HHRSchemas{
    
    private final Schema schemaKey;
    private final Schema schemaValue;

    private SchemaProcedure(Schema schemaKey, Schema schemaValue) {
        this.schemaKey = schemaKey;
        this.schemaValue = schemaValue;
    }
    
    private static final class HOLDER {
        private static final HHRSchemas INSTANCE = buildInstance();
        
        static HHRSchemas buildInstance() {
            Schema key = buildKey();
            Schema value = buildValue();
            
            return new SchemaProcedure(key, value);
        } 
        
        static Schema buildKey() {            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("procedureID").namespace("eu.ihelp.hhr").fields();
            assember.name("procedureid").type().stringType().noDefault();
            assember.name("providerid").type().stringType().noDefault();
            return (Schema) assember.endRecord();
        }
        
        static Schema buildValue() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            
            SchemaBuilder.FieldAssembler assember = SchemaBuilder.record("procedure").namespace("eu.ihelp.hhr").fields();
            assember.name("status").type().nullable().stringType().noDefault();
            assember.name("procedurecode").type().nullable().stringType().noDefault();
            assember.name("proceduresystem").type().nullable().stringType().noDefault();
            assember.name("proceduredisplay").type().nullable().stringType().noDefault();
            assember.name("procedurecategorycode").type().nullable().stringType().noDefault();
            assember.name("procedurecategorysystem").type().nullable().stringType().noDefault();
            assember.name("procedurecategorydisplay").type().nullable().stringType().noDefault();
            
            assember.name("performedperiodstart").type(tsType).noDefault();
            assember.name("performedperiodend").type(tsType).noDefault();
            assember.name("cycles").type().nullable().stringType().noDefault();
            assember.name("radiotherapysettingsystem").type().nullable().stringType().noDefault();
            assember.name("radiotherapysettingcode").type().nullable().stringType().noDefault();
            assember.name("radiotherapysettingdisplay").type().nullable().stringType().noDefault();
            assember.name("radiotherapytimingsystem").type().nullable().stringType().noDefault();
            assember.name("radiotherapytimingcode").type().nullable().stringType().noDefault();
            assember.name("radiotherapytimingdisplay").type().nullable().stringType().noDefault();
            assember.name("radiotherapytechniquesystem").type().nullable().stringType().noDefault();
            assember.name("radiotherapytechniquecode").type().nullable().stringType().noDefault();
            assember.name("radiotherapytechniquedisplay").type().nullable().stringType().noDefault();
            assember.name("radiotherapyctv1dose").type().nullable().stringType().noDefault();
            assember.name("radiotherapyctv1fractionation").type().nullable().stringType().noDefault();
            assember.name("radiotherapyctv2dose").type().nullable().stringType().noDefault();
            assember.name("radiotherapyctv2fractionation").type().nullable().stringType().noDefault();
            assember.name("radiotherapyctv3dose").type().nullable().stringType().noDefault();
            assember.name("radiotherapyctv3fractionation").type().nullable().stringType().noDefault();
            assember.name("suspensionreasonsystem").type().nullable().stringType().noDefault();
            assember.name("suspensionreasoncode").type().nullable().stringType().noDefault();
            assember.name("suspensionreasondisplay").type().nullable().stringType().noDefault();
            assember.name("suspensionreasondays").type().nullable().stringType().noDefault();
            assember.name("closurereasonsystem").type().nullable().stringType().noDefault();
            assember.name("closurereasoncode").type().nullable().stringType().noDefault();
            assember.name("closurereasondisplay").type().nullable().stringType().noDefault();
            assember.name("surgeryapproachsystem").type().nullable().stringType().noDefault();
            assember.name("surgeryapproachcode").type().nullable().stringType().noDefault();
            assember.name("surgeryapproachdisplay").type().nullable().stringType().noDefault();
            assember.name("gradingsystem").type().nullable().stringType().noDefault();
            assember.name("gradingcode").type().nullable().stringType().noDefault();
            assember.name("gradingdisplay").type().nullable().stringType().noDefault();
            assember.name("duration").type().nullable().stringType().noDefault();
            assember.name("pancreaticfistula").type().nullable().booleanType().noDefault();
            assember.name("biliaryfistula").type().nullable().booleanType().noDefault();
            assember.name("resurgery").type().nullable().booleanType().noDefault();
            
            assember.name("patientid").type().nullable().stringType().noDefault();
            
            return (Schema) assember.endRecord();
        }
    }
    
    public static SchemaProcedure getInstance() {
        return (SchemaProcedure) HOLDER.INSTANCE;
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
