package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Nutrition implements Serializable {

  private String id;
  private PatientInformation patient;
  private Concept foodCategoryName;
  private Concept foodCategoryId;
  private Concept mealOfTheDay;
  
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
  public Concept getFoodCategoryName(){
    return foodCategoryName;
  }
  public void setFoodCategoryName(Concept foodCategoryName){
    this.foodCategoryName = foodCategoryName;
  }
  public Concept getFoodCategoryId(){
    return foodCategoryId;
  }
  public void setFoodCategoryId(Concept foodCategoryId){
    this.foodCategoryId = foodCategoryId;
  }
  public Concept getMealOfTheDay(){
    return mealOfTheDay;
  }
  public void setMealOfTheDay(Concept mealOfTheDay){
    this.mealOfTheDay = mealOfTheDay;
  }
}
