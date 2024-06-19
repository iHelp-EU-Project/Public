package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class MoodAssessment implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept moodAssessment;

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
  public Concept getMoodAssessment() {
    return moodAssessment;
  }
  public void setMoodAssessment(Concept moodAssessment) {
    this.moodAssessment = moodAssessment;
  }
}
