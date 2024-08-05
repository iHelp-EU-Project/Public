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
public class ConnectorArgumentsRest extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the url of the REST service to import", required=true)
    private final String url;
    
    @JsonCreator
    public ConnectorArgumentsRest(
            @JsonProperty("connectionURL") String url,
            @JsonProperty("datePattern")  String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("nullString")  String nullString) {
        super(ConnectorTypeEnum.REST, datePattern, timePattern, nullString);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    
    @Override
    public String toString() {
        return "ConnectorArgumentsRest{" + "type=" + type.name() + ";url=" + url + '}';
    }
}
