package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class LiquidConsumption implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept water;
  private Concept coffee;
  private Concept tea;
  private Concept refreshment;
  private Concept alcohol;

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
  public Concept getWater(){
    return water;
  }
  public void setWater(Concept water){
    this.water = water;
  }
  public Concept getCoffee(){
    return coffee;
  }
  public void setCoffee(Concept coffee){
    this.coffee = coffee;
  }
  public Concept getTea(){
    return tea;
  }
  public void setTea(Concept tea){
    this.tea = tea;
  }
  public Concept getRefreshment(){
    return refreshment;
  }
  public void setRefreshment(Concept refreshment){
    this.refreshment = refreshment;
  }
  public Concept getAlcohol(){
    return alcohol;
  }
  public void setAlcohol(Concept alcohol){
    this.alcohol = alcohol;
  }
}
