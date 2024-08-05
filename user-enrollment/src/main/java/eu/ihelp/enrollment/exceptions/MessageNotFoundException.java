package eu.ihelp.enrollment.exceptions;

import eu.ihelp.enrollment.model.MessageSmallDTO;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MessageNotFoundException extends Exception {
    
    public MessageNotFoundException(MessageSmallDTO smallDTO) {
        super("messge " + smallDTO.toString() + " does not exist");
    }

    
}
