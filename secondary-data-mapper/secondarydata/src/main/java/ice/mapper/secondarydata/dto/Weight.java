package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Weight implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept weight;

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
  public Concept getWeight(){
    return weight;
  }
  public void setWeight(Concept weight){
    this.weight = weight;
  }
}
