package eu.ihelp.enrollment.services.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.enrollment.exceptions.JsonSerializationException;
import eu.ihelp.enrollment.exceptions.MessageNotFoundException;
import eu.ihelp.enrollment.exceptions.MissingInputParametersException;
import eu.ihelp.enrollment.model.MessageSmallDTO;
import eu.ihelp.enrollment.services.MonitoringMessagesService;
import eu.ihelp.enrollment.services.impl.MonitoringMessagesServiceImpl;
import eu.ihelp.enrollment.utils.Consts;
import eu.ihelp.enrollment.utils.ErrorMessageResponser;
import eu.ihelp.enrollment.utils.JsonSerializers;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@Path("/monitoringmessages")
@Api(basePath="http://localhost:54735/ihelp", value = "/monitoringmessages", description = "REST Service for the monitoring messages services of the iHelp platform")
public class MonitoringMessagesResource {
    private static final Logger Log = LoggerFactory.getLogger(MonitoringMessagesResource.class);
    
    private final MonitoringMessagesService service;

    public MonitoringMessagesResource() {
        this.service = MonitoringMessagesServiceImpl.getInstance();
    }
    
    @GET
    @Path("/test")
    @ApiOperation(value="Testing operation", 
            notes="A hello world message", response = String.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response test() {
        String result = "<b>Hello from the " + this.getClass().getName() + " resource!</b>   ";
        Log.info("{} service has been invoked", this.getClass().getSimpleName());
        return Response.ok(result).build();
    }
    
    @POST
    @ApiOperation(value="Submits a new message to be added", notes="nothing")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response insertMessage(
            @ApiParam(value = "the message to submit", required = true) 
            String input) {
        Log.info("REST service '{}' was invoked", "insertMessage");
        
        try {
            this.service.insertMonitoringMessages(input);
        } catch(Exception ex) {
            if((ex instanceof JsonSerializationException) ||
               (ex instanceof MissingInputParametersException)     
                    ) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
            }
            
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok().build();
    }
    
    @PUT
    @ApiOperation(value="sets a message from proposed to approved")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message"),
        @ApiResponse(code=404, message="message not found")
    })
    @SuppressWarnings("UseSpecificCatch")
    public Response setsMessageToApproved(
            @ApiParam(value = "the studyID of the message", required = true) 
            @QueryParam("studyID") String studyID,
            @ApiParam(value = "the subjectId of the message", required = true) 
            @QueryParam("subjectId") String subjectId,
            @ApiParam(value = "the dialogueId of the message", required = true) 
            @QueryParam("dialogueId") String dialogueId,
            @ApiParam(value = "the triggeredId of the message", required = true) 
            @QueryParam("triggerId") String triggerId) {
        Log.info("REST service '{}' was invoked", "setsMessageToApproved");
        
        
        try {
            this.service.updateMessageStatus(studyID, subjectId, dialogueId, triggerId, Consts.MonitoringMessages.HealthentiaStatus.approved);
        } catch(Exception ex) {
            if(ex instanceof MessageNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(studyID, Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok().build();
    }
    
    @DELETE
    @ApiOperation(value="sets a message from proposed to rejected")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message"),
        @ApiResponse(code=404, message="message not found")
    })
    @SuppressWarnings("UseSpecificCatch")
    public Response setsMessageToRejected(
            @ApiParam(value = "the studyID of the message", required = true) 
            @QueryParam("studyID") String studyID,
            @ApiParam(value = "the subjectId of the message", required = true) 
            @QueryParam("subjectId") String subjectId,
            @ApiParam(value = "the dialogueId of the message", required = true) 
            @QueryParam("dialogueId") String dialogueId,
            @ApiParam(value = "the triggeredId of the message", required = true) 
            @QueryParam("triggeredId") String triggeredId) {
        Log.info("REST service '{}' was invoked", "setMessageToRejected");
        
        
        try {
            this.service.updateMessageStatus(studyID, subjectId, dialogueId, triggeredId, Consts.MonitoringMessages.HealthentiaStatus.rejected);
        } catch(Exception ex) {
            if(ex instanceof MessageNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(studyID, Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok().build();
    }
    
    @GET
    @ApiOperation(value="gets the messages of patients", notes="all messages or for a given patient", response = MessageSmallDTO.class, responseContainer = "List")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getMessaeges(
            @ApiParam(value = "the iHelpID of the patient", required = false) 
            @QueryParam("patientID") String patientID,
            @ApiParam(value = "status of the messages", required = false) 
            @QueryParam("status") String status) {
        Log.info("REST service '{}' was invoked", "getMessaeges");
        String result;
       
        try {
            List<MessageSmallDTO> list = this.service.getMessages(patientID, status);
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if((ex instanceof JsonSerializationException) ||
               (ex instanceof MissingInputParametersException)     
                    ) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
            }
            
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
}
