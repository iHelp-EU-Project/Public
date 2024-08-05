package eu.ihelp.enrollment.services.impl;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.HealthentiaPatientExistsException;
import eu.ihelp.enrollment.exceptions.IhelpPatientNotFoundException;
import eu.ihelp.enrollment.exceptions.ImporterException;
import eu.ihelp.enrollment.exceptions.MissingInputParametersException;
import eu.ihelp.enrollment.exceptions.PatientNotFoundException;
import eu.ihelp.enrollment.managers.IhelpManager;
import eu.ihelp.enrollment.managers.PatientManager;
import eu.ihelp.enrollment.model.IhelpDTO;
import eu.ihelp.enrollment.model.IhelpPatientDTO;
import eu.ihelp.enrollment.model.InsertPatientDTO;
import eu.ihelp.enrollment.model.MUPRimaryData;
import eu.ihelp.enrollment.model.MassiveUserEnrollmentDTO;
import eu.ihelp.enrollment.model.MassiveUserEnrollmentPatientDTO;
import eu.ihelp.enrollment.model.PatientDTO;
import eu.ihelp.enrollment.services.UserEnrollmentService;
import eu.ihelp.enrollment.services.utils.DataCaptureClient;
import eu.ihelp.enrollment.services.utils.DataCaptureClientBuilder;
import eu.ihelp.enrollment.services.utils.MUPPrimaryDataParser;
import eu.ihelp.enrollment.services.utils.MUPPrimaryDataParserBuilder;
import eu.ihelp.enrollment.utils.Consts;
import eu.ihelp.enrollment.utils.DBAHelper;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class UserEnrollmentServiceImpl implements UserEnrollmentService {
    private static final Logger Log = LoggerFactory.getLogger(UserEnrollmentServiceImpl.class);
    
    private UserEnrollmentServiceImpl() {}

    private static final class UserEnrollmentServiceImplHolder {
        private static final UserEnrollmentServiceImpl INSTANCE = new UserEnrollmentServiceImpl();
    }
    
    public static UserEnrollmentServiceImpl getInstance() {
        return UserEnrollmentServiceImplHolder.INSTANCE;
    }
    
    @Override
    public List<IhelpPatientDTO> massiveEnrollPatients(MassiveUserEnrollmentDTO massiveUserEnrollmentDTO) throws MissingInputParametersException, DataStoreException {
        List<String> listOfIhelpIDs = new ArrayList<>(massiveUserEnrollmentDTO.getPatients().size());
        List<IhelpDTO> ihelpIdRecords = new ArrayList<>(massiveUserEnrollmentDTO.getPatients().size()*2);
        for(MassiveUserEnrollmentPatientDTO patientToEnroll : massiveUserEnrollmentDTO.getPatients()) {
            try {
                listOfIhelpIDs.add(massiveEnrollPatient(patientToEnroll));
            } catch(Exception ex) {
                Log.error("Could not add {}. {}:{}", patientToEnroll, ex.getClass().getName(), ex.getMessage());
            }
        } 
        
        ihelpIdRecords = IhelpManager.getIhelpPersons(listOfIhelpIDs);
        
        //now return the list of newly enrolled patients
        return getIhelpPatientDTOFromIhelpIdRecords(ihelpIdRecords);
    }
    
    private List<IhelpPatientDTO> getIhelpPatientDTOFromIhelpIdRecords(List<IhelpDTO> ihelpIdRecords) {
        HashMap<String, List<PatientDTO>> map = new HashMap<>();
        for(IhelpDTO ihelpDTO : ihelpIdRecords) {
            if(map.containsKey(ihelpDTO.getiHelpID())) {
                map.get(ihelpDTO.getiHelpID()).add(new PatientDTO(ihelpDTO.getPatientID(), ihelpDTO.getProviderID(), null, null, null));
            } else {
                List<PatientDTO> list = new ArrayList<>(2);
                list.add(new PatientDTO(ihelpDTO.getPatientID(), ihelpDTO.getProviderID(), null, null, null));
                map.put(ihelpDTO.getiHelpID(), list);
            }
        }
        
        List<IhelpPatientDTO> result = new ArrayList<>(map.size());
        for (Map.Entry<String, List<PatientDTO>> entry : map.entrySet()) {
            result.add(new IhelpPatientDTO(entry.getKey(), entry.getValue()));
        }
        
        return result;
    }
    
    private String massiveEnrollPatient(MassiveUserEnrollmentPatientDTO insertPatientDTO) throws MissingInputParametersException, DataStoreException {
        if(!valueExists(insertPatientDTO.getPatientID())) {
            throw new MissingInputParametersException("patientID");
        }
        if(!valueExists(insertPatientDTO.getProviderID())) {
            throw new MissingInputParametersException("providerID");
        }
        Connection connection = null;
        String ihelpIDResult = null;
        try {
            connection = DBAHelper.getInstance().getConnection();
            connection.setAutoCommit(false);
            
            //get/set patient for given provider
            PatientDTO patient = new PatientDTO(insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID(), insertPatientDTO.getActive(), insertPatientDTO.getBirthdate(), insertPatientDTO.getGender());
            if(PatientManager._getPatient(connection, patient.getPatientID(), patient.getProviderID())==null) {
                PatientManager._insertPatient(connection, patient);
            } else {
                PatientManager._updatePatient(connection, patient);
            }
            
            //get/set healthentia patient
            if(PatientManager._getPatient(connection, insertPatientDTO.getHealthentiaID(), Consts.HEALTHENTIA_PROVIDER)==null) {
                PatientManager._insertPatient(connection, new PatientDTO(insertPatientDTO.getHealthentiaID(), Consts.HEALTHENTIA_PROVIDER, null, null, null));
            }
        
            
            //check if iHelpID exists for given provider
            IhelpDTO providerIheplID = IhelpManager._getIhelpPerson(connection, insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID());
            if(providerIheplID!=null) {
                //check if iHelpID exists for healthentia
                IhelpDTO healthentiaIhelpID = IhelpManager._getIhelpPerson(connection, insertPatientDTO.getHealthentiaID(), Consts.HEALTHENTIA_PROVIDER);
                
                if(healthentiaIhelpID!=null) { //if both exists, then return the iHelpID
                    ihelpIDResult = healthentiaIhelpID.getiHelpID();
                } else {//else add the healthentia IhelpID and then return this id
                    IhelpManager._addIhelpPerson(connection, providerIheplID.getiHelpID(), insertPatientDTO.getHealthentiaID(), Consts.HEALTHENTIA_PROVIDER);
                    ihelpIDResult = providerIheplID.getiHelpID();
                }
            } else {
                //ihelpID does not exist for the given provider
                //check if iHelpID exists for healthentia
                IhelpDTO healthentiaIhelpID = null;
                
                if((insertPatientDTO.getHealthentiaID()!=null)&&(!insertPatientDTO.getHealthentiaID().equalsIgnoreCase(""))) {
                    IhelpManager._getIhelpPerson(connection, insertPatientDTO.getHealthentiaID(), Consts.HEALTHENTIA_PROVIDER);
                }
                
                if(healthentiaIhelpID!=null) {//if ihelpID exists for healthentia (but not for provider)
                    //then add the provider iHelpID and return the id
                    IhelpManager._addIhelpPerson(connection, healthentiaIhelpID.getiHelpID(), insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID());
                    ihelpIDResult = healthentiaIhelpID.getiHelpID();
                } else {//no ihelp is there (neither for provider, nor for healthentia
                    //then add both
                    //String ihelpID = UUID.randomUUID().toString();
                    String ihelpID = generateIhelpID();
                    IhelpManager._addIhelpPerson(connection, ihelpID, insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID());
                    if((insertPatientDTO.getHealthentiaID()!=null)&&(!insertPatientDTO.getHealthentiaID().equalsIgnoreCase(""))) {
                        IhelpManager._addIhelpPerson(connection, ihelpID, insertPatientDTO.getHealthentiaID(), Consts.HEALTHENTIA_PROVIDER);
                    }
                    ihelpIDResult = ihelpID;
                }
            }
        
            connection.commit();
        } catch(SQLException ex) {
            throw new DataStoreException(ex.getMessage(), ex);
        } finally {
            if(connection!=null) {
                try {connection.close();} catch(SQLException ex) {/*DO NOTHING*/} 
            }
        }
        
        return ihelpIDResult;
    }

    @Override
    public IhelpPatientDTO insertPatient(InsertPatientDTO insertPatientDTO) throws MissingInputParametersException, DataStoreException {
        if(!valueExists(insertPatientDTO.getPatientID())) {
            throw new MissingInputParametersException("patientID");
        }
        
        if(!valueExists(insertPatientDTO.getProviderID())) {
            throw new MissingInputParametersException("providerID");
        }
        
        
        PatientDTO patient = new PatientDTO(insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID(), insertPatientDTO.getActive(), insertPatientDTO.getBirthdate(), insertPatientDTO.getGender());
        if(PatientManager.getPatient(patient.getPatientID(), patient.getProviderID())==null) {
            PatientManager.insertPatient(patient);
        } else {
            PatientManager.updatePatient(patient);
        }
        
        List<IhelpDTO> ihelpDTOs;
        if(valueExists(insertPatientDTO.getiHelpID())) { //ihelpID is provided
            ihelpDTOs = IhelpManager.addIhelpPerson(insertPatientDTO.getiHelpID(), patient.getPatientID(), patient.getProviderID());
        } else { //ihelpID is not provided
            ihelpDTOs = IhelpManager.addIhelpPerson(generateIhelpID(), patient.getPatientID(), patient.getProviderID());
        }
        
        return getIhelpPatientDTO(ihelpDTOs);
    }
    
    @Override
    public void deleteIhelpPatient(String iHelpID) throws IhelpPatientNotFoundException, DataStoreException {
        IhelpManager.deleteIhelpPerson(iHelpID);
    }
    
    @Override
    public IhelpPatientDTO addsHealthentiaAccoountTopatient(String ihelpID, String healthentiaID) throws PatientNotFoundException, IhelpPatientNotFoundException, DataStoreException, HealthentiaPatientExistsException {
        List<IhelpDTO> ihelpDTOs = IhelpManager.getIhelpPersons(ihelpID);
        if(ihelpDTOs.isEmpty()) {
            throw new IhelpPatientNotFoundException(ihelpID);
        }
        
        if(PatientManager.getPatient(healthentiaID, "HEALTHENTIA")==null) {
            throw new PatientNotFoundException(healthentiaID, "HEALTHENTIA");
        }
        
        for(IhelpDTO ihelpDTO : ihelpDTOs) {
            if(ihelpDTO.getProviderID().equalsIgnoreCase("HEALTHENTIA")) {
                throw new HealthentiaPatientExistsException(ihelpID, healthentiaID);
            }
        }
        
        ihelpDTOs = IhelpManager.addIhelpPerson(ihelpID, healthentiaID, "HEALTHENTIA");
        
        return getIhelpPatientDTO(ihelpDTOs);
    }
    

    @Override
    public IhelpPatientDTO getIhelpPatient(String iHelpID) throws IhelpPatientNotFoundException, DataStoreException {
        List<IhelpDTO> ihelpDTOs = IhelpManager.getIhelpPersons(iHelpID);
        if(ihelpDTOs.isEmpty()) {
            throw new IhelpPatientNotFoundException(iHelpID);
        }
        
        return getIhelpPatientDTO(ihelpDTOs);
    }

    @Override
    public void updatePatient(PatientDTO patientDTO) throws DataStoreException {
        PatientManager.updatePatient(patientDTO);
    }

    @Override
    public IhelpPatientDTO addPatient(String iHelpID, InsertPatientDTO insertPatientDTO) throws IhelpPatientNotFoundException, MissingInputParametersException, DataStoreException, PatientNotFoundException {
        if(!valueExists(insertPatientDTO.getPatientID())) {
            throw new MissingInputParametersException("patientID");
        }
        
        if(!valueExists(insertPatientDTO.getProviderID())) {
            throw new MissingInputParametersException("providerID");
        }
        
        List<IhelpDTO> ihelpDTOs = IhelpManager.getIhelpPersons(iHelpID);
        if(ihelpDTOs.isEmpty()) {
            throw new IhelpPatientNotFoundException(iHelpID);
        }
        
        if(PatientManager.getPatient(insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID())==null) {
            throw new PatientNotFoundException(insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID());
        }
        
        ihelpDTOs = IhelpManager.addIhelpPerson(iHelpID, insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID());
        
        return getIhelpPatientDTO(ihelpDTOs);
    }
    
    @Override
    public IhelpPatientDTO ingestsEnrollsPrimaryDataForMUP(MUPRimaryData data) throws DataStoreException, ImporterException, MissingInputParametersException {
        validateingestsEnrollsPrimaryDataForMUP(data);
        
        try {
            MUPPrimaryDataParser parser = MUPPrimaryDataParserBuilder.createInstance(data);
            DataCaptureClient dataCaptureClient = DataCaptureClientBuilder.createInstance(parser);
            dataCaptureClient.ingestData();
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
            ex.printStackTrace();
            throw new ImporterException(ex.getMessage(), ex);
        }
        
        List<IhelpDTO> ihelpDTOs;
        IhelpDTO ihelpDTO = IhelpManager.getIhelpPerson(data.getPatient_id().toString(), "MUP");
        if(ihelpDTO==null) {
            ihelpDTOs = IhelpManager.addIhelpPerson(generateIhelpID(), data.getPatient_id().toString(), "MUP");
        } else {
            ihelpDTOs = Arrays.asList(ihelpDTO);
        }
        
        return getIhelpPatientDTO(ihelpDTOs);
    }
    
    private void validateingestsEnrollsPrimaryDataForMUP(MUPRimaryData data) throws MissingInputParametersException {
        if(data.getPatient_id()==null) {
            throw new MissingInputParametersException("patient_id");
        }
        
        if(data.getGender()==null) {
            throw new MissingInputParametersException("gender");
        }
        
        if(data.getAge()==null) {
            throw new MissingInputParametersException("age");
        }
        
        //now there is the need for this check, it should allow if indexes are empty
        if((data.getIndexes()==null) || (data.getIndexes().isEmpty())) {
            throw new MissingInputParametersException("indexes");
        }
    }

    @Override
    public IhelpPatientDTO removePatient(String iHelpID, InsertPatientDTO insertPatientDTO) throws MissingInputParametersException, IhelpPatientNotFoundException, DataStoreException {
        if(!valueExists(insertPatientDTO.getPatientID())) {
            throw new MissingInputParametersException("patientID");
        }
        
        if(!valueExists(insertPatientDTO.getProviderID())) {
            throw new MissingInputParametersException("providerID");
        }
        
        List<IhelpDTO> ihelpDTOs = IhelpManager.getIhelpPersons(iHelpID);
        if(ihelpDTOs.isEmpty()) {
            throw new IhelpPatientNotFoundException(iHelpID);
        }
        
        ihelpDTOs = IhelpManager.removeIhelpPerson(iHelpID, insertPatientDTO.getPatientID(), insertPatientDTO.getProviderID());
        
        return getIhelpPatientDTO(ihelpDTOs);
    }
    
    private IhelpPatientDTO getIhelpPatientDTO(List<IhelpDTO> ihelpDTOs) throws DataStoreException {
        IhelpPatientDTO resultDTO = new IhelpPatientDTO(ihelpDTOs.get(0).getiHelpID());
        for(IhelpDTO ihelpDTO : ihelpDTOs) {
            PatientDTO patientDTO = PatientManager.getPatient(ihelpDTO.getPatientID(), ihelpDTO.getProviderID());
            if(patientDTO!=null) {
                resultDTO.getPatients().add(patientDTO);
            }
        }
        
        return resultDTO;
    }
    
    private boolean valueExists(String value) {
        return !((value==null) || (value.isEmpty()));
    }
    
    private String generateIhelpID() {
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }
}
