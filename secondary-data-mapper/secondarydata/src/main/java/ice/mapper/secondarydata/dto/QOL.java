package ice.mapper.secondarydata.dto;

@SuppressWarnings("rawtypes")
public class QOL {
  private String id;
  private PatientInformation patient;
  private Concept anxietyDepression;
  private Concept usualDiscomfort;
  private Concept healthScale;
  private Concept selfCare;
  private Concept usualActivity;
  private Concept mobility;

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
  public Concept getAnxietyDepression() {
    return anxietyDepression;
  }
  public void setAnxietyDepression(Concept anxietyDepression) {
    this.anxietyDepression = anxietyDepression;
  }
  public Concept getUsualDiscomfort() {
    return usualDiscomfort;
  }
  public void setUsualDiscomfort(Concept usualDiscomfort) {
    this.usualDiscomfort = usualDiscomfort;
  }
  public Concept getHealthScale() {
    return healthScale;
  }
  public void setHealthScale(Concept healthScale) {
    this.healthScale = healthScale;
  }
  public Concept getSelfCare() {
    return selfCare;
  }
  public void setSelfCare(Concept selfCare) {
    this.selfCare = selfCare;
  }
  public Concept getUsualActivity() {
    return usualActivity;
  }
  public void setUsualActivity(Concept usualActivity) {
    this.usualActivity = usualActivity;
  }
  public Concept getMobility() {
    return mobility;
  }
  public void setMobility(Concept mobility) {
    this.mobility = mobility;
  }

  
}
