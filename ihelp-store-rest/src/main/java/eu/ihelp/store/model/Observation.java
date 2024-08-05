package eu.ihelp.store.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Observation implements Serializable {
    protected static final long serialVersionUID = 1L;
    
    protected final String observationID;
    protected final String providerID;
    protected final String status;
    protected final String observationCode;
    protected final String observationSystem;
    protected final String observationDisplay;
    protected final Date effectiveDate;
    protected final String observationCategoryCode;
    protected final String observationCategorySystem;
    protected final String observationCategoryDisplay;
    protected final String valueSystem;
    protected final String valueCode;
    protected final String valueUnit;
    protected final Double valueQuantity;
    protected final String valueSystemLow;
    protected final String valueCodeLow;
    protected final String valueUnitLow;
    protected final Double valueQuantityLow;
    protected final String valueSystemHigh;
    protected final String valueCodeHigh;
    protected final String valueUnitHigh;
    protected final Double valueQuantityHigh;
    protected final String valueString;
    protected final Boolean valueBoolean;
    protected final String bodysiteSystem;
    protected final String bodysiteCode;
    protected final String bodysiteDisplay;
    protected final Integer encounterID;
    protected final Integer practitionerID;
    protected final String patientID;

    protected Observation(String observationID, String providerID, String status, String observationCode, String observationSystem, String observationDisplay, Date effectiveDate, String observationCategoryCode, String observationCategorySystem, String observationCategoryDisplay, String valueSystem, String valueCode, String valueUnit, Double valueQuantity, String valueSystemLow, String valueCodeLow, String valueUnitLow, Double valueQuantityLow, String valueSystemHigh, String valueCodeHigh, String valueUnitHigh, Double valueQuantityHigh, String valueString, Boolean valueBoolean, String bodysiteSystem, String bodysiteCode, String bodysiteDisplay, Integer encounterID, Integer practitionerID, String patientID) {
        this.observationID = observationID;
        this.providerID = providerID;
        this.status = status;
        this.observationCode = observationCode;
        this.observationSystem = observationSystem;
        this.observationDisplay = observationDisplay;
        this.effectiveDate = effectiveDate;
        this.observationCategoryCode = observationCategoryCode;
        this.observationCategorySystem = observationCategorySystem;
        this.observationCategoryDisplay = observationCategoryDisplay;
        this.valueSystem = valueSystem;
        this.valueCode = valueCode;
        this.valueUnit = valueUnit;
        this.valueQuantity = valueQuantity;
        this.valueSystemLow = valueSystemLow;
        this.valueCodeLow = valueCodeLow;
        this.valueUnitLow = valueUnitLow;
        this.valueQuantityLow = valueQuantityLow;
        this.valueSystemHigh = valueSystemHigh;
        this.valueCodeHigh = valueCodeHigh;
        this.valueUnitHigh = valueUnitHigh;
        this.valueQuantityHigh = valueQuantityHigh;
        this.valueString = valueString;
        this.valueBoolean = valueBoolean;
        this.bodysiteSystem = bodysiteSystem;
        this.bodysiteCode = bodysiteCode;
        this.bodysiteDisplay = bodysiteDisplay;
        this.encounterID = encounterID;
        this.practitionerID = practitionerID;
        this.patientID = patientID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getObservationID() {
        return observationID;
    }

    public String getProviderID() {
        return providerID;
    }

    public String getStatus() {
        return status;
    }

    public String getObservationCode() {
        return observationCode;
    }

    public String getObservationSystem() {
        return observationSystem;
    }

    public String getObservationDisplay() {
        return observationDisplay;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public String getObservationCategoryCode() {
        return observationCategoryCode;
    }

    public String getObservationCategorySystem() {
        return observationCategorySystem;
    }

    public String getObservationCategoryDisplay() {
        return observationCategoryDisplay;
    }

    public String getValueSystem() {
        return valueSystem;
    }

    public String getValueCode() {
        return valueCode;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public Double getValueQuantity() {
        return valueQuantity;
    }

    public String getValueSystemLow() {
        return valueSystemLow;
    }

    public String getValueCodeLow() {
        return valueCodeLow;
    }

    public String getValueUnitLow() {
        return valueUnitLow;
    }

    public Double getValueQuantityLow() {
        return valueQuantityLow;
    }

    public String getValueSystemHigh() {
        return valueSystemHigh;
    }

    public String getValueCodeHigh() {
        return valueCodeHigh;
    }

    public String getValueUnitHigh() {
        return valueUnitHigh;
    }

    public Double getValueQuantityHigh() {
        return valueQuantityHigh;
    }

    public String getValueString() {
        return valueString;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public String getBodysiteSystem() {
        return bodysiteSystem;
    }

    public String getBodysiteCode() {
        return bodysiteCode;
    }

    public String getBodysiteDisplay() {
        return bodysiteDisplay;
    }

    public Integer getEncounterID() {
        return encounterID;
    }

    public Integer getPractitionerID() {
        return practitionerID;
    }

    public String getPatientID() {
        return patientID;
    }

    @Override
    public String toString() {
        return "Observation{" + "observationID=" + observationID + ", providerID=" + providerID + ", observationCode=" + observationCode + ", observationSystem=" + observationSystem + ", observationDisplay=" + observationDisplay + ", patientID=" + patientID + '}';
    }
    
    
    
}
