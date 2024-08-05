package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MassiveUserEnrollmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<MassiveUserEnrollmentPatientDTO> patients;

    public MassiveUserEnrollmentDTO() {
    }

    @JsonCreator
    public MassiveUserEnrollmentDTO(
            @JsonProperty("patients") List<MassiveUserEnrollmentPatientDTO> patients) {
        this.patients = patients;
    }

    public List<MassiveUserEnrollmentPatientDTO> getPatients() {
        return patients;
    }

    public void setPatients(List<MassiveUserEnrollmentPatientDTO> patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return "MassiveUserEnrollmentDTO{" + "patients=" + patients + '}';
    }
    
}
