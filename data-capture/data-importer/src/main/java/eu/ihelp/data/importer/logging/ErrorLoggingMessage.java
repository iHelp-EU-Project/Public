package eu.ihelp.data.importer.logging;

import eu.ihelp.data.importer.utils.Consts;
import org.joda.time.LocalDate;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ErrorLoggingMessage extends LoggingMessage {
    protected final String status;
    protected final String message;

    public ErrorLoggingMessage(boolean lastBatch, int startRecord, int endRecord, String jobID, String dataCaptureUrl, String status, String message) {
        super(lastBatch, startRecord, endRecord, jobID, dataCaptureUrl);
        this.status = status;
        this.message = message;
    }
    
    
    @Override
    public String createMessage() {
        JSONObject jsonInObject = new JSONObject();
        jsonInObject.put("jobID", this.jobID);
        jsonInObject.put("functionName", Consts.FUNCTION_NAME);
        jsonInObject.put("status", status);
        jsonInObject.put("timestamp", LocalDate.now().toString());
        jsonInObject.put("message", message);        
        return jsonInObject.toString();
    }

    @Override
    public String toString() {
        return "ErrorLoggingMessage{" + "status=" + status + ", message=" + message + ", jobID=" + jobID + ", dataCaptureUrl=" + dataCaptureUrl + '}';
    }

    
    
}
