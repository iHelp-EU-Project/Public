package eu.ihelp.data.importer.logging;

import eu.ihelp.data.importer.utils.Consts;
import org.joda.time.LocalDate;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class LoggingMessage {
    protected final boolean lastBatch;
    protected final int startRecord;
    protected final int endRecord;
    protected final String jobID;
    protected final String dataCaptureUrl;

    protected LoggingMessage(boolean lastBatch, int startRecord, int endRecord, String jobID, String dataCaptureUrl) {
        this.lastBatch = lastBatch;
        this.startRecord = startRecord;
        this.endRecord = endRecord;
        this.jobID = jobID;
        this.dataCaptureUrl = dataCaptureUrl;
    }

    public String getJobID() {
        return jobID;
    }

    boolean isLastBatch() {
        return lastBatch;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public int getEndRecord() {
        return endRecord;
    }

    public String getDataCaptureUrl() {
        return dataCaptureUrl;
    }
    
    public String createMessage() {
        JSONObject jsonInObject = new JSONObject();
        jsonInObject.put("jobID", this.jobID);
        jsonInObject.put("functionName", Consts.FUNCTION_NAME);
        jsonInObject.put("status", "INFO");
        jsonInObject.put("timestamp", LocalDate.now().toString());
        if(isLastBatch()) {
            jsonInObject.put("message", "last batch has been ingested for batch " + startRecord + "-" + endRecord);
        } else {
            jsonInObject.put("message", "Ingested batch " + startRecord + "-" + endRecord);
        }
        
        return jsonInObject.toString();
    }

    @Override
    public String toString() {
        return "LoggingMessage{" + "lastBatch=" + lastBatch + ", startRecord=" + startRecord + ", endRecord=" + endRecord + ", jobID=" + jobID + ", dataCaptureUrl=" + dataCaptureUrl + '}';
    }
    
    
} 
