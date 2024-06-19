package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class DailyActivity implements Serializable {
  
  private String id;
  private PatientInformation patient;
  private Concept stepsWalked;
  private Concept distanceTravelled;
  private Concept caloriesBurned;
  private Concept floorsClimbed;
  private Concept lightlyActiveMinutes;
  private Concept moderatelyActiveMinutes;
  private Concept highlyActiveMinutes;
  private Concept sedentaryMinutes;
  private Concept totalActiveMinutes;
  
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
  public Concept getStepsWalked(){
    return stepsWalked;
  }
  public void setStepsWalked(Concept stepsWalked){
    this.stepsWalked = stepsWalked;
  }
  public Concept getDistanceTravelled(){
    return distanceTravelled;
  }
  public void setDistanceTravelled(Concept distanceTravelled){
    this.distanceTravelled = distanceTravelled;
  }
  public Concept getCaloriesBurned(){
    return caloriesBurned;
  }
  public void setCaloriesBurned(Concept caloriesBurned){
    this.caloriesBurned = caloriesBurned;
  }
  public Concept getFloorsClimbed(){
    return floorsClimbed;
  }
  public void setFloorsClimbed(Concept floorsClimbed){
    this.floorsClimbed = floorsClimbed;
  }
  public Concept getLightlyActiveMinutes(){
    return lightlyActiveMinutes;
  }
  public void setLightlyActiveMinutes(Concept lightlyActiveMinutes){
    this.lightlyActiveMinutes = lightlyActiveMinutes;
  }
  public Concept getModeratelyActiveMinutes(){
    return moderatelyActiveMinutes;
  }
  public void setModeratelyActiveMinutes(Concept moderatelyActiveMinutes){
    this.moderatelyActiveMinutes = moderatelyActiveMinutes;
  }
  public Concept getHighlyActiveMinutes(){
    return highlyActiveMinutes;
  }
  public void setHighlyActiveMinutes(Concept highlyActiveMinutes){
    this.highlyActiveMinutes = highlyActiveMinutes;
  }
  public Concept getSedentaryMinutes(){
    return sedentaryMinutes;
  }
  public void setSedentaryMinutes(Concept sedentaryMinutes){
    this.sedentaryMinutes = sedentaryMinutes;
  }
  public Concept getTotalActiveMinutes(){
    return totalActiveMinutes;
  }
  public void setTotalActiveMinutes(Concept totalActiveMinutes){
    this.totalActiveMinutes = totalActiveMinutes;
  }
}
