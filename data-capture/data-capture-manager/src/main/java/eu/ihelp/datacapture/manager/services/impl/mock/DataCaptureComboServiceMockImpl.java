package eu.ihelp.datacapture.manager.services.impl.mock;

import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFile;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsTest;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsPrint;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsTest;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJob;
import eu.ihelp.datacapture.commons.model.DataCaptureComboJobElement;
import eu.ihelp.datacapture.commons.model.DataCaptureJob;
import eu.ihelp.datacapture.commons.model.JobStatusEnum;
import eu.ihelp.datacapture.commons.utils.DataCaptureDateFormater;
import eu.ihelp.datacapture.commons.utils.UniqueIDManager;
import eu.ihelp.datacapture.manager.exceptions.InvalidComboJobException;
import eu.ihelp.datacapture.manager.exceptions.JobNotFoundException;
import eu.ihelp.datacapture.manager.services.DataCaptureComboService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureComboServiceMockImpl implements DataCaptureComboService {
    
    private final HashMap<String, DataCaptureComboJob> cache;

    private DataCaptureComboServiceMockImpl(HashMap<String, DataCaptureComboJob> cache) {
        this.cache = cache;
    }
    
    private static final class DataCaptureComboServiceMockImplHOlder {
        private static final DataCaptureComboServiceMockImpl INSTANCE = buildInstance();
        
        static DataCaptureComboServiceMockImpl buildInstance() {
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
            
            DataCaptureJob job = new DataCaptureJob();
            job.setDatasourceID("FPG");
            job.setDatasetID("dataset1");
            job.setSchema((new JSONObject(valueSchema.toString())).toMap());
            job.setSchemaKey((new JSONObject(keySchema.toString())).toMap());
            ConnectorArgumentsFile argumentsFile = new ConnectorArgumentsFile("path to the file", "yyyy/MM/dd HH:mm:ss", null, ";", "NULL", Boolean.TRUE);
            ConverterArgumentsPrint argumentsPrint = new ConverterArgumentsPrint("yyyy-MM-dd HH:mm:ss z");
            job.setConnectorArguments(argumentsFile);            
            job.setConverterArguments(argumentsPrint);
            
            
            DataCaptureJob job1 = new DataCaptureJob();
            job1.setDatasourceID("FPG");
            job1.setDatasetID("dataset2");
            job1.setSchema((new JSONObject(valueSchema.toString())).toMap());
            job1.setSchemaKey((new JSONObject(keySchema.toString())).toMap());   
            ConnectorArgumentsTest argumentsTest = new ConnectorArgumentsTest("yyyy-MM-dd HH:mm:ss", null, "NULL");
            job1.setConnectorArguments(argumentsTest);            
            ConverterArgumentsTest converterArgumentsTest = new ConverterArgumentsTest("yyyy/MM/dd HH:mm:ss z");
            job1.setConverterArguments(converterArgumentsTest);
            
            DataCaptureComboJobElement element1 = new DataCaptureComboJobElement(1, job);
            DataCaptureComboJobElement element2 = new DataCaptureComboJobElement(2, job1);
            
            DataCaptureComboJob comboJob = new DataCaptureComboJob();
            comboJob.setId(UniqueIDManager.getInstance().getNewID());
            comboJob.setName("A combo job");
            comboJob.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
            comboJob.setStatus(JobStatusEnum.STARTED.name());
            comboJob.getJobs().add(element1);
            comboJob.getJobs().add(element2);
            
            DataCaptureServiceMockImpl.getInstance().addDataCaptureJob(job);
            DataCaptureServiceMockImpl.getInstance().addDataCaptureJob(job1);
            
            HashMap<String, DataCaptureComboJob> _cache = new HashMap<>();
            _cache.put(comboJob.getId(), comboJob);
            
            return new DataCaptureComboServiceMockImpl(_cache); 
        }
    }
    
    public static DataCaptureComboServiceMockImpl getInstance() {
        return DataCaptureComboServiceMockImplHOlder.INSTANCE;
    }

    @Override
    public List<DataCaptureComboJob> getAllDataCaptureComboJobs() {
        List<DataCaptureComboJob> list = new ArrayList<>(this.cache.size());
        Iterator<Map.Entry<String, DataCaptureComboJob>> it = this.cache.entrySet().iterator();
        while(it.hasNext()) {
            list.add(it.next().getValue());
        }
        return list;
    }

    @Override
    public DataCaptureComboJob getDataCaptureComboJob(String id) throws JobNotFoundException {
        DataCaptureComboJob comboJob = this.cache.get(id);
        if(comboJob==null) {
            throw new JobNotFoundException(id);
        }
        return comboJob;
    }

    @Override
    public DataCaptureComboJob addDataCaptureComboJob(DataCaptureComboJob dataCaptureJobCombo) throws InvalidComboJobException  {
        dataCaptureJobCombo.setId(UniqueIDManager.getInstance().getNewID());
        dataCaptureJobCombo.setStatus(JobStatusEnum.SUBMITTED.name());
        dataCaptureJobCombo.setDateAdded(DataCaptureDateFormater.getStringFromDate(new Date(System.currentTimeMillis())));
        
        TreeMap<Integer, DataCaptureJob> orderdedJobs = new TreeMap<>();
        dataCaptureJobCombo.getJobs().forEach(captureComboJobElement -> {
            orderdedJobs.put(captureComboJobElement.getOrder(), captureComboJobElement.getJob());
        });
        
        Iterator<Map.Entry<Integer, DataCaptureJob>> it = orderdedJobs.entrySet().iterator();
        while(it.hasNext()) {
            DataCaptureServiceMockImpl.getInstance().addDataCaptureJob(it.next().getValue());
        }
        
        cache.put(dataCaptureJobCombo.getId(), dataCaptureJobCombo);
        return dataCaptureJobCombo;
    }
    
}
