package eu.ihelp.store.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ModelMetaInfoBuilder {
    private static final String JSON_COLUMN_NAME = "name";
    private static final String JSON_COLUMN_SNOMED = "snomed";
    private static final String JSON_COLUMN_MULTIPLE_VALUES = "multipleValues";
    private static final String JSON_COLUMN_POSSIBLE_VALUES = "possibleValues";
    private static final String JSON_COLUMN_REAL_VALUES = "realValues";
    
    private final int modelID;
    private final JSONArray jsonArray;

    private ModelMetaInfoBuilder(int modelID, JSONArray jsonArray) {
        this.modelID = modelID;
        this.jsonArray = jsonArray;
    }
    
    public static ModelMetaInfoBuilder newInstance(int modelID, String input) {
        return new ModelMetaInfoBuilder(modelID, new JSONArray(input));
    }
    
    public ModelMetaInfo build() {
        List<ModelMetaInfoElement> elements = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject jSONObject = jsonArray.getJSONObject(i);
            String name = null;
            String snomed = null;
            boolean multipleValues = false;
            final List<Object> possibleValues = new ArrayList<>();
            final List<Object> realValues = new ArrayList<>();
            if(jSONObject.has(JSON_COLUMN_NAME)) {
                name = jSONObject.getString(JSON_COLUMN_NAME);
            }
            if(jSONObject.has(JSON_COLUMN_SNOMED)) {
                snomed = jSONObject.getString(JSON_COLUMN_SNOMED);
            }
            if(jSONObject.has(JSON_COLUMN_MULTIPLE_VALUES)) {
                multipleValues = jSONObject.getBoolean(JSON_COLUMN_MULTIPLE_VALUES);
            }
            if(jSONObject.has(JSON_COLUMN_POSSIBLE_VALUES)) {
                JSONArray values = jSONObject.getJSONArray(JSON_COLUMN_POSSIBLE_VALUES);
                values.forEach((t) -> {
                    possibleValues.add(t);
                });
            }
            if(jSONObject.has(JSON_COLUMN_REAL_VALUES)) {
                JSONArray values = jSONObject.getJSONArray(JSON_COLUMN_REAL_VALUES);
                values.forEach((t) -> {
                    realValues.add(t);
                });
            }
            
            HashMap<Object, Object> mappedValues = new HashMap<>(possibleValues.size());
            for(int j=0; j<possibleValues.size(); j++) {
                mappedValues.put(possibleValues.get(j), realValues.get(j));
            }
            
            elements.add(new ModelMetaInfoElement(modelID, name, snomed, multipleValues, possibleValues, realValues, mappedValues));
        }
        
        return new ModelMetaInfo(elements);
    }
}
