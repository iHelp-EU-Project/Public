package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectorArgumentsTest extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @JsonCreator
    public ConnectorArgumentsTest(
            @JsonProperty("datePattern")  String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("nullString")  String nullString) {
        super(ConnectorTypeEnum.TEST, datePattern, timePattern, nullString);
    }

    @Override
    public String toString() {
        return "ConnectorArgumentsTest{" + "type=" + type.name() + '}';
    }
    
}
