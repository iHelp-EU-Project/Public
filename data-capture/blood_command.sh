#!/bin/bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"datasourceID":"FPG","datasetID":"blood","schema":{"type":"record","name":"blood","namespace":"eu.ihelp.fpg","fields":[{"name":"analysis_name","type":"string"},{"name":"analysis_date","type":{"type":"long","logicalType":"timestamp-millis"}},{"name":"analysis_value","type":"double"},{"name":"analysis_unit","type":["string","null"]}]},"schemaKey":{"type":"record","name":"bloodPK","namespace":"eu.ihelp.fpg","fields":[{"name":"patient_id","type":"string"}]},"confParameters":{"cleaning":[{"id":["int","not_null","max_5_digit"]},{"patient_id":["string","not_null"]}],"harmonizer":{"key1":"value1","key2":"value2"}},"connectorArguments":{"type":"FILE","path":"/datasets/blood_table_v2.csv","datePattern":"yyyy-MM-dd","delimiter":";","nullString":"NULL","skipFirst":true},"converterArguments":{"type":"KAFKA","datePattern":"yyyy-MM-dd HH:mm:ss z","url":"10.160.65.26","port":"30003","topic":"data_capture_input"},"batchSize":2}' 'http://10.160.65.26:30007/ihelp/datacapture'
