package eu.ihelp.data.importer.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.ihelp.data.importer.logging.LoggingMessageBuilder;
import eu.ihelp.data.importer.logging.LoggingUtils;
import eu.ihelp.datacapture.commons.DataCaptureDatasets;
import eu.ihelp.datacapture.commons.DataProviders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRImportParser {
    private static final Logger Log = LoggerFactory.getLogger(HHRImportParser.class);
    private final String input;
    
    private HHRImportParser(String input) {
        this.input = input;
    }
    
    public static HHRImportParser createInstance(String input) {
        return new HHRImportParser(input);
    }
    
    
    
    public MessageToKafkaProducer process() {
        JSONObject jsonInObject = new JSONObject(input);
        String dataSource = jsonInObject.getString("datasourceID");
        String datasetID = jsonInObject.getString("datasetID");
        int startRecord = jsonInObject.getInt("currentBatchStart");
        int endRecord = jsonInObject.getInt("currentBatchEnd");
        String jobID = jsonInObject.getString("jobID");
        String dataCapturelogginURL = jsonInObject.getString("loggingURL");
        if(!dataCapturelogginURL.endsWith("/")) {
            dataCapturelogginURL+="/";
        }
        
        boolean isLastBatch = jsonInObject.getBoolean("lastBatch");
        JSONArray values = jsonInObject.getJSONArray("values");
        List<HHRRecordToImport> result = null;
        
        //HDM mappers returns list of Patiens/Procedures/Observations etc
        if(DataProviders.PrimaryDataProviders.HDM.name().equalsIgnoreCase(dataSource)) {
            result =  processHDM(dataSource, datasetID, startRecord, endRecord, jobID, dataCapturelogginURL, values);
        }
        
        //FPG mappers returns list of Bundles that might contain whatever
        else if(DataProviders.PrimaryDataProviders.FPG.name().equalsIgnoreCase(dataSource)) {
            result =  processBundles(dataSource, startRecord, endRecord, jobID, dataCapturelogginURL, values);
        }
        
        //MUP mappers returns list of Bundles that might contain whatever
        else if(DataProviders.PrimaryDataProviders.MUP.name().equalsIgnoreCase(dataSource)) {
            result =  processBundles(dataSource, startRecord, endRecord, jobID, dataCapturelogginURL, values);
        } 
        
        //Healthentia mappers returns list of Bundles that might contain whatever
        else if(DataProviders.SecondaryDataProviders.HEALTHENTIA.name().equalsIgnoreCase(dataSource)) {
            result =  processBundles(dataSource, startRecord, endRecord, jobID, dataCapturelogginURL, values);
        } 
        
        else {
            throw new UnsupportedOperationException("Datasource '" + dataSource + "' is not supported yet");
        }
        
        return new MessageToKafkaProducer(result, isLastBatch, dataCapturelogginURL, startRecord, endRecord, jobID);
    }
    
    private List<HHRRecordToImport> processBundles(String dataSource, int startRecord, int endRecord, String jobID, String dataCapturelogginURL, JSONArray values) {
        List<HHRRecordToImport> list = new ArrayList<>();
        HashMap<String, HHRRecordToImport> map = new HashMap<>();
        
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        
        for(int i=0; i<values.length(); i++) {
            JSONObject item = null;
            if(values.get(i) instanceof JSONObject) {
                item = values.getJSONObject(i);
            } else if(values.get(i) instanceof String) {
                item = new JSONObject(values.getString(i));
            } else {
                throw new IllegalArgumentException("values is not JSONObject or serialized (String) JSONObject");
            }
            
            try {
                Bundle bundle = parser.parseResource(Bundle.class, item.toString());
                for(Bundle.BundleEntryComponent bundleEntryComponent : bundle.getEntry()) {
                    if(bundleEntryComponent.getResource() instanceof DiagnosticReport) {
                        throw new UnsupportedOperationException(bundleEntryComponent.getResource().getResourceType().name() + " is currently not supported");
                    }

                    if(bundleEntryComponent.getResource() instanceof FamilyMemberHistory) {
                        proccessRecord(HHRParserFamilyMemberHistoryUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof Observation) {
                        proccessRecord(HHRParserObservationUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof Procedure) {
                        proccessRecord(HHRParserProcedureUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof MedicationAdministration) {
                        proccessRecord(HHRParserMedicationAdministrationUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof Condition) {
                        proccessRecord(HHRParserConditionUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof Encounter) {
                        proccessRecord(HHRParserEncounterUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof Patient) {
                        proccessRecord(HHRParserPatientUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }

                    if(bundleEntryComponent.getResource() instanceof QuestionnaireResponse) {
                       proccessRecord(HHRParserAnswerUtils.getInstance(), map, dataSource, bundleEntryComponent.getResource());
                    }
                }
            } catch(Exception ex) {
                Log.error("{}: {}", ex.getClass().getName(), ex.getMessage());
                LoggingUtils.getInstance().addMessageToQueue(
                        LoggingMessageBuilder.createErrorLoggingMessage(
                                startRecord, endRecord, jobID, dataCapturelogginURL, "ERROR", ex.getClass().getName() + ": '" + ex.getMessage() + "' for FHIR resource: " + item.toString())
                );
            }
        }
        
        Iterator<HHRRecordToImport> it = map.values().iterator();
        while(it.hasNext()) {
            list.add(it.next());
        }
        
        return list;
    }
    
    private List<HHRRecordToImport> processHDM(String dataSource, String datasetID, int startRecord, int endRecord, String jobID, String dataCapturelogginURL, JSONArray values) {
        List<HHRRecordToImport> list = new ArrayList<>();
        DataCaptureDatasets dataset = DataCaptureDatasets.valueOf(datasetID);
        switch(dataset) {
            case ConditionOccurrence:
                list.add(HHRParserConditionUtils.getInstance().parseValues(dataSource, values));
                break;
            case DrugExposure:
                list.add(HHRParserMedicationUtils.getInstance().parseValues(dataSource, values));
                break;
            case Observations:
                list.add(HHRParserObservationUtils.getInstance().parseValues(dataSource, values));
                break;
            case Persons:
                list.add(HHRParserPatientUtils.getInstance().parseValues(dataSource, values));
                break;
            case VisitOccurrence:
                list.add(HHRParserEncounterUtils.getInstance().parseValues(dataSource, values));
                break;
            case ProcedureOccurrence:
                list.add(HHRParserProcedureUtils.getInstance().parseValues(dataSource, values));
                break;
            case Measurement:
                list.add(HHRParserMeasurementUtils.getInstance().parseValues(dataSource, values));
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + dataset.name());
        }
        
        return list;
    }
    
    private void proccessRecord(HHRParserAbstractUtils parser, HashMap<String, HHRRecordToImport> map, String datasource, Resource resource) {
        HHRRecordToImport.KeyValueRecord keyValueRecord = parser.parseResource(datasource, resource);
        addEntry(map, parser.getTopic(), keyValueRecord);
    }
    
    private void addEntry(HashMap<String, HHRRecordToImport> map, String topic, HHRRecordToImport.KeyValueRecord keyValueRecord) {
        if(map.containsKey(topic)) {
            map.get(topic).addNewRecord(keyValueRecord);
        } else {
            HHRRecordToImport hHRRecordToImport = new HHRRecordToImport(topic);
            hHRRecordToImport.addNewRecord(keyValueRecord);
            map.put(topic, hHRRecordToImport);
        }
    }
    
    private void addEntry(HashMap<String, HHRRecordToImport> map, String topic, List<HHRRecordToImport.KeyValueRecord> keyValueRecords) {
        if(map.containsKey(topic)) {
            map.get(topic).addNewRecords(keyValueRecords);
        } else {
            HHRRecordToImport hHRRecordToImport = new HHRRecordToImport(topic);
            hHRRecordToImport.addNewRecords(keyValueRecords);
            map.put(topic, hHRRecordToImport);
        }
    }
}
