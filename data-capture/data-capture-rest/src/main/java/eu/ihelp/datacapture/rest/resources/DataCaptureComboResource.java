package eu.ihelp.datacapture.rest.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureComboService;
import eu.ihelp.datacapture.manager.services.impl.ComboJobManager;
import eu.ihelp.datacapture.manager.services.impl.mock.DataCaptureComboServiceMockImpl;
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
@Path("/datacapture/combo")
@Api(basePath="http://localhost:54735/ihelp", value = "/datacapture/combo", description = "REST Service to submit/stop/monitor data capture combo jobs")
public class DataCaptureComboResource {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureComboResource.class);
    
    private final DataCaptureComboService service;
    
    /**
     * Creates a new instance of DataCaptureComboResource
     */
    public DataCaptureComboResource() {
        //this.service = DataCaptureComboServiceMockImpl.getInstance();
        this.service = ComboJobManager.getInstance();
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
     * Returns the full list of all submitted combo jobs
     * 
     * @return 
     */
    @GET
    @ApiOperation(value="Returns the full list of all submitted combo jobs", notes="Returns the full list of all submitted combo jobs", 
            responseContainer = "List", response = DataCaptureComboJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getAllDataCaptureComboJobs() {
        Log.debug("REST service '{}' was invoked", "getAllDataCaptureComboJobs");
        String result;
        try {
            List<DataCaptureComboJob> list = this.service.getAllDataCaptureComboJobs();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Returns the combo job by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value="Returns a combo job based on its id", notes="Returns a job combo based on its id", 
            response = DataCaptureComboJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getDataCaptureComboJob(
            @ApiParam(value = "the id of the job", required = true) 
            @PathParam("id") String id
    ) {
        Log.debug("REST service '{}' was invoked", "getDataCaptureComboJob");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            DataCaptureComboJob dto = this.service.getDataCaptureComboJob(id);
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
     * Submits a new combo job to capture data
     * 
     * @param job
     * @return 
     */
    @POST
    @ApiOperation(value="Submits a new combo job to capture data", notes="Returns the new submitted combo job", 
            response = DataCaptureComboJob.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addDataCaptureComboJob(
            @ApiParam(value = "the data capture job to submit", required = true) 
            String job) {
        Log.debug("REST service '{}' was invoked", "addDataCaptureComboJob");
        String result;
        
        try {
            DataCaptureComboJob dto = JsonSerializers.deserialize(job, DataCaptureComboJob.class);
            dto= this.service.addDataCaptureComboJob(dto);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            ex.printStackTrace();
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
}
