package eu.ihelp.store.services.model.factories;

import com.leanxcale.kivi.database.Field;
import eu.ihelp.store.services.model.ColumnInfo;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ColumnInfoFactory {
    
    public static ColumnInfo create(Field field) {
        return new ColumnInfo(field.getName(), field.getType().name());
    }
    
    public static List<ColumnInfo> create(Collection<Field> fields) {
        List<ColumnInfo> columnInfos = new java.util.ArrayList<>(fields.size());
        fields.forEach((Field field) -> {
            columnInfos.add(ColumnInfoFactory.create(field));
        });
        return columnInfos;
    }
}
