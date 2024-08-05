package eu.ihelp.datacapture.rest.server.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TomcatGenericException extends Exception {
    
    public TomcatGenericException(String message) {
        super(message);
    }
    
    public TomcatGenericException(Throwable cause) {
        super(cause);
    }
    
    public TomcatGenericException(String message, Throwable cause) {
        super(message, cause);
    }
} 
