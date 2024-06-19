package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class OxygenSaturation implements Serializable {
  
  private String id;
  private PatientInformation patient;
  private Concept oxygen;
  private Concept pulse;
  private Concept pi;
  private Concept notes;

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
  public Concept getOxygen(){
    return oxygen;
  }
  public void setOxygen(Concept oxygen){
    this.oxygen = oxygen;
  }
  public Concept getPulse(){
    return pulse;
  }
  public void setPulse(Concept pulse){
    this.pulse = pulse;
  }
  public Concept getPI(){
    return pi;
  }
  public void setPI(Concept pi){
    this.pi = pi;
  }
  public Concept getNotes(){
    return notes;
  }
  public void setNotes(Concept notes){
    this.notes = notes;
  }
}
