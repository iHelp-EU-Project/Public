package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectorArgumentsData extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the path of the file to import", required=true)
    private final List<String> data;
    
    @ApiModelProperty(value="the delimiter character", required=true)
    private final String delimiter;
    
    @ApiModelProperty(value="indicates whether or not we must skip the first line", required=true)
    private final Boolean skipFirst;
    
    @JsonCreator
    public ConnectorArgumentsData(
            @JsonProperty("data") List<String> data,
            @JsonProperty("datePattern")  String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("delimiter")  String delimiter,
            @JsonProperty("nullString")  String nullString,
            @JsonProperty("skipFirst")  Boolean skipFirst) {
        super(ConnectorTypeEnum.FILE, datePattern, timePattern, nullString);
        this.data = data;
        this.delimiter = delimiter;
        this.skipFirst = skipFirst;
    }

    public List<String> getData() {
        return data;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public Boolean getSkipFirst() {
        return skipFirst;
    }
    
    
    @Override
    public String toString() {
        return "ConnectorArgumentsData{" + "type=" + type.name() + ";data=" + data.size() + ";skipFirst=" + skipFirst + "}";
    }
}
