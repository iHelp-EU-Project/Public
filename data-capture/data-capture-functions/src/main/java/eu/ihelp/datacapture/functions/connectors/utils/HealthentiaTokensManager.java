package eu.ihelp.datacapture.functions.connectors.utils;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HealthentiaTokensManager {
    private final HashMap<String, HealthentiaToken> tokens;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private HealthentiaTokensManager(HashMap<String, HealthentiaToken> tokens) {
        this.tokens = tokens;
    }
    
    private static final class HealthentiaTokensManagerHolder {
        private static final HealthentiaTokensManager INSTANCE = new HealthentiaTokensManager(new HashMap<>());
    }
    
    public static HealthentiaTokensManager getInstance() {
        return HealthentiaTokensManagerHolder.INSTANCE;
    }
    
    public HealthentiaToken getHealthentiaToken(String url, String username, String password) throws HealthentiaAuthenticationException {
        try {
            this.lock.readLock().lock();
            return _getHealthentiaToken(url, username, password);
        } finally {
            this.lock.readLock().unlock();;
        }
    }
    
    private HealthentiaToken _getHealthentiaToken(String url, String username, String password) throws HealthentiaAuthenticationException {
        HealthentiaToken healthentiaToken = null;        
        
        //first check if already exists
        while((healthentiaToken = this.tokens.get(username))==null) {
            try {
                //release the readlock and aquire the write, so as to prevent other theads getting a new tuneLock (via a readLock) that could be removed before locking the tune
                this.lock.readLock().unlock();
                this.lock.writeLock().lock();
                
                //recheck if token exists, two concurrent threads might have been trying to create the token
                if((healthentiaToken = this.tokens.get(username))==null) {
                    
                    healthentiaToken = HealthentiaTokenBuilder.newInstance(url, username, password).build();
                    this.tokens.put(username, healthentiaToken);
                }
                
                
                //downgrade by acquiring read lock before releasing write lock, so that the caller method will continue having this lock
                this.lock.readLock().lock();
            } finally {
                this.lock.writeLock().unlock();
            }
        }
        
        return healthentiaToken;
    }
}
