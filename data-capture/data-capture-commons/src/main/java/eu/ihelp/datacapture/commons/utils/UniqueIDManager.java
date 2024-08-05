package eu.ihelp.datacapture.commons.utils;

import java.util.UUID;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class UniqueIDManager {

    private UniqueIDManager() {}
    
    private static final class UniqueIDManagerHandler {
        private static final UniqueIDManager INSTANCE = new UniqueIDManager();
    }
    
    public static UniqueIDManager getInstance() {
        return UniqueIDManagerHandler.INSTANCE;
    }
    
    public String getNewID() {
        return UUID.randomUUID().toString();
    }
}
