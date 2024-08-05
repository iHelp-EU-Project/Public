package eu.ihelp.datacapture.commons.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.ihelp.datacapture.commons.exceptions.JsonSerializationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class provides useful static util methods for serializing/deserializing input/results
 * 
 * @author Pavlos Kranas (LeanXcale)
 */
public class JsonSerializers {
    
    public static String serialize(Object object) throws JsonSerializationException {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        }
        return json;
    }
    
    public static String serialize(Object object, PropertyAccessor jsonMethod) throws JsonSerializationException {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        mapper.setVisibility(jsonMethod, JsonAutoDetect.Visibility.ANY);
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        }
        return json;
    }
    
    public static String serialize(HashMap<String, Object> map) {
        JSONObject jSONObject = new JSONObject(map);
        
        return jSONObject.toString();
    }
    
    public static String serialize(List<HashMap<String, Object>> mapList) {
        JSONArray jSONArray = new JSONArray(mapList);
        
        return jSONArray.toString();
    }
    
    public static <T extends Object> T deserializeWithPolymorphicCollection(String input, Class<T> valueType) throws JsonSerializationException {
        Object result;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
        try {
            result = mapper.readValue(input, valueType);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } 
        
        return (T) result;
    }
    
    public static <T extends Object> T deserializePublicConstructor(String input, Class<T> valueType) throws JsonSerializationException {
        Object result;
        ObjectMapper readMapper = new ObjectMapper();
        readMapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY);
        //readMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        //readMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            result = readMapper.readValue(input, valueType);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } 
        
        return (T) result;
    }
    
    
    public static <T extends Object> T deserializeWithAnyVisibility(String input, Class<T> valueType) throws JsonSerializationException {
        Object result;
        ObjectMapper readMapper = new ObjectMapper();
        readMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        readMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            result = readMapper.readValue(input, valueType);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } 
        
        return (T) result;
    }
    
    public static HashMap<String, Object> deserialize(String input) throws IOException {
        HashMap<String,Object> result =
            new ObjectMapper().readValue(input, HashMap.class);
        return result;
    }
    

    public static <T extends Object> T deserialize(String input, Class<T> valueType) throws JsonSerializationException {
        Object result;
        ObjectMapper readMapper = new ObjectMapper();
        readMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        readMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        
        try {
            result = readMapper.readValue(input, valueType);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } 
        
        return (T) result;
        
    }
    
    public static <T extends Object> T deserialize(String input, TypeReference valueTypeRef) throws JsonSerializationException {
        Object result;
        ObjectMapper readMapper = new ObjectMapper();
        readMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        readMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            result = readMapper.readValue(input, valueTypeRef);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } 
        
        return (T) result;
    }
    
    public static <T extends Object> T deserialize(String input, TypeReference valueTypeRef, PropertyAccessor jsonMethod) throws JsonSerializationException {
        Object result;
        ObjectMapper readMapper = new ObjectMapper();
        
        readMapper.setVisibility(jsonMethod, JsonAutoDetect.Visibility.ANY);
        try {
            result = readMapper.readValue(input, valueTypeRef);
        } catch (JsonParseException ex) {
            String message = "JsonGenerationException. Cannot parse the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (JsonMappingException ex) {
            String message = "JsonMappingException. Cannot map values to the user's input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } catch (IOException ex) {
            String message = "IOException. Cannot read user' input json file. " + ex.getMessage();
            throw new JsonSerializationException(message, ex);
        } 
        
        return (T) result;
    }
}
