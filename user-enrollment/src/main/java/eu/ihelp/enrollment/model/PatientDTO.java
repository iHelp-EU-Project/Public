package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDTO implements Serializable {
    private static final long serialVersionUID = 1L;
        
    @ApiModelProperty(value="patientID", required=true)
    protected String patientID;    
    @ApiModelProperty(value="providerID", required=true)
    protected String providerID;
    @ApiModelProperty(value="active", required=false)
    protected Boolean active;
    @ApiModelProperty(value="birthdate", required=false)
    protected String birthdate;
    @ApiModelProperty(value="gender", required=false)
    protected String gender;

    public PatientDTO() {
    }

    @JsonCreator
    public PatientDTO(
            @JsonProperty("patientID") String patientID, 
            @JsonProperty("providerID") String providerID, 
            @JsonProperty("active") Boolean active, 
            @JsonProperty("birthdate") String birthdate, 
            @JsonProperty("gender") String gender) {
        this.patientID = patientID;
        this.providerID = providerID;
        this.active = active;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.patientID);
        hash = 83 * hash + Objects.hashCode(this.providerID);
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
        final PatientDTO other = (PatientDTO) obj;
        if (!Objects.equals(this.patientID, other.patientID)) {
            return false;
        }
        return Objects.equals(this.providerID, other.providerID);
    }

    @Override
    public String toString() {
        return "PatientDTO{" + "patientID=" + patientID + ", providerID=" + providerID + '}';
    }
    
}
