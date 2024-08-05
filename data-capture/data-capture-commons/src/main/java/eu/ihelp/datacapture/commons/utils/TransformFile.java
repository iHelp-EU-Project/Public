package eu.ihelp.datacapture.commons.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TransformFile {
    
    
    public static void main(String [] args) throws FileNotFoundException, IOException {
        final String inputFile = "/home/pavlos/leanxcale/Projects/MobiSpaces/datasets/20221109_bus.csv";
        final String outputFile = "/home/pavlos/leanxcale/Projects/MobiSpaces/datasets/20221109_bus_fixed.csv";
        
        Files.deleteIfExists(new File(outputFile).toPath());
        int count = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));) {
            String line;
            while((line=reader.readLine())!=null) {
                line = line.replace(",", ";");

                writer.write(line.toCharArray());
                writer.newLine();

                count++;
                if(count%100==0) {
                    System.out.println(count);
                }
            }
        }
    } 
}
