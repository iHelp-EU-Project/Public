package eu.ihelp.store.server.utils;

/**
 * Provides static methods for default return messages
 * 
 * @author Pavlos Kranas (LeanXcale)
 */
public class DefaultOkMessage {
    private DefaultOkMessage() {}
    
    private static final String defaultOkMessageStr = "{ \"status\" : \"ok\" }";
    private static final String affectedRowsMessageStr = "{ \"affected rows\" : \"ok\" }";
    
    public static String getDefaultOkMessage() {
        return defaultOkMessageStr;
    }
    
    public static String getDefaultOkMessage(int affectedRows) {
        return affectedRowsMessageStr.replace("ok", String.valueOf(affectedRows));
    }
    
    public static String getDefaultTestMessage(String className) {
        return "{ \"service\" : \"" + className + "\" }";
    }
}
