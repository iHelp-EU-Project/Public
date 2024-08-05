package eu.ihelp.store.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ObservationBuilder {
    private final ResultSet resultSet;
    protected String observationID;
    protected String providerID;
    protected String status;
    protected String observationCode;
    protected String observationSystem;
    protected String observationDisplay;
    protected Date effectiveDate;
    protected String observationCategoryCode;
    protected String observationCategorySystem;
    protected String observationCategoryDisplay;
    protected String valueSystem;
    protected String valueCode;
    protected String valueUnit;
    protected Double valueQuantity;
    protected String valueSystemLow;
    protected String valueCodeLow;
    protected String valueUnitLow;
    protected Double valueQuantityLow;
    protected String valueSystemHigh;
    protected String valueCodeHigh;
    protected String valueUnitHigh;
    protected Double valueQuantityHigh;
    protected String valueString;
    protected Boolean valueBoolean;
    protected String bodysiteSystem;
    protected String bodysiteCode;
    protected String bodysiteDisplay;
    protected Integer encounterID;
    protected Integer practitionerID;
    protected String patientID;
    
    private ObservationBuilder(ResultSet resultSet) {
        this.resultSet = resultSet;
    } 
    
    public static ObservationBuilder createInstance(ResultSet resultSet) {
        return new ObservationBuilder(resultSet);
    }
    
    public List<Observation> buildAll() throws SQLException {
        List<Observation> observations = new ArrayList<>();
        Observation observation = null;
        while((observation = buildNext())!=null) {
            observations.add(observation);
        }
        
        return observations;
    }
    
    public Observation buildNext() throws SQLException {
        if(resultSet.next()) {
            _restoreValues();
            _setValues();
            return _buildObservation();
        } else {
            return null;
        }
    }
    
    private void _setValues() throws SQLException {
        this.observationID = resultSet.getString("OBSERVATIONID");
        this.providerID = resultSet.getString("PROVIDERID");
        this.status = resultSet.getString("STATUS");
        this.observationCode = resultSet.getString("OBSERVATIONCODE");
        this.observationSystem = resultSet.getString("OBSERVATIONSYSTEM");
        this.observationDisplay = resultSet.getString("OBSERVATIONDISPLAY");
        this.observationCategoryCode = resultSet.getString("OBSERVATIONCATEGORYCODE");
        this.observationCategorySystem = resultSet.getString("OBSERVATIONCATEGORYSYSTEM");
        this.observationCategoryDisplay = resultSet.getString("OBSERVATIONCATEGORYDISPLAY");
        this.valueSystem = resultSet.getString("VALUESYSTEM");
        this.valueCode = resultSet.getString("VALUECODE");
        this.valueUnit = resultSet.getString("VALUEUNIT");
        this.valueSystemLow = resultSet.getString("VALUESYSTEMLOW");
        this.valueCodeLow = resultSet.getString("VALUECODELOW");
        this.valueUnitLow = resultSet.getString("VALUEUNITLOW");
        this.valueSystemHigh = resultSet.getString("VALUESYSTEMHIGH");
        this.valueCodeHigh = resultSet.getString("VALUECODEHIGH");
        this.valueUnitHigh = resultSet.getString("VALUEUNITHIGH");        
        this.valueString = resultSet.getString("VALUESTRING");
        this.bodysiteSystem = resultSet.getString("BODYSITESYSTEM");
        this.bodysiteCode = resultSet.getString("BODYSITECODE");
        this.bodysiteDisplay = resultSet.getString("BODYSITEDISPLAY");
        this.patientID = resultSet.getString("PATIENTID");
        
        Timestamp timestamp = resultSet.getTimestamp("EFFECTIVEDATETIME");
        if(!resultSet.wasNull()) {
            this.effectiveDate = new Date(timestamp.getTime());
        }
        
        Double value = resultSet.getDouble("VALUEQUANTITY");
        if(!resultSet.wasNull()) {
            this.valueQuantity = value;
        }
        value = resultSet.getDouble("VALUEQUANTITYLOW");
        if(!resultSet.wasNull()) {
            this.valueQuantityLow = value;
        }
        value = resultSet.getDouble("VALUEQUANTITYHIGH");
        if(!resultSet.wasNull()) {
            this.valueQuantityHigh = value;
        }
        
        Boolean boolVAlue = resultSet.getBoolean("VALUEBOOLEAN");
        if(!resultSet.wasNull()) {
            this.valueBoolean = boolVAlue;
        }
        
        Integer intValue = resultSet.getInt("ENCOUNTERID");
        if(!resultSet.wasNull()) {
            this.encounterID = intValue;
        }
        intValue = resultSet.getInt("PRACTITIONERID");
        if(!resultSet.wasNull()) {
            this.practitionerID = intValue;
        }
    }
    
    
    private void _restoreValues() {
        this.observationID = null;
        this.providerID = null;
        this.status = null;
        this.observationCode = null;
        this.observationSystem = null;
        this.observationDisplay = null;
        this.effectiveDate = null;
        this.observationCategoryCode = null;
        this.observationCategorySystem = null;
        this.observationCategoryDisplay = null;
        this.valueSystem = null;
        this.valueCode = null;
        this.valueUnit = null;
        this.valueQuantity = null;
        this.valueSystemLow = null;
        this.valueCodeLow = null;
        this.valueUnitLow = null;
        this.valueQuantityLow = null;
        this.valueSystemHigh = null;
        this.valueCodeHigh = null;
        this.valueUnitHigh = null;
        this.valueQuantityHigh = null;
        this.valueString = null;
        this.valueBoolean = null;
        this.bodysiteSystem = null;
        this.bodysiteCode = null;
        this.bodysiteDisplay = null;
        this.encounterID = null;
        this.practitionerID = null;
        this.patientID = null;
    }
    
    private Observation _buildObservation() {
        return new Observation(observationID, providerID, status, observationCode, observationSystem, observationDisplay, effectiveDate, observationCategoryCode, observationCategorySystem, observationCategoryDisplay, valueSystem, valueCode, valueUnit, valueQuantity, valueSystemLow, valueCodeLow, valueUnitLow, valueQuantityLow, valueSystemHigh, valueCodeHigh, valueUnitHigh, valueQuantityHigh, valueString, valueBoolean, bodysiteSystem, bodysiteCode, bodysiteDisplay, encounterID, practitionerID, patientID);
    }
}
