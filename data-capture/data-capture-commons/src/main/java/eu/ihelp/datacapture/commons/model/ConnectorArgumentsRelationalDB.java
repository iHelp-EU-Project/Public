package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectorArgumentsRelationalDB extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the connection url of the database to import", required=true)
    private final String connectionURL;
    
    @JsonCreator
    public ConnectorArgumentsRelationalDB(
            @JsonProperty("connectionURL") String connectionURL,
            @JsonProperty("datePattern")  String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("nullString")  String nullString) {
        super(ConnectorTypeEnum.RELATIONAL_DB, datePattern, timePattern, nullString);
        this.connectionURL = connectionURL;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    
    @Override
    public String toString() {
        return "ConnectorArgumentsRelationalDB{" + "type=" + type.name() + ";connectionURL=" + connectionURL + '}';
    }
}
