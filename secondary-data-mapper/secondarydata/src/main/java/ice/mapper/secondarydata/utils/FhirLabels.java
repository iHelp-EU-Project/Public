package ice.mapper.secondarydata.utils;

import org.springframework.stereotype.Component;

@Component
public class FhirLabels {

  //Vocabularies
  private String snomedVocabulary = "SNOMED";
  private String loincVocabulary = "LOINC";
  private String customVocabulary = "https://fhir.ihelp-project.eu/CodeSystem/codes";

  public String getLoincVocabulary() {
    return loincVocabulary;
  }
  public String getSnomedVocabulary() {
    return snomedVocabulary;
  }
  public String getCustomVocabulary() {
    return customVocabulary;
  }

  //Fhir Types
  private  String observationLabel = "Observation";
  private  String questionnaireResponseLabel = "QuestionnaireResponse";

  public String getObservationLabel(){
    return observationLabel;
  }
  public String getQuestionnaireResponseLabel() {
    return questionnaireResponseLabel;
  }

  //Patient
  private String patientDisplay = "Patient";

  public String getPatientDisplay(){
    return patientDisplay;
  }

  //dailyActivity
  private String dailyActivityCode = "dailyActivity";
  private String dailyActivityDisplay = "Daily Activity";

  public String getDailyActivityCode() {
    return dailyActivityCode;
  }
  public String getDailyActivityDisplay() {
    return dailyActivityDisplay;
  }

  //dailyHeart
  private String dailyHeartCode = "dailyHeart";
  private String dailyHeartDisplay = "Daily Heart";

  public String getDailyHeartCode(){
    return dailyHeartCode;
  }
  public String getDailyHeartDisplay(){
    return dailyHeartDisplay;
  }

  //dailySleep
  private String dailySleepCode = "dailySleep";
  private String dailySleepDisplay = "Daily Sleep";

  public String getDailySleepCode(){
    return dailySleepCode;
  }
  public String getDailySleepDisplay(){
    return dailySleepDisplay;
  }

  //activitySessions
  private String activitySessionCode = "activitySession";
  private String activitySessionDisplay = "Activity Session";

  public String getActivitySessionCode(){
    return activitySessionCode;
  }
  public String getActivitySessionDisplay(){
    return activitySessionDisplay;
  }

  //Weight
  private String weightCode = "observedWeight";
  private String weightDisplay = "Weight";

  public String getWeightCode(){
    return weightCode;
  }
  public String getWeightDisplay(){
    return weightDisplay;
  }

  //Blood Pressure
  private String bloodPressureCode = "observedBloodPressure";
  private String bloodPressureDisplay = "Blood Pressure";

  public String getBloodPressureCode(){
    return bloodPressureCode;
  }
  public String getBloodPressureDisplay(){
    return bloodPressureDisplay;
  }

  //Oxygen Saturation
  private String oxygenSaturationCode = "observedOxygenSaturation";
  private String oxygenSaturationDisplay = "Oxygen Saturation";

  public String getOxygenSaturationCode(){
    return oxygenSaturationCode;
  }
  public String getOxygenSaturationDisplay(){
    return oxygenSaturationDisplay;

  }

  //Liquid Consumption
  private String liquidConsumptionCode = "dailyLiquidConsumption";
  private String liquidConsumptionDisplay = "Liquid Consumption";

  public String getLiquidConsumptionCode(){
    return liquidConsumptionCode;
  }
  public String getLiquidConsumptionDisplay(){
    return liquidConsumptionDisplay;
  }

  //Treatment Reminder
  private String treatmentReminderCode = "treatmentReminder";
  private String treatmentReminderDisplay = "Treatment Reminder";

  public String getTreatmentReminderCode(){
    return treatmentReminderCode;
  }
  public String getTreatmentReminderDisplay(){
    return treatmentReminderDisplay;
  }

  //Nutrition
  private String nutritionCode = "384759009";
  private String nutritionDisplay = "Nutrition";

  public String getNutritionCode(){
    return nutritionCode;
  }
  public String getNutritionDisplay(){
    return nutritionDisplay;
  }

