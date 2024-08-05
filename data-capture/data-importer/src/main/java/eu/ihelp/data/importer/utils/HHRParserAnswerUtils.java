package eu.ihelp.data.importer.utils;

import eu.ihelp.data.importer.DataImporterProperties;
import eu.ihelp.data.importer.schemas.SchemaAnswer;
import eu.ihelp.data.importer.schemas.SchemaCondition;
import java.util.UUID;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRParserAnswerUtils extends HHRParserAbstractUtils {
    private static final Logger Log = LoggerFactory.getLogger(HHRParserConditionUtils.class);
    private final String topic = DataImporterProperties.getInstance().getKafkaTopicOutputAnswer();
    
    private static final String EXTENSION_QUESTIONNAIRE_ID = "https://fhir.ihelp-project.eu/StructureDefinition/questionnaireid";
    private static final String EXTENSION_QUESTION_ID = "https://fhir.ihelp-project.eu/StructureDefinition/questionid";
    private static final String EXTENSION_QUESTIONNAIRE_CODE_NAME = "https://fhir.ihelp-project.eu/StructureDefinition/questionnaire_code_name";
    private static final String EXTENSION_QUESTIONNAIRE_TITLE = "https://fhir.ihelp-project.eu/StructureDefinition/questionnaire_title";
    
    private HHRParserAnswerUtils() {}

    private static final class HHRParserAnswerUtilsHolder {
        private static final HHRParserAnswerUtils INSTANCE = new HHRParserAnswerUtils();
    }
    
    public static HHRParserAnswerUtils getInstance() {
        return HHRParserAnswerUtilsHolder.INSTANCE;
    }

    @Override
    public String getTopic() {
        return topic;
    }
    
    @Override
    public HHRRecordToImport parseValues(String dataSourceID, JSONArray values) {
        HHRRecordToImport hHRRecordToImport = new HHRRecordToImport(topic, values.length());
        for(int i=0; i<values.length(); i++) {
            JSONObject item = null;
            if(values.get(i) instanceof JSONObject) {
                item = values.getJSONObject(i);
            } else if(values.get(i) instanceof String) {
                item = new JSONObject(values.getString(i));
            } else {
                throw new IllegalArgumentException("values is not JSONObject or serialized (String) JSONObject");
            }
            
            try {
                QuestionnaireResponse questionnaireResponse = parser.parseResource(QuestionnaireResponse.class, item.toString());
                hHRRecordToImport.addNewRecord(parseResource(dataSourceID, questionnaireResponse));
            } catch(Exception ex) {
                Log.error("Error parsing questionnaireResponse {}. {}", item, ex);
                ex.printStackTrace();
            }
        }
        
        return hHRRecordToImport;
    }
    
    @Override
    public HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource) {
        QuestionnaireResponse questionnaireResponse = (QuestionnaireResponse)resource;
        
        GenericRecord avroKeyRecord = new GenericData.Record(SchemaAnswer.getInstance().getSchemaKey());
        GenericRecord avroValueRecord = new GenericData.Record(SchemaAnswer.getInstance().getSchemaValue());

        //parse the key                
        if(!questionnaireResponse.hasId()) {
            avroKeyRecord.put("answerid", UUID.randomUUID().toString());
        } else {
            avroKeyRecord.put("answerid", questionnaireResponse.getIdElement().getIdPart());
        }
        avroKeyRecord.put("providerid", dataSourceID);


        //parse the value
        if(questionnaireResponse.hasStatus()) {
            avroValueRecord.put("status", questionnaireResponse.getStatus().toCode());
        }
        
        if(questionnaireResponse.hasAuthored()) {
            long timestampValue = questionnaireResponse.getAuthored().getTime();
            avroValueRecord.put("authored", timestampValue + TIME_ZONE.getOffset(timestampValue));
        } else {
            avroValueRecord.put("authored", 0L);
        }
        
        if((questionnaireResponse.hasExtension()) && ((!questionnaireResponse.getExtension().isEmpty()))) {
            for(Extension extension : questionnaireResponse.getExtension().get(0).getExtension()) {
                if(extension.getUrl()!=null) {
                    if((extension.getUrl().equalsIgnoreCase(EXTENSION_QUESTIONNAIRE_ID)) && (extension.getValue() instanceof StringType)) {
                        avroValueRecord.put("questionnaireid", ((StringType) extension.getValue()).getValueAsString());
                    }
                    if((extension.getUrl().equalsIgnoreCase(EXTENSION_QUESTIONNAIRE_CODE_NAME)) && (extension.getValue() instanceof StringType)) {
                        avroValueRecord.put("questionnairename", ((StringType) extension.getValue()).getValueAsString());
                    }
                    if((extension.getUrl().equalsIgnoreCase(EXTENSION_QUESTIONNAIRE_TITLE)) && (extension.getValue() instanceof StringType)) {
                        avroValueRecord.put("questionnairetitle", ((StringType) extension.getValue()).getValueAsString());
                    }
                    if((extension.getUrl().equalsIgnoreCase(EXTENSION_QUESTION_ID)) && (extension.getValue() instanceof StringType)) {
                        avroValueRecord.put("questionid", ((StringType) extension.getValue()).getValueAsString());
                    }
                }
            }
        }
        
        if(questionnaireResponse.hasItem()) {
            avroValueRecord.put("linkId", questionnaireResponse.getItemFirstRep().getLinkId());
            avroValueRecord.put("question", questionnaireResponse.getItemFirstRep().getText());
            if(questionnaireResponse.getItemFirstRep().hasAnswer()) {
                //Object object = questionnaireResponse.getItemFirstRep().getAnswerFirstRep();
                avroValueRecord.put("answersystem", questionnaireResponse.getItemFirstRep().getAnswerFirstRep().getValueCoding().getSystem());
                avroValueRecord.put("answer", questionnaireResponse.getItemFirstRep().getAnswerFirstRep().getValueCoding().getDisplay());
                String score = questionnaireResponse.getItemFirstRep().getAnswerFirstRep().getValueCoding().getCode();
                try {
                    avroValueRecord.put("score", Double.valueOf(score));
                } catch(Exception ex) {
                    Log.warn("Cannot parse value {}", score);
                }
            }
        }
        
        if(questionnaireResponse.hasSubject() && questionnaireResponse.getSubject().hasReferenceElement()) {
            avroValueRecord.put("patientid", questionnaireResponse.getSubject().getReferenceElement().getIdPart());
        }

        return new HHRRecordToImport.KeyValueRecord(avroKeyRecord, avroValueRecord);
    }
    
}
