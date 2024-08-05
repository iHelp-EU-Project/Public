package eu.ihelp.store.server.exceptions;

/**
 *
 * @author Pavlos Kranas (LXS)
 */
public class ImporterException extends Exception {
    
    public ImporterException(String message) {
        super(message);
    }
    
    public ImporterException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
