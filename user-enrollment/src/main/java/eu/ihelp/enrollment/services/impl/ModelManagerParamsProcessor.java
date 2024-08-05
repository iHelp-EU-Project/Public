package eu.ihelp.enrollment.services.impl;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.managers.ParamValuesManager;
import eu.ihelp.enrollment.model.PatientDTO;
import eu.ihelp.enrollment.utils.DBAHelper;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ModelManagerParamsProcessor implements Closeable {
    
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    
    private static final String COLUMN_NAME_PATIENTID = "PATIENTID";
    private static final String COLUMN_NAME_PROVIDERID = "PROVIDERID";
    
    private static final HashMap<String, ParamsMapping> mappings = generateMapping();
    
    static HashMap<String, ParamsMapping> generateMapping() {
        HashMap<String, ParamsMapping> _mapping = new HashMap<>();
        _mapping.put("symptoms", new ParamsMapping("symptoms", "\"CONDITION\"", "CODEDISPLAY"));
        _mapping.put("comorbidities", new ParamsMapping("comorbidities", "\"CONDITION\"", "CODEDISPLAY"));
        _mapping.put("morbidity history", new ParamsMapping("morbidity history", "\"CONDITION\"", "CODEDISPLAY"));
        _mapping.put("family history", new ParamsMapping("family history", "\"FAMILYMEMBERHISTORY\"", "CONDITIONDISPLAY"));
        _mapping.put("negative habits", new ParamsMapping("negative habits", "\"CONDITION\"", "CODEDISPLAY"));
        _mapping.put("physical finding", new ParamsMapping("physical finding", "\"CONDITION\"", "CODEDISPLAY"));
        _mapping.put("Laboratory tests", new ParamsMapping("Laboratory tests", "\"OBSERVATION\"", "OBSERVATIONDISPLAY"));
        _mapping.put("imaging diagnostics", new ParamsMapping("imaging diagnostics", "\"OBSERVATION\"", "OBSERVATIONDISPLAY"));
        _mapping.put("genotyping", new ParamsMapping("genotyping", "\"OBSERVATION\"", "OBSERVATIONDISPLAY"));
        
        
        _mapping.put("271245006", new ParamsMapping("271245006", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("1005671000000105", new ParamsMapping("1005671000000105", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("89659001", new ParamsMapping("89659001", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("313849004", new ParamsMapping("909301000000101", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("908961000000105", new ParamsMapping("908961000000105", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("313849004", new ParamsMapping("313849004", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("1010611000000107", new ParamsMapping("1010611000000107", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("166832000", new ParamsMapping("166832000", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("395065005", new ParamsMapping("395065005", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("271216009", new ParamsMapping("271216009", "\"MEASUREMENT\"", "VALUEQUANTITY", "MEASUREMENTCODE", "EFFECTIVEDATETIME"));
        _mapping.put("277267003", new ParamsMapping("SELECT YEAR(BIRTHDATE) FROM PATIENT ", true));
        _mapping.put("397669002", new ParamsMapping("SELECT YEAR(CURRENT_DATE) - YEAR(BIRTHDATE) FROM PATIENT  ", true));
        _mapping.put("263495000", new ParamsMapping("SELECT GENDER FROM PATIENT ", true));
        _mapping.put("184100006", new ParamsMapping("SELECT CASE GENDER WHEN 'female' THEN 0  WHEN 'male' THEN 1 ELSE NULL END FROM PATIENT ", true));
        _mapping.put("39953003", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODECODE = '66562002' AND "));
        _mapping.put("29001004", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODECODE = '34' AND    "));
        _mapping.put("53041004", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODEDISPLAY  LIKE '%alcohol%' AND    "));
        _mapping.put("414916001", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODEDISPLAY  LIKE '%besity%' AND    "));
        _mapping.put("73211009", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODECODE  = '73211009' AND    "));
        _mapping.put("84698008", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODECODE  = '84698008' AND    "));
        _mapping.put("181277001", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODECODE  = '181277001' AND    "));
        _mapping.put("80774000", new ParamsMapping("SELECT CASE WHEN count(*) > 0 THEN 1 ELSE 0 END FROM \"CONDITION\" WHERE CODECODE  = '80774000' AND    "));
        
        
        return _mapping;
    }
    
    private final Connection connection;
    
    private final List<PatientDTO> patients;
    private final HashMap<String, List<Object>> dbValues;

    private ModelManagerParamsProcessor(List<PatientDTO> patients, HashMap<String, List<Object>> dbValues, Connection connection) {
        this.patients = patients;
        this.dbValues = dbValues;
        this.connection = connection;
    }

    public static ModelManagerParamsProcessor newInstance(List<PatientDTO> patients) throws SQLException {
        return new ModelManagerParamsProcessor(patients, new HashMap<>(), DBAHelper.getInstance().getConnection());
    }
    
    public String getGender() {
        return patients.get(0).getGender();
    }
    
    public String getBirthDate() {
        return patients.get(0).getBirthdate();
    }
    
    public HashMap<String, ParamsMapping> getMappings() {
        return ModelManagerParamsProcessor.mappings;
    }
    
    
    public List<Object> getValuesForCode(String name) throws DataStoreException {
        ParamsMapping mapping = mappings.get(name);
        
        if(mapping.isExecuteDefinedQuery()) {
            if(mapping.queryStatement.contains( " WHERE ")) {
                return ParamValuesManager.executeQuery(connection, mapping.queryStatement + getWhereClause(ADD_WHERE_KEYWORD.NO), mapping.isReturnFirstElement());
            } else {
                return ParamValuesManager.executeQuery(connection, mapping.queryStatement + getWhereClause(ADD_WHERE_KEYWORD.YES), mapping.isReturnFirstElement());
            }
        }
        
        else if(mapping.isGetValueForSNOMED()) {
            String query = buildSQLQuery(mapping);
            return ParamValuesManager.executeQuery(connection, query);
            
        } else {
            if(this.dbValues.containsKey(mapping.tableName+mapping.tableColumn)) {
                return this.dbValues.get(mapping.tableName+mapping.tableColumn);
            } else {
                 List<Object> list = ParamValuesManager.executeQuery(this.connection, buildSQLQuery(mapping));
                 this.dbValues.put(mapping.tableName+mapping.tableColumn, list);
                 return list;
            }
        }
    }
    
    private String buildSQLQuery(ParamsMapping mapping) {
        if(mapping.isGetValueForSNOMED()) {
            return buildSQLQuerySNOMED_Value(mapping);
        } else {
            return buildSQLQuerySNOMED_Exists(mapping);
        }
    }
    
    
    private String buildSQLQuerySNOMED_Exists(ParamsMapping mapping) {
        return "SELECT DISTINCT " + mapping.tableColumn + " FROM " + mapping.tableName + getWhereClause(ADD_WHERE_KEYWORD.YES);
                
    }
    
    private String buildSQLQuerySNOMED_Value(ParamsMapping mapping) {
        return "SELECT " + mapping.tableColumn + " FROM " + mapping.tableName + getWhereClause(ADD_WHERE_KEYWORD.YES)
        + " AND " + mapping.tableColumnFilter + " = '" + mapping.getSnomed() + "' " 
        + " ORDER BY " + mapping.tableColumnDate + " DESC "
        + " LIMIT 1";        
    }
    
    private enum ADD_WHERE_KEYWORD {
        YES, NO
    }
    
    private String getWhereClause(ADD_WHERE_KEYWORD add_where_keyword) {
        StringBuilder sb = new StringBuilder("  ");
        if(add_where_keyword==ADD_WHERE_KEYWORD.YES) {
            sb.append(" WHERE ");
        }
        sb.append("( ");
        sb.append("(" + COLUMN_NAME_PATIENTID + " = '").append(this.patients.get(0).getPatientID()).append("' ");
        sb.append(" AND " + COLUMN_NAME_PROVIDERID + " ='").append(this.patients.get(0).getProviderID()).append("')");
        for(int i=1; i<this.patients.size(); i++) {
            sb.append(" OR (" + COLUMN_NAME_PATIENTID + " = '").append(this.patients.get(i).getPatientID()).append("' ");
            sb.append(" AND " + COLUMN_NAME_PROVIDERID + " ='").append(this.patients.get(i).getProviderID()).append("')");
        }
        sb.append(" ) ");
        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            //DO NOTHING
        }
    }
    
    public static class ParamsMapping {
        final String snomed;
        final String tableName;
        final String tableColumn;
        final String tableColumnFilter;
        final String tableColumnDate;
        final String queryStatement;
        final boolean returnFirstElement;

        public ParamsMapping(String snomed, String tableName, String tableColumn) {
            this.snomed = snomed;
            this.tableName = tableName;
            this.tableColumn = tableColumn;
            this.tableColumnFilter = null;
            this.tableColumnDate = null;
            this.queryStatement = null;
            this.returnFirstElement = false;
        }

        public ParamsMapping(String snomed, String tableName, String tableColumn, String tableColumnFilter, String tableColumnDate) {
            this.snomed = snomed;
            this.tableName = tableName;
            this.tableColumn = tableColumn;
            this.tableColumnFilter = tableColumnFilter;
            this.tableColumnDate = tableColumnDate;
            this.queryStatement = null;
            this.returnFirstElement = false;
        }
        
        public ParamsMapping(String queryStatement) {
            this.snomed = null;
            this.tableName = null;
            this.tableColumn = null;
            this.tableColumnFilter = null;
            this.tableColumnDate = null;
            this.queryStatement = queryStatement;
            this.returnFirstElement = false;
        }
        
        public ParamsMapping(String queryStatement, boolean returnFirstElement) {
            this.snomed = null;
            this.tableName = null;
            this.tableColumn = null;
            this.tableColumnFilter = null;
            this.tableColumnDate = null;
            this.queryStatement = queryStatement;
            this.returnFirstElement = returnFirstElement;
        }
        
        
        public String getSnomed() {
            return snomed;
        }

        public String getTableName() {
            return tableName;
        }

        public String getTableColumn() {
            return tableColumn;
        }

        public String getTableColumnFilter() {
            return tableColumnFilter;
        }

        public String getTableColumnDate() {
            return tableColumnDate;
        }

        public String getQueryStatement() {
            return queryStatement;
        }
        
        //deceides wheteher to check if a snomed code exists (returns false) or we have to get the value for that code (returns true)
        public boolean isGetValueForSNOMED() {
            return this.queryStatement==null && this.tableColumnFilter!=null && this.tableColumnDate!=null ;
        }
        
        
        public boolean isExecuteDefinedQuery() {
            return this.queryStatement!=null;
        }

        public boolean isReturnFirstElement() {
            return returnFirstElement;
        }
        
        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + Objects.hashCode(this.tableName);
            hash = 53 * hash + Objects.hashCode(this.tableColumn);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ParamsMapping other = (ParamsMapping) obj;
            if (!Objects.equals(this.tableName, other.tableName)) {
                return false;
            }
            return Objects.equals(this.tableColumn, other.tableColumn);
        }
    }
}
