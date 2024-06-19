package ice.mapper.secondarydata.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.QuestionnaireResponse;

import ice.mapper.secondarydata.utils.FhirLabels;
import ice.mapper.secondarydata.utils.FhirUtils;

import ice.mapper.secondarydata.dto.*;

@Component
@SuppressWarnings({"rawtypes","unused"})
public class FhirConverter {
  
  @Autowired
  private FhirLabels fhirLabels;

  @Autowired
  private FhirUtils fhirUtils;

  private Bundle addObservation(Bundle bundle, String patientID, Concept concept, String Vocabulary, String CategoryCode, String CategoryDisplay, String HelthentiaCode){
    Observation Obs = fhirUtils.createFhirObject(concept, null ,patientID, Vocabulary,
        CategoryCode,CategoryDisplay, HelthentiaCode);

    BundleEntryComponent bec = new BundleEntryComponent();
    bec.setResource(Obs);

    return bundle.addEntry(bec);
  }

  private Bundle addObservation(Bundle bundle, String patientID, Concept time, Concept concept, String Vocabulary, String CategoryCode, String CategoryDisplay, String HelthentiaCode){
    Observation Obs = fhirUtils.createFhirObject(concept, time ,patientID, Vocabulary,
        CategoryCode,CategoryDisplay, HelthentiaCode);

    BundleEntryComponent bec = new BundleEntryComponent();
    bec.setResource(Obs);

    return bundle.addEntry(bec);
  }

