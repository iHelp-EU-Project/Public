package eu.ihelp.store.server.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super();
    }
    
    public InvalidInputException(String message) {
        super(message);
    }
    
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
