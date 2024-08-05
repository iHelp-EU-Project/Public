package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataTableNotFoundException extends Exception {
    
    public DataTableNotFoundException(String name) {
        super("Table " + name + " does not exist");
    }
    
}
