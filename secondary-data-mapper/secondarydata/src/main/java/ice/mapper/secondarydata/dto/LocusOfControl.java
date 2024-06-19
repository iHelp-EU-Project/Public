package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class LocusOfControl implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept ownDecisions;
  private Concept resistPersuasion;
  private Concept victim;
  private Concept pleaseOthers;
  private Concept weightAndFitness;
  private Concept ownFault;
  private Concept changeDirection;
  private Concept childhoodPersonality;
  private Concept breakBadHabit;
  private Concept scepticalAboutHoroscope;
  
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
  public Concept getOwnDecisions() {
    return ownDecisions;
  }
  public void setOwnDecisions(Concept ownDecisions) {
    this.ownDecisions = ownDecisions;
  }
  public Concept getResistPersuasion() {
    return resistPersuasion;
  }
  public void setResistPersuasion(Concept resistPersuasion) {
    this.resistPersuasion = resistPersuasion;
  }
  public Concept getVictim() {
    return victim;
  }
  public void setVictim(Concept victim) {
    this.victim = victim;
  }
  public Concept getPleaseOthers() {
    return pleaseOthers;
  }
  public void setPleaseOthers(Concept pleaseOthers) {
    this.pleaseOthers = pleaseOthers;
  }
  public Concept getWeightAndFitness() {
    return weightAndFitness;
  }
  public void setWeightAndFitness(Concept weightAndFitness) {
    this.weightAndFitness = weightAndFitness;
  }
  public Concept getOwnFault() {
    return ownFault;
  }
  public void setOwnFault(Concept ownFault) {
    this.ownFault = ownFault;
  }
  public Concept getChangeDirection() {
    return changeDirection;
  }
  public void setChangeDirection(Concept changeDirection) {
    this.changeDirection = changeDirection;
  }
  public Concept getChildhoodPersonality() {
    return childhoodPersonality;
  }
  public void setChildhoodPersonality(Concept childhoodPersonality) {
    this.childhoodPersonality = childhoodPersonality;
  }
  public Concept getBreakBadHabit() {
    return breakBadHabit;
  }
  public void setBreakBadHabit(Concept breakBadHabit) {
    this.breakBadHabit = breakBadHabit;
  }
  public Concept getScepticalAboutHoroscope() {
    return scepticalAboutHoroscope;
  }
  public void setScepticalAboutHoroscope(Concept scepticalAboutHoroscope) {
    this.scepticalAboutHoroscope = scepticalAboutHoroscope;
  }

  
}
