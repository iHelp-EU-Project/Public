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
public class PatientBuilder {
    
    private final ResultSet resultSet;
    private String ihelpID;
    private String patientID;
    private String providerID;
    private String gender;
    private Boolean active;
    private Date birthDate;

    private PatientBuilder(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    public static Patient createTestInstance(String ihelpID, String patientID, String providerID) {
        return new Patient(ihelpID, patientID, providerID, null, null, null);
    }
    
    public static PatientBuilder createInstance(ResultSet resultSet) {
        return new PatientBuilder(resultSet);
    }
    
    public List<Patient> buildAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        Patient patient = null;
        while((patient = buildNext())!=null) {
            patients.add(patient);
        }
        
        return patients;
    }
    
    public Patient buildNext() throws SQLException {
        if(resultSet.next()) {
            _restoreValues();
            _setValues();
            return _buildPatient();
        } else {
            return null;
        }
    }
    
    private void _setValues() throws SQLException {
        this.ihelpID = resultSet.getString("IHELPID");
        this.patientID = resultSet.getString("PATIENTID");
        this.providerID = resultSet.getString("PROVIDERID");
        this.gender = resultSet.getString("GENDER");
        
        Boolean isActive = resultSet.getBoolean("ACTIVE");
        if(!resultSet.wasNull()) {
            this.active = isActive;
        }
        
        Timestamp timestamp = resultSet.getTimestamp("BIRTHDATE");
        if(!resultSet.wasNull()) {
            this.birthDate = new Date(timestamp.getTime());
        }
    }
    
    private void _restoreValues() {
        this.ihelpID = null;
        this.patientID = null;
        this.providerID = null;
        this.gender = null;
        this.active = null;
        this.birthDate = null;
    }
    
    private Patient _buildPatient() {
        return new Patient(ihelpID, patientID, providerID, gender, active, birthDate);
    }
}
