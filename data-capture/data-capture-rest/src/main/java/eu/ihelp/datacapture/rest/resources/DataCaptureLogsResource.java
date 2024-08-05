package eu.ihelp.datacapture.rest.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.DataCaptureJobLogging;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureLogsService;
import eu.ihelp.datacapture.manager.services.impl.DataCaptureLogsServiceImpl;
import eu.ihelp.datacapture.rest.server.utils.ErrorMessageResponser;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@Path("/datacapture/logs")
@Api(basePath="http://localhost:54735/ihelp", value = "/datacapture/logs", description = "REST Service to submit/monitor logs of data capture jobs")
public class DataCaptureLogsResource {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureLogsResource.class);
    
    private final DataCaptureLogsService service;

    public DataCaptureLogsResource() {
        this.service = DataCaptureLogsServiceImpl.getInstance();
    }
    
    /**
     * Submits a new job to capture data
     * 
     * @param log
     * @return 
     */
    @POST
    @ApiOperation(value="Submits a new log to capture data job", notes="Returns the new submitted log", 
            response = DataCaptureJobLogging.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addDataCaptureLog(
            @ApiParam(value = "the log to submit", required = true) 
            String log) {
        Log.debug("REST service '{}' was invoked", "addDataCaptureLog");
        String result;
        
        try {
            DataCaptureJobLogging dto = JsonSerializers.deserialize(log, DataCaptureJobLogging.class);
            dto = this.service.insertDataCaptureLog(dto);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
    /**
     * Returns the job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/job/{id}")
    @ApiOperation(value="Returns list of logs of a job based on its id", notes="Returns a list of logs of a job based on its id", 
            response = DataCaptureJob.class, responseContainer = "List")
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getDataCaptureJobLogging(
            @ApiParam(value = "the id of the job to get the logs from", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "getDataCaptureJobLogging");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            List<DataCaptureJobLogging> dto = this.service.getAllDataCaptureLogs(id);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof JobNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
}
