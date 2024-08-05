package eu.ihelp.datacapture.functions.connectors;

import eu.ihelp.datacapture.commons.exceptions.DataCaptureInitException;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsHealthentiaPersons;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.DataRowItemEnd;
import eu.ihelp.datacapture.functions.connectors.utils.HealthentiaUtils;
import eu.ihelp.datacapture.functions.connectors.utils.ParseJSONObject;
import java.util.ArrayList;
import java.util.List;
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
public class ConnectorHealthentiaPersons extends ConnectorAbstract {
    private static final Logger Log = LoggerFactory.getLogger(ConnectorHealthentiaPersons.class);
    private final ConnectorArgumentsHealthentiaPersons params;
    private final ParseJSONObject parser;
    private List<String> subjectIDs = new ArrayList<>();

    public ConnectorHealthentiaPersons(ConnectorArgumentsHealthentiaPersons params, Schema schema, Schema schemaKey, ArrayBlockingQueue<DataRowItem> queue) {
        super(schema, schemaKey, queue);
        this.params = params;
        this.parser = ParseJSONObject.createInstance(schema, schemaKey, params.getDatePattern());
    }

    @Override
    public void init() throws DataCaptureInitException {
        for(Integer studyId : this.params.getStudies()) {
            try {
                JSONArray jSONArray = HealthentiaUtils.getAllSubjects(params.getUrl(), params.getUsername(), params.getPassword(), studyId);
                for(int i=0; i<jSONArray.length(); i++) {
                    subjectIDs.add(jSONArray.getJSONObject(i).getString("subjectIdentificationNumber"));
                }
            
            } catch(Exception ex) {
                Log.error("Error getting subjects for study with id {}. {}", studyId, ex);
            }
        }
    }

    @Override
    public void close() {
        Log.info("{} - {} has been closed", Thread.currentThread().getName(), this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        for(String subjectID : this.subjectIDs) {
            try {
                if(Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                
                JSONObject jSONObject = HealthentiaUtils.getSubject(this.params.getUrl(), this.params.getUsername(), this.params.getPassword(), subjectID);
                DataRowItem dataRowItem = this.parser.createDataRowItem(jSONObject);
                this.queue.put(dataRowItem);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                close();
                return;
            } catch(Exception ex) {
                Log.error("Error getting more info for subect: {}. {}", subjectID, ex);
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
