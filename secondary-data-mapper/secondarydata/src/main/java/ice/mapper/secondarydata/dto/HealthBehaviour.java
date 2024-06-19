package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class HealthBehaviour implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept smoking;
  private Concept nutritionalConsultation;
  private Concept physicalActivity;
  private Concept alcoholConsumption;
  private Concept mammographyOrUltrasound;
  
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
  public Concept getSmoking() {
    return smoking;
  }
  public void setSmoking(Concept smoking) {
    this.smoking = smoking;
  }
  public Concept getNutritionalConsultation() {
    return nutritionalConsultation;
  }
  public void setNutritionalConsultation(Concept nutritionalConsultation) {
    this.nutritionalConsultation = nutritionalConsultation;
  }
  public Concept getPhysicalActivity() {
    return physicalActivity;
  }
  public void setPhysicalActivity(Concept physicalActivity) {
    this.physicalActivity = physicalActivity;
  }
  public Concept getAlcoholConsumption() {
    return alcoholConsumption;
  }
  public void setAlcoholConsumption(Concept alcoholConsumption) {
    this.alcoholConsumption = alcoholConsumption;
  }
  public Concept getMammographyOrUltrasound() {
    return mammographyOrUltrasound;
  }
  public void setMammographyOrUltrasound(Concept mammographyOrUltrasound) {
    this.mammographyOrUltrasound = mammographyOrUltrasound;
  }
  
  
}
