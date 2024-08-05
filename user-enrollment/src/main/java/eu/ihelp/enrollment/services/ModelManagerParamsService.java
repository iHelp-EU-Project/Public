package eu.ihelp.enrollment.services;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.IhelpPatientNotFoundException;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface ModelManagerParamsService {
    
    public String fillParams(String ihelpID, String input) throws DataStoreException, IhelpPatientNotFoundException;
    
    public String fillParams(String patientID, String providerID, String input) throws DataStoreException, IhelpPatientNotFoundException;
}
