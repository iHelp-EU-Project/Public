package ice.mapper.secondarydata.utils;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ice.mapper.secondarydata.dto.Concept;

@Component
@SuppressWarnings("rawtypes")
public class FhirUtils {
  
  @Autowired
  private FhirLabels fhirLabels;
  
  /**
   * 
   * @param codeSystem
   * @param categoryCode
   * @param categoryDisplay
   * @return
   */
  public CodeableConcept createCodeableConcept(String codeSystem, String categoryCode, String categoryDisplay) {

    CodeableConcept code;
    Coding codeCoding;

    code = new CodeableConcept();

    codeCoding = new Coding(codeSystem, categoryCode, categoryDisplay);
    code.addCoding(codeCoding);

    return code;
  }

  /**
   * 
   * @param <T>
   * @param codeConcept
   * @param patientID
   * @param codeSystem
   * @param categoryCode
   * @param categoryDisplay
   * @param fhirType
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T createFhirObject(Concept codeConcept, Concept timeConcept, String patientID, String codeSystem, String categoryCode, String categoryDisplay, String HelthentiaCode){
    CodeableConcept code;
    CodeableConcept category;

    code = createCodeableConcept(codeConcept.getVocabulary(),codeConcept.getId(),codeConcept.getName());

    category = createCodeableConcept(codeSystem, categoryCode, categoryDisplay);

    List<CodeableConcept> categories = new ArrayList<>();
    categories.add(category);

    Reference patient = new Reference();
    patient.setReference(fhirLabels.getPatientDisplay() + "/" + patientID);
    patient.setType(fhirLabels.getPatientDisplay());

    Observation observation = new Observation();

    observation.setId(HelthentiaCode);
    observation.setCode(code);
    observation.setCategory(categories);
    observation.setSubject(patient);
    if (timeConcept != null){
      String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Long.parseLong((String)timeConcept.getValue()));
      
      observation.setEffective(new DateTimeType(date));
    }

    if(codeConcept.getValue() instanceof String){
      observation.setValue(new StringType((String)codeConcept.getValue()));
    }else if(codeConcept.getValue() instanceof Concept) {
      Concept<Concept<String>> valueConcept = (Concept<Concept<String>>) codeConcept.getValue();

      CodeableConcept valueCodeableConcept = createCodeableConcept(valueConcept.getVocabulary(),valueConcept.getId(),valueConcept.getName());

      observation.setValue(valueCodeableConcept);
    }

    return (T) observation;
  }

  public Patient addExtension(Patient patient, Concept concept){
    Extension extension = new Extension();

    extension.setValue(createCodeableConcept(concept.getVocabulary(), concept.getId(), concept.getName()));

    patient.getExtension().add(extension);

    return patient;
  }

  public QuestionnaireResponse addItem(QuestionnaireResponse questionnaireResponse, Concept concept){
    QuestionnaireResponseItemComponent item = new QuestionnaireResponseItemComponent();

    item.setLinkId(concept.getVocabulary() + " " + concept.getId());

    item.addAnswer(new QuestionnaireResponseItemAnswerComponent().setValue(new StringType((String)concept.getValue())));

    questionnaireResponse.addItem(item);

    return questionnaireResponse;
  }

  public QuestionnaireResponse setPatientAndCategory(QuestionnaireResponse questionnaireResponse, String patientID, String vocabulary, String categoryCode, String categoryDisplay){
    
    Reference patient = new Reference();
    patient.setReference(fhirLabels.getPatientDisplay() + "/" + patientID);
    patient.setType(fhirLabels.getPatientDisplay());

    questionnaireResponse.setSubject(patient);

    Identifier identifier = new Identifier();
    identifier.setType(createCodeableConcept(vocabulary,categoryCode,categoryDisplay));

    questionnaireResponse.setIdentifier(identifier);

    return questionnaireResponse;
  }
}
