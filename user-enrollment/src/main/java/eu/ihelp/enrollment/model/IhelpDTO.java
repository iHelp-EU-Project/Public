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
public class IhelpDTO implements Serializable {
    private static final long serialVersionUID = 1L;
        
    @ApiModelProperty(value="iHelpID", required=true)
    private String iHelpID; 
    @ApiModelProperty(value="patientID", required=true)
    private String patientID;    
    @ApiModelProperty(value="providerID", required=true)
    private String providerID;

    public IhelpDTO() {
    }

    @JsonCreator
    public IhelpDTO(
            @JsonProperty("iHelpID") String iHelpID, 
            @JsonProperty("patientID") String patientID, 
            @JsonProperty("providerID") String providerID) {
        this.iHelpID = iHelpID;
        this.patientID = patientID;
        this.providerID = providerID;
    }

    public String getiHelpID() {
        return iHelpID;
    }

    public void setiHelpID(String iHelpID) {
        this.iHelpID = iHelpID;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.iHelpID);
        hash = 97 * hash + Objects.hashCode(this.patientID);
        hash = 97 * hash + Objects.hashCode(this.providerID);
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
        final IhelpDTO other = (IhelpDTO) obj;
        if (!Objects.equals(this.iHelpID, other.iHelpID)) {
            return false;
        }
        if (!Objects.equals(this.patientID, other.patientID)) {
            return false;
        }
        return Objects.equals(this.providerID, other.providerID);
    }

    @Override
    public String toString() {
        return "IhelpDTO{" + "iHelpID=" + iHelpID + ", patientID=" + patientID + ", providerID=" + providerID + '}';
    }
    
    
}
