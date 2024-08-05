package eu.ihelp.data.harmonizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ShutDownHook extends Thread {
    private static final Logger Log = LoggerFactory.getLogger(ShutDownHook.class);
    private final HarmonizerMain harmonizerMain;
    
    
    public ShutDownHook(HarmonizerMain harmonizerMain) {
        this.harmonizerMain = harmonizerMain;
    }
    
    @Override
    public void run() {
        Log.info("Shutdown hook is triggered");      
        this.harmonizerMain.close();
    }
}