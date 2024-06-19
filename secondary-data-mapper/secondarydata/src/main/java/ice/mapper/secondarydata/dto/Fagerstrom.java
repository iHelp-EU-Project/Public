package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Fagerstrom implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept firstCigarette;
  private Concept forbiddenPlaces;
  private Concept cigaretteGiveUp;
  private Concept dailyCigarettes;
  private Concept firstHoursSmoking;
  private Concept illSmoking;
  
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
  public Concept getFirstCigarette() {
    return firstCigarette;
  }
  public void setFirstCigarette(Concept firstCigarette) {
    this.firstCigarette = firstCigarette;
  }
  public Concept getForbiddenPlaces() {
    return forbiddenPlaces;
  }
  public void setForbiddenPlaces(Concept forbiddenPlaces) {
    this.forbiddenPlaces = forbiddenPlaces;
  }
  public Concept getCigaretteGiveUp() {
    return cigaretteGiveUp;
  }
  public void setCigaretteGiveUp(Concept cigaretteGiveUp) {
    this.cigaretteGiveUp = cigaretteGiveUp;
  }
  public Concept getDailyCigarettes() {
    return dailyCigarettes;
  }
  public void setDailyCigarettes(Concept dailyCigarettes) {
    this.dailyCigarettes = dailyCigarettes;
  }
  public Concept getFirstHoursSmoking() {
    return firstHoursSmoking;
  }
  public void setFirstHoursSmoking(Concept firstHoursSmoking) {
    this.firstHoursSmoking = firstHoursSmoking;
  }
  public Concept getIllSmoking() {
    return illSmoking;
  }
  public void setIllSmoking(Concept illSmoking) {
    this.illSmoking = illSmoking;
  }

  
  
}
