package eu.ihelp.datacapture.commons.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureInitException extends Exception {
    
    public DataCaptureInitException(String message) {
        super(message);
    }
    
    public DataCaptureInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
