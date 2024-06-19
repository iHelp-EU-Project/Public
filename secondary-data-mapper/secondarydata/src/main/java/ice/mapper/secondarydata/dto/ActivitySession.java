package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class ActivitySession implements Serializable {
  
  private String id;
  private PatientInformation patient;
  private Concept activityType;
  private Concept startTime;
  private Concept activeDuration;
  private Concept duration;
  private Concept calories;
  private Concept speed;
  private Concept pace;
  private Concept steps;
  private Concept distance;
  private Concept distanceUnit;
  private Concept elevationGain;
  private Concept averageHeartRate;
  private Concept outOfRangeMinutes;
  private Concept fatBurnMinutes;
  private Concept cardioMinutes;
  private Concept peakMinutes;
  private Concept heartRateZones;
  private Concept lightlyMinutes;
  private Concept fairlyMinutes;
  private Concept veryMinutes;
  private Concept sedentaryMinutes;

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
  public Concept getActivityType(){
    return activityType;
  }
  public void setActivityType(Concept activityType){
    this.activityType = activityType;
  }
  public Concept getStartTime() {
    return startTime;
  }
  public void setStartTime(Concept startTime) {
    this.startTime = startTime;
  }
  public Concept getActiveDuration(){
    return activeDuration;
  }
  public void setActiveDuration(Concept activeDuration){
    this.activeDuration = activeDuration;
  }
  public Concept getDuration(){
    return duration;
  }
  public void setDuration(Concept duration){
    this.duration = duration;
  }
  public Concept getCalories(){
    return calories;
  }
  public void setCalories(Concept calories){
    this.calories = calories;
  }
  public Concept getSpeed(){
    return speed;
  }
  public void setSpeed(Concept speed){
    this.speed = speed;
  }
  public Concept getPace(){
    return pace;
  }
  public void setPace(Concept pace){
    this.pace = pace;
  }
  public Concept getSteps(){
    return steps;
  }
  public void setSteps(Concept steps){
    this.steps = steps;
  }
  public Concept getDistance(){
    return distance;
  }
  public void setDistance(Concept distance){
    this.distance = distance;
  }
  public Concept getDistanceUnit(){
    return distanceUnit;
  }
  public void setDistanceUnit(Concept distanceUnit){
    this.distanceUnit = distanceUnit;
  }
  public Concept getElevationGain(){
    return elevationGain;
  }
  public void setElevationGain(Concept elevationGain){
    this.elevationGain = elevationGain;
  }
  public Concept getAverageHeartRate(){
    return averageHeartRate;
  }
  public void setAverageHeartRate(Concept averageHeartRate){
    this.averageHeartRate = averageHeartRate;
  }
  public Concept getOutOfRangeMinutes() {
    return outOfRangeMinutes;
  }
  public void setOutOfRangeMinutes(Concept outOfRangeMinutes){
    this.outOfRangeMinutes = outOfRangeMinutes;
  }
  public Concept getFatBurnMinutes(){
    return fatBurnMinutes;
  }
  public void setFatBurnMinutes(Concept fatBurnMinutes){
    this.fatBurnMinutes = fatBurnMinutes;
  }
  public Concept getCardioMinutes(){
    return cardioMinutes;
  }
  public void setCardioMinutes(Concept cardioMinutes){
    this.cardioMinutes = cardioMinutes;
  }
  public Concept getPeakMinutes(){
    return peakMinutes;
  }
  public void setPeakMinutes(Concept peakMinutes){
    this.peakMinutes = peakMinutes;
  }
  public Concept getHeartRateZones(){
    return heartRateZones;
  }
  public void setHeartRateZones(Concept heartRateZones){
    this.heartRateZones = heartRateZones;
  }
  public Concept getLightlyMinutes(){
    return lightlyMinutes;
  }
  public void setLightlyMinutes(Concept lightlyMinutes){
    this.lightlyMinutes = lightlyMinutes;
  }
  public Concept getFairlyMinutes(){
    return fairlyMinutes;
  }
  public void setFairlyMinutes(Concept fairlyMinutes){
    this.fairlyMinutes = fairlyMinutes;
  }
  public Concept getVeryMinutes(){
    return veryMinutes;
  }
  public void setVeryMinutes(Concept veryMinutes){
    this.veryMinutes = veryMinutes;
  }
  public Concept getSedentaryMinutes(){
    return sedentaryMinutes;
  }
  public void setSedentaryMinutes(Concept sedentaryMinutes){
    this.sedentaryMinutes = sedentaryMinutes;
  }
}
