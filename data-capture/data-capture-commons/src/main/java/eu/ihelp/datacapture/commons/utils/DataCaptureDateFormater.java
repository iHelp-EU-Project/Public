package eu.ihelp.datacapture.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureDateFormater {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureDateFormater.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    public static String getStringFromDate(Date date) {
        if(date!=null) {
            return simpleDateFormat.format(date);
        } else {
            return null;
        }
    }
    
    public static Date getDateFromString(String value) {
        if(value!=null) {
            try {
                return simpleDateFormat.parse(value);
            } catch(ParseException ex) {
                Log.error("Unparasable date: {}. {}: {}", value, ex.getClass().getName(), ex.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
}
