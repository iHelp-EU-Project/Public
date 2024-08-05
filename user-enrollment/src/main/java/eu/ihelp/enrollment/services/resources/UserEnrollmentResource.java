package eu.ihelp.enrollment.services.resources;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import eu.ihelp.enrollment.exceptions.HealthentiaPatientExistsException;
import eu.ihelp.enrollment.exceptions.IhelpPatientNotFoundException;
import eu.ihelp.enrollment.exceptions.JsonSerializationException;
import eu.ihelp.enrollment.exceptions.MissingInputParametersException;
import eu.ihelp.enrollment.exceptions.PatientNotFoundException;
import eu.ihelp.enrollment.model.IhelpPatientDTO;
import eu.ihelp.enrollment.model.InsertPatientDTO;
import eu.ihelp.enrollment.model.MUPRimaryData;
import eu.ihelp.enrollment.model.MassiveUserEnrollmentDTO;
import eu.ihelp.enrollment.model.PatientDTO;
import eu.ihelp.enrollment.services.UserEnrollmentService;
import eu.ihelp.enrollment.services.impl.UserEnrollmentServiceImpl;
import eu.ihelp.enrollment.utils.ErrorMessageResponser;
import eu.ihelp.enrollment.utils.JsonSerializers;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("/userenrollment")
@Api(basePath="http://localhost:54735/ihelp", value = "/userenrollment", description = "REST Service for the user enrollment of the iHelp platform")
public class UserEnrollmentResource {
    private static final Logger Log = LoggerFactory.getLogger(UserEnrollmentResource.class);
    
    private final UserEnrollmentService service;

