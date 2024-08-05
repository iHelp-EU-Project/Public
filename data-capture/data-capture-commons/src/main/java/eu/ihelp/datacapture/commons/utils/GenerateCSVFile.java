package eu.ihelp.datacapture.commons.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class GenerateCSVFile {
    private static final Logger Log = LoggerFactory.getLogger(GenerateCSVFile.class);
    private static final String DEFAULT_FILE_NAME = "output.csv";
    private static final DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    private static final Random rd = new Random();    
    
    public static void main(String [] args) throws FileNotFoundException, IOException {
        Log.info("Starting...");
        
        File file;
        if(args.length==0) {
            file = new File(DEFAULT_FILE_NAME);
        } else {
            file = new File(args[0]);
        }
        
        try(FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
            for(int i=1; i<10000; i++) {
                String line = generateLine(i);
                Log.info(line);
                bw.write(line);
                bw.newLine();
            }
        }
    }
    
    
    private static String generateLine(int count) {        
        StringBuilder sb = new StringBuilder(count+";");
        sb.append("firstname" + count +";");
        sb.append("lastname" + count +";");
        sb.append(rd.nextInt(90) + ";");
        sb.append(dateTimeFormatter.format(new Date(System.currentTimeMillis() - rd.nextInt()*1000)) + ";");
        sb.append(rd.nextDouble() + ";");
        sb.append("city" + count + ";");
        sb.append(rd.nextBoolean() + ";");
        sb.append(rd.nextLong());
        return sb.toString();
    }
}
