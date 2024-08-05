package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsRest;
import eu.ihelp.datacapture.functions.DataRowItem;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConnectorRest extends ConnectorAbstract {
    private final ConnectorArgumentsRest params;

    public ConnectorRest(ConnectorArgumentsRest params, Schema schema,  Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        super(schema, schemaKey, queue);
        this.params = params;
    }

    @Override
    public void init() throws DataCaptureInitException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
