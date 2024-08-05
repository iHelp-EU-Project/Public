package eu.ihelp.enrollment.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ModelMetaInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final HashMap<String, ModelMetaInfoElement> elements;

    protected ModelMetaInfo(HashMap<String, ModelMetaInfoElement> elements) {
        this.elements = elements;
    }

    public HashMap<String, ModelMetaInfoElement> getElements() {
        return elements;
    }

}
