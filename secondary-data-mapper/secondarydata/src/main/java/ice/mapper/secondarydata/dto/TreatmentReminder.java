package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class TreatmentReminder implements Serializable{

  private String id;
  private PatientInformation patient;
  private Concept dosesTaken;
  private Concept dailyDoses;
  private Concept unit;
  private Concept dose;
  private Concept treatmentName;
  
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
  public Concept getDosesTaken(){
    return dosesTaken;
  }
  public void setDosesTaken(Concept dosesTaken){
    this.dosesTaken = dosesTaken;
  }
  public Concept getDailyDoses(){
    return dailyDoses;
  }
  public void setDailyDoses(Concept dailyDoses){
    this.dailyDoses = dailyDoses;
  }
  public Concept getUnit(){
    return unit;
  }
  public void setUnit(Concept unit){
    this.unit = unit;
  }
  public Concept getDose(){
    return dose;
  }
  public void setDose(Concept dose){
    this.dose = dose;
  }
  public Concept getTreatmentName(){
    return treatmentName;
  }
  public void setTreatmentName(Concept treatmentName){
    this.treatmentName = treatmentName;
  }
}
