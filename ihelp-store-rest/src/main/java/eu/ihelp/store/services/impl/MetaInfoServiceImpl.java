package eu.ihelp.store.services.impl;

import com.leanxcale.exception.LeanxcaleException;
import com.leanxcale.kivi.database.Field;
import com.leanxcale.kivi.database.Index;
import com.leanxcale.kivi.database.Table;
import com.leanxcale.kivi.session.Session;
import eu.ihelp.store.server.exceptions.DataTableNotFoundException;
import eu.ihelp.store.server.utils.DBAHelper;
import eu.ihelp.store.services.MetaInfoService;
import eu.ihelp.store.services.model.ColumnInfo;
import eu.ihelp.store.services.model.IndexInfo;
import eu.ihelp.store.services.model.factories.ColumnInfoFactory;
import eu.ihelp.store.services.model.factories.IndexInfoFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MetaInfoServiceImpl implements MetaInfoService {

    @Override
    public List<String> getTables() throws LeanxcaleException  {
        try(Session session = DBAHelper.getInstance().getKiviSession()) {
            Collection<Table> tables = session.database().getTables();
            List<String> tableNames = new ArrayList<>(tables.size());
            tables.forEach((Table tbl) -> {
                tableNames.add(tbl.getName());
            });
            return tableNames;
        }
    }

    @Override
    public List<ColumnInfo> getColumntInfo(String tableName) throws LeanxcaleException, DataTableNotFoundException {
        try(Session session = DBAHelper.getInstance().getKiviSession()) {
            Table table = session.database().getTable(tableName);
            if(table==null) {
                throw new DataTableNotFoundException(tableName);
            }
            
            Collection<Field> fields = table.getTableModel().getFields();
            return ColumnInfoFactory.create(fields);
        }
    }

    @Override
    public List<IndexInfo> getIndexInfo(String tableName) throws LeanxcaleException, DataTableNotFoundException {
        try(Session session = DBAHelper.getInstance().getKiviSession()) {
            Table table = session.database().getTable(tableName);
            if(table==null) {
                throw new DataTableNotFoundException(tableName);
            }
            
            Collection<Index> indexes = table.getTableModel().getIndexes();
            return IndexInfoFactory.createIndexInfo(indexes);
        }
    }

    @Override
    public List<ColumnInfo> getPrimaryKey(String tableName) throws LeanxcaleException, DataTableNotFoundException {
        try(Session session = DBAHelper.getInstance().getKiviSession()) {
            Table table = session.database().getTable(tableName);
            if(table==null) {
                throw new DataTableNotFoundException(tableName);
            }
            
            Collection<Field> fields = table.getTableModel().getPrimaryKey();
            return ColumnInfoFactory.create(fields);
        }
    }
    
    
}
