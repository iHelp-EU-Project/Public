package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsHealthentia;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.DataRowItemEnd;
import eu.ihelp.datacapture.functions.connectors.utils.HealthentiaUtils;
import eu.ihelp.datacapture.functions.connectors.utils.ParseJSONObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ConnectorHealthentia extends ConnectorAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConnectorHealthentia.class);
    private final ConnectorArgumentsHealthentia params;
    private final ParseJSONObject parser;
    private int count = 0;

    public ConnectorHealthentia(ConnectorArgumentsHealthentia params, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        super(schema, schemaKey, queue);
        this.params = params;
        this.parser = ParseJSONObject.createInstance(schema, schemaKey, params.getDatePattern());
    }

    @Override
    public void init() throws DataCaptureInitException {
        Log.info("{} - {} has been started", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void close() {
        Log.info("{} - {} has been closed after adding {} records", Thread.currentThread().getName(), this.getClass().getSimpleName(), count);
    }

    @Override
    public void run() {
        for(Integer studyId : this.params.getStudies()) {
            try {
                if(Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                
                boolean isLastChunk = false;
                int lastProcessedId = 0;
                do {
                    String dateTimeStr = null;
                    
                    if(!params.isFromStart()) {
                        dateTimeStr = LocalDateTime.of(LocalDate.now().minusMonths(2), LocalTime.MIN).format(DateTimeFormatter.ISO_DATE_TIME);
                    }
                    
                    JSONObject jSONObject = HealthentiaUtils.getHealthentiaRecords(params.getDataset(), params.getUrl(), params.getUsername(), params.getPassword(), studyId, lastProcessedId, dateTimeStr);
                    if(jSONObject!=null) {
                        JSONArray array = null;
                        if(jSONObject.has("answers")) {
                            array = jSONObject.getJSONArray("answers");
                        } else if(jSONObject.has("data")) {
                            array = jSONObject.getJSONArray("data");
                        } else {
                            Log.warn("Could not find valid data for study {}", studyId);
                            continue;
                        }

                        for(int i=0; i<array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            DataRowItem dataRowItem = this.parser.createDataRowItem(data);
                            if(dataRowItem!=null) {
                                this.queue.put(dataRowItem);    
                                count++;
                            } else {
                                Log.warn("Could not generate datarowitem for datarecord {}", data.getLong("id"));
                            }
                        }   
                        
                        isLastChunk = jSONObject.getBoolean("isLastChunk");
                        lastProcessedId = jSONObject.getInt("lastProcessedId");                            
                    }
                } while(!isLastChunk); 
                
                
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                close();
                return;
            } catch(Exception ex) {
                Log.error("Error getting info for stury: {}. {}", studyId, ex);
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