    public UserEnrollmentResource() {
        this.service = UserEnrollmentServiceImpl.getInstance();
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
    
    /**
     * Returns the ihelp by its id
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value="Returns an iHelp person based on its id", notes="Returns an iHelp person based on its id", 
            response = IhelpPatientDTO.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response getIhelpID(
            @ApiParam(value = "the id of the iHelp person", required = true) 
            @PathParam("id") String id
    ) {
        Log.info("REST service '{}' was invoked", "getIhelpID");
        String result;
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            IhelpPatientDTO dto = this.service.getIhelpPatient(id);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof IhelpPatientNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Returns the ihelp by its id
     * 
     * @param ihelpID
     * @param healthentiaID
     * @return 
     */
    @GET
    @Path("/add/{id}")
    @ApiOperation(value="Adds a heathentia ID to the given iHelp person", notes="Returns an iHelp person based on its id", 
            response = IhelpPatientDTO.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addsHealthentiaAccoountTopatient(
            @ApiParam(value = "the id of the iHelp person - iHelpID", required = true) 
            @PathParam("id") String ihelpID,
            @ApiParam(value = "the healthentiaID to add", required = true) 
            @QueryParam("healthentiaid") String healthentiaID
    ) {
        Log.info("REST service '{}' was invoked", "addsHealthentiaAccoountTopatient");
        String result;
        
        if((ihelpID==null) || (ihelpID.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("ihelpID cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        if((healthentiaID==null) || (healthentiaID.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("healthentiaID cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            IhelpPatientDTO dto = this.service.addsHealthentiaAccoountTopatient(ihelpID, healthentiaID);
            result = JsonSerializers.serialize(dto, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if(ex instanceof IhelpPatientNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            if(ex instanceof PatientNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            if(ex instanceof HealthentiaPatientExistsException) {
                return ErrorMessageResponser.buildErrorResponse("Healthentia patient already exists for this ihelpID. " + ex.getMessage(), Response.Status.BAD_REQUEST);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    
    /**
     * Delete the ihelp by its id
     * 
     * @param id
     * @return 
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation(value="Delete an iHelp person based on its id", notes="Delete an iHelp person based on its id", 
            response = IhelpPatientDTO.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response deleteIhelpID(
            @ApiParam(value = "the id of the iHelp person", required = true) 
            @PathParam("id") String id
    ) {
        Log.info("REST service '{}' was invoked", "deleteIhelpID");
        
        if((id==null) || (id.isEmpty())) {
            return ErrorMessageResponser
                    .buildErrorResponse("id cannot be empty", Response.Status.BAD_REQUEST);
        }
        
        try {
            this.service.deleteIhelpPatient(id);
        } catch(Exception ex) {
            if(ex instanceof IhelpPatientNotFoundException) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok().build();
    }
    
    
    
    /**
     * Submits a patient to enroll
     * 
     * @param input
     * @return 
     */
    @POST
    @ApiOperation(value="Submits a new patient to enroll", notes="Returns iHelpPatient", 
            response = IhelpPatientDTO.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response insertPatient(
            @ApiParam(value = "the ihelppatient to submit", required = true) 
            String input) {
        Log.info("REST service '{}' was invoked", "insertPatient");
        String result;
        
        try {
            InsertPatientDTO inputDTO = JsonSerializers.deserialize(input, InsertPatientDTO.class);
            IhelpPatientDTO resultDTO= this.service.insertPatient(inputDTO);
            result = JsonSerializers.serialize(resultDTO, PropertyAccessor.GETTER);
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
    
    /**
     * Ingests a new patient for MUP and enrolls it
     * 
     * @param input
     * @return 
     */
    @POST
    @Path("/ingest/mup")
    @ApiOperation(value="Inserts a new MUP patient to ingest its data and enrolls him/her", notes="Returns iHelpPatient", 
            response = IhelpPatientDTO.class)
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response ingestEnrollMUPPatient(
            @ApiParam(value = "the primary data to ingest", required = true) 
            String input) {
        Log.info("REST service '{}' was invoked with this input: {}", "ingestEnrollMUPPatient", input);
        String result;
        
        try {
            MUPRimaryData inputDTO = JsonSerializers.deserialize(input, MUPRimaryData.class);
            IhelpPatientDTO resultDTO= this.service.ingestsEnrollsPrimaryDataForMUP(inputDTO);
            result = JsonSerializers.serialize(resultDTO, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            Log.error("Error executing the {} method. {}: {}", "ingestEnrollMUPPatient", ex.getClass().getName(), ex.getMessage());
            if((ex instanceof JsonSerializationException) ||
               (ex instanceof MissingInputParametersException)     
                    ) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
            }
            
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    /**
     * Submits a list of patients to enroll
     * 
     * @param input
     * @return 
     */
    @POST
    @Path("/massive")
    @ApiOperation(value="Submits a list of patient to enroll", notes="Returns a list of iHelpPatient", 
            response = IhelpPatientDTO.class, responseContainer = "List")
    @ApiResponses(value= {
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response massiveEnrollPatients(
            @ApiParam(value = "the list of patients to submit", required = true) 
            String input) {
        Log.info("REST service '{}' was invoked with input {}", "massiveEnrollPatients", input);
        String result;
        
        try {
            MassiveUserEnrollmentDTO inputDTO = JsonSerializers.deserialize(input, MassiveUserEnrollmentDTO.class);
            List<IhelpPatientDTO> resultDTO= this.service.massiveEnrollPatients(inputDTO);
            result = JsonSerializers.serialize(resultDTO, PropertyAccessor.GETTER);
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
    
    /**
     * Updates a patient
     * 
     * @param input
     * @return 
     */
    @PUT
    @ApiOperation(value="Updates a patient")
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response updatePatient(
            @ApiParam(value = "the patient to update", required = true) 
            String input) {
        Log.info("REST service '{}' was invoked", "updatePatient");
        
        try {
            PatientDTO dto = JsonSerializers.deserializeWithAnyVisibility(input, PatientDTO.class);
            this.service.updatePatient(dto);
        } catch(Exception ex) {
            if((ex instanceof JsonSerializationException) ||
               (ex instanceof MissingInputParametersException)     
                    ) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok().build();
    }
    
    /**
     * adds/removes a patient to an existed iHelpID
     * 
     * @param input
     * @param id
     * @param actionStr
     * @return 
     */
    @POST
    @Path("/{id}")
    @ApiOperation(value="Adds or removes a new patient to enroll to existing iHelpID", notes="Returns iHelpPatient", 
            response = IhelpPatientDTO.class)
    @ApiResponses(value= {
        @ApiResponse(code=400, message="Bad Request"),
        @ApiResponse(code=404, message="Not Found"),
        @ApiResponse(code=500, message="Exception's message")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("UseSpecificCatch")
    public Response addsPatientToIhelpID(
            @ApiParam(value = "the ihelppatient to submit", required = true) 
            String input,
            @ApiParam(value = "the id of the iHelp person", required = true) 
            @PathParam("id") String id,
            @ApiParam(value = "should be either 'add' or 'remove'", required = true) 
            @QueryParam("action") String actionStr
            ) {
        Log.info("REST service '{}' was invoked", "addsPatientToIhelpID");
        String result;
        
        
        
        try {
            Action action = Action.valueOf(actionStr.toLowerCase());
            
            InsertPatientDTO inputDTO = JsonSerializers.deserialize(input, InsertPatientDTO.class);
            IhelpPatientDTO resultDTO = null;
            
            switch (action) {
                case add:
                    resultDTO = this.service.addPatient(id, inputDTO);
                    break;
                case remove:
                    resultDTO = this.service.removePatient(id, inputDTO);
                    break;
                default:
                    throw new AssertionError();
            }
            
            
            result = JsonSerializers.serialize(resultDTO, PropertyAccessor.GETTER);
        } catch(Exception ex) {
            if((ex instanceof IhelpPatientNotFoundException) || (ex instanceof PatientNotFoundException)) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.NOT_FOUND);
            }
            if((ex instanceof JsonSerializationException) ||
               (ex instanceof MissingInputParametersException)     
                    ) {
                return ErrorMessageResponser.buildErrorResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
            }
            return ErrorMessageResponser.buildErrorResponse(ex);
        }
        
        return Response.ok(result).build();
    }
    
    private enum Action {add, remove}
}
