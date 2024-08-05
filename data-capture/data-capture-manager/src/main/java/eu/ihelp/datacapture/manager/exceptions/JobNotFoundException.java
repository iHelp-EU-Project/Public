package eu.ihelp.datacapture.manager.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JobNotFoundException extends Exception {
    public JobNotFoundException() {
        super();
    }
    
    public JobNotFoundException(String id) {
        super("Cannot find job with id: " + id);
    }
}
