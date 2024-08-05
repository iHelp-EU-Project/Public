package eu.ihelp.enrollment.managers;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.model.IhelpDTO;
import eu.ihelp.enrollment.model.PatientDTO;
import eu.ihelp.enrollment.utils.DBAHelper;
import eu.ihelp.enrollment.utils.IhelpDateFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class PatientManager {
    private static final Logger Log = LoggerFactory.getLogger(PatientManager.class);
    private static final String qGetPatient = "SELECT PATIENTID, PROVIDERID, GENDER, ACTIVE, BIRTHDATE FROM APP.PATIENT WHERE PATIENTID = ? AND PROVIDERID = ?";
    private static final String qInsertPatient = "INSERT INTO APP.PATIENT (PATIENTID, PROVIDERID, GENDER, ACTIVE, BIRTHDATE) VALUES (?, ?, ?, ?, ?)";
    private static final String qUpdatePatient = "UPDATE APP.PATIENT SET GENDER = ?, ACTIVE = ?, BIRTHDATE = ? WHERE PATIENTID = ? AND PROVIDERID = ?";
    private static final String qGetPatientsFromIhelpID = "SELECT PATIENTID, PROVIDERID FROM IHELP_PERSON WHERE IHELPID = ?";
    
    public static List<PatientDTO> getPatients(String ihelpID) throws DataStoreException {
        List<PatientDTO> patients = new ArrayList<>();    
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            List<IhelpDTO> ihelpDTOs = IhelpManager._getIhelpPersons(connection, ihelpID);
            for(IhelpDTO ihelPerson : ihelpDTOs) {
                patients.add(_getPatient(connection, ihelPerson.getPatientID(), ihelPerson.getProviderID()));
            }
        } catch(SQLException ex) {
            Log.error("Could not get patient {}. {}", ihelpID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
        return patients;
    }
     
    public static PatientDTO getPatient(String patientID, String providerID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _getPatient(connection, patientID, providerID);
        } catch(SQLException ex) {
            Log.error("Could not get patient {}:{}. {}", providerID, patientID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static PatientDTO insertPatient(PatientDTO patient) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _insertPatient(connection, patient);
        } catch(SQLException ex) {
            Log.error("Could not insert patient {}:{}. {}", patient.getProviderID(), patient.getPatientID(), ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static void updatePatient(PatientDTO patient) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            _updatePatient(connection, patient);
        } catch(SQLException ex) {
            Log.error("Could not update patient {}:{}. {}", patient.getProviderID(), patient.getPatientID(), ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static PatientDTO _getPatient(Connection connection, String patientID, String providerID) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qGetPatient);) {
            statement.setString(1, patientID);
            statement.setString(2, providerID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return buildPatient(resultSet);
            } else {
                return null;
            }            
        } catch(SQLException ex) {
            Log.error("Could not get patient {}:{}. {}", providerID, patientID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static PatientDTO _insertPatient(Connection connection, PatientDTO patient) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qInsertPatient);) {
            statement.setString(1, patient.getPatientID());
            statement.setString(2, patient.getProviderID());
            statement.setString(3, patient.getGender());
            
            if(patient.getActive()!=null) {
                statement.setBoolean(4, patient.getActive());
            } else {
                statement.setNull(4, Types.BOOLEAN);
            }
            
            if((patient.getBirthdate()!=null) && (!patient.getBirthdate().isEmpty())) {
                statement.setTimestamp(5, new Timestamp(IhelpDateFormatter.getDateFromString(patient.getBirthdate()).getTime()));
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }
            
            statement.executeUpdate();
            
            return patient;
        } catch(SQLException ex) {
            Log.error("Could not insert patient {}:{}. {}", patient.getProviderID(), patient.getPatientID(), ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static void _updatePatient(Connection connection, PatientDTO patient) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qUpdatePatient);) {
            statement.setString(1, patient.getGender());
            
            if(patient.getActive()!=null) {
                statement.setBoolean(2, patient.getActive());
            } else {
                statement.setNull(2, Types.BOOLEAN);
            }
            
            if((patient.getBirthdate()!=null) && (!patient.getBirthdate().isEmpty())) {
                statement.setTimestamp(3, new Timestamp(IhelpDateFormatter.getDateFromString(patient.getBirthdate()).getTime()));
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }
            
            statement.setString(4, patient.getPatientID());
            statement.setString(5, patient.getProviderID());
            
            statement.executeUpdate();            
            
        } catch(SQLException ex) {
            Log.error("Could not update patient {}:{}. {}", patient.getProviderID(), patient.getPatientID(), ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static PatientDTO buildPatient(ResultSet resultSet) throws SQLException {
        PatientDTO patient = new PatientDTO();
        patient.setPatientID(resultSet.getString("PATIENTID"));
        patient.setProviderID(resultSet.getString("PROVIDERID"));
        patient.setGender(resultSet.getString("GENDER"));
        
        Boolean active = resultSet.getBoolean("ACTIVE");
        if(!resultSet.wasNull()) {
            patient.setActive(active);
        }
        
        Timestamp date = resultSet.getTimestamp("BIRTHDATE");
        if(!resultSet.wasNull()) {
            patient.setBirthdate(IhelpDateFormatter.getStringFromDate(new Date(date.getTime())));
        }
        
        return patient;
    }
}
