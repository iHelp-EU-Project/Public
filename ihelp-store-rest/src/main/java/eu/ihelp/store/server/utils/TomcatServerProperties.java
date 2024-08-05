package eu.ihelp.store.server.utils;

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
    private final String baseURL;
    private final String tomcatKeyStorePath;
    private final boolean tomcatHttps;

    private TomcatServerProperties(int tomcatPort, String baseURL, String tomcatKeyStorePath, boolean tomcatHttps) {
        this.tomcatPort = tomcatPort;
        this.baseURL = baseURL;
        this.tomcatKeyStorePath = tomcatKeyStorePath;
        this.tomcatHttps = tomcatHttps;
    }
    
    private static final class TomcatServerPropertiesHolder {
        private static final TomcatServerProperties INSTANCE = buildInstance();
        
        static TomcatServerProperties buildInstance() {
            int tomcatport = -1;
            String baseURL = null;
            String _tomcatKeyStorePath = null;
            boolean _tomcatHttps = false;
            try {
                Properties properties = new Properties();
                ClassLoader classLoader = TomcatServerProperties.class.getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
                properties.load(inStream);
                
                String _tomcatHttpsString = getTomcatHttps(properties);
                
                try {
                    _tomcatHttps = Boolean.valueOf(_tomcatHttpsString);
                } catch (Exception ex) {
                    System.err.println("Invalid input: " + _tomcatHttpsString);
                    _tomcatHttps = false;
                }
                
                baseURL = getBaseURLt(properties);
                String tomcatPortStr = getTomcatPort(properties);
                tomcatport = Integer.valueOf(tomcatPortStr);            
                _tomcatKeyStorePath = getKeystorePath(properties);
            } catch(IOException ex) {
                Log.error("Could not load ingestion properties file. {}: {}", ex.getClass().getName(), ex.getMessage());
            }
            
            TomcatServerProperties tomcatServerProperties = new TomcatServerProperties(tomcatport, baseURL, _tomcatKeyStorePath, _tomcatHttps);
            return tomcatServerProperties;
        }
        
        static String getBaseURLt(Properties properties) {
            String tomcatPortStr = System.getenv("base.url");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            tomcatPortStr = System.getProperty("base.url");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            return properties.getProperty("base.url", "http://147.102.230.182:30005/ihelp/");
        }
        
        static String getKeystorePath(Properties properties) {
            String property = System.getenv("TOMCAT_KEYSTORE_PATH");
            if((property!=null) && (!property.isEmpty())) {
                return property;
            }
            
            property = System.getProperty("TOMCAT_KEYSTORE_PATH");
            if((property!=null) && (!property.isEmpty())) {
                return property;
            }
            
            return properties.getProperty("TOMCAT_KEYSTORE_PATH", "/home/pavlos/leanxcale/Projects/iHelp/ihelp-store-rest/keystore.jks");
        }
        
        static String getTomcatHttps(Properties properties) {
            String tomcatPortStr = System.getenv("tomcat.https");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            tomcatPortStr = System.getProperty("tomcat.https");
            if((tomcatPortStr!=null) && (!tomcatPortStr.isEmpty())) {
                return tomcatPortStr;
            }
            
            return properties.getProperty("tomcat.https", "false");
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
    }
    
    public static TomcatServerProperties getInstance() {
        return TomcatServerPropertiesHolder.INSTANCE;
    }

    public int getTomcatPort() {
        return tomcatPort;
    }

    public String getTomcatKeyStorePath() {
        return tomcatKeyStorePath;
    }
    
    public String getBaseURL() {
        if(baseURL.endsWith("/")) {
            return baseURL;
        } else {
            return baseURL + "/";
        }
    }

    public boolean isTomcatHttps() {
        return tomcatHttps;
    }
    
}
