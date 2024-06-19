package ice.mapper.secondarydata.server;

import java.io.IOException;
import ice.mapper.secondarydata.utils.IhelpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.uhn.fhir.rest.annotation.Operation;

import ice.mapper.secondarydata.converter.FhirConverter;

import ice.mapper.secondarydata.dto.*;

@Component
public class FhirProvider {
  
  private FhirConverter converter;

  private String missingToken = "Unrecognized token, wrong body input!";
  
  private String missingPatientID = "No patientID provided!";
  
  public FhirProvider(FhirConverter converter) {
     
      this.converter = converter;
  }

  public FhirProvider() {}

  @Operation(name = "$all", manualRequest = true)
  public Bundle getAll(HttpServletRequest request, HttpServletResponse response) throws IhelpException{
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    All all;

    try {
      bytes = request.getInputStream().readAllBytes();
      all = objectMapper.readValue(bytes, All.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(all.getPatient() == null || all.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getAll(all);
  }

  @Operation(name = "$dailyActivity", manualRequest = true)
  public Bundle getDailyActivity(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
      
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    DailyActivity dailyActivity;

    try {
      bytes = request.getInputStream().readAllBytes();
      dailyActivity = objectMapper.readValue(bytes, DailyActivity.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(dailyActivity.getPatient() == null || dailyActivity.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getDailyActivity(dailyActivity,null,null);

  }

  @Operation(name = "$dailyHeart", manualRequest = true)
  public Bundle getDailyHeart(HttpServletRequest request, HttpServletResponse response) throws IhelpException {

    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    DailyHeart dailyHeart;

    try {
      bytes = request.getInputStream().readAllBytes();
      dailyHeart = objectMapper.readValue(bytes, DailyHeart.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(dailyHeart.getPatient() == null || dailyHeart.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getDailyHeart(dailyHeart,null,null);
  }

  @Operation(name= "$dailySleep", manualRequest = true)
  public Bundle getDailySleep(HttpServletRequest request, HttpServletResponse response) throws IhelpException {

    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    DailySleep dailySleep;

    try {
      bytes = request.getInputStream().readAllBytes();
      dailySleep = objectMapper.readValue(bytes, DailySleep.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(dailySleep.getPatient() == null || dailySleep.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getDailySleep(dailySleep,null,null);
  }

  @Operation(name = "$activitySession", manualRequest = true)
  public Bundle getActivitySession(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    ActivitySession activitySession;

    try {
      bytes = request.getInputStream().readAllBytes();
      activitySession = objectMapper.readValue(bytes, ActivitySession.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(activitySession.getPatient() == null || activitySession.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getActivitySession(activitySession,null,null);
  }

  @Operation(name = "$observedWeight", manualRequest = true)
  public Bundle getWeight(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Weight weight;

    try {
      bytes = request.getInputStream().readAllBytes();
      weight = objectMapper.readValue(bytes, Weight.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(weight.getPatient() == null || weight.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getWeight(weight,null,null);
  }

  @Operation(name = "$bloodPressure", manualRequest = true)
  public Bundle getBloodPressure(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    BloodPressure bloodPressure;

    try {
      bytes = request.getInputStream().readAllBytes();
      bloodPressure = objectMapper.readValue(bytes, BloodPressure.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(bloodPressure.getPatient() == null || bloodPressure.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getBloodPresure(bloodPressure,null,null);
  }

  @Operation(name = "$oxygenSaturation", manualRequest = true)
  public Bundle getOxygenSaturation(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    OxygenSaturation oxygenSaturation;

    try {
      bytes = request.getInputStream().readAllBytes();
      oxygenSaturation = objectMapper.readValue(bytes, OxygenSaturation.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(oxygenSaturation.getPatient() == null || oxygenSaturation.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getOxygenSaturation(oxygenSaturation,null,null);
  }

  @Operation(name = "$liquidConsumption", manualRequest = true)
  public Bundle getLiquidConsumption(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    LiquidConsumption liquidConsumption;

    try {
      bytes = request.getInputStream().readAllBytes();
      liquidConsumption = objectMapper.readValue(bytes, LiquidConsumption.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(liquidConsumption.getPatient() == null || liquidConsumption.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getLiquidConsumption(liquidConsumption,null,null);
  }

  @Operation(name = "$treatmentReminder", manualRequest = true)
  public Bundle getTreatmentReminder(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    TreatmentReminder treatmentReminder;

    try {
      bytes = request.getInputStream().readAllBytes();
      treatmentReminder = objectMapper.readValue(bytes, TreatmentReminder.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(treatmentReminder.getPatient() == null || treatmentReminder.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getTreatmentReminder(treatmentReminder,null,null);
  }

  @Operation(name = "$nutrition", manualRequest = true)
  public Bundle getNutrition(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Nutrition nutrition;

    try {
      bytes = request.getInputStream().readAllBytes();
      nutrition = objectMapper.readValue(bytes, Nutrition.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(nutrition.getPatient() == null || nutrition.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getNutrition(nutrition,null,null);
  }

  @Operation(name = "$bodyTemperature", manualRequest = true)
  public Bundle getBodyTemperature(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    BodyTemperature bodyTemperature;

    try {
      bytes = request.getInputStream().readAllBytes();
      bodyTemperature = objectMapper.readValue(bytes, BodyTemperature.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(bodyTemperature.getPatient() == null || bodyTemperature.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getBodyTemperature(bodyTemperature,null,null);
  }

  @Operation(name = "$cough", manualRequest = true)
  public Bundle getCough(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Cough cough;

    try {
      bytes = request.getInputStream().readAllBytes();
      cough = objectMapper.readValue(bytes, Cough.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(cough.getPatient() == null || cough.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getCough(cough,null,null);
  }

  @Operation(name = "$diarrhea", manualRequest = true)
  public Bundle getDiarrhea(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Diarrhea diarrhea;

    try {
      bytes = request.getInputStream().readAllBytes();
      diarrhea = objectMapper.readValue(bytes, Diarrhea.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(diarrhea.getPatient() == null || diarrhea.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getDiarrhea(diarrhea,null,null);
  }

  @Operation(name = "$height", manualRequest = true)
  public Bundle getHeight(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Height height;

    try {
      bytes = request.getInputStream().readAllBytes();
      height = objectMapper.readValue(bytes, Height.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(height.getPatient() == null || height.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getHeight(height,null,null);
  }

  @Operation(name = "$auditQuestionnaire", manualRequest = true)
  public QuestionnaireResponse getAuditQuestionnaire(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    AuditQuestionnaire auditQuestionnaire;

    try {
      bytes = request.getInputStream().readAllBytes();
      auditQuestionnaire = objectMapper.readValue(bytes, AuditQuestionnaire.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(auditQuestionnaire.getPatient() == null || auditQuestionnaire.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getAuditQuestionnaire(auditQuestionnaire,null,null);
  }

  @Operation(name = "$bodyAwareness", manualRequest = true)
  public QuestionnaireResponse getBodyAwareness(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    BodyAwareness bodyAwareness;

    try {
      bytes = request.getInputStream().readAllBytes();
      bodyAwareness = objectMapper.readValue(bytes, BodyAwareness.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(bodyAwareness.getPatient() == null || bodyAwareness.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getBodyAwareness(bodyAwareness,null,null);
  }

  @Operation(name = "$comorbidity", manualRequest = true)
  public QuestionnaireResponse getComorbidity(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    ComorbidityQuestionnaire comorbidityQuestionnaire;

    try {
      bytes = request.getInputStream().readAllBytes();
      comorbidityQuestionnaire = objectMapper.readValue(bytes, ComorbidityQuestionnaire.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(comorbidityQuestionnaire.getPatient() == null || comorbidityQuestionnaire.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getComorbidity(comorbidityQuestionnaire,null,null);
  }

  @Operation(name = "$edmontonAssessment", manualRequest = true)
  public QuestionnaireResponse getEdmontonAssessment(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    EdmontonAssessment edmontonAssessment;

    try {
      bytes = request.getInputStream().readAllBytes();
      edmontonAssessment = objectMapper.readValue(bytes, EdmontonAssessment.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(edmontonAssessment.getPatient() == null || edmontonAssessment.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getEdmontonAssessment(edmontonAssessment,null,null);
  }

  @Operation(name = "$pan26", manualRequest = true)
  public QuestionnaireResponse getPAN26(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    PAN26 pan26;

    try {
      bytes = request.getInputStream().readAllBytes();
      pan26 = objectMapper.readValue(bytes, PAN26.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(pan26.getPatient() == null || pan26.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getPAN26(pan26,null,null);
  }

  @Operation(name = "$qlq30", manualRequest = true)
  public QuestionnaireResponse getQLQ30(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    QLQ30 qlq30;

    try {
      bytes = request.getInputStream().readAllBytes();
      qlq30 = objectMapper.readValue(bytes, QLQ30.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(qlq30.getPatient() == null || qlq30.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getQLQ30(qlq30,null,null);
  }

  @Operation(name = "$fagerstrom", manualRequest = true)
  public QuestionnaireResponse getFagerstrom(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Fagerstrom fagerstrom;

    try {
      bytes = request.getInputStream().readAllBytes();
      fagerstrom = objectMapper.readValue(bytes, Fagerstrom.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(fagerstrom.getPatient() == null || fagerstrom.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getFagerstrom(fagerstrom,null,null);
  }

  @Operation(name = "$familyHistoryPC", manualRequest = true)
  public QuestionnaireResponse getFamilyHistoryPC(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    FamilyHistoryPC familyHistoryPC;

    try {
      bytes = request.getInputStream().readAllBytes();
      familyHistoryPC = objectMapper.readValue(bytes, FamilyHistoryPC.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(familyHistoryPC.getPatient() == null || familyHistoryPC.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getFamilyHistoryPC(familyHistoryPC,null,null);
  }

  @Operation(name = "$fatigue", manualRequest = true)
  public QuestionnaireResponse getFatigue(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Fatigue fatigue;

    try {
      bytes = request.getInputStream().readAllBytes();
      fatigue = objectMapper.readValue(bytes, Fatigue.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(fatigue.getPatient() == null || fatigue.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getFatigue(fatigue,null,null);
  }

  @Operation(name = "$generalHealth", manualRequest = true)
  public QuestionnaireResponse getGeneralHealth(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    GeneralHealth generalHealth;

    try {
      bytes = request.getInputStream().readAllBytes();
      generalHealth = objectMapper.readValue(bytes, GeneralHealth.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(generalHealth.getPatient() == null || generalHealth.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getGeneralHealth(generalHealth,null,null);
  }

  @Operation(name = "$healthBehaviour", manualRequest = true)
  public QuestionnaireResponse getHealthBehaviour(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    HealthBehaviour healthBehaviour;

    try {
      bytes = request.getInputStream().readAllBytes();
      healthBehaviour = objectMapper.readValue(bytes, HealthBehaviour.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(healthBehaviour.getPatient() == null || healthBehaviour.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getHealthBehaviour(healthBehaviour,null,null);
  }

  @Operation(name = "$healthHabits", manualRequest = true)
  public QuestionnaireResponse getHealthHabits(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    HealthHabits healthHabits;

    try {
      bytes = request.getInputStream().readAllBytes();
      healthHabits = objectMapper.readValue(bytes, HealthHabits.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(healthHabits.getPatient() == null || healthHabits.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getHealthHabits(healthHabits,null,null);
  }

  @Operation(name = "$healthLiteracyScale", manualRequest = true)
  public QuestionnaireResponse getHealthLiteracyScale(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    HealthLiteracyScale healthLiteracyScale;

    try {
      bytes = request.getInputStream().readAllBytes();
      healthLiteracyScale = objectMapper.readValue(bytes, HealthLiteracyScale.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(healthLiteracyScale.getPatient() == null || healthLiteracyScale.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getHealthLiteracyScale(healthLiteracyScale,null,null);
  }

  @Operation(name = "$locusOfControl", manualRequest = true)
  public QuestionnaireResponse getLocusOfControl(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    LocusOfControl locusOfControl;

    try {
      bytes = request.getInputStream().readAllBytes();
      locusOfControl = objectMapper.readValue(bytes, LocusOfControl.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(locusOfControl.getPatient() == null || locusOfControl.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getLocusOfControl(locusOfControl,null,null);
  }

  @Operation(name = "$moodAssessment", manualRequest = true)
  public QuestionnaireResponse getMoodAssessment(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    MoodAssessment moodAssessment;

    try {
      bytes = request.getInputStream().readAllBytes();
      moodAssessment = objectMapper.readValue(bytes, MoodAssessment.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(moodAssessment.getPatient() == null || moodAssessment.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getMoodAssessment(moodAssessment,null,null);
  }

  @Operation(name = "$pain", manualRequest = true)
  public QuestionnaireResponse getPain(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    Pain pain;

    try {
      bytes = request.getInputStream().readAllBytes();
      pain = objectMapper.readValue(bytes, Pain.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(pain.getPatient() == null || pain.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getPain(pain,null,null);
  }

  @Operation(name = "$percievedStress", manualRequest = true)
  public QuestionnaireResponse getPercievedStress(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    PercievedStress percievedStress;

    try {
      bytes = request.getInputStream().readAllBytes();
      percievedStress = objectMapper.readValue(bytes, PercievedStress.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(percievedStress.getPatient() == null || percievedStress.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getPercievedStress(percievedStress,null,null);
  }

  @Operation(name = "$psqi", manualRequest = true)
  public QuestionnaireResponse getPSQI(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    PSQI psqi;

    try {
      bytes = request.getInputStream().readAllBytes();
      psqi = objectMapper.readValue(bytes, PSQI.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(psqi.getPatient() == null || psqi.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getPSQI(psqi,null,null);
  }

  @Operation(name = "$qol", manualRequest = true)
  public QuestionnaireResponse getQOL(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    QOL qol;

    try {
      bytes = request.getInputStream().readAllBytes();
      qol = objectMapper.readValue(bytes, QOL.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(qol.getPatient() == null || qol.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getQOL(qol,null,null);
  }

  @Operation(name = "$selfEsteem", manualRequest = true)
  public QuestionnaireResponse getSelfEsteem(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    SelfEsteem selfEsteem;

    try {
      bytes = request.getInputStream().readAllBytes();
      selfEsteem = objectMapper.readValue(bytes, SelfEsteem.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(selfEsteem.getPatient() == null || selfEsteem.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getSelfEsteem(selfEsteem,null,null);
  }

  @Operation(name = "$mentalWellbeing", manualRequest = true)
  public QuestionnaireResponse getMentalWellbeing(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    MentalWellbeing mentalWellbeing;

    try {
      bytes = request.getInputStream().readAllBytes();
      mentalWellbeing = objectMapper.readValue(bytes, MentalWellbeing.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    if(mentalWellbeing.getPatient() == null || mentalWellbeing.getPatient().getPatientID() == null){
      throw new IhelpException(missingPatientID);
    }

    return converter.getMentalWellbeing(mentalWellbeing,null,null);
  }

  @Operation(name = "$patientInformation", manualRequest = true)
  public Patient getPatientInformation(HttpServletRequest request, HttpServletResponse response) throws IhelpException {
        
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] bytes;
    PatientInformation patientInformation;

    try {
      bytes = request.getInputStream().readAllBytes();
      patientInformation = objectMapper.readValue(bytes, PatientInformation.class);
    } catch (IOException e) { 
      throw new IhelpException(missingToken);
    }

    return converter.getPatientInformation(patientInformation,null);
  }
}


