package eu.ihelp.datacapture.functions.connectors.utils;

import java.io.IOException;
import java.net.http.HttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SetJobFinishedUtil {
    private static final Logger Log = LoggerFactory.getLogger(SetJobFinishedUtil.class);
    private static final String CONNECTION_URL = "http://localhost:54735/ihelp/datacapture/finished/";
    
    public static void setJobAsFinished(String id) throws IOException {
        HttpGet httpGet = new HttpGet(CONNECTION_URL + id);
    
    
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpGet);) {
            
            Log.info(response.getStatusLine().toString());
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            Log.info(headers.toString());
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                //System.out.println(result);
            }
        } 
    }
}
