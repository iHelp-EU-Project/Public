package eu.ihelp.store;

import eu.ihelp.store.model.ModelMetaInfoBuilder;
import eu.ihelp.store.server.utils.DBAHelper;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Main {
    
    public static void main(String [] args) throws IOException, FileNotFoundException, SQLException {
        importUniManData("/home/pavlos/Downloads/UNIMANPILOT_DATA_08_01_2024.csv");
        
        String serializedMessage = readJsonFromFile("model_manager_meta_8147.json");
        
        
        ModelMetaInfoBuilder.newInstance(8072, serializedMessage).build();
    }
    
    private static String readJsonFromFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
            String line;
            while((line = br.readLine())!=null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
    
    private static boolean hasValidData(String value) {
        if(value==null) {
            return false;
        }
        
        if(value.equalsIgnoreCase("")) {
            return false;
        }
        
        if(value.contains("-")) {
            return false;
        }
        
        return true;
    }
    
    private static void importUniManData(String filepath) throws FileNotFoundException, IOException, SQLException {
        String qInsertPatient = "INSERT INTO PATIENT (PATIENTID, PROVIDERID, GENDER, ACTIVE, BIRTHDATE, HEIGHT, WEIGHT) VALUES (?, 'UNIMAN', ? , true, ?, ?, ?)";
        
        String qInsertBMI = "INSERT INTO OBSERVATION (OBSERVATIONID, PROVIDERID, OBSERVATIONCODE, OBSERVATIONSYSTEM, OBSERVATIONDISPLAY, \n" +
            "EFFECTIVEDATETIME, OBSERVATIONCATEGORYCODE, OBSERVATIONCATEGORYSYSTEM, OBSERVATIONCATEGORYDISPLAY, VALUEQUANTITY,\n" +
            "VALUESTRING,PATIENTID, "
          + "STATUS,  VALUESYSTEM, VALUECODE, VALUEUNIT, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, VALUEBOOLEAN, BODYSITESYSTEM, BODYSITECODE, BODYSITEDISPLAY, ENCOUNTERID, PRACTITIONERID)\n" +
            "VALUES(\n" +
            "?, 'UNIMAN', '446974000', 'SNOMED', 'BMI (body mass index) centile', \n" +
            "?, 'vital-signs', 'http://terminology.hl7.org/CodeSystem/observation-category', 'Vital Signs', ?,\n" +
            "?, ?,\n" +
            "'', '', '', '', '', \n" +
            "'', '', 0, '', '', \n" +
            "'', 0, false, '', '', \n" +
            "'', 1, 1\n" +
            ")";
        String qInserBiologicalAge = "INSERT INTO OBSERVATION (OBSERVATIONID, PROVIDERID, OBSERVATIONCODE, OBSERVATIONSYSTEM, OBSERVATIONDISPLAY, \n" +
            "EFFECTIVEDATETIME, OBSERVATIONCATEGORYCODE, OBSERVATIONCATEGORYSYSTEM, OBSERVATIONCATEGORYDISPLAY, VALUEQUANTITY,\n" +
            "VALUESTRING,PATIENTID, "
          + "STATUS,  VALUESYSTEM, VALUECODE, VALUEUNIT, VALUESYSTEMLOW, VALUECODELOW, VALUEUNITLOW, VALUEQUANTITYLOW, VALUESYSTEMHIGH, VALUECODEHIGH, VALUEUNITHIGH, VALUEQUANTITYHIGH, VALUEBOOLEAN, BODYSITESYSTEM, BODYSITECODE, BODYSITEDISPLAY, ENCOUNTERID, PRACTITIONERID)\n" +
            "VALUES(\n" +
            "?, 'UNIMAN', '102519007', 'SNOMED', 'Biological age', \n" +
            "?, 'vital-signs', 'http://terminology.hl7.org/CodeSystem/observation-category', 'Vital Signs', ?,\n" +
            "?, ?,\n" +
            "'', '', '', '', '', \n" +
            "'', '', 0, '', '', \n" +
            "'', 0, false, '', '', \n" +
            "'', 1, 1\n" +
            ")";
        
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        
        
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            Connection conn = DBAHelper.getInstance().getConnection();
            PreparedStatement statementPatient = conn.prepareStatement(qInsertPatient);
            PreparedStatement statementBMI = conn.prepareStatement(qInsertBMI);
            PreparedStatement statementBiologicalAge = conn.prepareStatement(qInserBiologicalAge);    
                ) {
            String line;
            conn.setAutoCommit(false);
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if(count==1) {
                    continue;
                }
                String [] values = line.split(";");
                String personID = values[0];
                String age = values[1];
                String gender = values[2];
                String heightStr = values[3];
                String weightStr = values[4];
                String bmi = values[5];
                String biologicalAge = values[21];
                
                String result = personID + "|" +
                        age + "|" +
                        gender + "|" +
                        heightStr + "|" +
                        weightStr + "|" +
                        bmi + "|" +
                        biologicalAge;
                
                System.out.println(result);
                
                /*
                //add patient
                statementPatient.setString(1, personID);
                statementPatient.setString(2, gender);
                if(hasValidData(age)) {
                    Long ageLong = Long.valueOf(age);
                    LocalDate dateToAdd = localDate.minusYears(ageLong);
                    
                    dateToAdd = dateToAdd.minusDays(localDate.getDayOfYear()-1);
                    statementPatient.setTimestamp(3, Timestamp.valueOf(dateToAdd.atStartOfDay()));
                } else {
                    statementPatient.setNull(3, Types.TIMESTAMP);
                }
                
                if(hasValidData(heightStr)) {
                    statementPatient.setDouble(4, Double.valueOf(heightStr));
                } else {
                    statementPatient.setNull(4, Types.DOUBLE);
                }
                
                if(hasValidData(weightStr)) {
                    statementPatient.setDouble(5, Double.valueOf(weightStr));
                } else {
                    statementPatient.setNull(5, Types.DOUBLE);
                }
                statementPatient.execute();
                */
                
                //add bmi
                if(hasValidData(bmi)) {
                    statementBMI.setString(1, UUID.randomUUID().toString());
                    statementBMI.setTimestamp(2, Timestamp.valueOf(currentDateTime));
                    statementBMI.setDouble(3, Double.valueOf(bmi));
                    statementBMI.setString(4, bmi);
                    statementBMI.setString(5, personID);
                    statementBMI.execute();                
                }
                
                
                //add biological age
                if(hasValidData(biologicalAge)) {
                    statementBiologicalAge.setString(1, UUID.randomUUID().toString());
                    statementBiologicalAge.setTimestamp(2, Timestamp.valueOf(currentDateTime));
                    statementBiologicalAge.setDouble(3, Double.valueOf(biologicalAge));
                    statementBiologicalAge.setString(4, biologicalAge);
                    statementBiologicalAge.setString(5, personID);
                    statementBiologicalAge.execute();                
                }
                
                conn.commit();
            }
        }
    }
}
