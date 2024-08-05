package eu.ihelp.store.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String iHelpID;
    private final String patientID;
    private final String providerID;
    private final String gender;
    private final Boolean active;
    private final Date birthDate;

    protected Patient(String iHelpID, String patientID, String providerID, String gender, Boolean active, Date birthDate) {
        this.iHelpID = iHelpID;
        this.patientID = patientID;
        this.providerID = providerID;
        this.gender = gender;
        this.active = active;
        this.birthDate = birthDate;
    }

    public String getiHelpID() {
        return iHelpID;
    }
    
    public String getPatientID() {
        return patientID;
    }

    public String getProviderID() {
        return providerID;
    }

    public String getGender() {
        return gender;
    }

    public Boolean isActive() {
        return active;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Patient{" + "iHelpID=" + iHelpID + ", patientID=" + patientID + ", providerID=" + providerID + ", gender=" + gender + ", active=" + active + ", birthDate=" + birthDate + '}';
    }
}
