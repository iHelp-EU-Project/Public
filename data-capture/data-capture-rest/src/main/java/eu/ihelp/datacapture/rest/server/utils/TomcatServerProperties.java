package eu.ihelp.datacapture.rest.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TomcatServerProperties {
    private static final Logger Log = LoggerFactory.getLogger(TomcatServerProperties.class);
    private static final String PROPERTIES_FILE = "tomcat.properties";
    
    private final int tomcatPort;
    private final String keyStorePath;

    private TomcatServerProperties(int tomcatPort, String keyStorePath) {
        this.tomcatPort = tomcatPort;
         this.keyStorePath = keyStorePath;
    }
    
    private static final class TomcatServerPropertiesHolder {
        private static final TomcatServerProperties INSTANCE = buildInstance();
        
        static TomcatServerProperties buildInstance() {
            int tomcatport = -1;
            String _keyStorePath = null;
            
            try {
                Properties properties = new Properties();
                ClassLoader classLoader = TomcatServerProperties.class.getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
                properties.load(inStream);
                
                String tomcatPortStr = getTomcatPort(properties);
                tomcatport = Integer.valueOf(tomcatPortStr);         
                _keyStorePath = getKeyStοrePath(properties);
            } catch(IOException ex) {
                Log.error("Could not load ingestion properties file. {}: {}", ex.getClass().getName(), ex.getMessage());
            }
            
            TomcatServerProperties adsDeployWP4Properties = new TomcatServerProperties(tomcatport, _keyStorePath);
            return adsDeployWP4Properties;
        }
        
        static String getTomcatPort(Properties properties) {
            String tomcatPortStr = System.getenv("tomcat.port");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            tomcatPortStr = System.getProperty("tomcat.port");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            return properties.getProperty("tomcat.port", "54735");
        }
        
        static String getKeyStοrePath(Properties properties) {
            String tomcatPortStr = System.getenv("TOMCAT_KEYSTORE_PATH");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            tomcatPortStr = System.getProperty("TOMCAT_KEYSTORE_PATH");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            return properties.getProperty("TOMCAT_KEYSTORE_PATH", "/home/pavlos/leanxcale/Projects/iHelp/data-capture/keystore.jks");
        }
    }
    
    public static TomcatServerProperties getInstance() {
        return TomcatServerPropertiesHolder.INSTANCE;
    }

    public int getTomcatPort() {
        return tomcatPort;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }
}