  //Body Temperature
  private String bodyTemperatureCode = "BodyTemperature";
  private String bodyTemperatureDisplay = "Body Temperature";

  public String getBodyTemperatureCode(){
    return bodyTemperatureCode;
  }
  public String getBodyTemperatureDisplay(){
    return bodyTemperatureDisplay;
  }

  //Cough
  private String coughCode = "CoughCategory";
  private String coughDisplay = "Cough";

  public String getCoughCode(){
    return coughCode;
  }
  public String getCoughDisplay(){
    return coughDisplay;
  }

  //Diarrhea
  private String diarrheaCode = "DiarrheaCategory";
  private String diarrheaDisplay = "Diarrhea";

  public String getDiarrheaCode(){
    return diarrheaCode;
  }
  public String getDiarrheaDisplay(){
    return diarrheaDisplay;
  }

  //Height
  private String heightCode = "HeightCategory";
  private String heightDisplay = "Height";

  public String getHeightCode(){
    return heightCode;
  }
  public String getHeightDisplay(){
    return heightDisplay;
  }

  //Audit Questionnaire
  private String auditQuestionnaireCode = "AUDITQuestionnaire";
  private String auditQuestionnaireDisplay = "AUDIT Questionnaire";

  public String getAuditQuestionnaireCode(){
    return auditQuestionnaireCode;
  }
  public String getAuditQuestionnaireDisplay(){
    return auditQuestionnaireDisplay;
  }

  //Body Awareness Questionnaire
  private String bodyAwarenessCode = "BodyAwarenessQuestionnaire";
  private String bodyAwarenessDisplay = "Body Awareness Questionnaire";

  public String getBodyAwarenessCode(){
    return bodyAwarenessCode;
  }
  public String getBodyAwarenessDisplay(){
    return bodyAwarenessDisplay;
  }

  //Comorbidity Questionnaire
  private String comorbidityQuestionnaireCode = "ComorbidityQuestionnaire";
  private String comorbidityQuestionnaireDisplay = "Comorbidity Questionnaire";

  public String getComorbidityQuestionnaireCode(){
    return comorbidityQuestionnaireCode;
  }
  public String getComorbidityQuestionnaireDisplay(){
    return comorbidityQuestionnaireDisplay;
  }

  //Edmonton Symptom Assessment
  private String edmontonAssessmentCode = "EdmontonSymptomAssessmentSystem";
  private String edmontonAssessmentDisplay = "Edmonton Symptom Assessment System";

  public String getEdmontonAssessmentCode(){
    return edmontonAssessmentCode;
  }
  public String getEdmontonAssessmentDisplay(){
    return edmontonAssessmentDisplay;
  }

  //QLQ-PAN26
  private String pan26Code = "QLQ-PAN26";
  private String pan26Display = "QLQ-PAN26";

  public String getPAN26Code(){
    return pan26Code;
  }
  public String getPAN26Display(){
    return pan26Display;
  }

  //QLQ-30
  private String qlq30Code = "QLQ-30";
  private String qlq30Display = "QLQ-30";

  public String getQLQ30Code(){
    return qlq30Code;
  }
  public String getQLQ30Display(){
    return qlq30Display;
  }

  //Fagerstrom
  private String fagerstromCode = "FagerstromTestForNicotineDependence";
  private String fagerstromDisplay = "Fagerstrom Test For Nicotine Dependence";

  public String getFagerstromCode(){
    return fagerstromCode;
  }
  public String getFagerstromDisplay(){
    return fagerstromDisplay;
  }

  //Family History Pancreatic Cancer
  private String familyHistoryPCCode = "FamilyHistoryPancreaticCancer";
  private String familyHistoryPCDisplay = "Family History Pancreatic Cancer";

  public String getFamilyHistoryPCCode(){
    return familyHistoryPCCode;
  }
  public String getFamilyHistoryPCDisplay(){
    return familyHistoryPCDisplay;
  }

  //Fatigue
  private String fatigueCode = "FatigueCategory";
  private String fatigueDisaply = "Fatigue Category";

