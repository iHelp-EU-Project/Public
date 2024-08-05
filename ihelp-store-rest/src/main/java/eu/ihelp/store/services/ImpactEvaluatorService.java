package eu.ihelp.store.services;

import eu.ihelp.store.server.exceptions.DataStoreException;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface ImpactEvaluatorService {
    
    
    public JSONObject getMetaData() throws IOException;
    
    public String getCommunications(List<String> sent, List<String> _include) throws DataStoreException;
    
    public String getObservations(List<String> date, String subject, String combocode) throws DataStoreException;
    
    public String getQuestionnaireResponses(List<String> date, String subject, String questionnaireID) throws DataStoreException;
}
