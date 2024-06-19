package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class SelfEsteem implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept feelFailure;
  private Concept doThingsLikeOthers;
  private Concept thinkNoGood;
  private Concept moreSelfRespect;
  private Concept personOfWorth;
  private Concept uselessTimes;
  private Concept goodQualities;
  private Concept notMuchProud;
  private Concept satisfiedSelf;
  private Concept positiveAttitude;
  
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
  public Concept getFeelFailure() {
    return feelFailure;
  }
  public void setFeelFailure(Concept feelFailure) {
    this.feelFailure = feelFailure;
  }
  public Concept getDoThingsLikeOthers() {
    return doThingsLikeOthers;
  }
  public void setDoThingsLikeOthers(Concept doThingsLikeOthers) {
    this.doThingsLikeOthers = doThingsLikeOthers;
  }
  public Concept getThinkNoGood() {
    return thinkNoGood;
  }
  public void setThinkNoGood(Concept thinkNoGood) {
    this.thinkNoGood = thinkNoGood;
  }
  public Concept getMoreSelfRespect() {
    return moreSelfRespect;
  }
  public void setMoreSelfRespect(Concept moreSelfRespect) {
    this.moreSelfRespect = moreSelfRespect;
  }
  public Concept getPersonOfWorth() {
    return personOfWorth;
  }
  public void setPersonOfWorth(Concept personOfWorth) {
    this.personOfWorth = personOfWorth;
  }
  public Concept getUselessTimes() {
    return uselessTimes;
  }
  public void setUselessTimes(Concept uselessTimes) {
    this.uselessTimes = uselessTimes;
  }
  public Concept getGoodQualities() {
    return goodQualities;
  }
  public void setGoodQualities(Concept goodQualities) {
    this.goodQualities = goodQualities;
  }
  public Concept getNotMuchProud() {
    return notMuchProud;
  }
  public void setNotMuchProud(Concept notMuchProud) {
    this.notMuchProud = notMuchProud;
  }
  public Concept getSatisfiedSelf() {
    return satisfiedSelf;
  }
  public void setSatisfiedSelf(Concept satisfiedSelf) {
    this.satisfiedSelf = satisfiedSelf;
  }
  public Concept getPositiveAttitude() {
    return positiveAttitude;
  }
  public void setPositiveAttitude(Concept positiveAttitude) {
    this.positiveAttitude = positiveAttitude;
  }

  
}
