package eu.ihelp.enrollment.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IhelpServerProperties {
    private static final Logger Log = LoggerFactory.getLogger(IhelpServerProperties.class);
    private static final String PROPERTIES_FILE = "tomcat.properties";
    
    private static final String TOMCAT_KEYSTORE_PATH = "TOMCAT_KEYSTORE_PATH";
    private static final String TOMCAT_HTTPS = "tomcat.https";
    private static final String DATETIME_FORMAT = "datetime.format";
    private static final String DATETIME_FORMAT_MESSAGES = "datetime.format.messages";
    private static final String DATETIME_FORMAT_USERENROLLMENT = "datetime.format.userenrollment";
    private static final String DATA_CAPTURE_URL = "datacapture_url";
    private static final String DATA_CAPTURE_PORT = "datacapture_port";
    
    private final int tomcatPort;
    private final boolean tomcatHttps;
    private final String dateTimeFormat;
    private final String dateTimeFormatMessages;
    private final String dateTimeFormatUserEnrollment;
    private final String tomcatKeyStorePath;
    private final String dataCaptureUrl;
    private final String dataCapturePort;

    private IhelpServerProperties(int tomcatPort, String dateTimeFormat, String dateTimeFormatMessages, String dateTimeFormatUserEnrollment, String tomcatKeyStorePath, boolean tomcatHttps, String dataCaptureUrl, String dataCapturePort) {
        this.tomcatPort = tomcatPort;
        this.dateTimeFormat = dateTimeFormat;
        this.dateTimeFormatMessages = dateTimeFormatMessages;
        this.dateTimeFormatUserEnrollment = dateTimeFormatUserEnrollment;
        this.tomcatKeyStorePath = tomcatKeyStorePath;
        this.tomcatHttps = tomcatHttps;
        this.dataCaptureUrl = dataCaptureUrl;
        this.dataCapturePort = dataCapturePort;
    }
    
    private static final class TomcatServerPropertiesHolder {
        private static final IhelpServerProperties INSTANCE = buildInstance();
        
        static IhelpServerProperties buildInstance() {
            int tomcatport = -1;
            boolean _tomcatHttps = false;
            String _dateTimeFormat = null;
            String _dateTimeFormatMessages = null;
            String _dateTimeFormatUserEnrollment = null;
            String _tomcatKeyStorePath = null;
            String _dataCaptureUrl = null;
            String _dataCapturePort = null;
            
            try {
                Properties properties = new Properties();
                ClassLoader classLoader = IhelpServerProperties.class.getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
                properties.load(inStream);
                
                _dateTimeFormat = getProperty(properties, DATETIME_FORMAT, "yyyy-MM-dd'T'HH:mm:ss.SSS");
                _dateTimeFormatMessages = getProperty(properties, DATETIME_FORMAT_MESSAGES, "yyyy-MM-dd'T'HH:mm:ssXXX");
                _dateTimeFormatUserEnrollment = getProperty(properties, DATETIME_FORMAT_USERENROLLMENT, "yyyy-MM-dd");
                _tomcatKeyStorePath = getProperty(properties, TOMCAT_KEYSTORE_PATH, "/home/pavlos/leanxcale/Projects/iHelp/user-enrollment/keystore.jks");
                _dataCaptureUrl = getProperty(properties, DATA_CAPTURE_URL, "localhost");
                _dataCapturePort = getProperty(properties, DATA_CAPTURE_PORT, "54735");
                
                Log.info("{} property has value: {}", DATA_CAPTURE_URL, _dataCaptureUrl);
                Log.info("{} property has value: {}", DATA_CAPTURE_PORT, _dataCapturePort);
                
                
                String tomcatPortStr = getTomcatPort(properties);
                tomcatport = Integer.valueOf(tomcatPortStr);     
                
                String _tomcatHttpsStr = getProperty(properties, TOMCAT_HTTPS, "false");
                _tomcatHttps = Boolean.valueOf(_tomcatHttpsStr);
            } catch(IOException ex) {
                Log.error("Could not load ingestion properties file. {}: {}", ex.getClass().getName(), ex.getMessage());
            }
            
            IhelpServerProperties tomcatServerProperties = new IhelpServerProperties(tomcatport, _dateTimeFormat, _dateTimeFormatMessages, _dateTimeFormatUserEnrollment, _tomcatKeyStorePath, _tomcatHttps, _dataCaptureUrl, _dataCapturePort);
            return tomcatServerProperties;
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
        
        static String getProperty(Properties properties, String property) {
            String value = System.getenv(property);
            if((value!=null) && (!value.isEmpty())) {
                return value;
            }
            
            value = System.getProperty(property);
            if((value!=null) && (!value.isEmpty())) {
                return value;
            }
            
            return properties.getProperty(property);
        }
        
        static String getProperty(Properties properties, String property, String defaultValue) {
            String value = System.getenv(property);
            if((value!=null) && (!value.isEmpty())) {
                return value;
            }
            
            value = System.getProperty(property);
            if((value!=null) && (!value.isEmpty())) {
                return value;
            }
            
            return properties.getProperty(property, defaultValue);
        }
    }
    
    public static IhelpServerProperties getInstance() {
        return TomcatServerPropertiesHolder.INSTANCE;
    }

    public int getTomcatPort() {
        return tomcatPort;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public String getDateTimeFormatMessages() {
        return dateTimeFormatMessages;
    }
    
    public String getDateTimeFormatUserEnrollment() {
        return dateTimeFormatUserEnrollment;
    }

    public String getTomcatKeyStorePath() { 
        return tomcatKeyStorePath;
    }

    public boolean isTomcatHttps() {
        return tomcatHttps;
    }

    public String getDataCaptureUrl() {
        return dataCaptureUrl;
    }

    public String getDataCapturePort() {
        return dataCapturePort;
    }
    
    
}
