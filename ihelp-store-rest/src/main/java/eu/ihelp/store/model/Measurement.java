package eu.ihelp.store.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Measurement implements Serializable {
    protected static final long serialVersionUID = 1L;
    
    protected final String measurementID;
    protected final String providerID;
    protected final String status;
    protected final String measurementCode;
    protected final String measurementSystem;
    protected final String measurementDisplay;
    protected final Date effectiveDate;
    protected final String measurementCategoryCode;
    protected final String measurementCategorySystem;
    protected final String measurementCategoryDisplay;
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
    protected final Integer encounterID;
    protected final Integer practitionerID;
    protected final String patientID;

    protected Measurement(String measurementID, String providerID, String status, String measurementCode, String measurementSystem, String measurementDisplay, Date effectiveDate, String measurementCategoryCode, String measurementCategorySystem, String measurementCategoryDisplay, String valueSystem, String valueCode, String valueUnit, Double valueQuantity, String valueSystemLow, String valueCodeLow, String valueUnitLow, Double valueQuantityLow, String valueSystemHigh, String valueCodeHigh, String valueUnitHigh, Double valueQuantityHigh, Integer encounterID, Integer practitionerID, String patientID) {
        this.measurementID = measurementID;
        this.providerID = providerID;
        this.status = status;
        this.measurementCode = measurementCode;
        this.measurementSystem = measurementSystem;
        this.measurementDisplay = measurementDisplay;
        this.effectiveDate = effectiveDate;
        this.measurementCategoryCode = measurementCategoryCode;
        this.measurementCategorySystem = measurementCategorySystem;
        this.measurementCategoryDisplay = measurementCategoryDisplay;
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
        this.encounterID = encounterID;
        this.practitionerID = practitionerID;
        this.patientID = patientID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMeasurementID() {
        return measurementID;
    }

    public String getProviderID() {
        return providerID;
    }

    public String getStatus() {
        return status;
    }

    public String getMeasurementCode() {
        return measurementCode;
    }

    public String getMeasurementSystem() {
        return measurementSystem;
    }

    public String getMeasurementDisplay() {
        return measurementDisplay;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public String getMeasurementCategoryCode() {
        return measurementCategoryCode;
    }

    public String getMeasurementCategorySystem() {
        return measurementCategorySystem;
    }

    public String getMeasurementCategoryDisplay() {
        return measurementCategoryDisplay;
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
        return "Measurement{" + "measurementID=" + measurementID + ", providerID=" + providerID + ", status=" + status + ", measurementCode=" + measurementCode + ", measurementSystem=" + measurementSystem + ", measurementDisplay=" + measurementDisplay + ", patientID=" + patientID + '}';
    }
    
    
    
}
