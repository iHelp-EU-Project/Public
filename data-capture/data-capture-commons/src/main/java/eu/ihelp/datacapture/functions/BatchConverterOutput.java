package eu.ihelp.datacapture.functions;

import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class BatchConverterOutput extends ConverterOutput {
    
    private final String jobID;
    private final int batchSize;
    private final int currentBatchStart;
    private final int currentBatchEnd;
    private final boolean lastBatch;
    private final String loggingURL;

    public BatchConverterOutput(String jobID, int batchSize, int currentBatchStart, int currentBatchEnd, boolean lastBatch, String loggingURL, String datasourceID, String datasetID, Schema schema, Schema schemaKey, JSONObject confParameters, Object[][] values) {
        super(datasourceID, datasetID, schema, schemaKey, confParameters, values);
        this.jobID = jobID;
        this.batchSize = batchSize;
        this.currentBatchStart = currentBatchStart;
        this.currentBatchEnd = currentBatchEnd;
        this.lastBatch = lastBatch;
        this.loggingURL = loggingURL;
    }

    public String getJobID() {
        return jobID;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getCurrentBatchStart() {
        return currentBatchStart;
    }

    public int getCurrentBatchEnd() {
        return currentBatchEnd;
    }

    public boolean isLastBatch() {
        return lastBatch;
    }

    public String getLoggingURL() {
        return loggingURL;
    }
    
    public BatchConverterOutput toLastBatchMessage() {
        return new BatchConverterOutput(jobID, batchSize, currentBatchStart, currentBatchEnd, true, loggingURL, datasourceID, datasetID, schema, schemaKey, confParameters, values);
    }
}
