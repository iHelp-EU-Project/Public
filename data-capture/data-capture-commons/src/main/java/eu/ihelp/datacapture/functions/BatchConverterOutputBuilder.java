package eu.ihelp.datacapture.functions;

import eu.ihelp.datacapture.commons.utils.DataCapturePropertiesUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class BatchConverterOutputBuilder {
    
    private final String jobID;
    private final String datasourceID;
    private final String datasetID;
    private final Schema schema;
    private final Schema schemaKey;
    private final JSONObject confParameters;
    private final List<Object []> values;
    private final int batchFrom;
    private final int batchSize;
    

    private BatchConverterOutputBuilder(String datasourceID, String datasetID, Schema schema, Schema schemaKey, JSONObject confParameters, int batchFrom, int batchSize, String jobID) {
        this.jobID = jobID;
        this.datasourceID = datasourceID;
        this.datasetID = datasetID;
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.confParameters = confParameters;
        this.batchFrom = batchFrom;
        this.batchSize = batchSize;
        this.values = new ArrayList<>(batchSize);
    }
    
    public static BatchConverterOutputBuilder newInstance(String datasourceID, String datasetID, Schema schema, Schema schemaKey, JSONObject confParameters, int batchFrom, int batchSize, String jobID) {
        return new BatchConverterOutputBuilder(datasourceID, datasetID, schema, schemaKey, confParameters, batchFrom, batchSize, jobID);
    }
    
    public synchronized void addRow(DataRowItem row) {
        this.values.add(row.getRow());
    }
    
    public synchronized boolean isEmpty() {
        return this.values.isEmpty();
    }
    
    public synchronized BatchConverterOutput build() {
        Object [][] data = new Object[values.size()][];
        data = this.values.toArray(data);
        String loggingURL = "http://" + DataCapturePropertiesUtil.getInstance().getGatewayURL() + ":" + DataCapturePropertiesUtil.getInstance().getGatewayPort() + "/ihelp/datacapture/logs/";
        return new BatchConverterOutput(jobID, batchSize, batchFrom, batchFrom + this.values.size() -1, false, loggingURL, datasourceID, datasetID, schema, schemaKey, confParameters, data);
    }
}
