package eu.ihelp.datacapture.rest.server.utils;

import eu.ihelp.datacapture.rest.server.exceptions.InvalidInputException;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class InputUtils extends Exception {
 
    /**
     * Checks if input is valid. If any of the args is null or empty, it will throw 
     * an InvalidInputException
     * 
     * @param args 
     * @throws eu.crowdhealth.datastore.rest.exceptions.InvalidInputException 
     */
    public static void checkIfInputExists(String ... args) throws InvalidInputException {
        for(String arg : args) {
            if((arg==null) || (arg.isEmpty())) {
                throw new InvalidInputException("One or more if the input parameters is empty");
            }
        }
    }
    
    /**
     * it will return true only if string to lowercase is 'true'. in all other cases 
     * (including empty or null strings) will return false
     * 
     * @param value
     * @return 
     */
    public static boolean checkIfStringHasTrueValue(String value) {
        try {
            return Boolean.valueOf(value);
        } catch(Exception ex) {
            //do not do anything and return false at the very end
        }
        
        return false;
    }
}
