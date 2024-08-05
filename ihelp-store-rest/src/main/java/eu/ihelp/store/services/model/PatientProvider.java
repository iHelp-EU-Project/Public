package eu.ihelp.store.services.model;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class PatientProvider {
    
    private static final String HEALTHENTIA_VALUE = "HEALTHENTIA";

    private final String patientID;
    private final String providerID;

    public PatientProvider(String patientID, String providerID) {
        this.patientID = patientID;
        this.providerID = providerID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getProviderID() {
        return providerID;
    }
    
    public boolean isHealthentia() {
        return this.providerID.equalsIgnoreCase(HEALTHENTIA_VALUE);
    }

    @Override
    public String toString() {
        return "PatientProvider{" + "patientID=" + patientID + ", providerID=" + providerID + '}';
    }
    
}
