package eu.ihelp.datacapture.functions.converters;

import eu.ihelp.datacapture.commons.model.ConverterArgumentsTest;
import eu.ihelp.datacapture.functions.DataRowItem;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterTest extends ConverterAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConverterAbstract.class);
    protected final ConverterArgumentsTest params;

    public ConverterTest(String jobID, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue, ConverterArgumentsTest params) {
        super(jobID, schema, schemaKey, queue);
        this.params = params;
    }

    @Override
    public void init() {
        Log.info("{} - {} has been initialized", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void close() {
        Log.info("{} - {} has been closed", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    int count = 0;
    
    @Override
    public void process(DataRowItem dataRowItem) {
        count++;
        if(count%50==0) {
            System.out.println("I took row no " + count);
        }
    }
    
}
