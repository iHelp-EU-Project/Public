package eu.ihelp.enrollment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MUPRimaryData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="patient_id", required=true)
    private final Integer patient_id;
    @ApiModelProperty(value="gender", required=true)
    private final Integer gender;
    @ApiModelProperty(value="age", required=true)
    private final Integer age;
    @ApiModelProperty(value="indexes", required=true)
    private final List<Integer> indexes;

    @JsonCreator
    public MUPRimaryData(
            @JsonProperty("patient_id") Integer patient_id, 
            @JsonProperty("gender") Integer gender, 
            @JsonProperty("age") Integer age, 
            @JsonProperty("indexes") List<Integer> indexes) {
        this.patient_id = patient_id;
        this.gender = gender;
        this.age = age;
        this.indexes = indexes;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public Integer getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.patient_id);
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
        final MUPRimaryData other = (MUPRimaryData) obj;
        return Objects.equals(this.patient_id, other.patient_id);
    }

    

    @Override
    public String toString() {
        return "MUPRimaryData{" + "patient_id=" + patient_id + ", gender=" + gender + ", age=" + age + ", indexes=" + indexes + '}';
    }
    
    
    
}
