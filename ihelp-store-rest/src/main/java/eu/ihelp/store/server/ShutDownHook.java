package eu.ihelp.store.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ShutDownHook extends Thread {
    private static final Logger Log = LoggerFactory.getLogger(ShutDownHook.class);
    private final IhelpStoreServerMain serverMain;
    
    public ShutDownHook(IhelpStoreServerMain serverMain) {
        this.serverMain = serverMain;
    }
    
    @Override
    public void run() {
        Log.info("Shutdown hook is triggered");      
        this.serverMain.stop();
    }
    
}
