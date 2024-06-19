package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class HealthHabits implements Serializable{
  private String id;
  private PatientInformation patient;
  private Concept fruitsOrVegetables;
  private Concept change;
  private Concept lowFatMilk;
  private Concept sodaOrPunch;
  private Concept fruitOrSportsDrink;
  private Concept water;
  private Concept juice100;
  private Concept bedroomDevice;
  private Concept fastFood;
  private Concept fastHeartRate;
  private Concept timeActive;
  private Concept breakfast;
  private Concept familyDinner;
  private Concept recreationalScreenTime;
  private Concept sleep;
  private Concept wholeMilk;
  private Concept planConfidence;
  
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
  public Concept getFruitsOrVegetables() {
    return fruitsOrVegetables;
  }
  public void setFruitsOrVegetables(Concept fruitsOrVegetables) {
    this.fruitsOrVegetables = fruitsOrVegetables;
  }
  public Concept getChange() {
    return change;
  }
  public void setChange(Concept change) {
    this.change = change;
  }
  public Concept getLowFatMilk() {
    return lowFatMilk;
  }
  public void setLowFatMilk(Concept lowFatMilk) {
    this.lowFatMilk = lowFatMilk;
  }
  public Concept getSodaOrPunch() {
    return sodaOrPunch;
  }
  public void setSodaOrPunch(Concept sodaOrPunch) {
    this.sodaOrPunch = sodaOrPunch;
  }
  public Concept getFruitOrSportsDrink() {
    return fruitOrSportsDrink;
  }
  public void setFruitOrSportsDrink(Concept fruitOrSportsDrink) {
    this.fruitOrSportsDrink = fruitOrSportsDrink;
  }
  public Concept getWater() {
    return water;
  }
  public void setWater(Concept water) {
    this.water = water;
  }
  public Concept getJuice100() {
    return juice100;
  }
  public void setJuice100(Concept juice100) {
    this.juice100 = juice100;
  }
  public Concept getBedroomDevice() {
    return bedroomDevice;
  }
  public void setBedroomDevice(Concept bedroomDevice) {
    this.bedroomDevice = bedroomDevice;
  }
  public Concept getFastFood() {
    return fastFood;
  }
  public void setFastFood(Concept fastFood) {
    this.fastFood = fastFood;
  }
  public Concept getFastHeartRate() {
    return fastHeartRate;
  }
  public void setFastHeartRate(Concept fastHeartRate) {
    this.fastHeartRate = fastHeartRate;
  }
  public Concept getTimeActive() {
    return timeActive;
  }
  public void setTimeActive(Concept timeActive) {
    this.timeActive = timeActive;
  }
  public Concept getBreakfast() {
    return breakfast;
  }
  public void setBreakfast(Concept breakfast) {
    this.breakfast = breakfast;
  }
  public Concept getFamilyDinner() {
    return familyDinner;
  }
  public void setFamilyDinner(Concept familyDinner) {
    this.familyDinner = familyDinner;
  }
  public Concept getRecreationalScreenTime() {
    return recreationalScreenTime;
  }
  public void setRecreationalScreenTime(Concept recreationalScreenTime) {
    this.recreationalScreenTime = recreationalScreenTime;
  }
  public Concept getSleep() {
    return sleep;
  }
  public void setSleep(Concept sleep) {
    this.sleep = sleep;
  }
  public Concept getWholeMilk() {
    return wholeMilk;
  }
  public void setWholeMilk(Concept wholeMilk) {
    this.wholeMilk = wholeMilk;
  }
  public Concept getPlanConfidence() {
    return planConfidence;
  }
  public void setPlanConfidence(Concept planConfidence) {
    this.planConfidence = planConfidence;
  }

  
}
