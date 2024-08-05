package eu.ihelp.datacapture.functions.converters;

import eu.ihelp.datacapture.commons.model.ConverterArgumentsGateway;
import eu.ihelp.datacapture.functions.DataRowItem;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterGateway extends ConverterAbstract {
    protected final ConverterArgumentsGateway params;

    public ConverterGateway(String jobID, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue, ConverterArgumentsGateway params) {
        super(jobID, schema, schemaKey, queue);
        this.params = params;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(DataRowItem dataRowItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
