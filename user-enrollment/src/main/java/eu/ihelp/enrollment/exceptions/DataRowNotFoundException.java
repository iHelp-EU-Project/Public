package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataRowNotFoundException extends Exception {
    public DataRowNotFoundException(String tableName) {
        super("Could not found row in table '" + tableName + "'");
    }
    
    public DataRowNotFoundException(String tableName, Object id) {
        super("Could not found id: " + id + " for table '" + tableName + "'");
    }
}
