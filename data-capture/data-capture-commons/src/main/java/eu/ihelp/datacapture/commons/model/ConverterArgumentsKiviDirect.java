package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConverterArgumentsKiviDirect extends ConverterArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @JsonCreator
    public ConverterArgumentsKiviDirect(
            @JsonProperty("datePattern") String datePattern) {
        super(ConverterTypeEnum.PRINT, datePattern);
    }

    @Override
    public String toString() {
        return "ConverterArgumentsKiviDirect{" + "type=" + type.name() + '}';
    }
    
}
