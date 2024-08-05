package eu.ihelp.datacapture.rest.server;

import eu.ihelp.datacapture.rest.server.utils.TomcatServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureMain {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureMain.class);
    private final TomcatServerHandler tomcatServerHandler;
    private static String webAppBase = null;
 
    
    public static void main(String [] args) {
        Log.info("Starting the Data Capture server");
        if(args.length>0) {
            webAppBase = args[0];
        }
        DataCaptureMain.getInstance();
    }
    
    private DataCaptureMain(TomcatServerHandler tomcatServerHandler) {
        this.tomcatServerHandler = tomcatServerHandler;
    }
    
    
    private static final class DataCaptureMainHandler {
        private static final DataCaptureMain INSTANCE = buildInstance();
        
        static DataCaptureMain buildInstance() {
            DataCaptureMain serverMain = null;
            try {
                System.out.println("Process runs on java " + System.getProperty("java.version"));
                
                TomcatServerHandler tomcatServerHandler = new TomcatServerHandler(TomcatServerProperties.getInstance().getTomcatPort(), DataCaptureMain.webAppBase);
                serverMain = new DataCaptureMain(tomcatServerHandler);
                tomcatServerHandler.start();
                
                Runtime runtime = Runtime.getRuntime();
                runtime.addShutdownHook(new ShutDownHook(serverMain));
            } catch(Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
            
            return serverMain;
        }
    }
    
    public static DataCaptureMain getInstance() {
        return DataCaptureMainHandler.INSTANCE;
    }
    
    public void stop() {
        this.tomcatServerHandler.shutdown();
    }
}
