package ice.mapper.secondarydata.dto;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class QLQ30 implements Serializable {
  private String id;
  private PatientInformation patient;
  private Concept financialDifficulty;
  private Concept interferedSocialActivities;
  private Concept interferedFamilyLife;
  private Concept difficultyRemembering;
  private Concept depressed;
  private Concept irritable;
  private Concept worry;
  private Concept tense;
  private Concept difficultyConcentrating;
  private Concept painInterfereDailyActivities;
  private Concept tired;
  private Concept diarrhea;
  private Concept constipated;
  private Concept rateHealthWeek;
  private Concept vomited;
  private Concept lackedAppetite;
  private Concept feltWeak;
  private Concept troubleSleeping;
  private Concept needToRest;
  private Concept pain;
  private Concept shortOfBreath;
  private Concept limitedPursuingHobbies;
  private Concept limitedDoingWork;
  private Concept needHelp;
  private Concept stayInBedOrChair;
  private Concept troubleShortWalk;
  private Concept troubleLongWalk;
  private Concept strenuousActivities;
  private Concept nauseated;
  private Concept rateQOL;

  public Concept getFinancialDifficulty() {
    return financialDifficulty;
  }
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
  public void setFinancialDifficulty(Concept financialDifficulty) {
    this.financialDifficulty = financialDifficulty;
  }
  public Concept getInterferedSocialActivities() {
    return interferedSocialActivities;
  }
  public void setInterferedSocialActivities(Concept interferedSocialActivities) {
    this.interferedSocialActivities = interferedSocialActivities;
  }
  public Concept getInterferedFamilyLife() {
    return interferedFamilyLife;
  }
  public void setInterferedFamilyLife(Concept interferedFamilyLife) {
    this.interferedFamilyLife = interferedFamilyLife;
  }
  public Concept getDifficultyRemembering() {
    return difficultyRemembering;
  }
  public void setDifficultyRemembering(Concept difficultyRemembering) {
    this.difficultyRemembering = difficultyRemembering;
  }
  public Concept getDepressed() {
    return depressed;
  }
  public void setDepressed(Concept depressed) {
    this.depressed = depressed;
  }
  public Concept getIrritable() {
    return irritable;
  }
  public void setIrritable(Concept irritable) {
    this.irritable = irritable;
  }
  public Concept getWorry() {
    return worry;
  }
  public void setWorry(Concept worry) {
    this.worry = worry;
  }
  public Concept getTense() {
    return tense;
  }
  public void setTense(Concept tense) {
    this.tense = tense;
  }
  public Concept getDifficultyConcentrating() {
    return difficultyConcentrating;
  }
  public void setDifficultyConcentrating(Concept difficultyConcentrating) {
    this.difficultyConcentrating = difficultyConcentrating;
  }
  public Concept getPainInterfereDailyActivities() {
    return painInterfereDailyActivities;
  }
  public void setPainInterfereDailyActivities(Concept painInterfereDailyActivities) {
    this.painInterfereDailyActivities = painInterfereDailyActivities;
  }
  public Concept getTired() {
    return tired;
  }
  public void setTired(Concept tired) {
    this.tired = tired;
  }
  public Concept getDiarrhea() {
    return diarrhea;
  }
  public void setDiarrhea(Concept diarrhea) {
    this.diarrhea = diarrhea;
  }
  public Concept getConstipated() {
    return constipated;
  }
  public void setConstipated(Concept constipated) {
    this.constipated = constipated;
  }
  public Concept getRateHealthWeek() {
    return rateHealthWeek;
  }
  public void setRateHealthWeek(Concept rateHealthWeek) {
    this.rateHealthWeek = rateHealthWeek;
  }
  public Concept getVomited() {
    return vomited;
  }
  public void setVomited(Concept vomited) {
    this.vomited = vomited;
  }
  public Concept getLackedAppetite() {
    return lackedAppetite;
  }
  public void setLackedAppetite(Concept lackedAppetite) {
    this.lackedAppetite = lackedAppetite;
  }
  public Concept getFeltWeak() {
    return feltWeak;
  }
  public void setFeltWeak(Concept feltWeak) {
    this.feltWeak = feltWeak;
  }
  public Concept getTroubleSleeping() {
    return troubleSleeping;
  }
  public void setTroubleSleeping(Concept troubleSleeping) {
    this.troubleSleeping = troubleSleeping;
  }
  public Concept getNeedToRest() {
    return needToRest;
  }
  public void setNeedToRest(Concept needToRest) {
    this.needToRest = needToRest;
  }
  public Concept getPain() {
    return pain;
  }
  public void setPain(Concept pain) {
    this.pain = pain;
  }
  public Concept getShortOfBreath() {
    return shortOfBreath;
  }
  public void setShortOfBreath(Concept shortOfBreath) {
    this.shortOfBreath = shortOfBreath;
  }
  public Concept getLimitedPursuingHobbies() {
    return limitedPursuingHobbies;
  }
  public void setLimitedPursuingHobbies(Concept limitedPursuingHobbies) {
    this.limitedPursuingHobbies = limitedPursuingHobbies;
  }
  public Concept getLimitedDoingWork() {
    return limitedDoingWork;
  }
  public void setLimitedDoingWork(Concept limitedDoingWork) {
    this.limitedDoingWork = limitedDoingWork;
  }
  public Concept getNeedHelp() {
    return needHelp;
  }
  public void setNeedHelp(Concept needHelp) {
    this.needHelp = needHelp;
  }
  public Concept getStayInBedOrChair() {
    return stayInBedOrChair;
  }
  public void setStayInBedOrChair(Concept stayInBedOrChair) {
    this.stayInBedOrChair = stayInBedOrChair;
  }
  public Concept getTroubleShortWalk() {
    return troubleShortWalk;
  }
  public void setTroubleShortWalk(Concept troubleShortWalk) {
    this.troubleShortWalk = troubleShortWalk;
  }
  public Concept getTroubleLongWalk() {
    return troubleLongWalk;
  }
  public void setTroubleLongWalk(Concept troubleLongWalk) {
    this.troubleLongWalk = troubleLongWalk;
  }
  public Concept getStrenuousActivities() {
    return strenuousActivities;
  }
  public void setStrenuousActivities(Concept strenuousActivities) {
    this.strenuousActivities = strenuousActivities;
  }
  public Concept getNauseated() {
    return nauseated;
  }
  public void setNauseated(Concept nauseated) {
    this.nauseated = nauseated;
  }
  public Concept getRateQOL() {
    return rateQOL;
  }
  public void setRateQOL(Concept rateQOL) {
    this.rateQOL = rateQOL;
  }
  
  
}
