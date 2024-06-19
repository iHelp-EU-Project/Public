package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class GeneralHealth implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept description;

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
  public Concept getDescription() {
    return description;
  }
  public void setDescription(Concept description) {
    this.description = description;
  }
  
}
