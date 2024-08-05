package eu.ihelp.datacapture.rest.server.utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ErrorMessageResponser {
    
    private ErrorMessageResponser() {}
    
    public static String getJsonMessage(Exception ex) {
        String message = ex.getMessage();
        
        return "{\"message\": \"" + message + "\"}";
    }
    
    public static String getJsonMessage(String message) {       
        return "{\"message\": \"" + message + "\"}";
    }
    
    public static Response buildErrorResponse(Exception ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorMessageResponser.getJsonMessage(ex))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
    }
    
    public static Response buildErrorResponse(String message, Response.Status statusResponse) {
        return Response.status(statusResponse)
                    .entity(ErrorMessageResponser.getJsonMessage(message))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
    }
}
