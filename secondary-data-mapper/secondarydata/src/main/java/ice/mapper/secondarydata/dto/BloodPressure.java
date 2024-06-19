package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class BloodPressure implements Serializable {
  
  private String id;
  private PatientInformation patient;
  private Concept diastolic;
  private Concept systolic;
  private Concept heartRate;
  private Concept arythmia;
  
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
  public Concept getDiastolic(){
    return diastolic;
  }
  public void setDiastolic(Concept diastolic){
    this.diastolic = diastolic;
  }
  public Concept getSystolic(){
    return systolic;
  }
  public void setSystolic(Concept systolic){
    this.systolic = systolic;
  }
  public Concept getHeartRate(){
    return heartRate;
  }
  public void setHeartRate(Concept heartRate){
    this.heartRate = heartRate;
  }
  public Concept getArythmia(){
    return arythmia;
  }
  public void setArythmia(Concept arythmia){
    this.arythmia = arythmia;
  }

}
