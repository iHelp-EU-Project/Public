package eu.ihelp.enrollment.services;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.HealthentiaPatientExistsException;
import eu.ihelp.enrollment.exceptions.IhelpPatientNotFoundException;
import eu.ihelp.enrollment.exceptions.ImporterException;
import eu.ihelp.enrollment.exceptions.MissingInputParametersException;
import eu.ihelp.enrollment.exceptions.PatientNotFoundException;
import eu.ihelp.enrollment.model.IhelpPatientDTO;
import eu.ihelp.enrollment.model.InsertPatientDTO;
import eu.ihelp.enrollment.model.MUPRimaryData;
import eu.ihelp.enrollment.model.MassiveUserEnrollmentDTO;
import eu.ihelp.enrollment.model.PatientDTO;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface UserEnrollmentService {
    
    public IhelpPatientDTO ingestsEnrollsPrimaryDataForMUP(MUPRimaryData data) throws DataStoreException, ImporterException, MissingInputParametersException;
    
    public IhelpPatientDTO addsHealthentiaAccoountTopatient(String ihelpID, String healthentiaID) throws PatientNotFoundException, IhelpPatientNotFoundException, DataStoreException, HealthentiaPatientExistsException;
    
    public IhelpPatientDTO insertPatient(InsertPatientDTO insertPatientDTO) throws MissingInputParametersException, DataStoreException;
    
    public List<IhelpPatientDTO> massiveEnrollPatients(MassiveUserEnrollmentDTO massiveUserEnrollmentDTO) throws MissingInputParametersException, DataStoreException; 
    
    public IhelpPatientDTO getIhelpPatient(String iHelpID) throws IhelpPatientNotFoundException, DataStoreException;
    
    public void deleteIhelpPatient(String iHelpID) throws IhelpPatientNotFoundException, DataStoreException;
     
    public void updatePatient(PatientDTO patientDTO) throws DataStoreException;
    
    public IhelpPatientDTO addPatient(String iHelpID, InsertPatientDTO insertPatientDTO) throws MissingInputParametersException, IhelpPatientNotFoundException, DataStoreException, PatientNotFoundException;
    
    public IhelpPatientDTO removePatient(String iHelpID, InsertPatientDTO insertPatientDTO) throws MissingInputParametersException, IhelpPatientNotFoundException, DataStoreException;
}
