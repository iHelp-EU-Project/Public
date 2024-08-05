package eu.ihelp.store.managers;

import eu.ihelp.store.model.MessageBuilder;
import eu.ihelp.store.model.Patient;
import eu.ihelp.store.model.PatientBuilder;
import eu.ihelp.store.server.exceptions.DataStoreException;
import eu.ihelp.store.server.utils.DBAHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class PatientManager {
    private static final Logger Log = LoggerFactory.getLogger(PatientManager.class);
    
    //this query causes the QE to run out of heap size
    //private static final String QUERY_PATIENTS = "SELECT p.PATIENTID, p.PROVIDERID, p.GENDER, p.ACTIVE, p.BIRTHDATE, ih.IHELPID  FROM APP.PATIENT p INNER JOIN APP.IHELP_PERSON ih ON p.PATIENTID = ih.PATIENTID AND p.PROVIDERID = ih.PROVIDERID";
    
    //I removed the double equition on the inner join. this is only for the review, this is not a fix. the fix should be on the QE itself
    private static final String QUERY_PATIENTS = "SELECT p.PATIENTID, p.PROVIDERID, p.GENDER, p.ACTIVE, p.BIRTHDATE, ih.IHELPID  FROM APP.PATIENT p INNER JOIN APP.IHELP_PERSON ih ON p.PATIENTID = ih.PATIENTID ";
    
    public static List<Patient> getPatients(List<String> ihelpIDS) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getFilterQuery(ihelpIDS));
            ResultSet resultSet = statement.executeQuery();) {
            return PatientBuilder.createInstance(resultSet).buildAll();
        } catch(SQLException ex) {
            Log.error("Could not get patients for messages {}", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static String getFilterQuery(List<String> ihelpIDs) {
        if(ihelpIDs.isEmpty()) {
            return QUERY_PATIENTS + " WHERE ih.IHELPID  IN  ('')"; //return empty list of IDs so that the resultset will be empty
        }
        
        String result = QUERY_PATIENTS + " WHERE ih.IHELPID  IN  (";
        for(String iHelpID : ihelpIDs) {
            result+= "'" + iHelpID + "', ";
        }
        
        result = result.substring(0, result.length()-2);
        return result + ")";
    }
    
    
    
}