  public Bundle getAll(All all){
    Bundle bundle = new Bundle();
    BundleEntryComponent bec;

    if(all == null){
      return bundle;
    }

    if(all.getPatient() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getPatientInformation(all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getDailyActivity() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getDailyActivity(all.getDailyActivity(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getDailyHeart() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getDailyHeart(all.getDailyHeart(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getDailySleep() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getDailySleep(all.getDailySleep(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    } 
    if(all.getActivitySession() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getActivitySession(all.getActivitySession(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getWeight() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getWeight(all.getWeight(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getBloodPressure() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getBloodPresure(all.getBloodPressure(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getOxygenSaturation() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getOxygenSaturation(all.getOxygenSaturation(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getLiquidConsumption() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getLiquidConsumption(all.getLiquidConsumption(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getTreatmentReminder() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getTreatmentReminder(all.getTreatmentReminder(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getNutrition() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getNutrition(all.getNutrition(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getBodyTemperature() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getBodyTemperature(all.getBodyTemperature(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getCough() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getCough(all.getCough(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getDiarrhea() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getDiarrhea(all.getDiarrhea(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getHeight() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getHeight(all.getHeight(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getAuditQuestionnaire() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getAuditQuestionnaire(all.getAuditQuestionnaire(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getBodyAwareness() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getBodyAwareness(all.getBodyAwareness(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getComorbidityQuestionnaire() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getComorbidity(all.getComorbidityQuestionnaire(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getEdmontonAssessment() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getEdmontonAssessment(all.getEdmontonAssessment(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getPan26() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getPAN26(all.getPan26(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getQlq30() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getQLQ30(all.getQlq30(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getFagerstrom() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getFagerstrom(all.getFagerstrom(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getFamilyHistoryPC() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getFamilyHistoryPC(all.getFamilyHistoryPC(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getFatigue() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getFatigue(all.getFatigue(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getGeneralHealth() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getGeneralHealth(all.getGeneralHealth(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getHealthBehaviour() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getHealthBehaviour(all.getHealthBehaviour(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getHealthHabits() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getHealthHabits(all.getHealthHabits(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getHealthLiteracyScale() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getHealthLiteracyScale(all.getHealthLiteracyScale(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getLocusOfControl() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getLocusOfControl(all.getLocusOfControl(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getMoodAssessment() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getMoodAssessment(all.getMoodAssessment(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getPain() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getPain(all.getPain(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getPercievedStress() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getPercievedStress(all.getPercievedStress(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getPsqi() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getPSQI(all.getPsqi(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getQol() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getQOL(all.getQol(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getSelfEsteem() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getSelfEsteem(all.getSelfEsteem(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }
    if(all.getMentalWellbeing() != null){
      bec = new BundleEntryComponent();
      bec.setResource(getMentalWellbeing(all.getMentalWellbeing(),all.getPatient(),all.getId()));
      bundle.addEntry(bec);
    }

    return bundle;
  }

  public Bundle getDailyActivity(DailyActivity dailyActivity, PatientInformation patient, String helthentiaID) {
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (dailyActivity == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = dailyActivity.getPatient().getPatientID();
      ID = dailyActivity.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if (dailyActivity.getStepsWalked() != null) {

      addObservation(bundle, patientID, dailyActivity.getStepsWalked(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getDistanceTravelled() != null) {

      addObservation(bundle, patientID, dailyActivity.getDistanceTravelled(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getCaloriesBurned() != null) {

      addObservation(bundle, patientID, dailyActivity.getCaloriesBurned(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getFloorsClimbed() != null) {

      addObservation(bundle, patientID, dailyActivity.getFloorsClimbed(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getLightlyActiveMinutes() != null) {

      addObservation(bundle, patientID, dailyActivity.getLightlyActiveMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getModeratelyActiveMinutes() != null) {

      addObservation(bundle, patientID, dailyActivity.getModeratelyActiveMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getHighlyActiveMinutes() != null) {

      addObservation(bundle, patientID, dailyActivity.getHighlyActiveMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getSedentaryMinutes() != null) {

      addObservation(bundle, patientID, dailyActivity.getSedentaryMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    if (dailyActivity.getTotalActiveMinutes() != null) {

      addObservation(bundle, patientID, dailyActivity.getTotalActiveMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyActivityCode(),fhirLabels.getDailyActivityDisplay(),ID);
    }
    return bundle;
  }

  public Bundle getDailyHeart(DailyHeart dailyHeart, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (dailyHeart == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = dailyHeart.getPatient().getPatientID();
      ID = dailyHeart.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if (dailyHeart.getRestingHeartRate() != null) {

      addObservation(bundle, patientID, dailyHeart.getRestingHeartRate(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }
    if (dailyHeart.getMinHeartRate() != null) {

      addObservation(bundle, patientID, dailyHeart.getMinHeartRate(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }
    if (dailyHeart.getMaxHeartRate() != null) {

      addObservation(bundle, patientID, dailyHeart.getMaxHeartRate(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }
    if (dailyHeart.getOutOfRangeMinutes() != null) {

      addObservation(bundle, patientID, dailyHeart.getOutOfRangeMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }
    if (dailyHeart.getFatBurnMinutes() != null) {

      addObservation(bundle, patientID, dailyHeart.getFatBurnMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }
    if (dailyHeart.getCardioMinutes() != null) {

      addObservation(bundle, patientID, dailyHeart.getCardioMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }
    if (dailyHeart.getPeakMinutes() != null) {

      addObservation(bundle, patientID, dailyHeart.getPeakMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailyHeartCode(),fhirLabels.getDailyHeartDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getDailySleep(DailySleep dailySleep, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (dailySleep == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = dailySleep.getPatient().getPatientID();
      ID = dailySleep.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if (dailySleep.getSleepStart() != null) {

      addObservation(bundle, patientID, dailySleep.getSleepStart(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }
    if (dailySleep.getSleepEnd() != null) {

      addObservation(bundle, patientID, dailySleep.getSleepEnd(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }
    if (dailySleep.getRemMinutes() != null) {

      addObservation(bundle, patientID, dailySleep.getRemMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }
    if (dailySleep.getLightMinutes() != null) {

      addObservation(bundle, patientID, dailySleep.getLightMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }
    if (dailySleep.getDeepMinutes() != null) {

      addObservation(bundle, patientID, dailySleep.getDeepMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }
    if (dailySleep.getAwakeMinutes() != null) {

      addObservation(bundle, patientID, dailySleep.getAwakeMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }
    if (dailySleep.getTotalMinutes() != null) {

      addObservation(bundle, patientID, dailySleep.getTotalMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDailySleepCode(),fhirLabels.getDailySleepDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getActivitySession(ActivitySession activitySession, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (activitySession == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = activitySession.getPatient().getPatientID();
      ID = activitySession.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if (activitySession.getActivityType() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getActivityType(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getActiveDuration() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getActiveDuration(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getDuration() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getDuration(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getCalories() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getCalories(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getSpeed() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getSpeed(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getPace() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getPace(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getSteps() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getSteps(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getDistance() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getDistance(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getDistanceUnit() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getDistanceUnit(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getElevationGain() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getElevationGain(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getAverageHeartRate() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getAverageHeartRate(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getOutOfRangeMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getOutOfRangeMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getFatBurnMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getFatBurnMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getCardioMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getCardioMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getPeakMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getPeakMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getHeartRateZones() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getHeartRateZones(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getLightlyMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getLightlyMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getFairlyMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getFairlyMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getVeryMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getVeryMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }
    if (activitySession.getSedentaryMinutes() != null) {

      addObservation(bundle, patientID, activitySession.getStartTime(), activitySession.getSedentaryMinutes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getActivitySessionCode(),fhirLabels.getActivitySessionDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getWeight(Weight weight, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (weight == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = weight.getPatient().getPatientID();
      ID = weight.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(weight.getWeight() != null){

      addObservation(bundle, patientID, weight.getWeight(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getWeightCode(),fhirLabels.getWeightDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getBloodPresure(BloodPressure bloodPressure, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (bloodPressure == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = bloodPressure.getPatient().getPatientID();
      ID = bloodPressure.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(bloodPressure.getDiastolic() != null){

      addObservation(bundle, patientID, bloodPressure.getDiastolic(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getBloodPressureCode(),fhirLabels.getBloodPressureDisplay(), ID);
    }
    if(bloodPressure.getSystolic() != null){

      addObservation(bundle, patientID, bloodPressure.getSystolic(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getBloodPressureCode(),fhirLabels.getBloodPressureDisplay(), ID);
    }
    if(bloodPressure.getHeartRate() != null){

      addObservation(bundle, patientID, bloodPressure.getHeartRate(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getBloodPressureCode(),fhirLabels.getBloodPressureDisplay(), ID);
    }
    if(bloodPressure.getArythmia() != null){

      addObservation(bundle, patientID, bloodPressure.getArythmia(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getBloodPressureCode(),fhirLabels.getBloodPressureDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getOxygenSaturation(OxygenSaturation oxygenSaturation, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (oxygenSaturation == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = oxygenSaturation.getPatient().getPatientID();
      ID = oxygenSaturation.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if (oxygenSaturation.getOxygen() != null){

      addObservation(bundle, patientID, oxygenSaturation.getOxygen(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getOxygenSaturationCode(),fhirLabels.getOxygenSaturationDisplay(), ID);
    }
    if (oxygenSaturation.getPulse() != null){

      addObservation(bundle, patientID, oxygenSaturation.getPulse(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getOxygenSaturationCode(),fhirLabels.getOxygenSaturationDisplay(), ID);
    }
    if (oxygenSaturation.getPI() != null){

      addObservation(bundle, patientID, oxygenSaturation.getPI(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getOxygenSaturationCode(),fhirLabels.getOxygenSaturationDisplay(), ID);
    }
    if (oxygenSaturation.getNotes() != null){

      addObservation(bundle, patientID, oxygenSaturation.getNotes(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getOxygenSaturationCode(),fhirLabels.getOxygenSaturationDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getLiquidConsumption(LiquidConsumption liquidConsumption, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (liquidConsumption == null) {
      return bundle;
    }

      if( patient == null || helthentiaID == null){
        patientID = liquidConsumption.getPatient().getPatientID();
        ID = liquidConsumption.getId();
      } else {
        patientID = patient.getPatientID();
        ID = helthentiaID;
      }

    if (liquidConsumption.getWater() != null){

      addObservation(bundle, patientID, liquidConsumption.getWater(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getLiquidConsumptionCode(),fhirLabels.getLiquidConsumptionDisplay(), ID);
    }
    if (liquidConsumption.getCoffee() != null){

      addObservation(bundle, patientID, liquidConsumption.getCoffee(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getLiquidConsumptionCode(),fhirLabels.getLiquidConsumptionDisplay(), ID);
    }
    if (liquidConsumption.getTea() != null){

      addObservation(bundle, patientID, liquidConsumption.getTea(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getLiquidConsumptionCode(),fhirLabels.getLiquidConsumptionDisplay(), ID);
    }
    if (liquidConsumption.getRefreshment() != null){

      addObservation(bundle, patientID, liquidConsumption.getRefreshment(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getLiquidConsumptionCode(),fhirLabels.getLiquidConsumptionDisplay(), ID);
    }
    if (liquidConsumption.getAlcohol() != null){

      addObservation(bundle, patientID, liquidConsumption.getAlcohol(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getLiquidConsumptionCode(),fhirLabels.getLiquidConsumptionDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getTreatmentReminder(TreatmentReminder treatmentReminder, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (treatmentReminder == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = treatmentReminder.getPatient().getPatientID();
      ID = treatmentReminder.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }
    
    if (treatmentReminder.getDosesTaken() != null){

      addObservation(bundle, patientID, treatmentReminder.getDosesTaken(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getTreatmentReminderCode(),fhirLabels.getTreatmentReminderDisplay(), ID);
    }
    if (treatmentReminder.getDailyDoses() != null){

      addObservation(bundle, patientID, treatmentReminder.getDailyDoses(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getTreatmentReminderCode(),fhirLabels.getTreatmentReminderDisplay(), ID);
    }
    if (treatmentReminder.getUnit() != null){

      addObservation(bundle, patientID, treatmentReminder.getUnit(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getTreatmentReminderCode(),fhirLabels.getTreatmentReminderDisplay(), ID);
    }
    if (treatmentReminder.getDose() != null){

      addObservation(bundle, patientID, treatmentReminder.getDose(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getTreatmentReminderCode(),fhirLabels.getTreatmentReminderDisplay(), ID);
    }
    if (treatmentReminder.getTreatmentName() != null){

      addObservation(bundle, patientID, treatmentReminder.getTreatmentName(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getTreatmentReminderCode(),fhirLabels.getTreatmentReminderDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getNutrition(Nutrition nutrition, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (nutrition == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = nutrition.getPatient().getPatientID();
      ID = nutrition.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if (nutrition.getFoodCategoryName() != null){

      addObservation(bundle, patientID, nutrition.getFoodCategoryName(), fhirLabels.getSnomedVocabulary(),
        fhirLabels.getNutritionCode(),fhirLabels.getNutritionDisplay(), ID);
    }
    if (nutrition.getFoodCategoryId() != null){

      addObservation(bundle, patientID, nutrition.getFoodCategoryId(), fhirLabels.getSnomedVocabulary(),
        fhirLabels.getNutritionCode(),fhirLabels.getNutritionDisplay(), ID);
    }
    if (nutrition.getMealOfTheDay() != null){

      addObservation(bundle, patientID, nutrition.getMealOfTheDay(), fhirLabels.getSnomedVocabulary(),
        fhirLabels.getNutritionCode(),fhirLabels.getNutritionDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getBodyTemperature(BodyTemperature bodyTemperature, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (bodyTemperature == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = bodyTemperature.getPatient().getPatientID();
      ID = bodyTemperature.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(bodyTemperature.getBodyTemperature() != null){

      addObservation(bundle, patientID, bodyTemperature.getBodyTemperature(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getBodyTemperatureCode(),fhirLabels.getBodyTemperatureDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getCough(Cough cough, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (cough == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = cough.getPatient().getPatientID();
      ID = cough.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(cough.getCough() != null){

      addObservation(bundle, patientID, cough.getCough(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getCoughCode(),fhirLabels.getCoughDisplay(), ID);
    }
    if(cough.getCoughType() != null){

      addObservation(bundle, patientID, cough.getCoughType(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getCoughCode(),fhirLabels.getCoughDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getDiarrhea(Diarrhea diarrhea, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (diarrhea == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = diarrhea.getPatient().getPatientID();
      ID = diarrhea.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(diarrhea.getDiarrhea() != null){

      addObservation(bundle, patientID, diarrhea.getDiarrhea(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getDiarrheaCode(),fhirLabels.getDiarrheaDisplay(), ID);
    }

    return bundle;
  }

  public Bundle getHeight(Height height, PatientInformation patient, String helthentiaID){
    Bundle bundle = new Bundle();
    String patientID;
    String ID;

    if (height == null) {
      return bundle;
    }

    if( patient == null || helthentiaID == null){
      patientID = height.getPatient().getPatientID();
      ID = height.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(height.getHeight() != null){

      addObservation(bundle, patientID, height.getHeight(), fhirLabels.getCustomVocabulary(),
        fhirLabels.getHeightCode(),fhirLabels.getHeightDisplay(), ID);
    }

    return bundle;
  }

  public QuestionnaireResponse getAuditQuestionnaire(AuditQuestionnaire auditQuestionnaire, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (auditQuestionnaire == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = auditQuestionnaire.getPatient().getPatientID();
      ID = auditQuestionnaire.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(auditQuestionnaire.getAudit1() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit1());
    }
    if(auditQuestionnaire.getAudit2() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit2());
    }
    if(auditQuestionnaire.getAudit3() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit3());
    }
    if(auditQuestionnaire.getAudit4() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit4());
    }
    if(auditQuestionnaire.getAudit5() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit5());
    }
    if(auditQuestionnaire.getAudit6() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit6());
    }
    if(auditQuestionnaire.getAudit7() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit7());
    }
    if(auditQuestionnaire.getAudit8() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit8());
    }
    if(auditQuestionnaire.getAudit9() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit9());
    }
    if(auditQuestionnaire.getAudit10() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, auditQuestionnaire.getAudit10());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getAuditQuestionnaireCode(),fhirLabels.getAuditQuestionnaireDisplay());

    return questionnaireResponse;
  }

  public QuestionnaireResponse getBodyAwareness(BodyAwareness bodyAwareness, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (bodyAwareness == null){
      return questionnaireResponse;
    }

    if(patient == null || helthentiaID == null){
      patientID = bodyAwareness.getPatient().getPatientID();
      ID = bodyAwareness.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(bodyAwareness.getBAQ1() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ1());
    }
    if(bodyAwareness.getBAQ2() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ2());
    }
    if(bodyAwareness.getBAQ3() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ3());
    }
    if(bodyAwareness.getBAQ4() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ4());
    }
    if(bodyAwareness.getBAQ5() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ5());
    }
    if(bodyAwareness.getBAQ6() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ6());
    }
    if(bodyAwareness.getBAQ7() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ7());
    }
    if(bodyAwareness.getBAQ8() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ8());
    }
    if(bodyAwareness.getBAQ9() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ9());
    }
    if(bodyAwareness.getBAQ10() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ10());
    }
    if(bodyAwareness.getBAQ11() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ11());
    }
    if(bodyAwareness.getBAQ12() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ12());
    }
    if(bodyAwareness.getBAQ13() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ13());
    }
    if(bodyAwareness.getBAQ14() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ14());
    }
    if(bodyAwareness.getBAQ15() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ15());
    }
    if(bodyAwareness.getBAQ16() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ16());
    }
    if(bodyAwareness.getBAQ17() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ17());
    }
    if(bodyAwareness.getBAQ18() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, bodyAwareness.getBAQ18());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getBodyAwarenessCode(),fhirLabels.getBodyAwarenessDisplay());

    return questionnaireResponse;
  }

  public QuestionnaireResponse getComorbidity(ComorbidityQuestionnaire comorbidityQuestionnaire, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (comorbidityQuestionnaire == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = comorbidityQuestionnaire.getPatient().getPatientID();
      ID = comorbidityQuestionnaire.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(comorbidityQuestionnaire.getUlcerAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getUlcerAge());
    }
    if(comorbidityQuestionnaire.getOtherConditionName() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getOtherConditionName());
    }
    if(comorbidityQuestionnaire.getPeridontalAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getPeridontalAge());
    }
    if(comorbidityQuestionnaire.getAcutePancreatitisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getAcutePancreatitisAge());
    }
    if(comorbidityQuestionnaire.getCholedocholithiasisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getCholedocholithiasisAge());
    }
    if(comorbidityQuestionnaire.getAcuteGastroduodenitisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getAcuteGastroduodenitisAge());
    }
    if(comorbidityQuestionnaire.getChronicPancreatitisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getChronicPancreatitisAge());
    }
    if(comorbidityQuestionnaire.getPancreasCystsAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getPancreasCystsAge());
    }
    if(comorbidityQuestionnaire.getHypertenstionAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHypertenstionAge());
    }
    if(comorbidityQuestionnaire.getPancreasOperationsAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getPancreasOperationsAge());
    }
    if(comorbidityQuestionnaire.getType1DiabetesMellitusAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getType1DiabetesMellitusAge());
    }
    if(comorbidityQuestionnaire.getChronicGastroduodenitisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getChronicGastroduodenitisAge());
    }
    if(comorbidityQuestionnaire.getType2DiabetesMellitusAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getType2DiabetesMellitusAge());
    }
    if(comorbidityQuestionnaire.getGastricSurgeryAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getGastricSurgeryAge());
    }
    if(comorbidityQuestionnaire.getCholecystectomyAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getCholecystectomyAge());
    }
    if(comorbidityQuestionnaire.getLiverCirrhosisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getLiverCirrhosisAge());
    }
    if(comorbidityQuestionnaire.getHepatitisOtherAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHepatitisOtherAge());
    }
    if(comorbidityQuestionnaire.getHpiAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHpiAge());
    }
    if(comorbidityQuestionnaire.getHepatitisTypeBAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHepatitisTypeBAge());
    }
    if(comorbidityQuestionnaire.getHepatitisTypeCAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHepatitisTypeCAge());
    }
    if(comorbidityQuestionnaire.getCholecytitisAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getCholecytitisAge());
    }
    if(comorbidityQuestionnaire.getUlcer() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getUlcer());
    }
    if(comorbidityQuestionnaire.getSle() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getSle());
    }
    if(comorbidityQuestionnaire.getAcutePancreatitis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getAcutePancreatitis());
    }
    if(comorbidityQuestionnaire.getChronicPancreatitis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getChronicPancreatitis());
    }
    if(comorbidityQuestionnaire.getHypertension() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHypertension());
    }
    if(comorbidityQuestionnaire.getType1DiabetesMellitus() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getType1DiabetesMellitus());
    }
    if(comorbidityQuestionnaire.getType2DiabetesMellitus() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getType2DiabetesMellitus());
    }
    if(comorbidityQuestionnaire.getLiverCirrhosis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getLiverCirrhosis());
    }
    if(comorbidityQuestionnaire.getHepatitisTypeB() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHepatitisTypeB());
    }
    if(comorbidityQuestionnaire.getHepatitisTypeC() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHepatitisTypeC());
    }
    if(comorbidityQuestionnaire.getHepatitisOther() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHepatitisOther());
    }
    if(comorbidityQuestionnaire.getOtherCondition() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getOtherCondition());
    }
    if(comorbidityQuestionnaire.getCholecystitis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getCholecystitis());
    }
    if(comorbidityQuestionnaire.getCholecystectomy() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getCholecystectomy());
    }
    if(comorbidityQuestionnaire.getAcuteGastroduodenitis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getAcuteGastroduodenitis());
    }
    if(comorbidityQuestionnaire.getChronicGastroduodenitis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getChronicGastroduodenitis());
    }
    if(comorbidityQuestionnaire.getSleAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getSleAge());
    }
    if(comorbidityQuestionnaire.getHpi() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getHpi());
    }
    if(comorbidityQuestionnaire.getGastricSurgery() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getGastricSurgery());
    }
    if(comorbidityQuestionnaire.getPancreasOperations() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getPancreasOperations());
    }
    if(comorbidityQuestionnaire.getPancreasCysts() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getPancreasCysts());
    }
    if(comorbidityQuestionnaire.getPeridontalDisease() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getPeridontalDisease());
    }
    if(comorbidityQuestionnaire.getCholedocholithiasis() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getCholedocholithiasis());
    }
    if(comorbidityQuestionnaire.getOtherAge() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, comorbidityQuestionnaire.getOtherAge());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getComorbidityQuestionnaireCode(),fhirLabels.getComorbidityQuestionnaireDisplay());

    return questionnaireResponse;
  }

  public QuestionnaireResponse getEdmontonAssessment(EdmontonAssessment edmontonAssessment, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (edmontonAssessment == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = edmontonAssessment.getPatient().getPatientID();
      ID = edmontonAssessment.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(edmontonAssessment.getEdmontonSymptomAssessment1() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment1());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment2() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment2());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment3() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment3());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment4() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment4());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment5() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment5());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment6() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment6());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment7() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment7());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment8() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment8());
    }
    if(edmontonAssessment.getEdmontonSymptomAssessment9() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getEdmontonSymptomAssessment9());
    }
    if(edmontonAssessment.getHurtLocation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getHurtLocation());
    }
    if(edmontonAssessment.getOtherSympotom() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getOtherSympotom());
    }
    if(edmontonAssessment.getAdditionalSymptom() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getAdditionalSymptom());
    }
    if(edmontonAssessment.getAdditionalSymptomDetails() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, edmontonAssessment.getAdditionalSymptomDetails());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getEdmontonAssessmentCode(),fhirLabels.getEdmontonAssessmentDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getPAN26(PAN26 pan26, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (pan26 == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = pan26.getPatient().getPatientID();
      ID = pan26.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(pan26.getTreatmentInformation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getTreatmentInformation());
    }
    if(pan26.getAdequateSupport() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getAdequateSupport());
    }
    if(pan26.getLimitedPlanningActivity() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getLimitedPlanningActivity());
    }
    if(pan26.getWorryAboutHealth() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getWorryAboutHealth());
    }
    if(pan26.getSideEffects() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getSideEffects());
    }
    if(pan26.getDissatisfiedBody() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getDissatisfiedBody());
    }
    if(pan26.getLessAttractive() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getLessAttractive());
    }
    if(pan26.getQuickBowels() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getQuickBowels());
    }
    if(pan26.getFrequentBowels() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getFrequentBowels());
    }
    if(pan26.getYellowSkin() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getYellowSkin());
    }
    if(pan26.getItching() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getItching());
    }
    if(pan26.getLessInterestSex() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getLessInterestSex());
    }
    if(pan26.getDryMouth() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getDryMouth());
    }
    if(pan26.getWorryLowWeight() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getWorryLowWeight());
    }
    if(pan26.getFlatulence() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getFlatulence());
    }
    if(pan26.getIndegestion() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getIndegestion());
    }
    if(pan26.getDifferentTaste() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getDifferentTaste());
    }
    if(pan26.getRestrictedAmmountFood() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getRestrictedAmmountFood());
    }
    if(pan26.getRestrictedTypeFood() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getRestrictedTypeFood());
    }
    if(pan26.getUncomfortablePositions() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getUncomfortablePositions());
    }
    if(pan26.getNightPain() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getNightPain());
    }
    if(pan26.getBackPain() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getBackPain());
    }
    if(pan26.getBloatedAbdomen() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getBloatedAbdomen());
    }
    if(pan26.getAbdominalDiscomfort() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getAbdominalDiscomfort());
    }
    if(pan26.getWeakArmsLegs() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getWeakArmsLegs());
    }
    if(pan26.getSexualEnjoyment() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pan26.getSexualEnjoyment());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getPAN26Code(),fhirLabels.getPAN26Display());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getQLQ30(QLQ30 qlq30, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (qlq30 == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = qlq30.getPatient().getPatientID();
      ID = qlq30.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(qlq30.getFinancialDifficulty() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getFinancialDifficulty());
    }
    if(qlq30.getInterferedSocialActivities() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getInterferedSocialActivities());
    }
    if(qlq30.getInterferedFamilyLife() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getInterferedFamilyLife());
    }
    if(qlq30.getDifficultyRemembering() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getDifficultyRemembering());
    }
    if(qlq30.getDepressed() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getDepressed());
    }
    if(qlq30.getIrritable() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getIrritable());
    }
    if(qlq30.getWorry() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getWorry());
    }
    if(qlq30.getTense() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getTense());
    }
    if(qlq30.getDifficultyConcentrating() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getDifficultyConcentrating());
    }
    if(qlq30.getPainInterfereDailyActivities() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getPainInterfereDailyActivities());
    }
    if(qlq30.getTired() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getTired());
    }
    if(qlq30.getDiarrhea() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getDiarrhea());
    }
    if(qlq30.getConstipated() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getConstipated());
    }
    if(qlq30.getRateHealthWeek() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getRateHealthWeek());
    }
    if(qlq30.getVomited() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getVomited());
    }
    if(qlq30.getLackedAppetite() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getLackedAppetite());
    }
    if(qlq30.getFeltWeak() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getFeltWeak());
    }
    if(qlq30.getTroubleSleeping() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getTroubleSleeping());
    }
    if(qlq30.getNeedToRest() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getNeedToRest());
    }
    if(qlq30.getPain() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getPain());
    }
    if(qlq30.getShortOfBreath() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getShortOfBreath());
    }
    if(qlq30.getLimitedPursuingHobbies() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getLimitedPursuingHobbies());
    }
    if(qlq30.getLimitedDoingWork() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getLimitedDoingWork());
    }
    if(qlq30.getNeedHelp() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getNeedHelp());
    }
    if(qlq30.getStayInBedOrChair() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getStayInBedOrChair());
    }
    if(qlq30.getTroubleShortWalk() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getTroubleShortWalk());
    }
    if(qlq30.getTroubleLongWalk() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getTroubleLongWalk());
    }
    if(qlq30.getStrenuousActivities() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getStrenuousActivities());
    }
    if(qlq30.getNauseated() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getNauseated());
    }
    if(qlq30.getRateQOL() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qlq30.getRateQOL());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getQLQ30Code(),fhirLabels.getQLQ30Display());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getFagerstrom(Fagerstrom fagerstrom, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (fagerstrom == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = fagerstrom.getPatient().getPatientID();
      ID = fagerstrom.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(fagerstrom.getFirstCigarette() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fagerstrom.getFirstCigarette());
    }
    if(fagerstrom.getForbiddenPlaces() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fagerstrom.getForbiddenPlaces());
    }
    if(fagerstrom.getCigaretteGiveUp() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fagerstrom.getCigaretteGiveUp());
    }
    if(fagerstrom.getDailyCigarettes() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fagerstrom.getDailyCigarettes());
    }
    if(fagerstrom.getFirstHoursSmoking() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fagerstrom.getFirstHoursSmoking());
    }
    if(fagerstrom.getIllSmoking() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fagerstrom.getIllSmoking());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getFagerstromCode(),fhirLabels.getFagerstromDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getFamilyHistoryPC(FamilyHistoryPC familyHistoryPC, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (familyHistoryPC == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = familyHistoryPC.getPatient().getPatientID();
      ID = familyHistoryPC.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }
    

    if(familyHistoryPC.getPerson2Age() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson2Age());
    }
    if(familyHistoryPC.getPerson5Relation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson5Relation());
    }
    if(familyHistoryPC.getPerson5Question() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson5Question());
    }
    if(familyHistoryPC.getPerson4Age() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson4Age());
    }
    if(familyHistoryPC.getPerson4Type() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson4Type());
    }
    if(familyHistoryPC.getPerson4Relation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson4Relation());
    }
    if(familyHistoryPC.getPerson1Age() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson1Age());
    }
    if(familyHistoryPC.getPerson1Type() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson1Type());
    }
    if(familyHistoryPC.getPerson2Relation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson2Relation());
    }
    if(familyHistoryPC.getPerson4Question() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson4Question());
    }
    if(familyHistoryPC.getPerson3Age() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson3Age());
    }
    if(familyHistoryPC.getPerson3Type() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson3Type());
    }
    if(familyHistoryPC.getPerson3Relation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson3Relation());
    }
    if(familyHistoryPC.getPerson2Question() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson2Question());
    }
    if(familyHistoryPC.getPerson5Type() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson5Type());
    }
    if(familyHistoryPC.getPerson3Question() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson3Question());
    }
    if(familyHistoryPC.getPerson2Type() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson2Type());
    }
    if(familyHistoryPC.getPerson1Relation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson1Relation());
    }
    if(familyHistoryPC.getPerson5Age() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, familyHistoryPC.getPerson5Age());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getFamilyHistoryPCCode(),fhirLabels.getFamilyHistoryPCDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getFatigue(Fatigue fatigue, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (fatigue == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = fatigue.getPatient().getPatientID();
      ID = fatigue.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(fatigue.getFatigue() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, fatigue.getFatigue());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getFatigueCode(),fhirLabels.getFatigueDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getGeneralHealth(GeneralHealth generalHealth, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (generalHealth == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = generalHealth.getPatient().getPatientID();
      ID = generalHealth.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(generalHealth.getDescription() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, generalHealth.getDescription());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getGeneralHealthCode(),fhirLabels.getGeneralHealthDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getHealthBehaviour(HealthBehaviour healthBehaviour, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (healthBehaviour == null){
      return questionnaireResponse;
    }

    patientID = healthBehaviour.getPatient().getPatientID();

    if(healthBehaviour.getSmoking() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthBehaviour.getSmoking());
    }
    if(healthBehaviour.getNutritionalConsultation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthBehaviour.getNutritionalConsultation());
    }
    if(healthBehaviour.getPhysicalActivity() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthBehaviour.getPhysicalActivity());
    }
    if(healthBehaviour.getAlcoholConsumption() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthBehaviour.getAlcoholConsumption());
    }
    if(healthBehaviour.getMammographyOrUltrasound() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthBehaviour.getMammographyOrUltrasound());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getHealthBehaviourCode(),fhirLabels.getHealthBehaviourDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getHealthHabits(HealthHabits healthHabits, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (healthHabits == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = healthHabits.getPatient().getPatientID();
      ID = healthHabits.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(healthHabits.getFruitsOrVegetables() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getFruitsOrVegetables());
    }
    if(healthHabits.getChange() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getChange());
    }
    if(healthHabits.getLowFatMilk() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getLowFatMilk());
    }
    if(healthHabits.getSodaOrPunch() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getSodaOrPunch());
    }
    if(healthHabits.getFruitOrSportsDrink() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getFruitOrSportsDrink());
    }
    if(healthHabits.getWater() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getWater());
    }
    if(healthHabits.getJuice100() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getJuice100());
    }
    if(healthHabits.getBedroomDevice() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getBedroomDevice());
    }
    if(healthHabits.getFastFood() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getFastFood());
    }
    if(healthHabits.getFastHeartRate() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getFastHeartRate());
    }
    if(healthHabits.getTimeActive() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getTimeActive());
    }
    if(healthHabits.getBreakfast() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getBreakfast());
    }
    if(healthHabits.getFamilyDinner() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getFamilyDinner());
    }
    if(healthHabits.getRecreationalScreenTime() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getRecreationalScreenTime());
    }
    if(healthHabits.getSleep() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getSleep());
    }
    if(healthHabits.getWholeMilk() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getWholeMilk());
    }
    if(healthHabits.getPlanConfidence() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthHabits.getPlanConfidence());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getHealthHabitsCode(),fhirLabels.getHealthHabitsDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getHealthLiteracyScale(HealthLiteracyScale healthLiteracyScale, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (healthLiteracyScale == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = healthLiteracyScale.getPatient().getPatientID();
      ID = healthLiteracyScale.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(healthLiteracyScale.getMediaUnderstanding() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getMediaUnderstanding());
    }
    if(healthLiteracyScale.getFamilyAdviceUnderstanding() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getFamilyAdviceUnderstanding());
    }
    if(healthLiteracyScale.getFindInformationMentalHealth() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getFindInformationMentalHealth());
    }
    if(healthLiteracyScale.getProtectIllnessMediaInformation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getProtectIllnessMediaInformation());
    }
    if(healthLiteracyScale.getMediaInformationRealiable() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getMediaInformationRealiable());
    }
    if(healthLiteracyScale.getUnderstandHealthScreenings() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getUnderstandHealthScreenings());
    }
    if(healthLiteracyScale.getUnderstandHealthWarnings() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getUnderstandHealthWarnings());
    }
    if(healthLiteracyScale.getFollowDoctorInstructions() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getFollowDoctorInstructions());
    }
    if(healthLiteracyScale.getMakeDecisionsDoctorInformation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getMakeDecisionsDoctorInformation());
    }
    if(healthLiteracyScale.getDoctorSecondOpinion() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getDoctorSecondOpinion());
    }
    if(healthLiteracyScale.getDoctorInstructionPerscription() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getDoctorInstructionPerscription());
    }
    if(healthLiteracyScale.getUnderstandDoctor() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getUnderstandDoctor());
    }
    if(healthLiteracyScale.getWhereProfessionalHelp() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getWhereProfessionalHelp());
    }
    if(healthLiteracyScale.getTreatmentsAndIllnesses() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getTreatmentsAndIllnesses());
    }
    if(healthLiteracyScale.getMentalWellbeing() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getMentalWellbeing());
    }
    if(healthLiteracyScale.getEverydayBehaviour() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, healthLiteracyScale.getEverydayBehaviour());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getHealthLiteracyScaleCode(),fhirLabels.getHealthLiteracyScaleDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getLocusOfControl(LocusOfControl locusOfControl, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (locusOfControl == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = locusOfControl.getPatient().getPatientID();
      ID = locusOfControl.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(locusOfControl.getOwnDecisions() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getOwnDecisions());
    }
    if(locusOfControl.getResistPersuasion() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getResistPersuasion());
    }
    if(locusOfControl.getVictim() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getVictim());
    }
    if(locusOfControl.getPleaseOthers() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getPleaseOthers());
    }
    if(locusOfControl.getWeightAndFitness() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getWeightAndFitness());
    }
    if(locusOfControl.getOwnFault() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getOwnFault());
    }
    if(locusOfControl.getChangeDirection() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getChangeDirection());
    }
    if(locusOfControl.getChildhoodPersonality() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getChildhoodPersonality());
    }
    if(locusOfControl.getBreakBadHabit() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getBreakBadHabit());
    }
    if(locusOfControl.getScepticalAboutHoroscope() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, locusOfControl.getScepticalAboutHoroscope());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getLocusOfControlCode(),fhirLabels.getLocusOfControlDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getMoodAssessment(MoodAssessment moodAssessment, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (moodAssessment == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = moodAssessment.getPatient().getPatientID();
      ID = moodAssessment.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(moodAssessment.getMoodAssessment() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, moodAssessment.getMoodAssessment());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getMoodAssessmentCode(),fhirLabels.getMoodAssessmentDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getPain(Pain pain, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (pain == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = pain.getPatient().getPatientID();
      ID = pain.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(pain.getExperience() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pain.getExperience());
    }
    if(pain.getLocation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, pain.getLocation());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getPainCode(),fhirLabels.getPainDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getPercievedStress(PercievedStress percievedStress, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (percievedStress == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = percievedStress.getPatient().getPatientID();
      ID = percievedStress.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(percievedStress.getUnexpectedUpset() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getUnexpectedUpset());
    }
    if(percievedStress.getUnableControlImportant() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getUnableControlImportant());
    }
    if(percievedStress.getNervousAndStressed() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getNervousAndStressed());
    }
    if(percievedStress.getConfidentPersonalProblems() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getConfidentPersonalProblems());
    }
    if(percievedStress.getGoingYourWay() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getGoingYourWay());
    }
    if(percievedStress.getCouldntCope() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getCouldntCope());
    }
    if(percievedStress.getControlIrritations() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getControlIrritations());
    }
    if(percievedStress.getOnTopOfThings() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getOnTopOfThings());
    }
    if(percievedStress.getAngeredOutsideControl() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getAngeredOutsideControl());
    }
    if(percievedStress.getPilingDifficulties() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getPilingDifficulties());
    }
    if(percievedStress.getSuccessfullyDealWithIrritation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getSuccessfullyDealWithIrritation());
    }
    if(percievedStress.getCopingWithChanges() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getCopingWithChanges());
    }
    if(percievedStress.getControlSpendingTime() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getControlSpendingTime());
    }
    if(percievedStress.getThinkingToAcomplish() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, percievedStress.getThinkingToAcomplish());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getPercievedStressCode(),fhirLabels.getPercievedStressDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getPSQI(PSQI psqi, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (psqi == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = psqi.getPatient().getPatientID();
      ID = psqi.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(psqi.getRoomMateDisorientation() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getRoomMateDisorientation());
    }
    if(psqi.getRoomMateTwitching() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getRoomMateTwitching());
    }
    if(psqi.getRoomMateLongPausesBetweenBreaths() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getRoomMateLongPausesBetweenBreaths());
    }
    if(psqi.getRoomMateLoudSnoring() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getRoomMateLoudSnoring());
    }
    if(psqi.getRoomMate() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getRoomMate());
    }
    if(psqi.getEnthusiasm() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getEnthusiasm());
    }
    if(psqi.getTroubleStayingAwake() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleStayingAwake());
    }
    if(psqi.getSleepMedicine() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getSleepMedicine());
    }
    if(psqi.getSleepQuality() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getSleepQuality());
    }
    if(psqi.getDescribeOtherAwake() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getDescribeOtherAwake());
    }
    if(psqi.getTroubleSleepingOther() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingOther());
    }
    if(psqi.getTroubleSleepingPain() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingPain());
    }
    if(psqi.getTroubleSleepingBadDreams() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingBadDreams());
    }
    if(psqi.getTroubleSleepingTooHot() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingTooHot());
    }
    if(psqi.getTroubleSleepingTooCold() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingTooCold());
    }
    if(psqi.getTroubleSleepingCoughingOrSnoring() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingCoughingOrSnoring());
    }
    if(psqi.getTroubleSleepingCantBreathe() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingCantBreathe());
    }
    if(psqi.getTroubleSleepingBathroom() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingBathroom());
    }
    if(psqi.getTroubleSleepingWakeUpEarly() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingWakeUpEarly());
    }
    if(psqi.getTroubleSleepingCantFallAsleep() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getTroubleSleepingCantFallAsleep());
    }
    if(psqi.getHoursSleeping() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getHoursSleeping());
    }
    if(psqi.getGetUpTime() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getGetUpTime());
    }
    if(psqi.getFallAsleep() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getFallAsleep());
    }
    if(psqi.getBedTime() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getBedTime());
    }
    if(psqi.getRoomMateRestlessness() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getRoomMateRestlessness());
    }
    if(psqi.getOtherRestlessness() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, psqi.getOtherRestlessness());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getPSQICode(),fhirLabels.getPSQIDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getQOL(QOL qol, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (qol == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = qol.getPatient().getPatientID();
      ID = qol.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(qol.getAnxietyDepression() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qol.getAnxietyDepression());
    }
    if(qol.getUsualDiscomfort() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qol.getUsualDiscomfort());
    }
    if(qol.getHealthScale() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qol.getHealthScale());
    }
    if(qol.getSelfCare() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qol.getSelfCare());
    }
    if(qol.getUsualActivity() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qol.getUsualActivity());
    }
    if(qol.getMobility() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, qol.getMobility());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getQOLCode(),fhirLabels.getQOLDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getSelfEsteem(SelfEsteem selfEsteem, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (selfEsteem == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = selfEsteem.getPatient().getPatientID();
      ID = selfEsteem.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(selfEsteem.getFeelFailure() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getFeelFailure());
    }
    if(selfEsteem.getDoThingsLikeOthers() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getDoThingsLikeOthers());
    }
    if(selfEsteem.getThinkNoGood() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getThinkNoGood());
    }
    if(selfEsteem.getMoreSelfRespect() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getMoreSelfRespect());
    }
    if(selfEsteem.getPersonOfWorth() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getPersonOfWorth());
    }
    if(selfEsteem.getUselessTimes() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getUselessTimes());
    }
    if(selfEsteem.getGoodQualities() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getGoodQualities());
    }
    if(selfEsteem.getNotMuchProud() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getNotMuchProud());
    }
    if(selfEsteem.getSatisfiedSelf() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getSatisfiedSelf());
    }
    if(selfEsteem.getPositiveAttitude() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, selfEsteem.getPositiveAttitude());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getSelfEsteemCode(),fhirLabels.getSelfEsteemDisplay());

    return questionnaireResponse;

  }

  public QuestionnaireResponse getMentalWellbeing(MentalWellbeing mentalWellbeing, PatientInformation patient, String helthentiaID){
    QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
    String patientID;
    String ID;

    if (mentalWellbeing == null){
      return questionnaireResponse;
    }

    if( patient == null || helthentiaID == null){
      patientID = mentalWellbeing.getPatient().getPatientID();
      ID = mentalWellbeing.getId();
    } else {
      patientID = patient.getPatientID();
      ID = helthentiaID;
    }

    if(mentalWellbeing.getOptimistic() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getOptimistic());
    }
    if(mentalWellbeing.getUseful() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getUseful());
    }
    if(mentalWellbeing.getRelaxed() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getRelaxed());
    }
    if(mentalWellbeing.getInterestedOthers() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getInterestedOthers());
    }
    if(mentalWellbeing.getEnergySpare() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getEnergySpare());
    }
    if(mentalWellbeing.getDealingProblems() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getDealingProblems());
    }
    if(mentalWellbeing.getThinkingClearly() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getThinkingClearly());
    }
    if(mentalWellbeing.getFeelingGoodSelf() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getFeelingGoodSelf());
    }
    if(mentalWellbeing.getCloseOthers() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getCloseOthers());
    }
    if(mentalWellbeing.getConfident() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getConfident());
    }
    if(mentalWellbeing.getMakeUpMind() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getMakeUpMind());
    }
    if(mentalWellbeing.getLoved() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getLoved());
    }
    if(mentalWellbeing.getNewThings() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getNewThings());
    }
    if(mentalWellbeing.getCheerful() != null){
      questionnaireResponse = fhirUtils.addItem(questionnaireResponse, mentalWellbeing.getCheerful());
    }

    fhirUtils.setPatientAndCategory(questionnaireResponse,patientID,fhirLabels.getCustomVocabulary(),fhirLabels.getMentalWellbeingCode(),fhirLabels.getMentalWellbeingDisplay());

    return questionnaireResponse;

  }

  public Patient getPatientInformation(PatientInformation patientInformation, String helthentiaID){
    Patient patient = new Patient();
    String ID;

    List<Identifier> identifiers = new ArrayList<Identifier>();
    Identifier identifier;

    if(patientInformation == null){
      return patient;
    }

    if(patientInformation.getPatientID() != null){
      identifier = new Identifier();
      if(patientInformation.getPatientID() != null){
        identifier.setValue(patientInformation.getPatientID());
      }
      identifiers.add(identifier);
    }
    if(patientInformation.getSubjectIdentificationNumber() != null){
      identifier = new Identifier();
      identifier.setType(fhirUtils.createCodeableConcept(patientInformation.getSubjectIdentificationNumber().getVocabulary(), patientInformation.getSubjectIdentificationNumber().getId(), patientInformation.getSubjectIdentificationNumber().getName()));
      if(patientInformation.getSubjectIdentificationNumber().getValue() != null){
        identifier.setValue(patientInformation.getSubjectIdentificationNumber().getValue().toString());
      }
      identifiers.add(identifier);
    }
    if(patientInformation.getTimeZone() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getTimeZone());
    }
    if(patientInformation.getTrackerId() != null){
      identifier = new Identifier();
      identifier.setType(fhirUtils.createCodeableConcept(patientInformation.getTrackerId().getVocabulary(), patientInformation.getTrackerId().getId(), patientInformation.getTrackerId().getName()));
      if(patientInformation.getTrackerId().getValue() != null){
        identifier.setValue(patientInformation.getTrackerId().getValue().toString());
      }
      identifiers.add(identifier);
    }
    if(patientInformation.getBaselineDate() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getBaselineDate());
    }
    if(patientInformation.getTerminationDate() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getTerminationDate());
    }
    if(patientInformation.getEarlyTerminationDate() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getEarlyTerminationDate());
    }
    if(patientInformation.getStatus() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getStatus());
    }
    if(patientInformation.getSex() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getSex());
    }
    if(patientInformation.getBirthDate() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getBirthDate());
    }
    if(patientInformation.getHeight() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getHeight());
    }
    if(patientInformation.getWeight() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getWeight());
    }
    if(patientInformation.getDisease() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getDisease());
    }
    if(patientInformation.getComplexity() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getComplexity());
    }
    if(patientInformation.getQtRobotId() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getQtRobotId());
    }
    if(patientInformation.getGender() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getGender());
    }
    if(patientInformation.getSiteId() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getSiteId());
    }
    if(patientInformation.getExternalClinicalData() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getExternalClinicalData());
    }
    if(patientInformation.getExternalDemographicData() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getExternalDemographicData());
    }
    if(patientInformation.getEmail() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getEmail());
    }
    if(patientInformation.getMetadata() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getMetadata());
    }
    if(patientInformation.getDischargedDateFromHospital() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getDischargedDateFromHospital());
    }
    if(patientInformation.getDischargedPeriodFromHealthentia() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getDischargedPeriodFromHealthentia());
    }
    if(patientInformation.getExternalCode() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getExternalCode());
    }
    if(patientInformation.getComorbidities() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getComorbidities());
    }
    if(patientInformation.getNotes() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getNotes());
    }
    if(patientInformation.getFiscalNumber() != null){
      patient = fhirUtils.addExtension(patient, patientInformation.getFiscalNumber());
    }

    patient.setIdentifier(identifiers);

    return patient;
  }

}
