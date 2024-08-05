package eu.ihelp.datacapture.rest.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobScheduled;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureComboScheduledService;
import eu.ihelp.datacapture.manager.services.impl.ScheduledComboJobManager;
import eu.ihelp.datacapture.manager.services.impl.mock.DataCaptureComboScheduleServiceMockImpl;
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
@Path("/datacapture/combo/schedule")
@Api(basePath="http://localhost:54735/ihelp", value = "/datacapture/combo/schedule", description = "REST Service to submit/stop/monitor scheduled data capture combo jobs")
public class DataCaptureComboScheduledResource {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureComboScheduledResource.class);
    
    
    private final DataCaptureComboScheduledService service;
    
    /**
     * Creates a new instance of DataCaptureScheduledResource
     */
    public DataCaptureComboScheduledResource() {
        this.service = ScheduledComboJobManager.getInstance();
        //this.service = DataCaptureComboScheduleServiceMockImpl.getInstance();
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
     * Returns the full list of all submitted scheduled combo jobs
     * 
     * @return 
     */
    @GET
    @ApiOperation(value="Returns the full list of all submitted scheduled combo jobs", notes="Returns the full list of all submitted scheduled combo jobs", 
            responseContainer = "List", response = DataCaptureComboJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getAllDataCaptureScheduleComboJobs() {
        Log.debug("REST service '{}' was invoked", "getAllDataCaptureScheduleComboJobs");
        String result;
        
        try {
            List<DataCaptureComboJobScheduled> list = this.service.getAllDataCaptureScheduleComboJobs();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Returns the scheduled combo job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value="Returns a scheduled combo job based on its id", notes="Returns a combo job scheduled based on its id", 
            response = DataCaptureComboJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getDataCaptureScheduleComboJob(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "getDataCaptureScheduleComboJob");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureComboJobScheduled dto = this.service.getDataCaptureScheduleComboJob(id);
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
     * Stops the scheduled combo job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}/stop")
    @ApiOperation(value="Stops a scheduled combo job based on its id", notes="Stops a combo job scheduled based on its id", 
            response = DataCaptureComboJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response stopDataCaptureScheduleComboJob(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "stopDataCaptureScheduleComboJob");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureComboJobScheduled dto = this.service.stopDataCaptureScheduleComboJob(id);
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
     * Submits a new scheduled combo job to capture data
     * 
     * @param job
     * @return 
     */
    @POST
    @ApiOperation(value="Submits a new scheduled combo job to capture data", notes="Returns the new submitted scheduled combo job", 
            response = DataCaptureComboJobScheduled.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addDataCaptureScheduleComboJob(
            @ApiParam(value = "the data capture job to submit", required = true) 
            String job) {
        Log.debug("REST service '{}' was invoked", "addDataCaptureScheduleComboJob");
        String result;
        
        try {
            DataCaptureComboJobScheduled dto = JsonSerializers.deserialize(job, DataCaptureComboJobScheduled.class);
            dto= this.service.addDataCaptureScheduleComboJob(dto);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            ex.printStackTrace();
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
}
