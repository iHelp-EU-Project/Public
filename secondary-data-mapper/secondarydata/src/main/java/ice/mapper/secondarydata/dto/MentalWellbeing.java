package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class MentalWellbeing implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept optimistic;
  private Concept useful;
  private Concept relaxed;
  private Concept interestedOthers;
  private Concept energySpare;
  private Concept dealingProblems;
  private Concept thinkingClearly;
  private Concept feelingGoodSelf;
  private Concept closeOthers;
  private Concept confident;
  private Concept makeUpMind;
  private Concept loved;
  private Concept newThings;
  private Concept cheerful;
  
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
  public Concept getOptimistic() {
    return optimistic;
  }
  public void setOptimistic(Concept optimistic) {
    this.optimistic = optimistic;
  }
  public Concept getUseful() {
    return useful;
  }
  public void setUseful(Concept useful) {
    this.useful = useful;
  }
  public Concept getRelaxed() {
    return relaxed;
  }
  public void setRelaxed(Concept relaxed) {
    this.relaxed = relaxed;
  }
  public Concept getInterestedOthers() {
    return interestedOthers;
  }
  public void setInterestedOthers(Concept interestedOthers) {
    this.interestedOthers = interestedOthers;
  }
  public Concept getEnergySpare() {
    return energySpare;
  }
  public void setEnergySpare(Concept energySpare) {
    this.energySpare = energySpare;
  }
  public Concept getDealingProblems() {
    return dealingProblems;
  }
  public void setDealingProblems(Concept dealingProblems) {
    this.dealingProblems = dealingProblems;
  }
  public Concept getThinkingClearly() {
    return thinkingClearly;
  }
  public void setThinkingClearly(Concept thinkingClearly) {
    this.thinkingClearly = thinkingClearly;
  }
  public Concept getFeelingGoodSelf() {
    return feelingGoodSelf;
  }
  public void setFeelingGoodSelf(Concept feelingGoodSelf) {
    this.feelingGoodSelf = feelingGoodSelf;
  }
  public Concept getCloseOthers() {
    return closeOthers;
  }
  public void setCloseOthers(Concept closeOthers) {
    this.closeOthers = closeOthers;
  }
  public Concept getConfident() {
    return confident;
  }
  public void setConfident(Concept confident) {
    this.confident = confident;
  }
  public Concept getMakeUpMind() {
    return makeUpMind;
  }
  public void setMakeUpMind(Concept makeUpMind) {
    this.makeUpMind = makeUpMind;
  }
  public Concept getLoved() {
    return loved;
  }
  public void setLoved(Concept loved) {
    this.loved = loved;
  }
  public Concept getNewThings() {
    return newThings;
  }
  public void setNewThings(Concept newThings) {
    this.newThings = newThings;
  }
  public Concept getCheerful() {
    return cheerful;
  }
  public void setCheerful(Concept cheerful) {
    this.cheerful = cheerful;
  }

  
}
