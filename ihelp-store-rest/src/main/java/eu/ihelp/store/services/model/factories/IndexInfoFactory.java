package eu.ihelp.store.services.model.factories;

import com.leanxcale.kivi.database.Index;
import eu.ihelp.store.services.model.IndexInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IndexInfoFactory {
    
    public static List<IndexInfo> createIndexInfo(Collection<Index> indexes) {
        List<IndexInfo> list = new ArrayList<>(indexes.size());
        indexes.forEach((Index index) -> {
            list.add(IndexInfoFactory.createIndexInfo(index));
        });
        return list;
    }
    
    public static IndexInfo createIndexInfo(Index index) {
        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setName(index.getName());
        indexInfo.setUnique(index.isUnique());
        indexInfo.setFields(ColumnInfoFactory.create(index.getFields()));
        return indexInfo;
    }
}
