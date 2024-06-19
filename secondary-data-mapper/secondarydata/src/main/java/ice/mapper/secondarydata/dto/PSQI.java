package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class PSQI implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept roomMateDisorientation;
  private Concept roomMateTwitching;
  private Concept roomMateLongPausesBetweenBreaths;
  private Concept roomMateLoudSnoring;
  private Concept roomMate;
  private Concept enthusiasm;
  private Concept troubleStayingAwake;
  private Concept sleepMedicine;
  private Concept sleepQuality;
  private Concept describeOtherAwake;
  private Concept troubleSleepingOther;
  private Concept troubleSleepingPain;
  private Concept troubleSleepingBadDreams;
  private Concept troubleSleepingTooHot;
  private Concept troubleSleepingTooCold;
  private Concept troubleSleepingCoughingOrSnoring;
  private Concept troubleSleepingCantBreathe;
  private Concept troubleSleepingBathroom;
  private Concept troubleSleepingWakeUpEarly;
  private Concept troubleSleepingCantFallAsleep;
  private Concept hoursSleeping;
  private Concept getUpTime;
  private Concept fallAsleep;
  private Concept bedTime;
  private Concept roomMateRestlessness;
  private Concept otherRestlessness;

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
  public Concept getRoomMateDisorientation() {
    return roomMateDisorientation;
  }
  public void setRoomMateDisorientation(Concept roomMateDisorientation) {
    this.roomMateDisorientation = roomMateDisorientation;
  }
  public Concept getRoomMateTwitching() {
    return roomMateTwitching;
  }
  public void setRoomMateTwitching(Concept roomMateTwitching) {
    this.roomMateTwitching = roomMateTwitching;
  }
  public Concept getRoomMateLongPausesBetweenBreaths() {
    return roomMateLongPausesBetweenBreaths;
  }
  public void setRoomMateLongPausesBetweenBreaths(Concept roomMateLongPausesBetweenBreaths) {
    this.roomMateLongPausesBetweenBreaths = roomMateLongPausesBetweenBreaths;
  }
  public Concept getRoomMateLoudSnoring() {
    return roomMateLoudSnoring;
  }
  public void setRoomMateLoudSnoring(Concept roomMateLoudSnoring) {
    this.roomMateLoudSnoring = roomMateLoudSnoring;
  }
  public Concept getRoomMate() {
    return roomMate;
  }
  public void setRoomMate(Concept roomMate) {
    this.roomMate = roomMate;
  }
  public Concept getEnthusiasm() {
    return enthusiasm;
  }
  public void setEnthusiasm(Concept enthusiasm) {
    this.enthusiasm = enthusiasm;
  }
  public Concept getTroubleStayingAwake() {
    return troubleStayingAwake;
  }
  public void setTroubleStayingAwake(Concept troubleStayingAwake) {
    this.troubleStayingAwake = troubleStayingAwake;
  }
  public Concept getSleepMedicine() {
    return sleepMedicine;
  }
  public void setSleepMedicine(Concept sleepMedicine) {
    this.sleepMedicine = sleepMedicine;
  }
  public Concept getSleepQuality() {
    return sleepQuality;
  }
  public void setSleepQuality(Concept sleepQuality) {
    this.sleepQuality = sleepQuality;
  }
  public Concept getDescribeOtherAwake() {
    return describeOtherAwake;
  }
  public void setDescribeOtherAwake(Concept describeOtherAwake) {
    this.describeOtherAwake = describeOtherAwake;
  }
  public Concept getTroubleSleepingOther() {
    return troubleSleepingOther;
  }
  public void setTroubleSleepingOther(Concept troubleSleepingOther) {
    this.troubleSleepingOther = troubleSleepingOther;
  }
  public Concept getTroubleSleepingPain() {
    return troubleSleepingPain;
  }
  public void setTroubleSleepingPain(Concept troubleSleepingPain) {
    this.troubleSleepingPain = troubleSleepingPain;
  }
  public Concept getTroubleSleepingBadDreams() {
    return troubleSleepingBadDreams;
  }
  public void setTroubleSleepingBadDreams(Concept troubleSleepingBadDreams) {
    this.troubleSleepingBadDreams = troubleSleepingBadDreams;
  }
  public Concept getTroubleSleepingTooHot() {
    return troubleSleepingTooHot;
  }
  public void setTroubleSleepingTooHot(Concept troubleSleepingTooHot) {
    this.troubleSleepingTooHot = troubleSleepingTooHot;
  }
  public Concept getTroubleSleepingTooCold() {
    return troubleSleepingTooCold;
  }
  public void setTroubleSleepingTooCold(Concept troubleSleepingTooCold) {
    this.troubleSleepingTooCold = troubleSleepingTooCold;
  }
  public Concept getTroubleSleepingCoughingOrSnoring() {
    return troubleSleepingCoughingOrSnoring;
  }
  public void setTroubleSleepingCoughingOrSnoring(Concept troubleSleepingCoughingOrSnoring) {
    this.troubleSleepingCoughingOrSnoring = troubleSleepingCoughingOrSnoring;
  }
  public Concept getTroubleSleepingCantBreathe() {
    return troubleSleepingCantBreathe;
  }
  public void setTroubleSleepingCantBreathe(Concept troubleSleepingCantBreathe) {
    this.troubleSleepingCantBreathe = troubleSleepingCantBreathe;
  }
  public Concept getTroubleSleepingBathroom() {
    return troubleSleepingBathroom;
  }
  public void setTroubleSleepingBathroom(Concept troubleSleepingBathroom) {
    this.troubleSleepingBathroom = troubleSleepingBathroom;
  }
  public Concept getTroubleSleepingWakeUpEarly() {
    return troubleSleepingWakeUpEarly;
  }
  public void setTroubleSleepingWakeUpEarly(Concept troubleSleepingWakeUpEarly) {
    this.troubleSleepingWakeUpEarly = troubleSleepingWakeUpEarly;
  }
  public Concept getTroubleSleepingCantFallAsleep() {
    return troubleSleepingCantFallAsleep;
  }
  public void setTroubleSleepingCantFallAsleep(Concept troubleSleepingCantFallAsleep) {
    this.troubleSleepingCantFallAsleep = troubleSleepingCantFallAsleep;
  }
  public Concept getHoursSleeping() {
    return hoursSleeping;
  }
  public void setHoursSleeping(Concept hoursSleeping) {
    this.hoursSleeping = hoursSleeping;
  }
  public Concept getGetUpTime() {
    return getUpTime;
  }
  public void setGetUpTime(Concept getUpTime) {
    this.getUpTime = getUpTime;
  }
  public Concept getFallAsleep() {
    return fallAsleep;
  }
  public void setFallAsleep(Concept fallAsleep) {
    this.fallAsleep = fallAsleep;
  }
  public Concept getBedTime() {
    return bedTime;
  }
  public void setBedTime(Concept bedTime) {
    this.bedTime = bedTime;
  }
  public Concept getRoomMateRestlessness() {
    return roomMateRestlessness;
  }
  public void setRoomMateRestlessness(Concept roomMateRestlessness) {
    this.roomMateRestlessness = roomMateRestlessness;
  }
  public Concept getOtherRestlessness() {
    return otherRestlessness;
  }
  public void setOtherRestlessness(Concept otherRestlessness) {
    this.otherRestlessness = otherRestlessness;
  }

  
}
