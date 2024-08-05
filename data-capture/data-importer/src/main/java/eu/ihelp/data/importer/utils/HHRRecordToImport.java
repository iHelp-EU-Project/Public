package eu.ihelp.data.importer.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.avro.generic.GenericRecord;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class HHRRecordToImport {
    private final String topic;
    private final List<KeyValueRecord> keyValueRecords;

    public HHRRecordToImport(String topic, int size) {
        this.topic = topic;
        this.keyValueRecords = new ArrayList<>(size);
    }
    
    public HHRRecordToImport(String topic) {
        this.topic = topic;
        this.keyValueRecords = new ArrayList<>();
    }
    
    public static class KeyValueRecord {
        private final GenericRecord keyRecord;
        private final GenericRecord valueRecord;

        public KeyValueRecord(GenericRecord keyRecord, GenericRecord valueRecord) {
            this.keyRecord = keyRecord;
            this.valueRecord = valueRecord;
        }

        public GenericRecord getKeyRecord() {
            return keyRecord;
        }

        public GenericRecord getValueRecord() {
            return valueRecord;
        }
    }

    public List<KeyValueRecord> getKeyValueRecords() {
        return keyValueRecords;
    }

    public String getTopic() {
        return topic;
    }
    
    public void addNewRecord(GenericRecord key, GenericRecord value) {
        this.keyValueRecords.add(new KeyValueRecord(key, value));
    }
    
    public void addNewRecord(KeyValueRecord keyValueRecord) {
        this.keyValueRecords.add(keyValueRecord);
    }
    
    public void addNewRecords(List<KeyValueRecord> keyValueRecords) {
        for(KeyValueRecord keyValueRecord : keyValueRecords) {
            this.keyValueRecords.add(keyValueRecord);
        }
    }

    @Override
    public String toString() {
        return "HHRRecordToImport{" + "topic=" + topic + ", keyValueRecords=" + keyValueRecords.size() + '}';
    }
    
    
}
