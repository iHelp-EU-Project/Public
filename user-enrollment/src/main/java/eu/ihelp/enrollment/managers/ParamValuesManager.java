package eu.ihelp.enrollment.managers;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.utils.DBAHelper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ParamValuesManager {
    private static final Logger Log = LoggerFactory.getLogger(ParamValuesManager.class);
    
    public static List<Object> executeQuery(Connection connection, String query) throws DataStoreException {
        return executeQuery(connection, query, false);
    }
    
    public static List<Object> executeQuery(Connection connection, String query, boolean getFirstElement) throws DataStoreException {
        Log.info("Will execute: {}", query);
        List<Object> result = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);) {
            
            if(getFirstElement) {
                if(resultSet.next()) {
                    result.add(resultSet.getObject(1));
                }
            } else {
                while(resultSet.next()) {
                    result.add(resultSet.getObject(1));
                }
            }
            
        } catch(SQLException ex) {
            Log.error("Could execute query {}. {}:{}. ", query, ex.getMessage(), ex);
            throw new DataStoreException(ex.getMessage(), ex);
        }
        return result;
    }
}
