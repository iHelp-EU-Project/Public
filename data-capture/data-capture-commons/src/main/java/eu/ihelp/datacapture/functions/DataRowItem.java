package eu.ihelp.datacapture.functions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataRowItem {
    private final Object [] row;
    
    public DataRowItem(int size) {
        this.row = new Object[size];
    }

    public Object[] getRow() {
        return row;
    }

    @Override
    public String toString() {
        String res = "";
        for (Object field : row) {
            res += field + ";";
        }
        return res;
    }
}
