package eu.ihelp.store.managers;

import eu.ihelp.store.model.Observation;
import eu.ihelp.store.model.ObservationBuilder;
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
public class ObservationManager {
    private static final Logger Log = LoggerFactory.getLogger(ObservationManager.class);
    private static final String QUERY_BASE = "SELECT OBSERVATIONID, PROVIDERID, STATUS, OBSERVATIONCODE, OBSERVATIONSYSTEM, OBSERVATIONDISPLAY, EFFECTIVEDATETIME, OBSERVATIONCATEGORYCODE, OBSERVATIONCATEGORYSYSTEM, OBSERVATIONCATEGORYDISPLAY, VALUESYSTEM, VALUECODE, VALUEUNIT, VALUEQUANTITY, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, VALUESTRING, VALUEBOOLEAN, BODYSITESYSTEM, BODYSITECODE, BODYSITEDISPLAY, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.OBSERVATION";
    private static final String FIELD_PATIENT = "PATIENTID";
    private static final String FIELD_PROVIDER = "PROVIDERID";
    private static final String FIELD_DATE = "EFFECTIVEDATETIME";
    private static final String FIELD_CODE_SYSTEM = "OBSERVATIONSYSTEM";
    private static final String FIELD_CODE_VALUE = "OBSERVATIONCODE";
    
    public static List<Observation> getObservations(List<String> date, List<Patient> patients, String combocode) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getFilterQuery(date, patients, combocode));
            ResultSet resultSet = statement.executeQuery();) {
            return ObservationBuilder.createInstance(resultSet).buildAll();
        } catch(SQLException ex) {
            Log.error("Could not get observations {}", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static String getFilterQuery(List<String> date, List<Patient> patients, String combocode) {
        if(patients.isEmpty()) {
            throw new RuntimeException("There is no patient to return observations");
        }
        String query = getFilterQuery(date, patients.get(0), combocode);
        for(int i=1; i<patients.size(); i++) {
            query+= " UNION ALL " + getFilterQuery(date, patients.get(i), combocode);
        }
        return query;
    }
    
    private static String getFilterQuery(List<String> date, Patient patient, String combocode) {
        String query = QUERY_BASE + " WHERE " + FIELD_PATIENT + "='" + patient.getPatientID() + "' AND " + FIELD_PROVIDER + "='" + patient.getProviderID() + "' ";
        query = getFilterQuery(query, date);
        query = getFilterQuery(query, combocode);
        return query;
    }
    
    private static String getFilterQuery(String query, String combocode) {
        if((combocode==null) || (combocode.isEmpty())) {
            return query;
        }
        
        return query+= " AND " + FIELD_CODE_SYSTEM + "='" + combocode.substring(0, combocode.lastIndexOf("|")) + "' AND " + FIELD_CODE_VALUE + "= '" + combocode.substring(combocode.lastIndexOf("|")+1) + "'";
    }
    
    private static String getFilterQuery(String query, List<String> dates) {
        if(dates.isEmpty()) {
            return query;
        }
        for(String dateStr : dates) {    
            query+= " AND ";
            if((dateStr.startsWith("e")) && (Character.isDigit(dateStr.charAt(1)))) {
                query+= FIELD_DATE + "='" + dateStr.substring(1) + "' ";
            } else if((dateStr.startsWith("g")) && (Character.isDigit(dateStr.charAt(1)))) {
                query+= FIELD_DATE + ">'" + dateStr.substring(1) + "' ";
            } else if((dateStr.startsWith("l")) && (Character.isDigit(dateStr.charAt(1)))) {
                query+= FIELD_DATE + "<'" + dateStr.substring(1) + "' ";
            } else if(dateStr.startsWith("ge") && (Character.isDigit(dateStr.charAt(2)))) {
                query+= FIELD_DATE + ">='" + dateStr.substring(2) + "' ";
            } else if(dateStr.startsWith("le") && (Character.isDigit(dateStr.charAt(2)))) {
                query+= FIELD_DATE + "<='" + dateStr.substring(2) + "' ";
            } else {
                throw new RuntimeException("Unsupported date filter operation '" + dateStr + "'");
            }
        }
        
        return query;
    }
}
