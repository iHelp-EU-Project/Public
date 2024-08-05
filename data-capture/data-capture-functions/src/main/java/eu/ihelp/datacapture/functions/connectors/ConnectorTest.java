package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsTest;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.DataRowItemEnd;
import eu.ihelp.datacapture.functions.connectors.utils.GenerateRandomValues;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConnectorTest extends ConnectorAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConnectorTest.class);
    private final ConnectorArgumentsTest params;
    private final GenerateRandomValues generator;

    public ConnectorTest(ConnectorArgumentsTest params, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        super(schema, schemaKey, queue);
        this.params = params;
        this.generator = GenerateRandomValues.createInstance(schema);
    }

    @Override
    public void init() throws DataCaptureInitException {
        Log.info("{} - {} has been initialized", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void close() {
        Log.info("{} - {} has been closed", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }
    
    @Override
    public void run() {
        
        for(int i=0; i<1000; i++) {
            try {
                if((i%100==0) && (Thread.currentThread().isInterrupted())) {
                    throw new InterruptedException();
                }
                
                //this.queue.put(new DataRowItem(0));
                this.queue.put(generator.generateRandom());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                close();
                return;
            }
        }
        
        try {
            this.queue.put(new DataRowItemEnd());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            close();
            return;
        }
        
        close();
    }
    
}
