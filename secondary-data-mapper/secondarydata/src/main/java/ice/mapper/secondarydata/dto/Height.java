package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Height implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept height;

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
  public Concept getHeight(){
    return height;
  }
  public void setHeight(Concept height){
    this.height = height;
  }
}
