package eu.ihelp.data.importer.schemas;

import org.apache.avro.Schema;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface HHRSchemas {
    
    public Schema getSchemaKey();
    
    public Schema getSchemaValue();
}
