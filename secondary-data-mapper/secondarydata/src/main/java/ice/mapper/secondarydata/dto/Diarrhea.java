package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Diarrhea implements Serializable {
  
  private String id;
  private PatientInformation patient;
  private Concept diarrhea;
  
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
  public Concept getDiarrhea(){
    return diarrhea;
  }
  public void setDiarrhea(Concept diarrhea){
    this.diarrhea = diarrhea;
  }
}
