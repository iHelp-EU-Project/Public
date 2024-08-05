package eu.ihelp.data.importer.logging;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.Header;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HttpClient {
    private static final Logger Log = LoggerFactory.getLogger(HttpClient.class);
    
    
    public static void executePOST(String loggingURl, String input) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        Log.info("Will execute a POST at {} with this input:\n{}", loggingURl, input);
        System.out.println("Will execute a POST at " + loggingURl + " with this input:\n" + input);
        
        HttpPost httpPost = new HttpPost(loggingURl);
        StringEntity stringEntity = new StringEntity(input);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        
        try(CloseableHttpClient httpClient = createTrustAllHttpClientBuilder().build();
                CloseableHttpResponse response = httpClient.execute(httpPost);) {
            
            Log.info(response.getStatusLine().toString());
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            Log.info(headers.toString());

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
            
        } 
    }
    
    public static void executeGET(String loggingURl) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        Log.info("Will execute a GET at {} ", loggingURl);
        
        HttpGet httpGet = new HttpGet(loggingURl);
        httpGet.setHeader("Accept", "application/json");
        
        try(CloseableHttpClient httpClient = createTrustAllHttpClientBuilder().build();
                CloseableHttpResponse response = httpClient.execute(httpGet);) {
            
            Log.info(response.getStatusLine().toString());
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            Log.info(headers.toString());

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }            
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
