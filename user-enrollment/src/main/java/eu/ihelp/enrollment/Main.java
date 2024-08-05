package eu.ihelp.enrollment;

import eu.ihelp.enrollment.model.ModelMetaInfo;
import eu.ihelp.enrollment.model.ModelMetaInfoBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class Main {
    
    public static void main(String [] args) throws IOException {
        
        processDataRemoveComas();
        
        String serializedMessage = readJsonFromFile("modelmanager/missing_snomed.json");
        
        JSONArray jsonArray = new JSONArray(serializedMessage);
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject jSONObject = jsonArray.getJSONObject(i);
            String res = jSONObject.getString("name") + ";";
            
            
            Object snomed = jSONObject.get("snomed");
            if(snomed instanceof String) {
                res+=snomed;
            } else {
                res+= ((JSONObject) snomed).getString("snomedCode");
            }
                    
                    
            System.out.println(res);        
        }
        
        
        
        //processDataRemoveComas();
        ModelMetaInfo modelMetaInfo = ModelMetaInfoBuilder.newInstance(serializedMessage).build();
        
        
        System.out.println("final");
    }
    
    private static void processData() throws FileNotFoundException, IOException {
        final String fileName = "/home/pavlos/leanxcale/Projects/iHelp/datasets/HDM_new/" + "DrugExposure_FullDataset";
        HashMap<String, String> keys = new HashMap<>();
        keys.put("2288", "");
        keys.put("3614", "");
        keys.put("3941", "");
        keys.put("4701", "");
        keys.put("6547", "");
        keys.put("6665", "");
        keys.put("6918", "");
        keys.put("7362", "");
        keys.put("8957", "");
        keys.put("14399", "");
        keys.put("15631", "");
        keys.put("19063", "");
        keys.put("19154", "");
        keys.put("19365", "");
        keys.put("19398", "");
        
        
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName + ".csv")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + "_demo.csv"));
                ) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String [] values = line.split(",");
                
                if(count++==0) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }
                
                if((count%1000)==0) {
                    System.out.println("Processing " + count);
                }
                
                if(keys.containsKey(values[1])) {
                    writer.write(line);
                    writer.newLine();
                }                
                
                if(values[0].equalsIgnoreCase("13039")) {
                    System.out.println("I am here");
                }
            }
        }
    }
    
    private static void processDataRemoveComas() throws FileNotFoundException, IOException {
        final String fileName = "/home/pavlos/leanxcale/Projects/iHelp/datasets/HDM_new/" + "measurement_limpios";
        HashMap<String, String> keys = new HashMap<>();
        keys.put("2288", "");
        keys.put("3614", "");
        keys.put("3941", "");
        keys.put("4701", "");
        keys.put("6547", "");
        keys.put("6665", "");
        keys.put("6918", "");
        keys.put("7362", "");
        keys.put("8957", "");
        keys.put("14399", "");
        keys.put("15631", "");
        keys.put("19063", "");
        keys.put("19154", "");
        keys.put("19365", "");
        keys.put("19398", "");
        
        
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName + ".csv")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + "_demo_no_comas.csv"));
                ) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String [] values = line.split(";");
                
                if(count++==0) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }
                
                if((count%1000)==0) {
                    System.out.println("Processing " + count);
                }
                
                if(keys.containsKey(values[1])) {
                    writer.write(removeComas(line));
                    writer.newLine();
                }                
            }
        }
    }
    
    private static String removeComas(String line) {
        char parenthesis = "\"".charAt(0);
        char coma = ",".charAt(0);
        boolean hasParenthesis = false;
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<line.length(); i++) {
            char currentCharacter = line.charAt(i);
            if(currentCharacter==parenthesis) {
                hasParenthesis = !hasParenthesis;
                continue;
            }
            
            if(currentCharacter==coma) {
                if(!hasParenthesis) {
                    sb.append(currentCharacter);
                }
            } else {
                sb.append(currentCharacter);
            }
        }
        
        return sb.toString();
    }
    
    
    private static void foros() {
        String input = null;
        while(!(input = readFromConsole()).equalsIgnoreCase("EXIT")) {
            String [] values = input.split(" ");
            Integer asfalistika = Integer.valueOf(values[0]);
            Double eisodima = Double.valueOf(values[1]);
            Double foros = 0.0;
            if(eisodima>10000) {
                foros = 900.0;
            }
            if(eisodima>20000) {
                foros+=2200;
            }
            if(eisodima>30000) {
                foros+=2800;
            }
            if(eisodima>40000) {
                foros+=3600;
            }
            
            foros += (eisodima - 40000)*0.44;
            eisodima = eisodima - foros;
            Double asfalistikesEisfores = 0.0;
            switch (asfalistika) {
                case 1:
                    asfalistikesEisfores = 238.22;
                    break;
                case 2:
                    asfalistikesEisfores = 285.87;
                    break;
                case 3:
                    asfalistikesEisfores = 342.59;
                    break;
                case 4:
                    asfalistikesEisfores = 411.78;
                    break;
                case 5:
                    asfalistikesEisfores = 493.46;
                    break;
                case 6:
                    asfalistikesEisfores = 642.06;
                    break;
                default:
                    throw new AssertionError();
            }
            
            asfalistikesEisfores = asfalistikesEisfores*12;
            eisodima = eisodima - asfalistikesEisfores;
            
            System.out.println("Gia eisodima " + values[1] + " k asfalistikes eisfores kathgorias " + values[0] + " tha plirwnw...");
            System.out.println(eisodima/12 + "e 8a pairnw to mhna");
            System.out.println(asfalistikesEisfores + "e sunolikes asfalistikes eisfores");
            System.out.println(foros + "e sunolikos foros");
        }
    }
    
    private static String readFromConsole() {
        System.out.println("-------------------------");
        System.out.println("Dwse asfalistikh kathgoria k sunoliko mis8o");
        Scanner in = new Scanner(System.in);
        return in.nextLine();
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
}
