package eu.ihelp.enrollment.exceptions;

import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class FunctionInvocationException extends Exception {
    private final JSONObject jSONObject;
    
    public FunctionInvocationException(JSONObject jSONObject) {
        super("Error invoking function: " + jSONObject.toString());
        this.jSONObject = jSONObject;
    }
 }
