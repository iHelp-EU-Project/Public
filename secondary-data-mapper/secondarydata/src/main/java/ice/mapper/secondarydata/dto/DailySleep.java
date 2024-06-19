package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class DailySleep implements Serializable {

  private String id;
  private PatientInformation patient;
  private Concept sleepStart;
  private Concept sleepEnd;
  private Concept remMinutes;
  private Concept lightMinutes;
  private Concept deepMinutes;
  private Concept awakeMinutes;
  private Concept totalMinutes;
  
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
  public Concept getSleepStart(){
    return sleepStart;
  }
  public void setSleepStart(Concept sleepStart){
    this.sleepStart = sleepStart;
  }
  public Concept getSleepEnd(){
    return sleepEnd;
  }
  public void setSleepEnd(Concept sleepEnd){
    this.sleepEnd = sleepEnd;
  }
  public Concept getRemMinutes(){
    return remMinutes;
  }
  public void setRemMinutes(Concept remMinutes){
    this.remMinutes = remMinutes;
  }
  public Concept getLightMinutes(){
    return lightMinutes;
  }
  public void setLightMinutes(Concept lightMinutes){
    this.lightMinutes = lightMinutes;
  }
  public Concept getDeepMinutes(){
    return deepMinutes;
  }
  public void setDeepMinutes(Concept deepMinutes){
    this.deepMinutes = deepMinutes;
  }
  public Concept getAwakeMinutes(){
    return awakeMinutes;
  }
  public void setAwakeMinutes(Concept awakeMinutes){
    this.awakeMinutes = awakeMinutes;
  }
  public Concept getTotalMinutes(){
    return totalMinutes;
  }
  public void setTotalMinutes(Concept totalMinutes){
    this.totalMinutes = totalMinutes;
  }

  
}
