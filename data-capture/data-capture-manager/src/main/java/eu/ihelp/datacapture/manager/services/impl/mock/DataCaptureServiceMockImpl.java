package eu.ihelp.datacapture.manager.services.impl.mock;

import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFile;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsHealthentia;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsHealthentiaPersons;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsTest;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafka;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsPrint;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsTest;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.services.DataCaptureService;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureServiceMockImpl implements DataCaptureService {
    
    private final HashMap<String, DataCaptureJob> cache;
    
    private DataCaptureServiceMockImpl(HashMap<String, DataCaptureJob> cache) {
        this.cache = cache;
    }

    private static final class DataCaptureServiceMockImplHandler {
        private static final DataCaptureServiceMockImpl INSTANCE = buildInstance();
        
        static DataCaptureServiceMockImpl buildInstance() {
            Schema tsType = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
            SchemaBuilder.FieldAssembler assembler = SchemaBuilder.record("myValue").namespace("eu.ihelp.test1").fields();
            assembler.name("id").type().intType().noDefault();
            assembler.name("firstname").type().stringType().noDefault();
            assembler.name("lastname").type().stringType().noDefault();
            assembler.name("age").type().intType().noDefault();
            assembler.name("dateAdded").type(tsType).noDefault();
            assembler.name("metric").type().doubleType().noDefault();
            assembler.name("city").type().stringType().noDefault();
            assembler.name("married").type().booleanType().noDefault();
            assembler.name("other").type().longType().noDefault();
            Schema valueSchema = (Schema) assembler.endRecord();
            
            SchemaBuilder.FieldAssembler assemblerKey = SchemaBuilder.record("myKey").namespace("eu.ihelp.test1").fields();
            assemblerKey.name("id").type().intType().noDefault();
            Schema keySchema = (Schema) assemblerKey.endRecord();
            
            HashMap<String, DataCaptureJob> _cache = new HashMap<>();
            
            
            DataCaptureJob job = new DataCaptureJob();
            job.setId(UniqueIDManager.getInstance().getNewID());
            job.setDatasourceID("Healthentia");
            job.setDatasetID("Subjects");
            job.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            job.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            job.setSchema((new JSONObject(valueSchema.toString())).toMap());
            job.setSchemaKey((new JSONObject(keySchema.toString())).toMap());
            job.setStatus(JobStatusEnum.FINISHED_CAPTURE.name());     
            //ConnectorArgumentsFile connectorArguments = new ConnectorArgumentsFile("path to the file", "yyyy/MM/dd HH:mm:ss", ";", "NULL", Boolean.TRUE);
            List<Integer> studies = new ArrayList<>(4);
            studies.add(59);
            studies.add(60);
            studies.add(61);
            ConnectorArgumentsHealthentiaPersons connectorArguments = new ConnectorArgumentsHealthentiaPersons("https://saas-api.healthentia.com/", "ihelpingestion@healthentia.com", "@GY+Rb3zTR", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", null, studies);
            ConverterArgumentsPrint argumentsPrint = new ConverterArgumentsPrint("yyyy-MM-dd HH:mm:ss z");
            job.setConnectorArguments(connectorArguments);            
            job.setConverterArguments(argumentsPrint);
            _cache.put(job.getId(), job);
            
            DataCaptureJob job1 = new DataCaptureJob();
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
            _cache.put(job1.getId(), job1);
            
            return new DataCaptureServiceMockImpl(_cache);
        }
    }
    
    public static DataCaptureServiceMockImpl getInstance() {
        return DataCaptureServiceMockImplHandler.INSTANCE;
    }

    @Override
    public synchronized List<DataCaptureJob> getAllDataCaptureJobs() {
        List<DataCaptureJob> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, DataCaptureJob>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue());
        }
        return list;
    }

    @Override
    public synchronized DataCaptureJob getDataCaptureJob(String id) throws JobNotFoundException {
        DataCaptureJob job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        return job;
    }
    
    @Override
    public DataCaptureJob setDataCaptureJobFinished(String id) throws JobNotFoundException {
        DataCaptureJob job = this.cache.get(id);
        if(job==null) {
            throw new JobNotFoundException(id);
        }
        
        job.setStatus(JobStatusEnum.FINISHED.name());
        
        return job;
    }

    @Override
    public synchronized DataCaptureJob addDataCaptureJob(DataCaptureJob dataCaptureJob) {
        dataCaptureJob.setId(UniqueIDManager.getInstance().getNewID());
        dataCaptureJob.setStatus(JobStatusEnum.SUBMITTED.name());
        dataCaptureJob.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        dataCaptureJob.setDateFinished(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        cache.put(dataCaptureJob.getId(), dataCaptureJob);
        return dataCaptureJob;
    }
    
}
