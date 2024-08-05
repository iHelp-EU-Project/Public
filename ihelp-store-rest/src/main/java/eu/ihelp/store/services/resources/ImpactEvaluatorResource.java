package eu.ihelp.store.services.resources;//

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import eu.ihelp.store.server.utils.ErrorMessageResponser;
import eu.ihelp.store.services.ImpactEvaluatorService;
import eu.ihelp.store.services.impl.ImpactEvaluatorServiceImpl;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@Path("/baseR4")
@Api(basePath="http://localhost:54735/ihelp", value = "/baseR4", description = "REST Service to get data from the database")
public class ImpactEvaluatorResource {
    private static final Logger Log = LoggerFactory.getLogger(ImpactEvaluatorResource.class);
    
    private final ImpactEvaluatorService service;
    
    public ImpactEvaluatorResource() {
        this.service = new ImpactEvaluatorServiceImpl();
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
    
    @GET
    @Path("/metadata")
    @ApiOperation(value="gets the metadata of this FHIR server", 
            notes="always returns the same static file to allow FHIR clients to proceed after firstly connecting", response = String.class)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getMetaData(
    ) {
        Log.info("Running web method: {}", "getMetaData");        
        JSONObject jSONObject;        
        try {
            jSONObject = this.service.getMetaData();
        } catch(Exception ex) {
            Log.error("Exception ex: {}", ex);
            return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        
        return Response.ok(jSONObject.toString()).build();
    }
    
    @GET
    @Path("/Communication")
    @ApiOperation(value="Operation to get communications", 
            notes="Operation to get communications", response = String.class)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getCommunications(
            @QueryParam("sent") List<String> sent,
            @QueryParam("_include") List<String> _include
    ) {
        Log.info("Running web method: {}", "getCommunications");
        String jSONObject;        
        try {
            jSONObject = this.service.getCommunications(sent, _include);
        } catch(Exception ex) {
            Log.error("Exception ex: {}", ex);
            return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        
        return Response.ok(jSONObject).build();
    }
    
    @GET
    @Path("/Observation")
    @ApiOperation(value="Operation to get observations", 
            notes="Operation to get observations", response = String.class)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getObservations(
            @QueryParam("date") List<String> date,
            @QueryParam("subject") String subject,
            @QueryParam("combo-code") String combocode
    ) {
        Log.info("Running web method: {}", "getObservations");
        String jSONObject;        
        try {
            jSONObject = this.service.getObservations(date, subject, combocode);
        } catch(Exception ex) {
            Log.error("Exception ex: {}", ex);
            return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        
        return Response.ok(jSONObject).build();
    }
    
    @GET
    @Path("/QuestionnaireResponse")
    @ApiOperation(value="Operation to get questionnaire responses", 
            notes="Operation to get questionnaire responses", response = String.class)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getQuestionnaireResponse(
            @QueryParam("authored") List<String> date,
            @QueryParam("subject") String subject,
            @QueryParam("questionnaire") String questionnaire
    ) {
        Log.info("Running web method: {}", "getQuestionnaireResponse");
        String jSONObject;        
        try {
            jSONObject = this.service.getQuestionnaireResponses(date, subject, questionnaire);
        } catch(Exception ex) {
            Log.error("Exception ex: {}", ex);
            return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        
        return Response.ok(jSONObject).build();
    }
    
    
}
