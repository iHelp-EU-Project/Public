package eu.ihelp.data.importer.logging;

import eu.ihelp.data.importer.utils.MessageToKafkaProducer;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class LoggingMessageBuilder {
    
    public static LoggingMessage createLoggingMessage(MessageToKafkaProducer input) {
        LoggingMessage loggingMessage = new LoggingMessage(input.isLastBatch(), input.getStartRecord(), input.getEndRecord(), input.getJobID(), input.getDataCaptureUrl());
        return loggingMessage;
    }
    
    public static LoggingMessage createErrorLoggingMessage(int startRecord, int endRecord, String jobID, String dataCapturelogginURL,  String status, String message) {
        ErrorLoggingMessage loggingMessage = new ErrorLoggingMessage(false, startRecord, endRecord, jobID, dataCapturelogginURL, status, message);
        return loggingMessage;
    }
}
