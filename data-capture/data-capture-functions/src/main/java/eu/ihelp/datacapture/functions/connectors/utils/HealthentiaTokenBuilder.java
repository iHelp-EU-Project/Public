package eu.ihelp.datacapture.functions.connectors.utils;

import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HealthentiaTokenBuilder {
    private final String url;
    private final String username;
    private final String password;

    private HealthentiaTokenBuilder(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    protected static HealthentiaTokenBuilder newInstance(String url, String username, String password) {
        return new HealthentiaTokenBuilder(url, username, password);
    }
    
    protected HealthentiaToken build() throws HealthentiaAuthenticationException {
        try {
            JSONObject jSONObject = HealthentiaUtils.login(url, username, password);
            return new HealthentiaToken(
                    url, 
                    username, 
                    password, 
                    jSONObject.getString("accessToken"), 
                    jSONObject.getString("refreshToken"),
                    Long.valueOf(jSONObject.getLong("expires")).intValue());
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new HealthentiaAuthenticationException(ex.getMessage(), ex);
        }
    }
    
    
    
    
}
