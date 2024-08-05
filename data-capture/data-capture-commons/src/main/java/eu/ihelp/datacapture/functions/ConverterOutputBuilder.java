package eu.ihelp.datacapture.functions;

import java.util.ArrayList;
import java.util.List;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterOutputBuilder {
    
    private final String datasourceID;
    private final String datasetID;
    private final Schema schema;
    private final Schema schemaKey;
    private final JSONObject confParameters;
    private final List<Object []> values;

    
    private ConverterOutputBuilder(String datasourceID, String datasetID, Schema schema, Schema schemaKey, JSONObject confParameters, int batch) {
        this.datasourceID = datasourceID;
        this.datasetID = datasetID;
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.confParameters = confParameters;
        this.values = new ArrayList<>(batch);
    }
    
    public static ConverterOutputBuilder newInstance(String datasourceID, String datasetID, Schema schema, Schema schemaKey, JSONObject confParameters, int batch) {
        return new ConverterOutputBuilder(datasourceID, datasetID, schema, schemaKey, confParameters, batch);
    }
    
    public synchronized void addRow(DataRowItem row) {
        this.values.add(row.getRow());
    }
    
    public synchronized boolean isEmpty() {
        return this.values.isEmpty();
    }
    
    public synchronized ConverterOutput build() {
        Object [][] data = new Object[values.size()][];
        data = this.values.toArray(data);
        return new ConverterOutput(datasourceID, datasetID, schema, schemaKey, confParameters, data);
    }
}
