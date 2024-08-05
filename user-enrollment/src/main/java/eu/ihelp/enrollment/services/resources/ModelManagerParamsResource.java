package eu.ihelp.enrollment.services.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.enrollment.exceptions.JsonSerializationException;
import eu.ihelp.enrollment.exceptions.MissingInputParametersException;
import eu.ihelp.enrollment.services.ModelManagerParamsService;
import eu.ihelp.enrollment.services.impl.ModelManagerParamsServiceImpl;
import eu.ihelp.enrollment.utils.ErrorMessageResponser;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("/modelmanagerparams")
@Api(basePath="http://localhost:54735/ihelp", value = "/modelmanagerparams", description = "REST Service for filling the parameters for the model")
public class ModelManagerParamsResource {
    private static final Logger Log = LoggerFactory.getLogger(ModelManagerParamsResource.class);
    
    private final ModelManagerParamsService service;

    public ModelManagerParamsResource() {
        this.service = ModelManagerParamsServiceImpl.getInstance();
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
    @ApiOperation(value="Fills the model manager parameters from the database", notes="based on the patient ID")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response fillParams(
            @ApiParam(value = "the parameters to fill", required = true) 
            String input,
            @ApiParam(value = "the patientID", required = true) 
            @QueryParam("patientID") String patientID,
            @ApiParam(value = "the providerID", required = true) 
            @QueryParam("providerID") String providerID) {
        Log.info("REST service '{}' was invoked for patient: " + providerID + "/" + patientID, "fillParams");
        String result;
        
        try {
            result = this.service.fillParams(patientID, providerID, input);
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
    
    
    @POST
    @Path("/{ihelpID}")
    @ApiOperation(value="Fills the model manager parameters from the database", notes="based on the ihelpID")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response fillParams(
            @ApiParam(value = "the parameters to fill", required = true) 
            String input,
            @ApiParam(value = "the ihelpID", required = true) 
            @PathParam("ihelpID") String ihelpID) {
        Log.info("REST service '{}' was invoked for patient: " + ihelpID + " with input:\n{}", "fillParams", input);
        String result;
        
        try {
            result = this.service.fillParams(ihelpID, input);
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
