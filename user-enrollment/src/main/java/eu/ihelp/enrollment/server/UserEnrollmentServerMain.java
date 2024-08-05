package eu.ihelp.enrollment.server;

import eu.ihelp.enrollment.managers.DBSchemaManager;
import eu.ihelp.enrollment.utils.IhelpServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class UserEnrollmentServerMain {
    private static final Logger Log = LoggerFactory.getLogger(UserEnrollmentServerMain.class);
    private final UserEnrollmentServerHandler tomcatServerHandler;
    private static String webAppBase = null;
 
    
    public static void main(String [] args) {
        Log.info("Starting the ihelp store rest server");
        if(args.length>0) {
            webAppBase = args[0];
        }
        UserEnrollmentServerMain.getInstance();
    }
    
    private UserEnrollmentServerMain(UserEnrollmentServerHandler tomcatServerHandler) {
        this.tomcatServerHandler = tomcatServerHandler;
    }
    
    
    private static final class IhelpStoreServerMainHandler {
        private static final UserEnrollmentServerMain INSTANCE = buildInstance();
        
        static UserEnrollmentServerMain buildInstance() {
            UserEnrollmentServerMain serverMain = null;
            try {
                System.out.println("Process runs on java " + System.getProperty("java.version"));
                
                DBSchemaManager.initDB();
                
                UserEnrollmentServerHandler tomcatServerHandler = new UserEnrollmentServerHandler(IhelpServerProperties.getInstance().getTomcatPort(), UserEnrollmentServerMain.webAppBase);
                serverMain = new UserEnrollmentServerMain(tomcatServerHandler);
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
    
    public static UserEnrollmentServerMain getInstance() {
        return IhelpStoreServerMainHandler.INSTANCE;
    }
    
    public void stop() {
        this.tomcatServerHandler.shutdown();
    }
}
