package eu.ihelp.datacapture.functions.converters;

import eu.ihelp.datacapture.functions.DataRowItem;
import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public abstract class ConverterAbstract implements Closeable {
    protected final String jobID;
    protected final Schema schema;
    protected final Schema schemaKey;
    protected final ArrayBlockingQueue<DataRowItem> queue;
    
    public abstract void init();
    
    @Override
    public abstract void close();

    public ConverterAbstract(String jobID, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        this.jobID = jobID;
        this.schema = schema;
        this.schemaKey = schemaKey;
        this.queue = queue;
    }
    
    public abstract void process(DataRowItem dataRowItem);
}
