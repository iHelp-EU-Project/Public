package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JsonSerializationException extends Exception {
    
    public JsonSerializationException(String message) {
        super(message);
    }
    
    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
