package eu.ihelp.datacapture.commons.utils;

import java.util.concurrent.ThreadFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureThreadFactory implements ThreadFactory {
    
    private final int threadPriority;
    private final boolean isDeamon;
    private int counter = 0;
    private String prefix = "";
    
    public DataCaptureThreadFactory(String prefix) {
        this.prefix = prefix;
        this.threadPriority = Thread.NORM_PRIORITY;
        this.isDeamon = false;
    }
    
    public DataCaptureThreadFactory(String prefix, int threadPriority) {
        this.prefix = prefix;
        this.threadPriority = threadPriority;
        this.isDeamon = false;
    }
    
    public DataCaptureThreadFactory(String prefix, int threadPriority, boolean isDeamon) {
        this.prefix = prefix;
        this.threadPriority = threadPriority;
        this.isDeamon = isDeamon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, prefix + "-" + counter++);
        thread.setDaemon(isDeamon);
        thread.setPriority(threadPriority);
        
        return thread;
    }
    
}
