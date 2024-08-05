package eu.ihelp.datacapture.functions.connectors.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HealthentiaUtils {
    private static final Logger Log = LoggerFactory.getLogger(HealthentiaUtils.class);
    private static final String LOGIN_METHOD = "v2/account/Login";
    private static final String REFRESH_METHOD = "v2/account/RefreshToken";
    private static final String GET_ALL_SUBJECTS_METHOD = "v2/subjects?studyId=";
    private static final String GET_SUBJECT_METHOD = "v2/subjects/";
    private static final String GET_ANSWERS_METHOD = "v2/data-provider/answers?studyId=";
    private static final String GET_EXCERCICES_METHOD = "v2/data-provider/exercises?studyId=";
    private static final String GET_PSYSIOLOGICAL_METHOD = "v2/data-provider/physiological?studyId=";
    //private static final String GET_PSYSIOLOGICAL_METHOD = "v3/aggregations?studyId=";
    
    public static JSONObject getHealthentiaRecords(String dataset, String url, String username, String password, Integer studyID, Integer lastProcessedID, String dateTimeStr) throws HealthentiaAuthenticationException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException  {
        if(!url.endsWith("/")) {
            url+="/";
        }
        
        String requestURL = url;
        
        if(dataset.equalsIgnoreCase("answers")) { //answers
            requestURL += GET_ANSWERS_METHOD + studyID;
        } else if(dataset.equalsIgnoreCase("physiological")) { //physiological
            requestURL += GET_PSYSIOLOGICAL_METHOD + studyID; 
        } else if(dataset.equalsIgnoreCase("exercises")) { //exercises
            requestURL += GET_EXCERCICES_METHOD + studyID; 
        } else {
            throw new IllegalArgumentException("Unsupported dataset: " + dataset);
        }
        
        if(dateTimeStr!=null) {
            requestURL += "&dateFrom=" + dateTimeStr;
        }
        
        requestURL+="&lastProcessedId=" + lastProcessedID;
        
        return sendGetRequest(requestURL, url, username, password);
    }
    
    
    
    public static JSONObject getSubject(String url, String username, String password, String subjectID) throws HealthentiaAuthenticationException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException  {
        if(!url.endsWith("/")) {
            url+="/";
        }
        
        String requestURL = url + GET_SUBJECT_METHOD + subjectID;
        
        return sendGetRequest(requestURL, url, username, password);
    }
    
    public static JSONArray getAllSubjects(String url, String username, String password, int studyID) throws HealthentiaAuthenticationException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException  {
        if(!url.endsWith("/")) {
            url+="/";
        }
        
        String requestURL = url + GET_ALL_SUBJECTS_METHOD + studyID;
        String authorizationToken = HealthentiaTokensManager.getInstance().getHealthentiaToken(url, username, password).getToken();
        HttpGet httpGet = new HttpGet(requestURL);
        httpGet.setHeader("Accept", "text/plain");
        httpGet.setHeader("Authorization", authorizationToken);
        
        Log.info("Will invoke url: {}", requestURL);
        
        try(CloseableHttpClient httpClient = createTrustAllHttpClientBuilder().build();
                CloseableHttpResponse response = httpClient.execute(httpGet);) {
            
            //if not 200, then throw an exception
            if(response.getStatusLine().getStatusCode()!=200) {
                Log.warn("Not valid response: {}", response.getStatusLine().toString());
                throw new RuntimeException("Could not connect to remot API. " + response.getStatusLine().toString() + "  " + response.getStatusLine().getStatusCode());
            }
            
            HttpEntity responseEntity = response.getEntity();
            
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                return new JSONArray(result);
            }
            
            return null;
        }    
        
    }
    
    public static JSONObject refresh(String url, String refresToken) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("refreshToken", refresToken);
        
        if(!url.endsWith("/")) {
            url+="/";
        }
        
        String requestURL = url + REFRESH_METHOD;
        return sendPostRequest(requestURL, jSONObject);
    }
    
    public static JSONObject login(String url, String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("userName", username);
        jSONObject.put("password", password);
        
        if(!url.endsWith("/")) {
            url+="/";
        }
        
        String requestURL = url + LOGIN_METHOD;
        return sendPostRequest(requestURL, jSONObject);
    }
    
    private static JSONObject sendGetRequest(String requestURL, String url, String username, String password) throws HealthentiaAuthenticationException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException  {
        String authorizationToken = HealthentiaTokensManager.getInstance().getHealthentiaToken(url, username, password).getToken();
        HttpGet httpGet = new HttpGet(requestURL);
        httpGet.setHeader("Accept", "text/plain");
        httpGet.setHeader("Authorization", authorizationToken);
        
        Log.info("Will invoke url: {}", requestURL);
        
        try(CloseableHttpClient httpClient = createTrustAllHttpClientBuilder().build();
                CloseableHttpResponse response = httpClient.execute(httpGet);) {
            
            //if not 200, then throw an exception
            if(response.getStatusLine().getStatusCode()!=200) {
                Log.warn("Not valid response: {}", response.getStatusLine().toString());
                throw new RuntimeException("Could not connect to remot API. " + response.getStatusLine().toString() + "  " + response.getStatusLine().getStatusCode());
            }
            
            HttpEntity responseEntity = response.getEntity();
            
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                return new JSONObject(result);
            }
            
            return null;
        }    
    }
    
    private static JSONObject sendPostRequest(String requestURL, JSONObject input) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpPost httpPost = new HttpPost(requestURL);
        StringEntity requestEntity = new StringEntity(input.toString());
        httpPost.setEntity(requestEntity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "application/json-patch+json");
        
        try(CloseableHttpClient httpClient = createTrustAllHttpClientBuilder().build();
                CloseableHttpResponse response = httpClient.execute(httpPost);) {
            
            //if not 200, then throw an exception
            if(response.getStatusLine().getStatusCode()!=200) {
                Log.warn("Not valid response: {}", response.getStatusLine().toString());
                throw new RuntimeException("Could not connect to remot API. " + response.getStatusLine().toString() + "  " + response.getStatusLine().getStatusCode());
            }
            
            HttpEntity responseEntity = response.getEntity();
            
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                return new JSONObject(result);
            }
            
            return null;
        }       
    }
    
    
    
    private static HttpClientBuilder createTrustAllHttpClientBuilder() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (chain, authType) -> true);           
        SSLConnectionSocketFactory sslsf = new 
        SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslsf);
    }
}
