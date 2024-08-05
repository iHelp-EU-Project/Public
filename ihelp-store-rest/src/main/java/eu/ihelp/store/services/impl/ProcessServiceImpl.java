package eu.ihelp.store.services.impl;

import eu.ihelp.store.server.utils.DBAHelper;
import eu.ihelp.store.services.model.DataColumn;
import eu.ihelp.store.services.model.DataRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import eu.ihelp.store.services.ProcessService;
import eu.ihelp.store.services.model.DataTable;
import eu.ihelp.store.services.model.IhelpPatient;
import eu.ihelp.store.services.model.PatientProvider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ProcessServiceImpl implements ProcessService {
    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(ProcessService.class);

    @Override
    public List<DataRow> processQuery(String query) throws SQLException {
        System.out.println("Will execute: " + query);
        try (Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(query);) {
            ResultSet resultSet = statement.executeQuery();
            int columnCount = resultSet.getMetaData().getColumnCount();
            List<DataRow> result = new ArrayList<>();
            while(resultSet.next()) {
                List<DataColumn> row = new ArrayList<>(columnCount);
                for(int i=0; i<columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i+1);
                    DataColumn column = new DataColumn();
                    column.setName(columnName);
                    column.setValue(resultSet.getObject(i+1));
                    row.add(column);
                }
                result.add(new DataRow(row));
            }
            resultSet.close();
            return result;
        }
    }
    
    
    private JSONObject processPrimaryQuery(String tableName, String query, int numOfColumns, boolean valueIsString) throws SQLException {
        JSONObject jSONObject = new JSONObject();
        JSONArray result = new JSONArray();
        jSONObject.put("table", tableName.toUpperCase());
        jSONObject.put("row", result);
        
        System.out.println("Will execute: " + query);
        try (Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(query);) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                JSONObject row = new JSONObject();
                JSONArray rowArray = new JSONArray();
                row.put("row", rowArray);
                JSONObject objName = new JSONObject();
                objName.put("NAME", resultSet.getString(1));
                rowArray.put(objName);
                
                if(numOfColumns==2) {
                    JSONObject objValue = new JSONObject();
                    if(valueIsString) {
                        objValue.put("VALUE", resultSet.getString(2));
                    } else {
                        objValue.put("VALUE", resultSet.getDouble(2));
                    }
                    rowArray.put(objValue);
                }
                
                result.put(row);
            }
            
            resultSet.close();
            return jSONObject;
        }
    }
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static String getStringFromDate(Date date) {
        return simpleDateFormat.format(date);
    }
    private static final String QUERY_GETSECONDARYDATA = "SELECT OBSERVATIONDISPLAY, EFFECTIVEDATETIME,  VALUESTRING \n" +
            "FROM APP.OBSERVATION\n" +
            "WHERE PROVIDERID = 'HEALTHENTIA' AND PATIENTID  = ? \n" +
            "ORDER BY OBSERVATIONDISPLAY, EFFECTIVEDATETIME DESC";
    
    @Override
    public JSONArray getSecondary(String ihelpID) throws SQLException {
        final JSONArray result = new JSONArray();
        final List<PatientProvider> patientIDs = new ArrayList<>();
        try (Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT PATIENTID, PROVIDERID FROM APP.IHELP_PERSON WHERE IHELP_PERSON.IHELPID = ?");) {
            statement.setString(1, ihelpID);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                PatientProvider patientProvider = new PatientProvider(resultSet.getString("PATIENTID"), resultSet.getString("PROVIDERID"));
                patientIDs.add(patientProvider);
            }
            resultSet.close();
        }
        
        //now find the healthentiaID
        String healthentiaID = null;
        for(PatientProvider patientProvider : patientIDs) {
            if(patientProvider.isHealthentia()) {
                healthentiaID = patientProvider.getPatientID();
                break;
            }
        }
        
        try(Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(QUERY_GETSECONDARYDATA);) {
            statement.setString(1, healthentiaID);
            ResultSet resultSet = statement.executeQuery();
            String type = "";
            JSONObject jSONObject = null;
            JSONArray list = null;
            while(resultSet.next()) {
                if(!type.equalsIgnoreCase(resultSet.getString(1))) {
                    type = resultSet.getString(1);
                    jSONObject = new JSONObject();
                    list = new JSONArray();
                    result.put(jSONObject);
                    jSONObject.put("observation", type);
                    jSONObject.put("row", list);                    
                }
                
                JSONObject rowObject = new JSONObject();
                JSONArray columns = new JSONArray();
                JSONObject dateTimeValue = new JSONObject();
                dateTimeValue.put("datetime", getStringFromDate(new Date(resultSet.getTimestamp(2).getTime())));
                JSONObject valueValue = new JSONObject();
                valueValue.put("VALUE", resultSet.getString(3));
                columns.put(dateTimeValue);
                columns.put(valueValue);
                rowObject.put("row", columns);
                list.put(rowObject);
                
            }
            resultSet.close();            
        }   
        
        return result;
    }
    
    @Override
    public JSONArray getPrimaryData(String ihelpID) throws SQLException {
        final JSONArray result = new JSONArray();
        final List<PatientProvider> patientIDs = new ArrayList<>();
        try (Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT PATIENTID, PROVIDERID FROM APP.IHELP_PERSON WHERE IHELP_PERSON.IHELPID = ?");) {
            statement.setString(1, ihelpID);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                PatientProvider patientProvider = new PatientProvider(resultSet.getString("PATIENTID"), resultSet.getString("PROVIDERID"));
                patientIDs.add(patientProvider);
            }
            resultSet.close();
        }
        
        IhelpPatient ihelpPatient = new IhelpPatient(patientIDs);
        Map<String, String> queries = getAllPrimaryQueries();
        queries.forEach((String table, String query) -> { 
            try {
                JSONObject jSONObject = null;
                
                if(table.equalsIgnoreCase("OBSERVATION")) {
                    jSONObject = processPrimaryQuery(table, query + ihelpPatient.toWhereClause(), 2, true);
                } else if(table.equalsIgnoreCase("MEASUREMENT")) {
                    jSONObject = processPrimaryQuery(table, query + ihelpPatient.toWhereClause(), 2, false);
                } else if(table.equalsIgnoreCase("CONDITION")) {
                    jSONObject = processPrimaryQuery(table, query + ihelpPatient.toWhereClause(), 2, true);
                } else if(table.equalsIgnoreCase("FAMILYMEMBERHISTORY")) {
                    jSONObject = processPrimaryQuery(table, query + ihelpPatient.toWhereClause(), 1, true);
                } 
                
                result.put(jSONObject);
                
            } catch (SQLException ex) {
                Log.error("Error executing statement {}. {}: {}", query + ihelpPatient.toWhereClause(), ex.getClass().getName(), ex.getMessage());
            }
        });
        
        return result;
    }

    @Override
    public List<DataTable> getAllData(String ihelpID) throws SQLException {
        final List<PatientProvider> patientIDs = new ArrayList<>();
        final List<DataTable> result = new ArrayList<>();
        try (Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT PATIENTID, PROVIDERID FROM APP.IHELP_PERSON WHERE IHELP_PERSON.IHELPID = ?");) {
            statement.setString(1, ihelpID);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                PatientProvider patientProvider = new PatientProvider(resultSet.getString("PATIENTID"), resultSet.getString("PROVIDERID"));
                patientIDs.add(patientProvider);
            }
            resultSet.close();
        }
        IhelpPatient ihelpPatient = new IhelpPatient(patientIDs);
        
        Map<String, String> queries = getAllQueries();
        queries.forEach((String table, String query) -> { 
            try {
                List<DataRow> tableResult = processQuery(query + ihelpPatient.toWhereClause());
                result.add(new DataTable(table, tableResult));
            } catch (SQLException ex) {
                Log.error("Error executing statement {}. {}: {}", query + ihelpPatient.toWhereClause(), ex.getClass().getName(), ex.getMessage());
            }
        });
        
        return result;
    }
    
    private Map<String, String> getAllQueries() {
        Map<String, String> queries = new HashMap<>();
        queries.put("ANSWER", "SELECT ANSWERID, PROVIDERID, STATUS, AUTHORED, QUESTIONNAIREID, QUESTIONNAIRENAME, QUESTIONNAIRETITLE, LINKID, QUESTIONID, QUESTION, ANSWERSYSTEM, ANSWER, SCORE, PATIENTID FROM APP.ANSWER ");
        queries.put("CONDITION", "SELECT CONDITIONID, PROVIDERID, ABATEMENTDATETIME, ONSETDATE, CATEGORYSYSTEM, CATEGORYCODE, CATEGORYDISPLAY, CODESYSTEM, CODECODE, CODEDISPLAY, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.\"CONDITION\" ");
        queries.put("ENCOUNTER", "SELECT ENCOUNTERID, PROVIDERID, STATUS, PERIODSTART, PERIODEND, TYPESYSTEM, TYPECODE, TYPEDISPLAY, SERVICEPROVIDERID, PRACTITIONERID, PATIENTID FROM APP.ENCOUNTER ");
        queries.put("FAMILYMEMBERHISTORY", "SELECT FAMILYMEMBERHISTORYID, PROVIDERID, CONDITIONSYSTEM, CONDITIONCODE, CONDITIONDISPLAY, PATIENTID FROM APP.FAMILYMEMBERHISTORY ");
        queries.put("MEASUREMENT", "SELECT MEASUREMENTID, PROVIDERID, STATUS, MEASUREMENTCODE, MEASUREMENTSYSTEM, MEASUREMENTDISPLAY, EFFECTIVEDATETIME, MEASUREMENTCATEGORYCODE, MEASUREMENTCATEGORYSYSTEM, MEASUREMENTCATEGORYDISPLAY, VALUESYSTEM, VALUECODE, VALUEUNIT, VALUEQUANTITY, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.MEASUREMENT ");
        queries.put("MEDICATIONADMINISTRATION", "SELECT MEDICATIONADMINISTRATIONID, PROVIDERID, STATUS, CATEGORYSYSTEM, CATEGORYCODE, CATEGORYDISPLAY, MEDICATIONCODESYSTEM, MEDICATIONCODECODE, MEDICATIONCODEDISPLAY, REASONCODESYSTEM, REASONCODECODE, REASONCODEDISPLAY, CYCLES, SUSPENSIONREASONSYSTEM, SUSPENSIONREASONCODE, SUSPENSIONREASONDISPLAY, SUSPENSIONREASONDAYS, PARTOFID, PARTOFPROCIDER, PATIENTID FROM APP.MEDICATIONADMINISTRATION ");
        //queries.put("MESSAGES", "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES ");
        queries.put("OBSERVATION", "SELECT OBSERVATIONID, PROVIDERID, STATUS, OBSERVATIONCODE, OBSERVATIONSYSTEM, OBSERVATIONDISPLAY, EFFECTIVEDATETIME, OBSERVATIONCATEGORYCODE, OBSERVATIONCATEGORYSYSTEM, OBSERVATIONCATEGORYDISPLAY, VALUESYSTEM, VALUECODE, VALUEUNIT, VALUEQUANTITY, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, VALUESTRING, VALUEBOOLEAN, BODYSITESYSTEM, BODYSITECODE, BODYSITEDISPLAY, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.OBSERVATION ");
        queries.put("PROCEDURE", "SELECT PROCEDUREID, PROVIDERID, STATUS, PROCEDURECODE, PROCEDURESYSTEM, PROCEDUREDISPLAY, PROCEDURECATEGORYCODE, PROCEDURECATEGORYSYSTEM, PROCEDURECATEGORYDISPLAY, PERFORMEDPERIODSTART, PERFORMEDPERIODEND, CYCLES, RADIOTHERAPYSETTINGSYSTEM, RADIOTHERAPYSETTINGCODE, RADIOTHERAPYSETTINGDISPLAY, RADIOTHERAPYTIMINGSYSTEM, RADIOTHERAPYTIMINGCODE, RADIOTHERAPYTIMINGDISPLAY, RADIOTHERAPYTECHNIQUESYSTEM, RADIOTHERAPYTECHNIQUECODE, RADIOTHERAPYTECHNIQUEDISPLAY, RADIOTHERAPYCTV1DOSE, RADIOTHERAPYCTV1FRACTIONATION, RADIOTHERAPYCTV2DOSE, RADIOTHERAPYCTV2FRACTIONATION, RADIOTHERAPYCTV3DOSE, RADIOTHERAPYCTV3FRACTIONATION, SUSPENSIONREASONSYSTEM, SUSPENSIONREASONCODE, SUSPENSIONREASONDISPLAY, SUSPENSIONREASONDAYS, CLOSUREREASONSYSTEM, CLOSUREREASONCODE, CLOSUREREASONDISPLAY, SURGERYAPPROACHSYSTEM, SURGERYAPPROACHCODE, SURGERYAPPROACHDISPLAY, GRADINGSYSTEM, GRADINGCODE, GRADINGDISPLAY, DURATION, PANCREATICFISTULA, BILIARYFISTULA, RESURGERY, PATIENTID FROM APP.\"PROCEDURE\" ");
        
        return queries;
    }
    
    private Map<String, String> getAllPrimaryQueries() {
        Map<String, String> queries = new HashMap<>();
        
        queries.put("OBSERVATION", "SELECT DISTINCT  OBSERVATIONDISPLAY, VALUESTRING FROM APP.OBSERVATION ");
        queries.put("MEASUREMENT", "SELECT DISTINCT MEASUREMENTDISPLAY, VALUEQUANTITY FROM APP.MEASUREMENT ");
        queries.put("CONDITION", "SELECT DISTINCT CATEGORYDISPLAY, CODEDISPLAY FROM APP.\"CONDITION\"");
        queries.put("FAMILYMEMBERHISTORY", "SELECT DISTINCT CONDITIONDISPLAY FROM APP.FAMILYMEMBERHISTORY ");
        
        //queries.put("ANSWER", "SELECT ANSWERID, PROVIDERID, STATUS, AUTHORED, QUESTIONNAIREID, QUESTIONNAIRENAME, QUESTIONNAIRETITLE, LINKID, QUESTIONID, QUESTION, ANSWERSYSTEM, ANSWER, SCORE, PATIENTID FROM APP.ANSWER ");
        //queries.put("CONDITION", "SELECT CONDITIONID, PROVIDERID, ABATEMENTDATETIME, ONSETDATE, CATEGORYSYSTEM, CATEGORYCODE, CATEGORYDISPLAY, CODESYSTEM, CODECODE, CODEDISPLAY, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.\"CONDITION\" ");
        //queries.put("ENCOUNTER", "SELECT ENCOUNTERID, PROVIDERID, STATUS, PERIODSTART, PERIODEND, TYPESYSTEM, TYPECODE, TYPEDISPLAY, SERVICEPROVIDERID, PRACTITIONERID, PATIENTID FROM APP.ENCOUNTER ");
        //queries.put("FAMILYMEMBERHISTORY", "SELECT FAMILYMEMBERHISTORYID, PROVIDERID, CONDITIONSYSTEM, CONDITIONCODE, CONDITIONDISPLAY, PATIENTID FROM APP.FAMILYMEMBERHISTORY ");
        //queries.put("MEASUREMENT", "SELECT MEASUREMENTID, PROVIDERID, STATUS, MEASUREMENTCODE, MEASUREMENTSYSTEM, MEASUREMENTDISPLAY, EFFECTIVEDATETIME, MEASUREMENTCATEGORYCODE, MEASUREMENTCATEGORYSYSTEM, MEASUREMENTCATEGORYDISPLAY, VALUESYSTEM, VALUECODE, VALUEUNIT, VALUEQUANTITY, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.MEASUREMENT ");
        //queries.put("MEDICATIONADMINISTRATION", "SELECT MEDICATIONADMINISTRATIONID, PROVIDERID, STATUS, CATEGORYSYSTEM, CATEGORYCODE, CATEGORYDISPLAY, MEDICATIONCODESYSTEM, MEDICATIONCODECODE, MEDICATIONCODEDISPLAY, REASONCODESYSTEM, REASONCODECODE, REASONCODEDISPLAY, CYCLES, SUSPENSIONREASONSYSTEM, SUSPENSIONREASONCODE, SUSPENSIONREASONDISPLAY, SUSPENSIONREASONDAYS, PARTOFID, PARTOFPROCIDER, PATIENTID FROM APP.MEDICATIONADMINISTRATION ");
        //queries.put("MESSAGES", "SELECT TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID, NOTES, CREATEDBY, CREATIONTIME, SENTTIME, STATUS, TOPICCODE, TOPICSYSTEM, TOPICDISPLAY, NOTIFICATION_MESSAGE, COACHING_ELEMENT, COACHING_DOMAIN, COACHING_INTENT, HEALTH_OUTCOME, MESSAGE_FRAMING, SUBJECT_ABILITY, HEALTH_ORIENTATION FROM APP.MESSAGES ");
        //queries.put("OBSERVATION", "SELECT OBSERVATIONID, PROVIDERID, STATUS, OBSERVATIONCODE, OBSERVATIONSYSTEM, OBSERVATIONDISPLAY, EFFECTIVEDATETIME, OBSERVATIONCATEGORYCODE, OBSERVATIONCATEGORYSYSTEM, OBSERVATIONCATEGORYDISPLAY, VALUESYSTEM, VALUECODE, VALUEUNIT, VALUEQUANTITY, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, VALUESTRING, VALUEBOOLEAN, BODYSITESYSTEM, BODYSITECODE, BODYSITEDISPLAY, ENCOUNTERID, PRACTITIONERID, PATIENTID FROM APP.OBSERVATION ");
        //queries.put("PROCEDURE", "SELECT PROCEDUREID, PROVIDERID, STATUS, PROCEDURECODE, PROCEDURESYSTEM, PROCEDUREDISPLAY, PROCEDURECATEGORYCODE, PROCEDURECATEGORYSYSTEM, PROCEDURECATEGORYDISPLAY, PERFORMEDPERIODSTART, PERFORMEDPERIODEND, CYCLES, RADIOTHERAPYSETTINGSYSTEM, RADIOTHERAPYSETTINGCODE, RADIOTHERAPYSETTINGDISPLAY, RADIOTHERAPYTIMINGSYSTEM, RADIOTHERAPYTIMINGCODE, RADIOTHERAPYTIMINGDISPLAY, RADIOTHERAPYTECHNIQUESYSTEM, RADIOTHERAPYTECHNIQUECODE, RADIOTHERAPYTECHNIQUEDISPLAY, RADIOTHERAPYCTV1DOSE, RADIOTHERAPYCTV1FRACTIONATION, RADIOTHERAPYCTV2DOSE, RADIOTHERAPYCTV2FRACTIONATION, RADIOTHERAPYCTV3DOSE, RADIOTHERAPYCTV3FRACTIONATION, SUSPENSIONREASONSYSTEM, SUSPENSIONREASONCODE, SUSPENSIONREASONDISPLAY, SUSPENSIONREASONDAYS, CLOSUREREASONSYSTEM, CLOSUREREASONCODE, CLOSUREREASONDISPLAY, SURGERYAPPROACHSYSTEM, SURGERYAPPROACHCODE, SURGERYAPPROACHDISPLAY, GRADINGSYSTEM, GRADINGCODE, GRADINGDISPLAY, DURATION, PANCREATICFISTULA, BILIARYFISTULA, RESURGERY, PATIENTID FROM APP.\"PROCEDURE\" ");
        
        return queries;
    }

    

    
    
}
