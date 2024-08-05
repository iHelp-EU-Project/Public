package eu.ihelp.datacapture.functions.connectors.utils;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HealthentiaToken {
    private static final Logger Log = LoggerFactory.getLogger(HealthentiaToken.class);
    
    private final String url;
    private final String username;
    private final String password;
    
    private volatile String token;
    private volatile String refreshToken;
    private volatile int expires;
    private volatile DateTime datetime;
    
    private final ReentrantReadWriteLock lock;

    protected HealthentiaToken(String url, String username, String password, String token, String refreshToken, int expires) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expires = expires;
        this.datetime = datetime = DateTime.now();
        this.lock = new ReentrantReadWriteLock();
    }
    
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.url);
        hash = 11 * hash + Objects.hashCode(this.username);
        hash = 11 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HealthentiaToken other = (HealthentiaToken) obj;
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this.password, other.password);
    }

    @Override
    public String toString() {
        return "HealthentiaToken{" + "url=" + url + ", username=" + username + ", password=" + password + '}';
    }
    
    
    public String getToken() throws HealthentiaAuthenticationException {
        try {
            this.lock.readLock().lock();
        
            //check if token has been expired
            while(datetime.isAfter(DateTime.now().plusDays(expires-7).toDate().getTime())) { //refresh 1 week before expiration
                synchronized(this) {
                    if(datetime.isAfter(DateTime.now().plusDays(expires-7).toDate().getTime())) {
                        refreshToken();
                    }
                }
            }
            
            return "bearer " + this.token;
        } finally {
            this.lock.readLock().unlock();
        }        
    }
    
    public String forceRefreshToken() throws HealthentiaAuthenticationException {
        try {
            this.lock.writeLock().lock();
            refreshToken();
            
            return "bearer " + this.token;
        } finally {
            this.lock.writeLock().unlock();
        }
    }
    
    private void refreshToken() throws HealthentiaAuthenticationException {
        try {
            JSONObject jSONObject = HealthentiaUtils.refresh(url, refreshToken);
            this.datetime = DateTime.now();
            this.token = jSONObject.getString("accessToken");
            this.refreshToken = jSONObject.getString("refreshToken");
            this.expires = Long.valueOf(jSONObject.getLong("expires")).intValue();
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new HealthentiaAuthenticationException(ex.getMessage(), ex);
        }
    }
}
