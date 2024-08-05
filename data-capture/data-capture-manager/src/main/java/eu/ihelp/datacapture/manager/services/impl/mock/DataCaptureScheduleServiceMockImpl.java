package eu.ihelp.datacapture.manager.services.impl.mock;

import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFile;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsTest;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafka;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsTest;
import eu.ihelp.datacapture.commons.model.DataCaptureJobScheduled;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.model.JobSchedule;
import eu.ihelp.datacapture.commons.model.JobScheduleFuture;
import eu.ihelp.datacapture.commons.model.JobSchedulePeriodic;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import eu.ihelp.datacapture.manager.services.DataCaptureScheduledService;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureScheduleServiceMockImpl implements DataCaptureScheduledService {
    
    private final HashMap<String, DataCaptureJobScheduled> cache;
    
    
    private DataCaptureScheduleServiceMockImpl(HashMap<String, DataCaptureJobScheduled> cache) {
        this.cache = cache;
    }

    private static final class DataCaptureScheduleServiceMockImplHandler {
        private static final DataCaptureScheduleServiceMockImpl INSTANCE = buildInstance();
        
        static DataCaptureScheduleServiceMockImpl buildInstance() {
            SchemaBuilder.FieldAssembler assembler = SchemaBuilder.record("myValue").namespace("eu.ihelp.test1").fields();
            assembler.name("firstname").type().stringType().noDefault();
            assembler.name("lastname").type().nullable().stringType().noDefault();
            assembler.name("city").type().nullable().stringType().stringDefault("ATHENS");
            assembler.name("age").type().nullable().intType().noDefault();
            Schema valueSchema = (Schema) assembler.endRecord();
            
            SchemaBuilder.FieldAssembler assemblerKey = SchemaBuilder.record("myKey").namespace("eu.ihelp.test1").fields();
            assemblerKey.name("firstname").type().stringType().noDefault();
            Schema keySchema = (Schema) assemblerKey.endRecord();
            
            HashMap<String, DataCaptureJobScheduled> _cache = new HashMap<>();
            
            
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
            
            JobScheduleFuture future = new JobScheduleFuture(1, "WEEK");
            job.setSchedule(new JobSchedule(future, null));
            
            _cache.put(job.getId(), job);
            
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
            JobScheduleFuture future1 = new JobScheduleFuture(1, "WEEK");
            JobSchedulePeriodic periodic1 = new JobSchedulePeriodic(1, "MONTH");
            job1.setSchedule(new JobSchedule(future1, periodic1));
            _cache.put(job1.getId(), job1);
            
            return new DataCaptureScheduleServiceMockImpl(_cache);
        }
    }
    
    public static DataCaptureScheduleServiceMockImpl getInstance() {
        return DataCaptureScheduleServiceMockImplHandler.INSTANCE;
    }

    @Override
    public List<DataCaptureJobScheduled> getAllDataCaptureScheduleJobs() {
        List<DataCaptureJobScheduled> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, DataCaptureJobScheduled>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue());
        }
        return list;
    }

    @Override
    public DataCaptureJobScheduled getDataCaptureScheduleJob(String id) throws JobNotFoundException {
        DataCaptureJobScheduled job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        return job;
    }

    @Override
    public DataCaptureJobScheduled addDataCaptureScheduleJob(DataCaptureJobScheduled dataCaptureJobScheduled) {
        dataCaptureJobScheduled.setId(UniqueIDManager.getInstance().getNewID());
        dataCaptureJobScheduled.setStatus(JobStatusEnum.SUBMITTED.name());
        dataCaptureJobScheduled.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        dataCaptureJobScheduled.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        cache.put(dataCaptureJobScheduled.getId(), dataCaptureJobScheduled);
        return dataCaptureJobScheduled;
    }
    
    @Override
    public DataCaptureJobScheduled stopDataCaptureScheduleJob(String id) throws JobNotFoundException {
        DataCaptureJobScheduled dataCaptureJobScheduled = getDataCaptureScheduleJob(id);
        dataCaptureJobScheduled.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        dataCaptureJobScheduled.setStatus(JobStatusEnum.STOPPED.name());
        return dataCaptureJobScheduled;
    }
    
}
