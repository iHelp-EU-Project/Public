package eu.ihelp.enrollment.services.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MUPPrimaryDataParser {
    private final String FIELD_CONNECTOR_ARGUMENTS = "connectorArguments";
    private final String FIELD_CONNECTOR_DATA = "data";
    
    private final JSONObject jsonObject;
    private final String primaryData;

    protected MUPPrimaryDataParser(JSONObject jsonObject, String primaryData) {
        this.jsonObject = jsonObject;
        this.primaryData = primaryData;
    }
    
    public String getData() {
        JSONObject connectorData = jsonObject.getJSONObject(FIELD_CONNECTOR_ARGUMENTS);
        JSONArray data = connectorData.getJSONArray(FIELD_CONNECTOR_DATA);
        data.put(primaryData);
     
        return jsonObject.toString();
    }
}
