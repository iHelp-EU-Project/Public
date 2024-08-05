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
public class ConnectorArgumentsFTP extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the path of the file to import", required=true)
    private final String path;
    @ApiModelProperty(value="indicates whether or not we must skip the first line", required=true)
    private final Boolean skipFirst;
    
    @JsonCreator
    public ConnectorArgumentsFTP(
            @JsonProperty("path") String path,
            @JsonProperty("datePattern")  String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("nullString")  String nullString,
            @JsonProperty("skipFirst")  Boolean skipFirst) {
        super(ConnectorTypeEnum.FTP, datePattern, timePattern, nullString);
        this.path = path;
        this.skipFirst = skipFirst;
    }

    public String getPath() {
        return path;
    }

    public Boolean getSkipFirst() {
        return skipFirst;
    }
    
    @Override
    public String toString() {
        return "ConnectorArgumentsFTP{" + "type=" + type.name() + ";path=" + path + ";skipFirst=" + skipFirst + "}";
    }
    
}
