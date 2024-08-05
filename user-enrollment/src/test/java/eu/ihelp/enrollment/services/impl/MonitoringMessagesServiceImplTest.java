package eu.ihelp.enrollment.services.impl;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.MonitoringMessageTransformException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MonitoringMessagesServiceImplTest {
    
    private static final String FILE_NAME = "messageNeedsApproval.json";
    
    
    public static void main(String [] args) throws IOException, MonitoringMessageTransformException, DataStoreException {
        System.out.println("starting ...");
        MonitoringMessagesServiceImpl service = MonitoringMessagesServiceImpl.getInstance();
        
        String message = readJsonFromFile(FILE_NAME);
        System.out.println(message);
        
        service.insertMonitoringMessages(message);
    }
    
    private static String readJsonFromFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
            String line;
            while((line = br.readLine())!=null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
