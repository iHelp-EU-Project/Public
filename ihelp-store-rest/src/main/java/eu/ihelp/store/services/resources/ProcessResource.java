package eu.ihelp.store.services.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import eu.ihelp.store.server.utils.ErrorMessageResponser;
import eu.ihelp.store.server.utils.JsonSerializers;
import eu.ihelp.store.services.ProcessService;
import eu.ihelp.store.services.impl.ProcessServiceImpl;
import eu.ihelp.store.services.model.DataRow;
import eu.ihelp.store.services.model.DataTable;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@Path("/process")
@Api(basePath="http://localhost:54735/ihelp", value = "/process", description = "REST Service to get data from the database")
public class ProcessResource {
    private static final Logger Log = LoggerFactory.getLogger(ProcessResource.class);
    
    private final ProcessService service = new ProcessServiceImpl();
    
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
    
    @GET
    @ApiOperation(value="Executs a query", 
            notes="Returns a list of data rows", responseContainer = "List", response = DataRow.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response executeQuery(
            @ApiParam(value = "the query to execute", required = true) 
            @QueryParam("query") String query
    ) {
        Log.info("Running web method: {}", "executeQuery");
        String result;
        try {
            List<DataRow> queryResult = service.processQuery(query);
            result = JsonSerializers.serialize(queryResult, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    @GET
    @Path("/id/{ihelpid}")
    @ApiOperation(value="Gets all data of a user", 
            notes="expects an ihelpID", responseContainer = "List", response = DataTable.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response getAllDataOfUser(
            @ApiParam(value = "the id of the user", required = true) 
            @PathParam("ihelpid") String ihelpid
    ) {
        Log.info("Running web method: {} with id {}", "getAllDataOfUser", ihelpid);
        String result;
        try {
            List<DataTable> queryResult = service.getAllData(ihelpid);
            result = JsonSerializers.serialize(queryResult, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
    
    @GET
    @Path("/primary/{ihelpid}")
    @ApiOperation(value="Gets all primary data of a user", 
            notes="expects an ihelpID")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getAllPrimaryDataOfUser(
            @ApiParam(value = "the id of the user", required = true) 
            @PathParam("ihelpid") String ihelpid
    ) {
        Log.info("Running web method: {} with id {}", "getAllPrimaryDataOfUser", ihelpid);
        String result;
        try {
            JSONArray queryResult = service.getPrimaryData(ihelpid);
            result = queryResult.toString();
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    @GET
    @Path("/secondary/{ihelpid}")
    @ApiOperation(value="Gets all secondary data of a user", 
            notes="expects an ihelpID")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getAllSecondaryDataOfUser(
            @ApiParam(value = "the id of the user", required = true) 
            @PathParam("ihelpid") String ihelpid
    ) {
        Log.info("Running web method: {} with id {}", "getAllSecondaryDataOfUser", ihelpid);
        String result;
        try {
            JSONArray queryResult = service.getSecondary(ihelpid);
            result = queryResult.toString();
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
    
    
    
}
