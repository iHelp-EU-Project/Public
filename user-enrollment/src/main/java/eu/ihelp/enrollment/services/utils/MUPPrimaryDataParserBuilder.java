package eu.ihelp.enrollment.services.utils;

import eu.ihelp.enrollment.model.MUPRimaryData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MUPPrimaryDataParserBuilder {
    
    private static final String JSON_TEMPLATE_FILENAME = "mup_primary_data_request.json";
    
    public static MUPPrimaryDataParser createInstance(MUPRimaryData primaryDataRow) throws IOException {
        String jsonTemplate = initJsonTemplate();
        
        final StringBuilder sb = new StringBuilder();
        sb.append(primaryDataRow.getPatient_id()).append(";");
        sb.append(primaryDataRow.getGender()).append(";");
        sb.append(primaryDataRow.getAge()).append(";");
        primaryDataRow.getIndexes().forEach((Integer index ) -> { 
          sb.append(index).append(",");
        });
        
        String rowData = sb.toString();
        
        return new MUPPrimaryDataParser(new JSONObject(jsonTemplate), rowData.substring(0, rowData.length()-1));
    }
    
    static String initJsonTemplate() throws IOException {
        return readJsonFromFile(JSON_TEMPLATE_FILENAME);
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
