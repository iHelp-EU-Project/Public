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
public class InsertPatientDTO extends PatientDTO {
    
    @ApiModelProperty(value="iHelpID", required=true)
    private String iHelpID; 

    public InsertPatientDTO() {
    }

    @JsonCreator
    public InsertPatientDTO(
            @JsonProperty("iHelpID") String iHelpID, 
            @JsonProperty("patientID") String patientID, 
            @JsonProperty("providerID") String providerID, 
            @JsonProperty("active") Boolean active, 
            @JsonProperty("birthdate") String birthdate, 
            @JsonProperty("gender") String gender) {
        super(patientID, providerID, active, birthdate, gender);
        this.iHelpID = iHelpID;
    }

    public String getiHelpID() {
        return iHelpID;
    }

    public void setiHelpID(String iHelpID) {
        this.iHelpID = iHelpID;
    }
    
    

    @Override
    public String toString() {
        return "InsertPatientDTO{" + "iHelpID=" + iHelpID + '}';
    }
    
    
}
