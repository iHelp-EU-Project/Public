package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Cough implements Serializable {

  private String id;
  private PatientInformation patient;
  private Concept cough;
  private Concept coughType;
  
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
  public Concept getCough(){
    return cough;
  }
  public void setCough(Concept cough){
    this.cough = cough;
  }
  public Concept getCoughType(){
    return coughType;
  }
  public void setCoughType(Concept coughType){
    this.coughType = coughType;
  }
}
