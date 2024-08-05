package eu.ihelp.datacapture.rest.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ShutDownHook extends Thread {
    private static final Logger Log = LoggerFactory.getLogger(ShutDownHook.class);
    private final DataCaptureMain dataCaptureMain;
    
    public ShutDownHook(DataCaptureMain dataCaptureMain) {
        this.dataCaptureMain = dataCaptureMain;
    }
    
    @Override
    public void run() {
        Log.info("Shutdown hook is triggered");      
        this.dataCaptureMain.stop();
    }
}
