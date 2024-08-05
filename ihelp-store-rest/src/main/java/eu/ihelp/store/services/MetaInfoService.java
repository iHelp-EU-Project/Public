package eu.ihelp.store.services;

import com.leanxcale.exception.LeanxcaleException;
import eu.ihelp.store.server.exceptions.DataTableNotFoundException;
import eu.ihelp.store.services.model.ColumnInfo;
import eu.ihelp.store.services.model.IndexInfo;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface MetaInfoService {
    
    public List<String> getTables() throws LeanxcaleException;
    
    public List<ColumnInfo> getColumntInfo(String tableName) throws LeanxcaleException, DataTableNotFoundException;
    
    public List<ColumnInfo> getPrimaryKey(String tableName) throws LeanxcaleException, DataTableNotFoundException;
    
    public List<IndexInfo> getIndexInfo(String tableName) throws LeanxcaleException, DataTableNotFoundException;
}
