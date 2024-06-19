package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class BodyTemperature implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept bodyTemperature;
  
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
  public Concept getBodyTemperature(){
    return bodyTemperature;
  }
  public void setBodyTemperature(Concept bodyTemperature){
    this.bodyTemperature = bodyTemperature;
  }
}
