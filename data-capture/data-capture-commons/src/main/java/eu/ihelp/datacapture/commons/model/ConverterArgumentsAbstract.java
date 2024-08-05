package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConverterArgumentsGateway.class, name="GATEWAY"),
    @JsonSubTypes.Type(value = ConverterArgumentsKafka.class, name="KAFKA"),
    @JsonSubTypes.Type(value = ConverterArgumentsKafkaAvro.class, name="KAFKA_AVRO"),
    @JsonSubTypes.Type(value = ConverterArgumentsPrint.class, name="PRINT"),
    @JsonSubTypes.Type(value = ConverterArgumentsTest.class, name="TEST")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConverterArgumentsAbstract implements Serializable {
    protected final ConverterTypeEnum type;
    
    @ApiModelProperty(value="the date time patternt for the output", required=false)
    protected final String datePattern;

    protected ConverterArgumentsAbstract(ConverterTypeEnum type, String datePattern) {
        this.type = type;
        this.datePattern = datePattern;
    }

    @JsonIgnore
    public ConverterTypeEnum getType() {
        return type;
    }

    public String getDatePattern() {
        return datePattern;
    }
}
