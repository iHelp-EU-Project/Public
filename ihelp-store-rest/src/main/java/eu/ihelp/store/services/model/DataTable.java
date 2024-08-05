package eu.ihelp.store.services.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataTable implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the name of the table", required=true)
    private String table;
    @ApiModelProperty(value="the rows of the table", required=true)
    private List<DataRow> row;

    public DataTable() {
    }

    @JsonCreator
    public DataTable(
            @JsonProperty("table") String table, 
            @JsonProperty("row") List<DataRow> row) {
        this.table = table;
        this.row = row;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<DataRow> getRow() {
        return row;
    }

    public void setRow(List<DataRow> row) {
        this.row = row;
    }
    
    
    
    
}
