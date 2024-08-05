package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MassiveUserEnrollmentPatientDTO extends PatientDTO {
    
    @ApiModelProperty(value="healthentiaID", required=false)
    private String healthentiaID;

    public MassiveUserEnrollmentPatientDTO() {
    }

    @JsonCreator
    public MassiveUserEnrollmentPatientDTO(
            @JsonProperty("healthentiaID") String healthentiaID, 
            @JsonProperty("patientID") String patientID, 
            @JsonProperty("providerID") String providerID, 
            @JsonProperty("active") Boolean active, 
            @JsonProperty("birthdate") String birthdate, 
            @JsonProperty("gender") String gender) {
        super(patientID, providerID, active, birthdate, gender);
        this.healthentiaID = healthentiaID;
    }

    public String getHealthentiaID() {
        return healthentiaID;
    }

    public void setHealthentiaID(String healthentiaID) {
        this.healthentiaID = healthentiaID;
    }

    @Override
    public String toString() {
        return "MassiveUserEnrollmentPatientDTO{" + "healthentiaID=" + healthentiaID + ", patientID=" + patientID + ", providerID=" + providerID + '}';
    }
}
