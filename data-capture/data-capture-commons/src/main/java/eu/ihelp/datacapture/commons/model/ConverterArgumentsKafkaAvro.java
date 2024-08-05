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
public class ConverterArgumentsKafkaAvro extends ConverterArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the url of the kafka to send the data", required=true)
    private final String url;
    @ApiModelProperty(value="the port of the kafka to send the data", required=true)
    private final String port;
    @ApiModelProperty(value="the port of the Avro Schema Registry to send the data", required=true)
    private final String avroPort;
    @ApiModelProperty(value="the topic name of the kafka queue to send the data", required=true)
    private final String topic;
    
    @JsonCreator
    public ConverterArgumentsKafkaAvro(
            @JsonProperty("url") String url,
            @JsonProperty("port") String port,
            @JsonProperty("avroPort") String avroPort,
            @JsonProperty("topic") String topic,
            @JsonProperty("datePattern") String datePattern) {
        super(ConverterTypeEnum.KAFKA_AVRO, datePattern);
        this.url = url;
        this.port = port;
        this.avroPort = avroPort;
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public String getPort() {
        return port;
    }

    public String getAvroPort() {
        return avroPort;
    }
    
    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "ConverterArgumentsKafkaAvro{" + "url=" + url + ", port=" + port + ", avroPort=" + avroPort + ", topic=" + topic + '}';
    }

}
