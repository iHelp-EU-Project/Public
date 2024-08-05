package eu.ihelp.datacapture.functions.converters;

import eu.ihelp.datacapture.commons.model.ConverterArgumentsPrint;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.connectors.utils.RowToJsonConverter;
import eu.ihelp.datacapture.functions.connectors.utils.SetJobFinishedUtil;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConverterPrint extends ConverterAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConverterPrint.class);
    protected final ConverterArgumentsPrint params;
    protected final RowToJsonConverter converter;
    protected final JSONObject confParameters;
    protected final int batchSize;
 
    public ConverterPrint(String jobID, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue, ConverterArgumentsPrint params, JSONObject confParameters, int batchSize) {
        super(jobID, schema, schemaKey, queue);
        this.params = params;
        this.confParameters = confParameters;
        this.batchSize = batchSize;
        this.converter = RowToJsonConverter.createInstance(schema, schemaKey, params.getDatePattern());
    }

    @Override
    public void init() {
        Log.info("{} - {} has been initialized with parameters: {}", Thread.currentThread().getName(), this.getClass().getSimpleName(), this.confParameters.toString());
    }

    @Override
    public void close() {
        try {
            SetJobFinishedUtil.setJobAsFinished(jobID);
            Log.info("{} - {} has been closed after {} batchsize", Thread.currentThread().getName(), this.getClass().getSimpleName(), this.batchSize);
        } catch(IOException ex) {
            Log.warn("Could not set job:{} as finished. {}:{}", this.jobID, ex.getClass().getName(), ex.getMessage());
        }
    }

    int count = 0;
    
    @Override
    public void process(DataRowItem dataRowItem) {
        System.out.println(++count + converter.convert(dataRowItem).toString());
    }
}
