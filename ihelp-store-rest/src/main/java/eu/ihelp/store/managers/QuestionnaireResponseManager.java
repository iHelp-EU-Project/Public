package eu.ihelp.store.managers;

import eu.ihelp.store.model.Patient;
import eu.ihelp.store.model.QuestionnaireResponse;
import eu.ihelp.store.model.QuestionnaireResponseBuilder;
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
public class QuestionnaireResponseManager {
    private static final Logger Log = LoggerFactory.getLogger(QuestionnaireResponseManager.class);
    private static final String QUERY_BASE = "SELECT ANSWERID, PROVIDERID, STATUS, AUTHORED, QUESTIONNAIREID, QUESTIONNAIRENAME, QUESTIONNAIRETITLE, LINKID, QUESTIONID, QUESTION, ANSWERSYSTEM, ANSWER, SCORE, PATIENTID FROM ANSWER ";
    private static final String FIELD_PATIENT = "PATIENTID";
    private static final String FIELD_PROVIDER = "PROVIDERID";
    private static final String FIELD_QUESTIONNAIRE_ID = "QUESTIONNAIREID";
    private static final String FIELD_DATE = "AUTHORED";
    
    public static List<QuestionnaireResponse> getQuestionnaireResponses(List<String> date, List<Patient> patients, String questionnaireID) throws DataStoreException {
        try(Connection connection = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getFilterQuery(date, patients, questionnaireID));
            ResultSet resultSet = statement.executeQuery();) {
            return QuestionnaireResponseBuilder.createIstance(resultSet).buildAll();
        } catch(SQLException ex) {
            Log.error("Could not get observations {}", ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    private static String getFilterQuery(List<String> date, List<Patient> patients, String questionnaireID) {
        if(patients.isEmpty()) {
            throw new RuntimeException("There is no patient to return observations");
        }
        String query = getFilterQuery(date, patients.get(0), questionnaireID);
        for(int i=1; i<patients.size(); i++) {
            query+= " UNION ALL " + getFilterQuery(date, patients.get(i), questionnaireID);
        }
        return query;
    }
    
    private static String getFilterQuery(List<String> date, Patient patient, String questionnaireID) {
        String query = QUERY_BASE + " WHERE " + FIELD_PATIENT + "='" + patient.getPatientID() + "' AND " + FIELD_PROVIDER + "='" + patient.getProviderID() + "' ";
        query = getFilterQuery(query, date);
        query = getFilterQuery(query, questionnaireID);
        return query;
    }
    
    private static String getFilterQuery(String query, String questionnaireID) {
        if((questionnaireID==null) || (questionnaireID.isEmpty())) {
            return query;
        }
        
        return query + (" AND " + FIELD_QUESTIONNAIRE_ID + "='" + questionnaireID + "' ");
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
