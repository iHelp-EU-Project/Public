package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class PercievedStress implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept unexpectedUpset;
  private Concept unableControlImportant;
  private Concept nervousAndStressed;
  private Concept confidentPersonalProblems;
  private Concept goingYourWay;
  private Concept couldntCope;
  private Concept controlIrritations;
  private Concept onTopOfThings;
  private Concept angeredOutsideControl;
  private Concept pilingDifficulties;
  private Concept successfullyDealWithIrritation;
  private Concept copingWithChanges;
  private Concept controlSpendingTime;
  private Concept thinkingToAcomplish;
  
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
  public Concept getUnexpectedUpset() {
    return unexpectedUpset;
  }
  public void setUnexpectedUpset(Concept unexpectedUpset) {
    this.unexpectedUpset = unexpectedUpset;
  }
  public Concept getUnableControlImportant() {
    return unableControlImportant;
  }
  public void setUnableControlImportant(Concept unableControlImportant) {
    this.unableControlImportant = unableControlImportant;
  }
  public Concept getNervousAndStressed() {
    return nervousAndStressed;
  }
  public void setNervousAndStressed(Concept nervousAndStressed) {
    this.nervousAndStressed = nervousAndStressed;
  }
  public Concept getConfidentPersonalProblems() {
    return confidentPersonalProblems;
  }
  public void setConfidentPersonalProblems(Concept confidentPersonalProblems) {
    this.confidentPersonalProblems = confidentPersonalProblems;
  }
  public Concept getGoingYourWay() {
    return goingYourWay;
  }
  public void setGoingYourWay(Concept goingYourWay) {
    this.goingYourWay = goingYourWay;
  }
  public Concept getCouldntCope() {
    return couldntCope;
  }
  public void setCouldntCope(Concept couldntCope) {
    this.couldntCope = couldntCope;
  }
  public Concept getControlIrritations() {
    return controlIrritations;
  }
  public void setControlIrritations(Concept controlIrritations) {
    this.controlIrritations = controlIrritations;
  }
  public Concept getOnTopOfThings() {
    return onTopOfThings;
  }
  public void setOnTopOfThings(Concept onTopOfThings) {
    this.onTopOfThings = onTopOfThings;
  }
  public Concept getAngeredOutsideControl() {
    return angeredOutsideControl;
  }
  public void setAngeredOutsideControl(Concept angeredOutsideControl) {
    this.angeredOutsideControl = angeredOutsideControl;
  }
  public Concept getPilingDifficulties() {
    return pilingDifficulties;
  }
  public void setPilingDifficulties(Concept pilingDifficulties) {
    this.pilingDifficulties = pilingDifficulties;
  }
  public Concept getSuccessfullyDealWithIrritation() {
    return successfullyDealWithIrritation;
  }
  public void setSuccessfullyDealWithIrritation(Concept successfullyDealWithIrritation) {
    this.successfullyDealWithIrritation = successfullyDealWithIrritation;
  }
  public Concept getCopingWithChanges() {
    return copingWithChanges;
  }
  public void setCopingWithChanges(Concept copingWithChanges) {
    this.copingWithChanges = copingWithChanges;
  }
  public Concept getControlSpendingTime() {
    return controlSpendingTime;
  }
  public void setControlSpendingTime(Concept controlSpendingTime) {
    this.controlSpendingTime = controlSpendingTime;
  }
  public Concept getThinkingToAcomplish() {
    return thinkingToAcomplish;
  }
  public void setThinkingToAcomplish(Concept thinkingToAcomplish) {
    this.thinkingToAcomplish = thinkingToAcomplish;
  }


  
}
