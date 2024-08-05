package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IncompleteObjectException extends RuntimeException {
    
    public IncompleteObjectException(Class className, String parameterName) {
        super("'" + parameterName  + "' cannot be null for entity '" + className.getSimpleName() + "'");
    }
}
