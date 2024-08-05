package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IhelpPatientNotFoundException extends Exception {
    private final String id;
    
    public IhelpPatientNotFoundException(String id) {
        super("Ihelp id " + id + " does not exist");
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
}
