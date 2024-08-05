package eu.ihelp.datacapture.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCapturePropertiesUtil {
    private static final Logger Log = LoggerFactory.getLogger(DataCapturePropertiesUtil.class);
    private static final String PROPERTIES_FILE = "datacapture.properties";
    private static final String PROP_GATEWAY_URL = "DATA_GATEWAY_IP";
    private static final String PROP_GATEWAY_PORT = "DATA_GATEWAY_PORT";
    
    private final String gatewayURL;
    private final int gatewayPort;

    private DataCapturePropertiesUtil(String gatewayURL, int gatewayPort) {
        this.gatewayURL = gatewayURL;
        this.gatewayPort = gatewayPort;
    }
    
    
    private static final class DataCapturePropertiesUtilHOLDER {
        private static final DataCapturePropertiesUtil INSTANCE = buildInstance();
        
        static DataCapturePropertiesUtil buildInstance() {
            String gatewayURL = null;
            int gatewayPort = -1;
            
            
            try {
                Properties properties = new Properties();
                ClassLoader classLoader = DataCapturePropertiesUtil.class.getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
                properties.load(inStream);
                gatewayURL = getProperty(properties, PROP_GATEWAY_URL);
                gatewayPort = Integer.valueOf(getProperty(properties, PROP_GATEWAY_PORT));
            } catch(IOException ex) {
                Log.error("Could not load ingestion properties file. {}: {}", ex.getClass().getName(), ex.getMessage());
            }
            
            return new DataCapturePropertiesUtil(gatewayURL, gatewayPort);
        }
        
        static String getProperty(Properties properties, String propertyName) {
            System.out.println("Will get property for: " + propertyName);
            String propertyValue = System.getenv(propertyName);
            System.out.println("env property " + propertyName + " is " + propertyValue);
            if((propertyValue!=null) && (!propertyValue.isEmpty())) {
                return propertyValue;
            }
            
            propertyValue = System.getProperty(propertyName);
            if((propertyValue!=null) && (!propertyValue.isEmpty())) {
                return propertyValue;
            }
            
            return properties.getProperty(propertyName);
        }
    }
    
    public static DataCapturePropertiesUtil getInstance() {
        return DataCapturePropertiesUtilHOLDER.INSTANCE;
    }

    public String getGatewayURL() {
        return gatewayURL;
    }

    public int getGatewayPort() {
        return gatewayPort;
    }
    
}
