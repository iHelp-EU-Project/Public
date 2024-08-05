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
    @JsonSubTypes.Type(value = ConnectorArgumentsRest.class, name="REST"),
    @JsonSubTypes.Type(value = ConnectorArgumentsRelationalDB.class, name="RELATIONAL_DB"),
    @JsonSubTypes.Type(value = ConnectorArgumentsFTP.class, name="FTP"),
    @JsonSubTypes.Type(value = ConnectorArgumentsFile.class, name="FILE"),
    @JsonSubTypes.Type(value = ConnectorArgumentsData.class, name="DATA"),
    @JsonSubTypes.Type(value = ConnectorArgumentsKafka.class, name="KAFKA"),
    @JsonSubTypes.Type(value = ConnectorArgumentsHealthentia.class, name="HEALTHENTIA"),
    @JsonSubTypes.Type(value = ConnectorArgumentsHealthentiaPersons.class, name="HEALTHENTIA_PERSONS"),
    @JsonSubTypes.Type(value = ConnectorArgumentsTest.class, name="TEST")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ConnectorArgumentsAbstract implements Serializable {
    protected final ConnectorTypeEnum type;
    
    @ApiModelProperty(value="the datetime pattern for the input", required=false)
    protected final String datePattern;
    @ApiModelProperty(value="the time pattern for the input", required=false)
    protected final String timePattern;
    @ApiModelProperty(value="this string will indicate if the value is null or not", required=false)
    protected final String nullString;
    
    
    protected ConnectorArgumentsAbstract(ConnectorTypeEnum type, String datePattern, String timePattern, String nullString) {
        this.type = type;
        this.datePattern = datePattern;
        this.timePattern = timePattern;
        this.nullString = nullString;
    }

    @JsonIgnore
    public ConnectorTypeEnum getType() {
        return type;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public String getTimePattern() {
        return timePattern;
    }
    
    public String getNullString() {
        return nullString;
    }
    
    
}
