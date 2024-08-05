package eu.ihelp.enrollment.services.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import eu.ihelp.enrollment.services.TestService;
import eu.ihelp.enrollment.services.impl.TestServiceImpl;

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
@Path("/test")
@Api(basePath="http://localhost:54735/ihelp", value = "/test", description = "REST Service to test that the services are running")
public class TestResource {
    private static final Logger Log = LoggerFactory.getLogger(TestResource.class);
    
    private final TestService service = new TestServiceImpl();
    
    
    @GET
    @ApiOperation(value="Testing operation", 
            notes="A hello world message", response = String.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response test() {
        Log.info("Running web method: {}", "test");
        return Response.ok(service.hello()).build();
    }
    
    @GET
    @Path("/{name}")
    @ApiOperation(value="Testing operation", 
            notes="A hello world message", response = String.class)
    @Produces(MediaType.TEXT_HTML) 
    public Response test(
            @ApiParam(value = "the name of the data model", required = true) 
            @PathParam("name") String name
    ) {
        Log.info("Running web method: {}", "test");
        return Response.ok(service.hello(name)).build();
    }
}
