package eu.ihelp.enrollment.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ModelMetaInfoElement implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected final String name;
    protected final String snomed;
    protected final boolean multipleValues;
    protected final List<Object> possibleValues;
    protected final List<Object> realValues;
    protected final HashMap<Object, Object> mappedValues;

    public ModelMetaInfoElement(String name, String snomed, boolean multipleValues, List<Object> possibleValues, List<Object> realValues, HashMap<Object, Object> mappedValues) {
        this.name = name;
        this.snomed = snomed;
        this.multipleValues = multipleValues;
        this.possibleValues = possibleValues;
        this.realValues = realValues;
        this.mappedValues = mappedValues;
    }

    public String getName() {
        return name;
    }

    public String getSnomed() {
        return snomed;
    }

    public boolean isMultipleValues() {
        return multipleValues;
    }

    public List<Object> getPossibleValues() {
        return possibleValues;
    }

    public List<Object> getRealValues() {
        return realValues;
    }

    public HashMap<Object, Object> getMappedValues() {
        return mappedValues;
    }

    

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelMetaInfoElement other = (ModelMetaInfoElement) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.snomed, other.snomed);
    }

    @Override
    public String toString() {
        return "ModelMetaInfoElement{" + "name=" + name + ", snomed=" + snomed + ", multipleValues=" + multipleValues + '}';
    }
    
}
