package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConverterArgumentsPrint extends ConverterArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @JsonCreator
    public ConverterArgumentsPrint(
            @JsonProperty("datePattern") String datePattern) {
        super(ConverterTypeEnum.PRINT, datePattern);
    }

    @Override
    public String toString() {
        return "ConverterArgumentsTest{" + "type=" + type.name() + '}';
    }

    
}
