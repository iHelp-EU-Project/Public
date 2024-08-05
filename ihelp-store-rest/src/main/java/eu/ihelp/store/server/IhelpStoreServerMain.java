package eu.ihelp.store.server;

import eu.ihelp.store.server.utils.TomcatServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IhelpStoreServerMain {
    private static final Logger Log = LoggerFactory.getLogger(IhelpStoreServerMain.class);
    private final IhelpStoreServerHandler tomcatServerHandler;
    private static String webAppBase = null;
 
    
    public static void main(String [] args) {
        Log.info("Starting the ihelp store rest server");
        if(args.length>0) {
            webAppBase = args[0];
        }
        IhelpStoreServerMain.getInstance();
    }
    
    private IhelpStoreServerMain(IhelpStoreServerHandler tomcatServerHandler) {
        this.tomcatServerHandler = tomcatServerHandler;
    }
    
    
    private static final class IhelpStoreServerMainHandler {
        private static final IhelpStoreServerMain INSTANCE = buildInstance();
        
        static IhelpStoreServerMain buildInstance() {
            IhelpStoreServerMain serverMain = null;
            try {
                System.out.println("Process runs on java " + System.getProperty("java.version"));
                
                
                IhelpStoreServerHandler tomcatServerHandler = new IhelpStoreServerHandler(TomcatServerProperties.getInstance().getTomcatPort(), IhelpStoreServerMain.webAppBase);
                serverMain = new IhelpStoreServerMain(tomcatServerHandler);
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
    
    public static IhelpStoreServerMain getInstance() {
        return IhelpStoreServerMainHandler.INSTANCE;
    }
    
    public void stop() {
        this.tomcatServerHandler.shutdown();
    }
}
