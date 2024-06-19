package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class EdmontonAssessment implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept edmontonSymptomAssessment1;
  private Concept edmontonSymptomAssessment2;
  private Concept edmontonSymptomAssessment3;
  private Concept edmontonSymptomAssessment4;
  private Concept edmontonSymptomAssessment5;
  private Concept edmontonSymptomAssessment6;
  private Concept edmontonSymptomAssessment7;
  private Concept edmontonSymptomAssessment8;
  private Concept edmontonSymptomAssessment9;
  private Concept hurtLocation;
  private Concept otherSympotom;
  private Concept additionalSymptom;
  private Concept additionalSymptomDetails;
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public PatientInformation getPatient() {
    return patient;
  }
  public void setPatient(PatientInformation patient) {
    this.patient = patient;
  }
  public Concept getEdmontonSymptomAssessment1() {
    return edmontonSymptomAssessment1;
  }
  public void setEdmontonSymptomAssessment1(Concept edmontonSymptomAssessment1) {
    this.edmontonSymptomAssessment1 = edmontonSymptomAssessment1;
  }
  public Concept getEdmontonSymptomAssessment2() {
    return edmontonSymptomAssessment2;
  }
  public void setEdmontonSymptomAssessment2(Concept edmontonSymptomAssessment2) {
    this.edmontonSymptomAssessment2 = edmontonSymptomAssessment2;
  }
  public Concept getEdmontonSymptomAssessment3() {
    return edmontonSymptomAssessment3;
  }
  public void setEdmontonSymptomAssessment3(Concept edmontonSymptomAssessment3) {
    this.edmontonSymptomAssessment3 = edmontonSymptomAssessment3;
  }
  public Concept getEdmontonSymptomAssessment4() {
    return edmontonSymptomAssessment4;
  }
  public void setEdmontonSymptomAssessment4(Concept edmontonSymptomAssessment4) {
    this.edmontonSymptomAssessment4 = edmontonSymptomAssessment4;
  }
  public Concept getEdmontonSymptomAssessment5() {
    return edmontonSymptomAssessment5;
  }
  public void setEdmontonSymptomAssessment5(Concept edmontonSymptomAssessment5) {
    this.edmontonSymptomAssessment5 = edmontonSymptomAssessment5;
  }
  public Concept getEdmontonSymptomAssessment6() {
    return edmontonSymptomAssessment6;
  }
  public void setEdmontonSymptomAssessment6(Concept edmontonSymptomAssessment6) {
    this.edmontonSymptomAssessment6 = edmontonSymptomAssessment6;
  }
  public Concept getEdmontonSymptomAssessment7() {
    return edmontonSymptomAssessment7;
  }
  public void setEdmontonSymptomAssessment7(Concept edmontonSymptomAssessment7) {
    this.edmontonSymptomAssessment7 = edmontonSymptomAssessment7;
  }
  public Concept getEdmontonSymptomAssessment8() {
    return edmontonSymptomAssessment8;
  }
  public void setEdmontonSymptomAssessment8(Concept edmontonSymptomAssessment8) {
    this.edmontonSymptomAssessment8 = edmontonSymptomAssessment8;
  }
  public Concept getEdmontonSymptomAssessment9() {
    return edmontonSymptomAssessment9;
  }
  public void setEdmontonSymptomAssessment9(Concept edmontonSymptomAssessment9) {
    this.edmontonSymptomAssessment9 = edmontonSymptomAssessment9;
  }
  public Concept getHurtLocation() {
    return hurtLocation;
  }
  public void setHurtLocation(Concept hurtLocation) {
    this.hurtLocation = hurtLocation;
  }
  public Concept getOtherSympotom() {
    return otherSympotom;
  }
  public void setOtherSympotom(Concept otherSympotom) {
    this.otherSympotom = otherSympotom;
  }
  public Concept getAdditionalSymptom() {
    return additionalSymptom;
  }
  public void setAdditionalSymptom(Concept additionalSymptom) {
    this.additionalSymptom = additionalSymptom;
  }
  public Concept getAdditionalSymptomDetails() {
    return additionalSymptomDetails;
  }
  public void setAdditionalSymptomDetails(Concept additionalSymptomDetails) {
    this.additionalSymptomDetails = additionalSymptomDetails;
  }

  
}
