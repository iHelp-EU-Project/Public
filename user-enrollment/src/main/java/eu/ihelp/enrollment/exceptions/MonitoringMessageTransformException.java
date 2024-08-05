package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MonitoringMessageTransformException extends Exception {
    public MonitoringMessageTransformException(String message) {
        super(message);
    }
    
    public MonitoringMessageTransformException(String message, Throwable cause) {
        super(message, cause);
    }
}
