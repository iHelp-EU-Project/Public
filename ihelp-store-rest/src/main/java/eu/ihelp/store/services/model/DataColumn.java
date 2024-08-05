package eu.ihelp.store.services.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataColumn implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="column name", required=true)
    private String name;
    @ApiModelProperty(value="column value", required=true)
    private Object value;

    public DataColumn() {
    }

    @JsonCreator
    public DataColumn(
            @JsonProperty("name") String name, 
            @JsonProperty("value") Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    
    
}
