package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Pain implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept experience;
  private Concept location;

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
  public Concept getExperience() {
    return experience;
  }
  public void setExperience(Concept experience) {
    this.experience = experience;
  }
  public Concept getLocation() {
    return location;
  }
  public void setLocation(Concept location) {
    this.location = location;
  }
  
}
