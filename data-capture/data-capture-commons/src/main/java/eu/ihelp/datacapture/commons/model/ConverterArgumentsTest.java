package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConverterArgumentsTest extends ConverterArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @JsonCreator
    public ConverterArgumentsTest(
            @JsonProperty("datePattern") String datePattern) {
        super(ConverterTypeEnum.TEST, datePattern);
    }

    @Override
    public String toString() {
        return "ConverterArgumentsTest{" + "type=" + type.name() + '}';
    }
}
