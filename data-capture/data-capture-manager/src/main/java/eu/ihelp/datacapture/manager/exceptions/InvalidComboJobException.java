package eu.ihelp.datacapture.manager.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class InvalidComboJobException extends Exception {
    private final String name;
    
    public InvalidComboJobException(String message) {
        super("Combo Job '" + message + "' has no jobs");
        this.name = message;
    }

    public String getName() {
        return name;
    }
}
