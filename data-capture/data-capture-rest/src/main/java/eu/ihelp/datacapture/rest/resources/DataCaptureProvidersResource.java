package eu.ihelp.datacapture.rest.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.datacapture.commons.utils.JsonSerializers;
import eu.ihelp.datacapture.manager.services.DataCaptureProvidersService;
import eu.ihelp.datacapture.manager.services.impl.DataCaptureProvidersServiceImpl;
import eu.ihelp.datacapture.rest.server.utils.ErrorMessageResponser;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@Path("/datacapture/dataproviders")
@Api(basePath="http://localhost:54735/ihelp", value = "/datacapture/dataproviders", description = "REST Service to get supported data providers")
public class DataCaptureProvidersResource {
    private static final Logger Log = LoggerFactory.getLogger(DataCaptureProvidersResource.class);
    
    
    private final DataCaptureProvidersService service;
    
    /**
     * Creates a new instance of DataCaptureResource
     */
    public DataCaptureProvidersResource() {
        //this.service = DataCaptureServiceMockImpl.getInstance();
        this.service = DataCaptureProvidersServiceImpl.getInstance();
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
     * Returns all data providers
     * 
     * @return 
     */
    @GET
    @ApiOperation(value="Returns the full list of all data providers", notes="Returns the full list of all data providers", 
            responseContainer = "List", response = String.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getDataProviders() {
        Log.debug("REST service '{}' was invoked", "getDataProviders");
        String result;
        
        try {
            List<String> list = this.service.getDataProviders();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Returns all primary data providers
     * 
     * @return 
     */
    @GET
    @Path("/primary")
    @ApiOperation(value="Returns the full list of all primary data providers", notes="Returns the full list of all primary data providers", 
            responseContainer = "List", response = String.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getPrimaryDataProviders() {
        Log.debug("REST service '{}' was invoked", "getPrimaryDataProviders");
        String result;
        
        try {
            List<String> list = this.service.getPrimaryDataProviders();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Returns all secondary data providers
     * 
     * @return 
     */
    @GET
    @Path("/secondary")
    @ApiOperation(value="Returns the full list of all secondary data providers", notes="Returns the full list of all secondary data providers", 
            responseContainer = "List", response = String.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getSecondaryDataProviders() {
        Log.debug("REST service '{}' was invoked", "getSecondaryDataProviders");
        String result;
        
        try {
            List<String> list = this.service.getSecondaryDataProviders();
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
}
