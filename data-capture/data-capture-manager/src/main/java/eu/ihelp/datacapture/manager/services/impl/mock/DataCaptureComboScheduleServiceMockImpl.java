package eu.ihelp.datacapture.manager.services.impl.mock;

import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFile;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsTest;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafka;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsTest;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobElement;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobScheduled;
import eu.ihelp.datacapture.commons.model.DataCaptureJobScheduled;
import eu.ihelp.datacapture.commons.model.JobSchedule;
import eu.ihelp.datacapture.commons.model.JobScheduleFuture;
import eu.ihelp.datacapture.commons.model.JobSchedulePeriodic;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.exceptions.InvalidComboJobException;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureComboScheduledService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureComboScheduleServiceMockImpl implements DataCaptureComboScheduledService {
    
    private final HashMap<String, DataCaptureComboJobScheduled> cache;
    
    
    private DataCaptureComboScheduleServiceMockImpl(HashMap<String, DataCaptureComboJobScheduled> cache) {
        this.cache = cache;
    }
    
    private static final class DataCaptureComboScheduleServiceMockImplHolder {
        private static final DataCaptureComboScheduleServiceMockImpl INSTANCE = buildInstance();
        
        static DataCaptureComboScheduleServiceMockImpl buildInstance() {
            SchemaBuilder.FieldAssembler assembler = SchemaBuilder.record("myValue").namespace("eu.ihelp.test1").fields();
            assembler.name("firstname").type().stringType().noDefault();
            assembler.name("lastname").type().nullable().stringType().noDefault();
            assembler.name("city").type().nullable().stringType().stringDefault("ATHENS");
            assembler.name("age").type().nullable().intType().noDefault();
            Schema valueSchema = (Schema) assembler.endRecord();
            
            SchemaBuilder.FieldAssembler assemblerKey = SchemaBuilder.record("myKey").namespace("eu.ihelp.test1").fields();
            assemblerKey.name("firstname").type().stringType().noDefault();
            Schema keySchema = (Schema) assemblerKey.endRecord();
            
            
            DataCaptureJobScheduled job = new DataCaptureJobScheduled();
            job.setId(UniqueIDManager.getInstance().getNewID());
            job.setDatasourceID("FPG");
            job.setDatasetID("dataset");
            job.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            job.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            job.setSchema((new JSONObject(valueSchema.toString())).toMap());
            job.setSchemaKey((new JSONObject(keySchema.toString())).toMap());
            job.setStatus(JobStatusEnum.FINISHED_CAPTURE.name());     
            ConnectorArgumentsFile argumentsFile = new ConnectorArgumentsFile("path to the file", "yyyy-MM-dd HH:mm:ss", null, ";", "NULL", Boolean.TRUE);
            ConverterArgumentsKafka argumentsKafka = new ConverterArgumentsKafka("kafkaUrL", "port", "topicKafka", "yyyy/MM/dd HH:mm:ss z");
            job.setConnectorArguments(argumentsFile);            
            job.setConverterArguments(argumentsKafka);
            
            
            DataCaptureJobScheduled job1 = new DataCaptureJobScheduled();
            job1.setId(UniqueIDManager.getInstance().getNewID());
            job1.setDatasourceID("FPG");
            job1.setDatasetID("datasetX");
            job1.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            job1.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            job1.setSchema((new JSONObject(valueSchema.toString())).toMap());
            job1.setSchemaKey((new JSONObject(keySchema.toString())).toMap());
            job1.setStatus(JobStatusEnum.FINISHED_CAPTURE.name());       
            ConnectorArgumentsTest argumentsTest = new ConnectorArgumentsTest("yyyy-MM-dd HH:mm:ss", null, "NULL");
            job1.setConnectorArguments(argumentsTest);            
            ConverterArgumentsTest converterArgumentsTest = new ConverterArgumentsTest("yyyy/MM/dd HH:mm:ss z");
            job1.setConverterArguments(converterArgumentsTest);
            
            List<DataCaptureComboJobElement> list = new ArrayList<>(2);
            list.add(new DataCaptureComboJobElement(1, job));
            list.add(new DataCaptureComboJobElement(1, job1));
            
            JobScheduleFuture future1 = new JobScheduleFuture(1, "SECONDS");
            JobSchedulePeriodic periodic1 = new JobSchedulePeriodic(2, "MINUTES");
            
            DataCaptureComboJobScheduled scheduled = new DataCaptureComboJobScheduled(
                    new JobSchedule(future1, periodic1), 
                    UniqueIDManager.getInstance().getNewID(), 
                    "scheduledCombo", 
                    JobStatusEnum.FINISHED_CAPTURE.name(), 
                    DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())), 
                    DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())), 
                    list);
            
            HashMap<String, DataCaptureComboJobScheduled> _cache = new HashMap<>();
            _cache.put(scheduled.getId(), scheduled);
            
            return new DataCaptureComboScheduleServiceMockImpl(_cache);
        }
    }
    
    public static DataCaptureComboScheduleServiceMockImpl getInstance() {
        return DataCaptureComboScheduleServiceMockImplHolder.INSTANCE;
    }

    @Override
    public List<DataCaptureComboJobScheduled> getAllDataCaptureScheduleComboJobs() {
        List<DataCaptureComboJobScheduled> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, DataCaptureComboJobScheduled>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue());
        }
        return list;
    }

    @Override
    public DataCaptureComboJobScheduled getDataCaptureScheduleComboJob(String id) throws JobNotFoundException {
        DataCaptureComboJobScheduled job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        return job;
    }

    @Override
    public DataCaptureComboJobScheduled addDataCaptureScheduleComboJob(DataCaptureComboJobScheduled dataCaptureComboJobScheduled) throws InvalidComboJobException {
        dataCaptureComboJobScheduled.setId(UniqueIDManager.getInstance().getNewID());
        dataCaptureComboJobScheduled.setStatus(JobStatusEnum.SUBMITTED.name());
        dataCaptureComboJobScheduled.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        dataCaptureComboJobScheduled.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        cache.put(dataCaptureComboJobScheduled.getId(), dataCaptureComboJobScheduled);
        return dataCaptureComboJobScheduled;
    }

    @Override
    public DataCaptureComboJobScheduled stopDataCaptureScheduleComboJob(String id) throws JobNotFoundException {
        DataCaptureComboJobScheduled dataCaptureJobComboScheduled = getDataCaptureScheduleComboJob(id);
        dataCaptureJobComboScheduled.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        dataCaptureJobComboScheduled.setStatus(JobStatusEnum.STOPPED.name());
        return dataCaptureJobComboScheduled;
    }
    
}
