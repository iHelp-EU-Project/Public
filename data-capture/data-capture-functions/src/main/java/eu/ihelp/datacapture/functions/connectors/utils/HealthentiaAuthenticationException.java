package eu.ihelp.datacapture.functions.connectors.utils;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HealthentiaAuthenticationException extends Exception {
    public HealthentiaAuthenticationException(String message) {
        super(message);
    }
    
    public HealthentiaAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
