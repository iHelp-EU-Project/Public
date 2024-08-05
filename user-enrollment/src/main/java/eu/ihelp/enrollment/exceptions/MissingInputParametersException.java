package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MissingInputParametersException extends Exception {
    public MissingInputParametersException(String parameterName) {
        super("Missing parameter " + parameterName);
    }
}
