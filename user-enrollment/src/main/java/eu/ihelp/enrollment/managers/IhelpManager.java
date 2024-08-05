package eu.ihelp.enrollment.managers;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.model.IhelpDTO;
import eu.ihelp.enrollment.model.IhelpPatientDTO;
import eu.ihelp.enrollment.utils.DBAHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IhelpManager {
    private static final Logger Log = LoggerFactory.getLogger(IhelpManager.class);
    private static final String qGetIhelpPerson = "SELECT IHELPID, PATIENTID, PROVIDERID FROM APP.IHELP_PERSON WHERE IHELPID = ?";
    private static final String qdeleteIhelpPerson = "DELETE FROM APP.IHELP_PERSON WHERE IHELPID = ?";
    private static final String qGetIhelpPersonFromList = "SELECT IHELPID, PATIENTID, PROVIDERID FROM APP.IHELP_PERSON WHERE IHELPID IN (";
    private static final String qGetIhelpPersonFromPatientProviderID = "SELECT IHELPID, PATIENTID, PROVIDERID FROM APP.IHELP_PERSON WHERE PATIENTID = ? AND PROVIDERID = ?";
    private static final String qAddIhelpPerson = "INSERT INTO APP.IHELP_PERSON (IHELPID, PATIENTID, PROVIDERID) VALUES (?, ?, ?)";
    private static final String qRmoveIhelpPerson = "DELETE FROM APP.IHELP_PERSON WHERE IHELPID = ? AND PATIENTID = ? AND  PROVIDERID = ?";
    
    
    
    public static List<IhelpDTO> getIhelpPersons(String ihelpID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _getIhelpPersons(connection, ihelpID);
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}. {}", ihelpID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static void deleteIhelpPerson(String ihelpID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            _deleteIhelpPersons(connection, ihelpID);
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}. {}", ihelpID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static IhelpDTO getIhelpPerson(String patientID, String providerID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _getIhelpPerson(connection, patientID, providerID);  
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}/{}. {}", patientID, providerID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> getIhelpPersons(List<String> ihelpIDs) throws DataStoreException {
        if(ihelpIDs.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _getIhelpPersons(connection, ihelpIDs);
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person. {}", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> addIhelpPerson(String ihelpID, String patientID, String providerID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _addIhelpPerson(connection, ihelpID, patientID, providerID);
        } catch(SQLException ex) {
            Log.error("Could not add ihelp person for {}:[{}-{}]. {}", ihelpID, providerID, patientID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> removeIhelpPerson(String ihelpID, String patientID, String providerID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection()) {
            return _removeIhelpPerson(connection, ihelpID, patientID, providerID);
        } catch(SQLException ex) {
            Log.error("Could not remove ihelp person for {}:[{}-{}]. {}", ihelpID, providerID, patientID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static void _deleteIhelpPersons(Connection connection, String ihelpID) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qdeleteIhelpPerson);) {
            statement.setString(1, ihelpID);
            
            statement.executeUpdate();
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}. {}", ihelpID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> _getIhelpPersons(Connection connection, String ihelpID) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qGetIhelpPerson);) {
            statement.setString(1, ihelpID);
            
            ResultSet resultSet = statement.executeQuery();
            List<IhelpDTO> result = new ArrayList<>();
            while(resultSet.next()) {
                result.add(buildIhelpPerson(resultSet));
            }
            return result;
            
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}. {}", ihelpID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> _getIhelpPersons(Connection connection, List<String> ihelpIDs) throws DataStoreException {
        try(
            Statement statement = connection.createStatement();) {
            String query = qGetIhelpPersonFromList;
            for(String ihelpID : ihelpIDs) {
                query+= "'" + ihelpID + "', ";
            }
            
            query = query.substring(0, query.length()-2);
            query+= ")";
            
            ResultSet resultSet = statement.executeQuery(query);
            List<IhelpDTO> result = new ArrayList<>();
            while(resultSet.next()) {
                result.add(buildIhelpPerson(resultSet));
            }
            return result;
            
        } catch(SQLException ex) {
            Log.error("Could not get ihelp ids . {}", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static IhelpDTO _getIhelpPerson(Connection connection, String patientID, String providerID) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qGetIhelpPersonFromPatientProviderID);) {
            statement.setString(1, patientID);
            statement.setString(2, providerID);
            
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return buildIhelpPerson(resultSet);
            }
            return null;
            
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}/{}. {}", patientID, providerID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> _addIhelpPerson(Connection connection, String ihelpID, String patientID, String providerID) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qAddIhelpPerson);) {
            statement.setString(1, ihelpID);
            statement.setString(2, patientID);
            statement.setString(3, providerID);
            
            statement.executeUpdate();
                    
            return _getIhelpPersons(connection, ihelpID);
            
        } catch(SQLException ex) {
            Log.error("Could not get ihelp person for {}. {}", ihelpID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    public static List<IhelpDTO> _removeIhelpPerson(Connection connection, String ihelpID, String patientID, String providerID) throws DataStoreException {
        try(
            PreparedStatement statement = connection.prepareStatement(qRmoveIhelpPerson);) {
            statement.setString(1, ihelpID);
            statement.setString(2, patientID);
            statement.setString(3, providerID);
            
            statement.executeUpdate();
                    
            return _getIhelpPersons(connection, ihelpID);
            
        } catch(SQLException ex) {
            Log.error("Could not remove ihelp person for {}:[{}-{}]. {}", ihelpID, providerID, patientID, ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    
    private static IhelpDTO buildIhelpPerson(ResultSet resultSet) throws SQLException {
        IhelpDTO ihelp = new IhelpDTO();
        ihelp.setiHelpID(resultSet.getString("IHELPID"));
        ihelp.setPatientID(resultSet.getString("PATIENTID"));
        ihelp.setProviderID(resultSet.getString("PROVIDERID"));
        
        return ihelp;
    } 
}
