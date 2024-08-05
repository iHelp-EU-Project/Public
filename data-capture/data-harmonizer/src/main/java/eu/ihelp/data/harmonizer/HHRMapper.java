package eu.ihelp.data.harmonizer;

import eu.ihelp.datacapture.commons.DataCaptureDatasets;
import eu.ihelp.datacapture.commons.exceptions.JsonSerializationException;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRMapper {
    private static final Logger Log = LoggerFactory.getLogger(HHRMapper.class);
    private static final String MAPPER_URL = HarmonizerProperties.getInstance().getHhrMaperURL();
    
    
    public static JSONArray mapToHHR(Schema schemaKey, Schema schemaValue, JSONArray jSONArray, DataCaptureDatasets datasetType) throws IOException, JsonSerializationException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
         List<String> entitiesRequest = new ArrayList<>(jSONArray.length());
         for(int i=0; i<jSONArray.length(); i++) {
             try {
                entitiesRequest.add(generateEntityRequest(schemaKey, schemaValue, datasetType, jSONArray.getJSONArray(i)));
             } catch(JSONException ex) {
                 Log.error("Could not generate input request object for dataset {}. {}", datasetType.name(), ex);
             }
         }
         
         String requestInput = JsonSerializers.serialize(entitiesRequest);
         System.out.println(requestInput);
         
         //for now, send one by one
         JSONArray result = new JSONArray();
         for(String str : entitiesRequest) {
             try {
                JSONObject jSONObject = getResult(str, datasetType);
                result.put(jSONObject);
             } catch(Exception ex) {
                 ex.printStackTrace();
             }
         }
         
         return result;
    }
    
    private static String generateEntityRequest(Schema schemaKey, Schema schemaValue, DataCaptureDatasets datasetType, JSONArray dataRowItem) throws IOException {
        String filePath = getTemplate(datasetType);
        String jsonTemplate = readJsonFromFile(filePath);
        
         int index = 0;
        for(Field field : schemaKey.getFields()) {
            Object value = dataRowItem.get(index++);
            String valueStr;
            if(isString(field)) {
                valueStr = "\"" + (String) value + "\'";
            } else {
                valueStr = String.valueOf(value);
            }

            jsonTemplate = jsonTemplate.replace("{{" + field.name() + "}}", String.valueOf(value));
        }

        for(Field field : schemaValue.getFields()) {
            Object value = dataRowItem.get(index++);
            if(value==JSONObject.NULL) {
                value = null;
            }
            
            if(value==null) {
                    
                //cheat for persons

                //cheat for conditions
                if(field.name().equalsIgnoreCase("condition_start_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("condition_end_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("visit_occurrence_id")) {
                    value = 10;
                }

                //cheat for encounters
                if(field.name().equalsIgnoreCase("visit_start_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("visit_end_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("provider_id")) {
                    value = 10;
                }

                //cheat for medication statements
                if(field.name().equalsIgnoreCase("drug_exposure_start_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("drug_exposure_end_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("visit_occurrence_id")) {
                    value = 10;
                }
                if(field.name().equalsIgnoreCase("route_concept_id")) {
                    value = 10;
                }
                if(field.name().equalsIgnoreCase("quantity")) {
                    value = 50.0;
                }

                //cheat for observations 
                if(field.name().equalsIgnoreCase("observation_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("unit_concept_id")) {
                    value = 10;
                }

                //cheat for measurements 
                if(field.name().equalsIgnoreCase("measurement_datetime")) {
                    value = System.currentTimeMillis();
                }
                if(field.name().equalsIgnoreCase("operator_concept_id")) {
                    value = 10;
                }
                if(field.name().equalsIgnoreCase("unit_concept_id")) {
                    value = 10;
                }
                if(field.name().equalsIgnoreCase("range_low")) {
                    value = 50.0;
                }
                if(field.name().equalsIgnoreCase("range_high")) {
                    value = 150.0;
                }
            }
            
            //cheat for measurements
            if(field.name().equalsIgnoreCase("measurement_concept_id")) {
                value = 4042059;
            }
            
            String valueStr;
            if(isString(field)) {
                valueStr = "\"" + (String) value + "\'";
            } else {
                valueStr = String.valueOf(value);
            }

            jsonTemplate = jsonTemplate.replace("{{" + field.name() + "}}", String.valueOf(value));
        }

        System.out.println(jsonTemplate);
        JSONObject jSONObject = new JSONObject(jsonTemplate);
        return jSONObject.toString();
    }
    
    private static JSONObject getResult(String input, DataCaptureDatasets dataset) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        String requestURL = MAPPER_URL + getWebMethod(dataset);
        HttpPost httpPost = new HttpPost(requestURL);
        StringEntity requestEntity = new StringEntity(input);
        httpPost.setEntity(requestEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        
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
    
    private static boolean isString(Field field) {
        return field.schema().getType()==Schema.Type.STRING;
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
    
    
    private static String getTemplate(DataCaptureDatasets dataset) {
        String path = "hhr-templates/";
        switch(dataset) {
            case ConditionOccurrence:
                return path + "Condition.template";
            case DrugExposure:
                return path + "Medication.template";
            case Measurement:
                return path + "Measurement.template";
            case Observations:
                return path + "Observation.template";
            case Persons:
                return path + "Patient.template";
            case ProcedureOccurrence:
                return path + "Procedure.template";
            case VisitOccurrence:
                return path + "Encounter.template";
            default:
                throw new IllegalArgumentException("Unknown type: " + dataset.name());
        }
    }
    
    private static String getWebMethod(DataCaptureDatasets dataset) {
        switch(dataset) {
            case ConditionOccurrence:
                return "tofhir/condition";
            case DrugExposure:
                return "tofhir/medicationStatement";
            case Measurement:
                return "tofhir/observationFromMeasurement";
            case Observations:
                return "tofhir/observationFromObservation";
            case Persons:
                return "tofhir/patientFromPerson";
            case ProcedureOccurrence:
                return "tofhir/procedure";
            case VisitOccurrence:
                return "tofhir/encounter";
            default:
                throw new IllegalArgumentException("Unknown type: " + dataset.name());
        }
    }
}
