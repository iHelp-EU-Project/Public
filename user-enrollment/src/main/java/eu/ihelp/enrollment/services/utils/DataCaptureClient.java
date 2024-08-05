package eu.ihelp.enrollment.services.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
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
public class DataCaptureClient {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureClient.class);
    private static final String WEB_METHOD_URL = "/ihelp/datacapture";
    
    private final String dataCaptureURL;
    private final String dataCapturePort;
    private final MUPPrimaryDataParser dataParser;

    protected DataCaptureClient(String dataCaptureURL, String dataCapturePort, MUPPrimaryDataParser dataParser) {
        this.dataCaptureURL = dataCaptureURL;
        this.dataCapturePort = dataCapturePort;
        this.dataParser = dataParser;
    }
    
    public void ingestData() throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        executePOST(getDataCaptureURL(), this.dataParser.getData());
    }

    private void executePOST(String dataCaptureURL, String input) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        Log.info("Will execute a POST at {} with this input:\n{}", dataCaptureURL, input);
        System.out.println("Will execute a POST at " + dataCaptureURL + " with this input:\n" + input);
        
        HttpPost httpPost = new HttpPost(dataCaptureURL);
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
    
    private String getDataCaptureURL() {
        return "http://" + this.dataCaptureURL + ":" + this.dataCapturePort + WEB_METHOD_URL;
    }
    
    private static HttpClientBuilder createTrustAllHttpClientBuilder() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (chain, authType) -> true);           
        SSLConnectionSocketFactory sslsf = new 
        SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslsf);
    }
}
