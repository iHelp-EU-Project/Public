package eu.ihelp.enrollment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IhelpDateFormatter {
    private static final Logger Log = LoggerFactory.getLogger(IhelpDateFormatter.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(IhelpServerProperties.getInstance().getDateTimeFormat());
    private static final SimpleDateFormat simpleDateFormatMessages = new SimpleDateFormat(IhelpServerProperties.getInstance().getDateTimeFormatMessages());
    private static final SimpleDateFormat simpleDateFormatUserEnrollment = new SimpleDateFormat(IhelpServerProperties.getInstance().getDateTimeFormatUserEnrollment());
    
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
                try {
                    return simpleDateFormatUserEnrollment.parse(value);
                } catch(ParseException ex1) {
                    Log.error("Unparasable date: {}. {}: {}", value, ex1.getClass().getName(), ex1.getMessage());
                    return null;
                }
            }
        } else {
            return null;
        }
    }
    
    public static Date getDateFromStringForMessages(String value) {
        if(value!=null) {
            try {
                return simpleDateFormatMessages.parse(value);
            } catch(ParseException ex) {
                Log.error("Unparasable date: {}. {}: {}", value, ex.getClass().getName(), ex.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static Date getDateFromStringForUserEnrollment(String value) {
        if(value!=null) {
            try {
                return simpleDateFormatUserEnrollment.parse(value);
            } catch(ParseException ex) {
                Log.error("Unparasable date: {}. {}: {}", value, ex.getClass().getName(), ex.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static String getStringFromDateForUserEnrollment(Date value) {
        if(value!=null) {
            return simpleDateFormatUserEnrollment.format(value);
        } else {
            return null;
        }
    }
}
