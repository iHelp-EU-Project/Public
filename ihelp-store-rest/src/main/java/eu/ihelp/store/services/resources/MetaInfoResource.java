package eu.ihelp.store.services.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import eu.ihelp.store.server.exceptions.DataTableNotFoundException;
import eu.ihelp.store.server.utils.ErrorMessageResponser;
import eu.ihelp.store.server.utils.JsonSerializers;
import eu.ihelp.store.services.MetaInfoService;
import eu.ihelp.store.services.impl.MetaInfoServiceImpl;
import eu.ihelp.store.services.model.ColumnInfo;
import eu.ihelp.store.services.model.IndexInfo;
import java.util.List;
import javax.ws.rs.GET;
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
@Path("/metainfo")
@Api(basePath="http://localhost:54735/ihelp", value = "/metainfo", description = "REST Service to get meta information of the database")
public class MetaInfoResource {
    private static final Logger Log = LoggerFactory.getLogger(MetaInfoResource.class);
    
    private final MetaInfoService service = new MetaInfoServiceImpl();
    
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
    @ApiOperation(value="Get table names", 
            notes="Returns a list of table names", responseContainer = "List", response = String.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response getTableNames() {
        Log.info("Running web method: {}", "getTableNames");
        String result;
        try {
            List<String> list = service.getTables();
            result = JsonSerializers.serialize(list);
        } catch(Exception ex) {
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    @GET
    @Path("/{table}/columns")
    @ApiOperation(value="Get the columns of a table", 
            notes="Returns a list of columns along with their types", responseContainer = "List", response = ColumnInfo.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response getColumns(
            @ApiParam(value = "the name of the table", required = true) 
            @PathParam("table") String table
    ) {
        Log.info("Running web method: {}", "getColumns");
        String result;
        try {
            List<ColumnInfo> list = service.getColumntInfo(table);
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof DataTableNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse("Table " + table + " does not exist", Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
    @GET
    @Path("/{table}/indexes")
    @ApiOperation(value="Get the indexes of a table", 
            notes="Returns a list of indexes along with their types", responseContainer = "List", response = IndexInfo.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response getIndexes(
            @ApiParam(value = "the name of the table", required = true) 
            @PathParam("table") String table
    ) {
        Log.info("Running web method: {}", "getIndexes");
        String result;
        try {
            List<IndexInfo> list = service.getIndexInfo(table);
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof DataTableNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse("Table " + table + " does not exist", Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
    @GET
    @Path("/{table}/primarykey")
    @ApiOperation(value="Get the primary key of a table", 
            notes="Returns a list of fiels along with their types that consists the table's primary key", responseContainer = "List", response = ColumnInfo.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response getPrimaryKey(
            @ApiParam(value = "the name of the table", required = true) 
            @PathParam("table") String table
    ) {
        Log.info("Running web method: {}", "getPrimaryKey");
        String result;
        try {
            List<ColumnInfo> list = service.getPrimaryKey(table);
            result = JsonSerializers.serialize(list, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof DataTableNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse("Table " + table + " does not exist", Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
}
