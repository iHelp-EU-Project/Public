package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataStoreException extends Exception {
    
    public DataStoreException(String message) {
        super(message);
    }
    
    public DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
