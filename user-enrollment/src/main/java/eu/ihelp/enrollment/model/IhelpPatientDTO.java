package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IhelpPatientDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="iHelpID", required=true)
    private String iHelpID; 
    @ApiModelProperty(value="patients", required=true)
    private List<PatientDTO> patients;

    public IhelpPatientDTO() {
        this.patients = new ArrayList<>();
    }
    
    public IhelpPatientDTO(String id) {
        this.iHelpID = id;
        this.patients = new ArrayList<>();
    }

    @JsonCreator
    public IhelpPatientDTO(
            @JsonProperty("iHelpID") String iHelpID, 
            @JsonProperty("patients") List<PatientDTO> patients) {
        this.iHelpID = iHelpID;
        this.patients = patients;
    }

    public String getiHelpID() {
        return iHelpID;
    }

    public void setiHelpID(String iHelpID) {
        this.iHelpID = iHelpID;
    }

    public List<PatientDTO> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientDTO> patients) {
        this.patients = patients;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.iHelpID);
        hash = 59 * hash + Objects.hashCode(this.patients);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IhelpPatientDTO other = (IhelpPatientDTO) obj;
        if (!Objects.equals(this.iHelpID, other.iHelpID)) {
            return false;
        }
        return Objects.equals(this.patients, other.patients);
    }

    @Override
    public String toString() {
        return "IhelpPatientDTO{" + "iHelpID=" + iHelpID + ", patients=" + patients + '}';
    }
    
    
}
