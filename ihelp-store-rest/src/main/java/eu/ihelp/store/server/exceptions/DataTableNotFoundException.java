package eu.ihelp.store.server.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataTableNotFoundException extends Exception {
    
    public DataTableNotFoundException(String name) {
        super("Table " + name + " does not exist");
    }
    
}
