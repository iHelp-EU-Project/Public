package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class PatientNotFoundException extends Exception {
    private final String id;
    private final String providerID;
    
    public PatientNotFoundException(String id, String providerID) {
        super("Patient '" + providerID + "/" + id + "' does not exist");
        this.id = id;
        this.providerID = providerID;
    }

    public String getId() {
        return id;
    }

    public String getProviderID() {
        return providerID;
    }
}