  public String getFatigueCode(){
    return fatigueCode;
  }
  public String getFatigueDisplay(){
    return fatigueDisaply;
  }

  //General Health
  private String generalHealthCode = "GeneralHealth";
  private String generalHealthDisplay = "General Health";

  public String getGeneralHealthCode(){
    return generalHealthCode;
  }
  public String getGeneralHealthDisplay(){
    return generalHealthDisplay;
  }

  //Health Behaviour
  private String healthBehaviourCode = "HealthBehaviourStageOfChangeQuestionnaire";
  private String healthBehaviourDisplay = "Health Behaviour Stage Of Change Questionnaire";

  public String getHealthBehaviourCode(){
    return healthBehaviourCode;
  }
  public String getHealthBehaviourDisplay(){
    return healthBehaviourDisplay;
  }

  //Health Habits
  private String healthHabitsCode = "HealthHabitsQuestionnaire";
  private String healthHabitsDisplay = "Health Habits Questionnaire";

  public String getHealthHabitsCode(){
    return healthHabitsCode;
  }
  public String getHealthHabitsDisplay(){
    return healthHabitsDisplay;
  }

  //Health Literacy Scale
  private String healthLiteracyScaleCode = "HealthLiteracyScale";
  private String healthLiteracyScaleDisplay = "Health Literacy Scale";

  public String getHealthLiteracyScaleCode(){
    return healthLiteracyScaleCode;
  }
  public String getHealthLiteracyScaleDisplay(){
    return healthLiteracyScaleDisplay;
  }

  //Locus Of Control
  private String locusOfControlCode = "LocusOfControlQuestionnaire";
  private String locusOfControlDisplay = "Locus Of Control Questionnaire";

  public String getLocusOfControlCode(){
    return locusOfControlCode;
  }
  public String getLocusOfControlDisplay(){
    return locusOfControlDisplay;
  }

  //Mood Assessment Category
  private String moodAssessmentCode = "MoodAssessmentCategory";
  private String moodAssessmentDisplay = "Mood Assessment Category";

  public String getMoodAssessmentCode(){
    return moodAssessmentCode;
  }
  public String getMoodAssessmentDisplay(){
    return moodAssessmentDisplay;
  }

  //Pain Category
  private String painCode = "PainCategory";
  private String painDisplay = "Pain Category";

  public String getPainCode(){
    return painCode;
  }
  public String getPainDisplay(){
    return painDisplay;
  }

  //Percieved Stress
  private String percievedStressCode = "PerceivedStressScale";
  private String percievedStressDisplay = "Perceived Stress Scale";

  public String getPercievedStressCode(){
    return percievedStressCode;
  }
  public String getPercievedStressDisplay(){
    return percievedStressDisplay;
  }

  //Pittsburgh Sleep Quality Index
  private String psqiCode = "PittsburghSleepQualityIndex";
  private String psqiDisplay = "Pittsburgh Sleep Quality Index";

  public String getPSQICode(){
    return psqiCode;
  }
  public String getPSQIDisplay(){
    return psqiDisplay;
  }

  //QualityOfLifeQuestionnaire
  private String qolCode = "QualityOfLifeQuestionnaire";
  private String qolDisplay = "Quality Of Life Questionnaire";

  public String getQOLCode(){
    return qolCode;
  }
  public String getQOLDisplay(){
    return qolDisplay;
  }

  //Rosenberg Self Esteem Scale
  private String selfEsteemCode = "RosenbergSelfEsteemScale";
  private String selfEsteemDisplay = "Rosenberg Self Esteem Scale";

  public String getSelfEsteemCode(){
    return selfEsteemCode;
  }
  public String getSelfEsteemDisplay(){
    return selfEsteemDisplay;
  }

  //Warwick Edinburgh Mental Wellbeing Scale
  private String mentalWellbeingCode = "WarwickEdinburghMentalWellbeingScale";
  private String mentalWellbeingDisplay = "Warwick Edinburgh Mental Wellbeing Scale";

  public String getMentalWellbeingCode(){
    return mentalWellbeingCode;
  }
  public String getMentalWellbeingDisplay(){
    return mentalWellbeingDisplay;
  }

}
