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
public class ConverterArgumentsGateway extends ConverterArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the url of the gateway to send the data", required=true)
    private final String url;
    
    @JsonCreator
    public ConverterArgumentsGateway(
            @JsonProperty("url") String url,
            @JsonProperty("datePattern") String datePattern) {
        super(ConverterTypeEnum.GATEWAY, datePattern);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ConverterArgumentsGateway{" + "type=" + type.name() + ";url=" + url + '}';
    }
    
}
