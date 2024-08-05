package eu.ihelp.enrollment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TestMain {
    private static final Logger Log = LoggerFactory.getLogger(TestMain.class);
    
    static final String OUTPUT_FOLDER_PATH = "/home/pavlos/Downloads/buses/";
    static final String INPUT_FILE_PATH = "/home/pavlos/Downloads/20231013_bus.csv";
    
    
    
    
    public static void main(String [] args) throws IOException {        
        HashMap<String, List<String>> processedLines = getProcessedLines(INPUT_FILE_PATH);
        
        String timeToStart = "13:55:55";
        String dateToStart = "19/03/2024";
        //String timeToStart = null;
        //String dateToStart = null;
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        if(timeToStart==null) { //if not provided, start by the first one
            timeToStart = "00:00:00";
        }
        LocalTime localTime = LocalTime.parse(timeToStart, timeFormatter);
        LocalDateTime localDateTimeToStart = null;
        
        //find datetime to start
        for (Map.Entry<String, List<String>> entry : processedLines.entrySet()) {
            localDateTimeToStart = LocalDateTime.parse(entry.getValue().get(0).split(",")[2], formatter);
            localDateTimeToStart = LocalDateTime.of(localDateTimeToStart.toLocalDate(), localTime);
            break;
        }
        
        //remove all dates that are before that time
        for (Map.Entry<String, List<String>> entry : processedLines.entrySet()) {
            Log.info("Filtering out dates for bus {}", entry.getKey());
            Iterator<String> it = entry.getValue().iterator();
            while(it.hasNext()) {
                String datetimeStr = it.next();
                LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr.split(",")[2], formatter);
                if(localDateTime.isBefore(localDateTimeToStart)) {
                    it.remove();
                } else {
                    break;
                }
            }
        }
        
        /*
        //now remove dates of the next day
        for (Map.Entry<String, List<String>> entry : processedLines.entrySet()) {
            Log.info("Filtering out dates for bus {}", entry.getKey());
            Iterator<String> it = entry.getValue().iterator();
            while(it.hasNext()) {
                String datetimeStr = it.next();
                LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr.split(",")[2], formatter);
                if(localDateTime.toLocalDate().isAfter(localDateTimeToStart.toLocalDate())) {
                    it.remove();
                } else {
                    break;
                }
            }
        }
        */
        
        //now from the remaining ones, calculate the minimum
        LocalDateTime minLocalDateTime = null;
        Iterator<Map.Entry<String, List<String>>> itBuses = processedLines.entrySet().iterator();
        while(itBuses.hasNext()) {
            Map.Entry<String, List<String>> entry = itBuses.next();
            if(entry.getValue().isEmpty()) {
                itBuses.remove();
            } else {
                String [] values = entry.getValue().get(0).split(",");
                LocalDateTime currentLocalDateTime = LocalDateTime.parse(values[2], formatter);
                if(minLocalDateTime==null) {
                    minLocalDateTime = currentLocalDateTime;
                } else {
                    if(currentLocalDateTime.compareTo(minLocalDateTime)<0) {
                        minLocalDateTime = currentLocalDateTime;
                    }
                }
            }
        }
        
        if(processedLines.isEmpty()) {
            Log.warn("No traffic for that time");
            return;
        }
        
        LocalDate localDateForSimulation = null;
        if(dateToStart!=null) {
            localDateForSimulation = LocalDate.parse(dateToStart, dateFormatter);
        }
        
        simulateTraffic(processedLines, minLocalDateTime, localDateForSimulation);
    }
    
    private static void simulateTraffic(HashMap<String, List<String>> buses, LocalDateTime minLocalDateTime, LocalDate localDateForSimulation) {
        for (Map.Entry<String, List<String>> entry : buses.entrySet()) {
            BusSimulation busSimulation = new BusSimulation(entry.getKey(), entry.getValue(), minLocalDateTime, localDateForSimulation);            
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(busSimulation);
            executor.shutdown();
        }
    }
    
    private static HashMap<String, List<String>> getProcessedLines(String inputFilePath) throws FileNotFoundException, IOException {
        HashMap<String, List<String>> buses = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(inputFilePath)));) {
            String currentLine;
            int count = 0;
            while((currentLine=reader.readLine())!=null) {
                count++;
                if(count%1000==0) {
                    Log.info("I read {} lines", count);
                }
                String [] values = currentLine.split(",");
                String bus = values[0];
                if(!buses.containsKey(bus)) {
                    buses.put(bus, new ArrayList<>());
                }
                buses.get(bus).add(currentLine);
            }
        }
        Log.info("Total buses are: {}", buses.size());
        return buses;
    }
    
    private static void processAMTBusses() throws FileNotFoundException, IOException {
        
        
        HashMap<String, List<String>> buses = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(INPUT_FILE_PATH)));) {
            String currentLine;
            int count = 0;
            while((currentLine=reader.readLine())!=null) {
                count++;
                if(count%1000==0) {
                    Log.info("I read {} lines", count);
                }
                String [] values = currentLine.split(",");
                String bus = values[0];
                if(!buses.containsKey(bus)) {
                    buses.put(bus, new ArrayList<>());
                }
                buses.get(bus).add(currentLine);
            }
        }
        Log.info("Total buses are: {}", buses.size());
        
        int count = 0;
        for (Map.Entry<String, List<String>> entry : buses.entrySet()) {
            Log.info("Will write file {} out of {}", ++count, buses.size());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_FOLDER_PATH + entry.getKey())));) {
                for(String line : entry.getValue()) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }        
    }
    
    static class BusSimulation implements Runnable {
        private final String busName;
        private final List<String> history;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        private final LocalDate localDateForSimulation;
        private LocalDateTime prevLocalDateTime;

        public BusSimulation(String busName, List<String> history, LocalDateTime minLocalDateTime, LocalDate localDateForSimulation) {
            this.busName = busName;
            this.history = history;
            this.prevLocalDateTime = minLocalDateTime;
            this.localDateForSimulation = localDateForSimulation;
        }
        
        

        @Override
        public void run() {
            if(this.localDateForSimulation!=null) {
                this.prevLocalDateTime = LocalDateTime.of(localDateForSimulation, this.prevLocalDateTime.toLocalTime());
            }
            
            try {
                for(String line : history) {
                    String [] values = line.split(",");
                    LocalDateTime currentLocalDateTime = LocalDateTime.parse(values[2], formatter);
                    
                    if(this.localDateForSimulation!=null) {
                        currentLocalDateTime = LocalDateTime.of(localDateForSimulation, currentLocalDateTime.toLocalTime());
                    }
                    
                    Duration duration = Duration.between(prevLocalDateTime, currentLocalDateTime);
                    Thread.sleep(duration.getSeconds()*1000);                    
                    
                    Log.info("{} at {}: {}", busName, formatter.format(currentLocalDateTime), values[1]);
                    prevLocalDateTime = currentLocalDateTime;
                }
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(TestMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
