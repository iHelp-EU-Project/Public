package eu.ihelp.enrollment.services.impl;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.exceptions.IhelpPatientNotFoundException;
import eu.ihelp.enrollment.managers.PatientManager;
import eu.ihelp.enrollment.model.ModelMetaInfo;
import eu.ihelp.enrollment.model.ModelMetaInfoBuilder;
import eu.ihelp.enrollment.model.ModelMetaInfoElement;
import eu.ihelp.enrollment.model.PatientDTO;
import eu.ihelp.enrollment.services.ModelManagerParamsService;
import eu.ihelp.enrollment.utils.IhelpDateFormatter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class ModelManagerParamsServiceImpl implements ModelManagerParamsService {
    private static final Logger Log = LoggerFactory.getLogger(ModelManagerParamsServiceImpl.class);
    
    private ModelManagerParamsServiceImpl() {}

    private static final class ModelManagerParamsServiceImplHolder {
        private static final ModelManagerParamsServiceImpl INSTANCE = new ModelManagerParamsServiceImpl();
    }
    
    public static ModelManagerParamsServiceImpl getInstance() {
        return ModelManagerParamsServiceImplHolder.INSTANCE;
    }

    @Override
    public String fillParams(String ihelpID, String input) throws DataStoreException, IhelpPatientNotFoundException {
        List<PatientDTO> patients = PatientManager.getPatients(ihelpID);
        if(patients.isEmpty()) {
            throw new IhelpPatientNotFoundException(ihelpID);
        }
        
        return fillParams(patients, ModelMetaInfoBuilder.newInstance(input).build());
    }
    
    @Override
    public String fillParams(String patientID, String providerID, String input) throws DataStoreException, IhelpPatientNotFoundException {
        PatientDTO patientDTO = PatientManager.getPatient(patientID, providerID);
        if(patientDTO == null) {
            throw new IhelpPatientNotFoundException(providerID + "/" + patientID);
        }
        
        return fillParams(Arrays.asList(patientDTO), ModelMetaInfoBuilder.newInstance(input).build());
    }
    
    
    
    
    private String fillParams(List<PatientDTO> patients, ModelMetaInfo modelMetaInfo) throws DataStoreException {
        try(ModelManagerParamsProcessor processor = ModelManagerParamsProcessor.newInstance(patients)) {
        
            final JSONObject jSONObject = new JSONObject();
            Iterator<Map.Entry<String, ModelMetaInfoElement>> it = modelMetaInfo.getElements().entrySet().iterator();
            while(it.hasNext()) {
                ModelMetaInfoElement metaInfoElement = it.next().getValue();
                System.out.println(metaInfoElement.getName());
                final JSONArray jSONArray = new JSONArray();
                jSONObject.put(metaInfoElement.getName(), jSONArray);
                
                //check first for snomed codes
                if(processor.getMappings().containsKey(metaInfoElement.getSnomed())) {
                    List<Object> values = processor.getValuesForCode(metaInfoElement.getSnomed());
                    if(!metaInfoElement.getMappedValues().isEmpty()) {
                        //calculate the value based on the mapping possible/real values
                        for(Object value : values) {
                            if(metaInfoElement.getMappedValues().containsKey(((String) value).toLowerCase())) {
                                jSONArray.put(metaInfoElement.getMappedValues().get(((String) value).toLowerCase()));
                            }
                        }
                    } else {
                        //get the value as it is
                        values.stream().forEach((Object obj) -> { jSONArray.put(obj);});
                    }
                } else if(metaInfoElement.getName().equalsIgnoreCase(ModelManagerParamsProcessor.GENDER)) {
                    String gender = processor.getGender().toLowerCase();
                    if(metaInfoElement.getMappedValues().containsKey(gender)) {
                        jSONArray.put(metaInfoElement.getMappedValues().get(gender));
                    } 
                } else if(metaInfoElement.getName().equalsIgnoreCase(ModelManagerParamsProcessor.AGE)) {
                    if(processor.getBirthDate()!=null) {
                        jSONArray.put(getAgeInterval(processor.getBirthDate()));
                    }
                } else if(processor.getMappings().containsKey(metaInfoElement.getName())) {
                    List<Object> values = processor.getValuesForCode(metaInfoElement.getName());
                    for(Object value : values) {
                        if(metaInfoElement.getMappedValues().containsKey(((String) value).toLowerCase())) {
                            jSONArray.put(metaInfoElement.getMappedValues().get(((String) value).toLowerCase()));
                        }
                    }
                } else {
                    Log.warn("Uknown snomed attribute named {}", metaInfoElement.getName());
                }
            }        

            return jSONObject.toString();
            
        } catch(SQLException | IOException ex) {
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    
    
    private String getAgeInterval(String input) {
        LocalDate currentDate = LocalDate.now();
        Date birthDate = IhelpDateFormatter.getDateFromString(input);
        LocalDate jodaDate = LocalDate.fromDateFields(birthDate);
        int interval = currentDate.getYear() - jodaDate.getYear();
        if(interval<40) {
            return "1";
        } else if(interval<50) {
            return "2";
        } else if(interval<60) {
            return "3";
        } else if(interval<70) {
            return "4";
        } else if(interval<80) {
            return "5";
        } else if(interval<90) {
            return "6";
        } else {
            return "7";
        }
    }
}
