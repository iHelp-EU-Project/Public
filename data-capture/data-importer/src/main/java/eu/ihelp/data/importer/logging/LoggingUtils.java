package eu.ihelp.data.importer.logging;

import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class LoggingUtils {
    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(LoggingUtils.class);
    protected final ArrayBlockingQueue<LoggingMessage> queue;

    public LoggingUtils(ArrayBlockingQueue<LoggingMessage> queue) {
        this.queue = queue;
    }
    
    
    private static final class LoggingUtilsHolder {
        private static final LoggingUtils INSTANCE = new LoggingUtils(new ArrayBlockingQueue<>(200));
    }
    
    public static LoggingUtils getInstance() {
        return LoggingUtilsHolder.INSTANCE;
    }

    public ArrayBlockingQueue<LoggingMessage> getQueue() {
        return queue;
    }
    
    public void addMessageToQueue(LoggingMessage loggingMessage) {
        try {
            this.queue.put(loggingMessage);
        } catch (InterruptedException ex) {
            Log.warn("{}: Could not send log message '{}'", ex.getClass().getName(), loggingMessage);
        }
        
    }
}
