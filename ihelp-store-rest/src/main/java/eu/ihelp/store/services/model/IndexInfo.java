package eu.ihelp.store.services.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="index name", required=true)
    private String name;
    @ApiModelProperty(value="index is unique", required=true)
    private boolean unique;
    @ApiModelProperty(value="column type", required=true)
    private List<ColumnInfo> fields;

    public IndexInfo() {
    }

    @JsonCreator
    public IndexInfo(
            @JsonProperty("name") String name, 
            @JsonProperty("unique") boolean unique, 
            @JsonProperty("fields") List<ColumnInfo> fields) {
        this.name = name;
        this.unique = unique;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public List<ColumnInfo> getFields() {
        return fields;
    }

    public void setFields(List<ColumnInfo> fields) {
        this.fields = fields;
    }
    
    
    
}
