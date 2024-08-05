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
public class MeasurementBuilder {
    private final ResultSet resultSet;
    protected String measurementID;
    protected String providerID;
    protected String status;
    protected String measurementCode;
    protected String measurementSystem;
    protected String measurementDisplay;
    protected Date effectiveDate;
    protected String measurementCategoryCode;
    protected String measurementCategorySystem;
    protected String measurementCategoryDisplay;
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
    protected Integer encounterID;
    protected Integer practitionerID;
    protected String patientID;

    private MeasurementBuilder(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    public static MeasurementBuilder createInstance(ResultSet resultSet) {
        return new MeasurementBuilder(resultSet);
    }
    
    public List<Measurement> buildAll() throws SQLException {
        List<Measurement> measurements = new ArrayList<>();
        Measurement measurement = null;
        while((measurement = buildNext())!=null) {
            measurements.add(measurement);
        }
        
        return measurements;
    }
    
    public Measurement buildNext() throws SQLException {
        if(resultSet.next()) {
            _restoreValues();
            _setValues();
            return _buildMeasurement();
        } else {
            return null;
        }
    }
    
    
    private void _setValues() throws SQLException {
        this.measurementID = resultSet.getString("MEASUREMENTID");
        this.providerID = resultSet.getString("PROVIDERID");
        this.status = resultSet.getString("STATUS");
        this.measurementCode = resultSet.getString("MEASUREMENTCODE");
        this.measurementSystem = resultSet.getString("MEASUREMENTSYSTEM");
        this.measurementDisplay = resultSet.getString("MEASUREMENTDISPLAY");
        this.measurementCategoryCode = resultSet.getString("MEASUREMENTCATEGORYCODE");
        this.measurementCategorySystem = resultSet.getString("MEASUREMENTCATEGORYSYSTEM");
        this.measurementCategoryDisplay = resultSet.getString("MEASUREMENTCATEGORYDISPLAY");
        this.valueSystem = resultSet.getString("VALUESYSTEM");
        this.valueCode = resultSet.getString("VALUECODE");
        this.valueUnit = resultSet.getString("VALUEUNIT");
        this.valueSystemLow = resultSet.getString("VALUESYSTEMLOW");
        this.valueCodeLow = resultSet.getString("VALUECODELOW");
        this.valueUnitLow = resultSet.getString("VALUEUNITLOW");
        this.valueSystemHigh = resultSet.getString("VALUESYSTEMHIGH");
        this.valueCodeHigh = resultSet.getString("VALUECODEHIGH");
        this.valueUnitHigh = resultSet.getString("VALUEUNITHIGH");      
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
        this.measurementID = null;
        this.providerID = null;
        this.status = null;
        this.measurementCode = null;
        this.measurementSystem = null;
        this.measurementDisplay = null;
        this.effectiveDate = null;
        this.measurementCategoryCode = null;
        this.measurementCategorySystem = null;
        this.measurementCategoryDisplay = null;
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
        this.encounterID = null;
        this.practitionerID = null;
        this.patientID = null;
    }
    
    private Measurement _buildMeasurement() {
        return new Measurement(measurementID, providerID, status, measurementCode, measurementSystem, measurementDisplay, effectiveDate, measurementCategoryCode, measurementCategorySystem, measurementCategoryDisplay, valueSystem, valueCode, valueUnit, valueQuantity, valueSystemLow, valueCodeLow, valueUnitLow, valueQuantityLow, valueSystemHigh, valueCodeHigh, valueUnitHigh, valueQuantityHigh, encounterID, practitionerID, patientID);
    }
}
