package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HealthentiaPatientExistsException extends Exception {
    private final String ihelpID;
    private final String healthentiaID;

    public HealthentiaPatientExistsException(String ihelpID, String healthentiaID) {
        super("patient " + ihelpID + " is already linked with a healthentia account: " + healthentiaID);
        this.ihelpID = ihelpID;
        this.healthentiaID = healthentiaID;
    }

    public String getIhelpID() {
        return ihelpID;
    }

    public String getHealthentiaID() {
        return healthentiaID;
    }
    
    
}
