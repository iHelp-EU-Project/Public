package eu.ihelp.store.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ModelMetaInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final List<ModelMetaInfoElement> elements;

    protected ModelMetaInfo(List<ModelMetaInfoElement> elements) {
        this.elements = elements;
    }

    public List<ModelMetaInfoElement> getElements() {
        return elements;
    }    
}
