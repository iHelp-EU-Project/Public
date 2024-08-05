package eu.ihelp.data.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ShutDownHook extends Thread {
    private static final Logger Log = LoggerFactory.getLogger(ShutDownHook.class);
    private final DataImporterMain main;
    
    
    public ShutDownHook(DataImporterMain main) {
        this.main = main;
    }
    
    @Override
    public void run() {
        Log.info("Shutdown hook is triggered");      
        this.main.close();
    }
}