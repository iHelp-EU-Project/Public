package eu.ihelp.datacapture.rest.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import eu.ihelp.datacapture.manager.services.DataCaptureService;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.impl.JobManager;
import eu.ihelp.datacapture.manager.services.impl.mock.DataCaptureServiceMockImpl;
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
@Path("/datacapture")
@Api(basePath="http://localhost:54735/ihelp", value = "/datacapture", description = "REST Service to submit/stop/monitor data capture jobs")
public class DataCaptureResource {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureResource.class);
    
    
    private final DataCaptureService service;
    
    /**
     * Creates a new instance of DataCaptureResource
     */
    public DataCaptureResource() {
        //this.service = DataCaptureServiceMockImpl.getInstance();
        this.service = JobManager.getInstance();
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
     * Returns the full list of all submitted jobs
     * 
     * @return 
     */
    @GET
    @ApiOperation(value="Returns the full list of all submitted jobs", notes="Returns the full list of all submitted jobs", 
            responseContainer = "List", response = DataCaptureJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getAllDataCaptureJobs() {
        Log.debug("REST service '{}' was invoked", "getAllDataCaptureJobs");
        String result;
        
        try {
            List<DataCaptureJob> list = this.service.getAllDataCaptureJobs();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
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
    @Path("/{id}")
    @ApiOperation(value="Returns a job based on its id", notes="Returns a job based on its id", 
            response = DataCaptureJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getDataCaptureJob(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "getDataCaptureJob");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureJob dto = this.service.getDataCaptureJob(id);
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
     * Returns the job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/finished/{id}")
    @ApiOperation(value="Returns a job based on its id", notes="Returns a job based on its id", 
            response = DataCaptureJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response setDataCaptureJobFinished(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "setDataCaptureJobFinished");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureJob dto = this.service.setDataCaptureJobFinished(id);
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
     * Submits a new job to capture data
     * 
     * @param job
     * @return 
     */
    @POST
    @ApiOperation(value="Submits a new job to capture data", notes="Returns the new submitted job", 
            response = DataCaptureJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addDataCaptureJob(
            @ApiParam(value = "the data capture job to submit", required = true) 
            String job) {
        Log.debug("REST service '{}' was invoked", "addDataCaptureJob");
        String result;
        
        try {
            DataCaptureJob dto = JsonSerializers.deserialize(job, DataCaptureJob.class);
            dto= this.service.addDataCaptureJob(dto);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
}
