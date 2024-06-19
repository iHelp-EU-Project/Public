package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class PatientInformation implements Serializable {
  private String id;
  private String patientID;
  private Concept subjectIdentificationNumber;
  private Concept timeZone;
  private Concept trackerId;
  private Concept baselineDate;
  private Concept terminationDate;
  private Concept earlyTerminationDate;
  private Concept status;
  private Concept sex;
  private Concept birthDate;
  private Concept height;
  private Concept weight;
  private Concept disease;
  private Concept complexity;
  private Concept qtRobotId;
  private Concept gender;
  private Concept siteId;
  private Concept externalClinicalData;
  private Concept externalDemographicData;
  private Concept email;
  private Concept metadata;
  private Concept dischargedDateFromHospital;
  private Concept dischargedPeriodFromHealthentia;
  private Concept externalCode;
  private Concept comorbidities;
  private Concept notes;
  private Concept fiscalNumber;
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getPatientID() {
    return patientID;
  }
  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }
  public Concept getSubjectIdentificationNumber() {
    return subjectIdentificationNumber;
  }
  public void setSubjectIdentificationNumber(Concept subjectIdentificationNumber) {
    this.subjectIdentificationNumber = subjectIdentificationNumber;
  }
  public Concept getTimeZone() {
    return timeZone;
  }
  public void setTimeZone(Concept timeZone) {
    this.timeZone = timeZone;
  }
  public Concept getTrackerId() {
    return trackerId;
  }
  public void setTrackerId(Concept trackerId) {
    this.trackerId = trackerId;
  }
  public Concept getBaselineDate() {
    return baselineDate;
  }
  public void setBaselineDate(Concept baselineDate) {
    this.baselineDate = baselineDate;
  }
  public Concept getTerminationDate() {
    return terminationDate;
  }
  public void setTerminationDate(Concept terminationDate) {
    this.terminationDate = terminationDate;
  }
  public Concept getEarlyTerminationDate() {
    return earlyTerminationDate;
  }
  public void setEarlyTerminationDate(Concept earlyTerminationDate) {
    this.earlyTerminationDate = earlyTerminationDate;
  }
  public Concept getStatus() {
    return status;
  }
  public void setStatus(Concept status) {
    this.status = status;
  }
  public Concept getSex() {
    return sex;
  }
  public void setSex(Concept sex) {
    this.sex = sex;
  }
  public Concept getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(Concept birthDate) {
    this.birthDate = birthDate;
  }
  public Concept getHeight() {
    return height;
  }
  public void setHeight(Concept height) {
    this.height = height;
  }
  public Concept getWeight() {
    return weight;
  }
  public void setWeight(Concept weight) {
    this.weight = weight;
  }
  public Concept getDisease() {
    return disease;
  }
  public void setDisease(Concept disease) {
    this.disease = disease;
  }
  public Concept getComplexity() {
    return complexity;
  }
  public void setComplexity(Concept complexity) {
    this.complexity = complexity;
  }
  public Concept getQtRobotId() {
    return qtRobotId;
  }
  public void setQtRobotId(Concept qtRobotId) {
    this.qtRobotId = qtRobotId;
  }
  public Concept getGender() {
    return gender;
  }
  public void setGender(Concept gender) {
    this.gender = gender;
  }
  public Concept getSiteId() {
    return siteId;
  }
  public void setSiteId(Concept siteId) {
    this.siteId = siteId;
  }
  public Concept getExternalClinicalData() {
    return externalClinicalData;
  }
  public void setExternalClinicalData(Concept externalClinicalData) {
    this.externalClinicalData = externalClinicalData;
  }
  public Concept getExternalDemographicData() {
    return externalDemographicData;
  }
  public void setExternalDemographicData(Concept externalDemographicData) {
    this.externalDemographicData = externalDemographicData;
  }
  public Concept getEmail() {
    return email;
  }
  public void setEmail(Concept email) {
    this.email = email;
  }
  public Concept getMetadata() {
    return metadata;
  }
  public void setMetadata(Concept metadata) {
    this.metadata = metadata;
  }
  public Concept getDischargedDateFromHospital() {
    return dischargedDateFromHospital;
  }
  public void setDischargedDateFromHospital(Concept dischargedDateFromHospital) {
    this.dischargedDateFromHospital = dischargedDateFromHospital;
  }
  public Concept getDischargedPeriodFromHealthentia() {
    return dischargedPeriodFromHealthentia;
  }
  public void setDischargedPeriodFromHealthentia(Concept dischargedPeriodFromHealthentia) {
    this.dischargedPeriodFromHealthentia = dischargedPeriodFromHealthentia;
  }
  public Concept getExternalCode() {
    return externalCode;
  }
  public void setExternalCode(Concept externalCode) {
    this.externalCode = externalCode;
  }
  public Concept getComorbidities() {
    return comorbidities;
  }
  public void setComorbidities(Concept comorbidities) {
    this.comorbidities = comorbidities;
  }
  public Concept getNotes() {
    return notes;
  }
  public void setNotes(Concept notes) {
    this.notes = notes;
  }
  public Concept getFiscalNumber() {
    return fiscalNumber;
  }
  public void setFiscalNumber(Concept fiscalNumber) {
    this.fiscalNumber = fiscalNumber;
  }
  
  
}
