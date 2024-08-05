package eu.ihelp.data.importer.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.TimeZone;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public abstract class HHRParserAbstractUtils {
    protected final FhirContext ctx = FhirContext.forR4();
    protected final IParser parser = ctx.newJsonParser();
    protected final TimeZone TIME_ZONE = TimeZone.getDefault();
    
    protected final String STRUCTURE_DEFINITION = "https://fhir.ihelp-project.eu/StructureDefinition/";
            
    public abstract HHRRecordToImport parseValues(String dataSourceID, JSONArray values);
    
    public abstract HHRRecordToImport.KeyValueRecord parseResource(String dataSourceID, Resource resource);
    
    public abstract String getTopic();
}
