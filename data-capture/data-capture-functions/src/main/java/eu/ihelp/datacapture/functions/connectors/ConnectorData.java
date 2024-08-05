package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsData;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.DataRowItemEnd;
import eu.ihelp.datacapture.functions.connectors.utils.ParseCSVLine;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConnectorData extends ConnectorAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConnectorData.class);
    private final ConnectorArgumentsData params;
    private final ParseCSVLine parser;
    private int count = 0;
    private final Boolean skipFirst;

    public ConnectorData(ConnectorArgumentsData params, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        super(schema, schemaKey, queue);
        this.params = params;
        this.parser = ParseCSVLine.createInstance(schema, schemaKey, params.getDelimiter(), params.getDatePattern(), params.getNullString(), params.getTimePattern());
        this.skipFirst = params.getSkipFirst();
    }
    
    @Override
    public void init() throws DataCaptureInitException {       
        count++;
        Log.info("{} - {} has been initialized", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void close() {
        Log.info("{} - {} has been closed", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        for(String line : this.params.getData()) {
            if(this.skipFirst) {
                ++count;
                continue;
            }
            
            try {
                if((++count%100==0) && (Thread.currentThread().isInterrupted())) {
                    throw new InterruptedException();
                }
                
                DataRowItem row = this.parser.createDataRowItem(line);
                if(row!=null) {
                    this.queue.put(row);
                }
                
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
