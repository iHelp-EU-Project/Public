package eu.ihelp.datacapture.rest.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.datacapture.commons.model.DataCaptureJobScheduled;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.ihelp.datacapture.manager.services.DataCaptureScheduledService;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.impl.ScheduledJobManager;
import eu.ihelp.datacapture.manager.services.impl.mock.DataCaptureScheduleServiceMockImpl;
import eu.ihelp.datacapture.rest.server.utils.ErrorMessageResponser;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@Path("/datacapture/schedule")
@Api(basePath="http://localhost:54735/ihelp", value = "/datacapture/schedule", description = "REST Service to submit/stop/monitor scheduled data capture jobs")
public class DataCaptureScheduledResource {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureScheduledResource.class);
    
    
    private final DataCaptureScheduledService service;
    
    /**
     * Creates a new instance of DataCaptureScheduledResource
     */
    public DataCaptureScheduledResource() {
        this.service = ScheduledJobManager.getInstance();
        //this.service = DataCaptureScheduleServiceMockImpl.getInstance();
    }
    
    @GET
    @Path("/test")
    @ApiOperation(value="Testing operation", 
            notes="A hello world message", response = String.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response test() {
        String result = "<b>Hello from the test resource!</b>   ";
        Log.info("{} service has been invoked", this.getClass().getSimpleName());
        return Response.ok(result).build();
    }
    
    
    /**
     * Returns the full list of all submitted scheduled jobs
     * 
     * @return 
     */
    @GET
    @ApiOperation(value="Returns the full list of all submitted scheduled jobs", notes="Returns the full list of all submitted scheduled jobs", 
            responseContainer = "List", response = DataCaptureJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getAllDataCaptureScheduleJobs() {
        Log.debug("REST service '{}' was invoked", "getAllDataCaptureScheduleJobs");
        String result;
        
        try {
            List<DataCaptureJobScheduled> list = this.service.getAllDataCaptureScheduleJobs();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Returns the scheduled job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value="Returns a scheduled job based on its id", notes="Returns a job scheduled based on its id", 
            response = DataCaptureJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getDataCaptureScheduleJob(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "getDataCaptureScheduleJob");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureJobScheduled dto = this.service.getDataCaptureScheduleJob(id);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof JobNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Stops the scheduled job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}/stop")
    @ApiOperation(value="Stops a scheduled job based on its id", notes="Stops a job scheduled based on its id", 
            response = DataCaptureJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response stopDataCaptureScheduleJob(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "stopDataCaptureScheduleJob");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureJobScheduled dto = this.service.stopDataCaptureScheduleJob(id);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof JobNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Submits a new scheduled job to capture data
     * 
     * @param job
     * @return 
     */
    @POST
    @ApiOperation(value="Submits a new scheduled job to capture data", notes="Returns the new submitted scheduled job", 
            response = DataCaptureJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addDataCaptureScheduleJob(
            @ApiParam(value = "the data capture job to submit", required = true) 
            String job) {
        Log.debug("REST service '{}' was invoked", "addDataCaptureScheduleJob");
        String result;
        
        try {
            DataCaptureJobScheduled dto = JsonSerializers.deserialize(job, DataCaptureJobScheduled.class);
            dto= this.service.addDataCaptureScheduleJob(dto);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
}
