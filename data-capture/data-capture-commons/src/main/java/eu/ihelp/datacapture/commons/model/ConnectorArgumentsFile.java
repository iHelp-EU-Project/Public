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
public class ConnectorArgumentsFile extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the path of the file to import", required=true)
    private final String path;
    
    @ApiModelProperty(value="the delimiter character", required=true)
    private final String delimiter;
    
    @ApiModelProperty(value="indicates whether or not we must skip the first line", required=true)
    private final Boolean skipFirst;
    
    @JsonCreator
    public ConnectorArgumentsFile(
            @JsonProperty("path") String path,
            @JsonProperty("datePattern")  String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("delimiter")  String delimiter,
            @JsonProperty("nullString")  String nullString,
            @JsonProperty("skipFirst")  Boolean skipFirst) {
        super(ConnectorTypeEnum.FILE, datePattern, timePattern, nullString);
        this.path = path;
        this.delimiter = delimiter;
        this.skipFirst = skipFirst;
    }

    public String getPath() {
        return path;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public Boolean getSkipFirst() {
        return skipFirst;
    }
    
    
    @Override
    public String toString() {
        return "ConnectorArgumentsFile{" + "type=" + type.name() + ";path=" + path + ";skipFirst=" + skipFirst + "}";
    }
}
