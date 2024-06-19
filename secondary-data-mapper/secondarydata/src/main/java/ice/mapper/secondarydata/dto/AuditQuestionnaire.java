package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class AuditQuestionnaire implements Serializable{

  private String id;
  private PatientInformation patient;
  private Concept audit1;
  private Concept audit2;
  private Concept audit3;
  private Concept audit4;
  private Concept audit5;
  private Concept audit6;
  private Concept audit7;
  private Concept audit8;
  private Concept audit9;
  private Concept audit10;

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
  public Concept getAudit1(){
    return audit1;
  }
  public void setAudit1(Concept audit1){
    this.audit1 = audit1;
  }
  public Concept getAudit2(){
    return audit2;
  }
  public void setAudit2(Concept audit2){
    this.audit2 = audit2;
  }
  public Concept getAudit3(){
    return audit3;
  }
  public void setAudit3(Concept audit3){
    this.audit3 = audit3;
  }
  public Concept getAudit4(){
    return audit4;
  }
  public void setAudit4(Concept audit4){
    this.audit4 = audit4;
  }
  public Concept getAudit5(){
    return audit5;
  }
  public void setAudit5(Concept audit5){
    this.audit5 = audit5;
  }
  public Concept getAudit6(){
    return audit6;
  }
  public void setAudit6(Concept audit6){
    this.audit6 = audit6;
  }
  public Concept getAudit7(){
    return audit7;
  }
  public void setAudit7(Concept audit7){
    this.audit7 = audit7;
  }
  public Concept getAudit8(){
    return audit8;
  }
  public void setAudit8(Concept audit8){
    this.audit8 = audit8;
  }
  public Concept getAudit9(){
    return audit9;
  }
  public void setAudit9(Concept audit9){
    this.audit9 = audit9;
  }
  public Concept getAudit10(){
    return audit10;
  }
  public void setAudit10(Concept audit10){
    this.audit10 = audit10;
  }
}
