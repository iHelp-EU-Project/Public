package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class DailyHeart implements Serializable {
  
  private String id;
  private PatientInformation patient;
  private Concept restingHeartRate;
  private Concept minHeartRate;
  private Concept maxHeartRate;
  private Concept outOfRangeMinutes;
  private Concept fatBurnMinutes;
  private Concept cardioMinutes;
  private Concept peakMinutes;
  
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
  public Concept getRestingHeartRate() {
    return restingHeartRate;
  }
  public void setRestingHeartRate(Concept restingHeartRate){
    this.restingHeartRate = restingHeartRate;
  }
  public Concept getMinHeartRate(){
    return minHeartRate;
  }
  public void setMinHeartRate(Concept minHeartRate){
    this.minHeartRate = minHeartRate;
  }
  public Concept getMaxHeartRate(){
    return maxHeartRate;
  }
  public void setMaxHeartRate(Concept maxHeartRate){
    this.maxHeartRate = maxHeartRate;
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
}
