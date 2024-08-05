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
public class DataRow implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the colums of the row", required=true)
    private List<DataColumn> row;

    public DataRow() {
    }

    @JsonCreator
    public DataRow(
            @JsonProperty("row") List<DataColumn> row) {
        this.row = row;
    }

    public List<DataColumn> getRow() {
        return row;
    }

    public void setRow(List<DataColumn> row) {
        this.row = row;
    }
    
}
