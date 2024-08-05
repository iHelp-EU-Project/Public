package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConnectorArgumentsKafka extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the url of the kafka to get the data", required=true)
    private final String url;
    @ApiModelProperty(value="the topic name of the kafka queue to get the data", required=true)
    private final String topic;
    
    @JsonCreator
    public ConnectorArgumentsKafka(
            @JsonProperty("url") String url,
            @JsonProperty("topic") String topic,
            @JsonProperty("datePattern") String datePattern,
            @JsonProperty("timePattern")  String timePattern) {
        super(ConnectorTypeEnum.KAFKA, datePattern, timePattern, null);
        this.url = url;
        this.topic = topic;
    }
    
    public String getUrl() {
        return url;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "ConnectorArgumentsKafka{" + "type=" + type.name() +  "url=" + url + ", topic=" + topic + '}';
    }

}
