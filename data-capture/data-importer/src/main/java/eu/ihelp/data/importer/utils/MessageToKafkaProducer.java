package eu.ihelp.data.importer.utils;

import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MessageToKafkaProducer {
    
    private final List<HHRRecordToImport> hHRRecordToImportList;
    private final boolean lastBatch;
    private final String dataCaptureUrl;
    private final int startRecord;
    private final int endRecord;
    private final String jobID;

    public MessageToKafkaProducer(List<HHRRecordToImport> hHRRecordToImportList, boolean lastBatch, String dataCaptureUrl, int startRecord, int endRecord, String jobID) {
        this.hHRRecordToImportList = hHRRecordToImportList;
        this.lastBatch = lastBatch;
        this.dataCaptureUrl = dataCaptureUrl;
        this.startRecord = startRecord;
        this.endRecord = endRecord;
        this.jobID = jobID;
    }

    public List<HHRRecordToImport> gethHRRecordToImportList() {
        return hHRRecordToImportList;
    }

    public boolean isLastBatch() {
        return lastBatch;
    }

    public String getDataCaptureUrl() {
        return dataCaptureUrl;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public int getEndRecord() {
        return endRecord;
    }

    public String getJobID() {
        return jobID;
    }
}
