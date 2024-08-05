package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFile;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.DataRowItemEnd;
import eu.ihelp.datacapture.functions.connectors.utils.ParseCSVLine;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConnectorFile extends ConnectorAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConnectorFile.class);
    private final ConnectorArgumentsFile params;
    private final ParseCSVLine parser;
    private Scanner scanner;
    private int count = 0;
    private final Boolean skipFirst;

    public ConnectorFile(ConnectorArgumentsFile params, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        super(schema, schemaKey, queue);
        this.params = params;
        this.parser = ParseCSVLine.createInstance(schema, schemaKey, params.getDelimiter(), params.getDatePattern(), params.getNullString(), params.getTimePattern());
        this.skipFirst = params.getSkipFirst();
    }

    @Override
    public void init() throws DataCaptureInitException {
        File file = new File(this.params.getPath());
        try {
            this.scanner = new Scanner(file);
            count++;
        } catch (FileNotFoundException ex) {
            throw new DataCaptureInitException(ex.getMessage(), ex);
        }
        
        Log.info("{} - {} has been initialized", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void close() {
        if(this.scanner!=null) {
            this.scanner.close();
        }
        
        Log.info("{} - {} has been closed", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if(this.scanner.hasNextLine()) {
            if(this.skipFirst) {
                ++count;
                this.scanner.nextLine();
            }
        }
        
        while(this.scanner.hasNextLine()) {
            try {
                if((++count%100==0) && (Thread.currentThread().isInterrupted())) {
                    throw new InterruptedException();
                }
                DataRowItem row = this.parser.createDataRowItem(this.scanner.nextLine());
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
