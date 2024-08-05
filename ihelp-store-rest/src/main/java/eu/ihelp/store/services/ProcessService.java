package eu.ihelp.store.services;

import eu.ihelp.store.services.model.DataRow;
import eu.ihelp.store.services.model.DataTable;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONArray;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface ProcessService {
    
    public List<DataRow> processQuery(String query) throws SQLException;
    
    public List<DataTable> getAllData(String ihelpID) throws SQLException;
    
    public JSONArray getPrimaryData(String ihelpID) throws SQLException;
    
    public JSONArray getSecondary(String ihelpID) throws SQLException;
}
