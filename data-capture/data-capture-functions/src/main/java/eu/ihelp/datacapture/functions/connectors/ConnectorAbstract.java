package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.functions.DataRowItem;
import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public abstract class ConnectorAbstract implements Runnable, Closeable {
    protected final Schema schema;
    protected final Schema schemaKey;
    protected final ArrayBlockingQueue<DataRowItem> queue;

    public ConnectorAbstract(Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.queue = queue;
    }
    
    public abstract void init() throws DataCaptureInitException;
    
    @Override
    public abstract void close();

    @Override
    public abstract void run();
    
}
