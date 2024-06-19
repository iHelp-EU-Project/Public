package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Fatigue implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept fatigue;
  
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
  public Concept getFatigue() {
    return fatigue;
  }
  public void setFatigue(Concept fatigue) {
    this.fatigue = fatigue;
  }


}
