package eu.ihelp.datacapture.functions;

import java.io.Serializable;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterOutput implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected final String datasourceID;
    protected final String datasetID;
    protected final Schema schema;
    protected final Schema schemaKey;
    protected final JSONObject confParameters;
    protected final Object [][] values;

    public ConverterOutput(String datasourceID, String datasetID, Schema schema, Schema schemaKey, JSONObject confParameters, Object[][] values) {
        this.datasourceID = datasourceID;
        this.datasetID = datasetID;
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.confParameters = confParameters;
        this.values = values;
    }

    public String getDatasourceID() {
        return datasourceID;
    }

    public String getDatasetID() {
        return datasetID;
    }
    
    public Object getSchema() {
        return new JSONObject(((Schema) schema).toString()).toMap();
    }

    public Object getSchemaKey() {
        return new JSONObject(((Schema) schemaKey).toString()).toMap();
    }

    public Object getConfParameters() {
        return confParameters.toMap();
    }

    public Object[][] getValues() {
        return values;
    }
    
}
