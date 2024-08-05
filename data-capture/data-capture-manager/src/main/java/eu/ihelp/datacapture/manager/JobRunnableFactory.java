package eu.ihelp.datacapture.manager;

import eu.ihelp.datacapture.commons.model.ConnectorArgumentsAbstract;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsData;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFTP;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsFile;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsHealthentia;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsHealthentiaPersons;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsRelationalDB;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsRest;
import eu.ihelp.datacapture.commons.model.ConnectorArgumentsTest;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsAbstract;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsGateway;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafka;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsKafkaAvro;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsPrint;
import eu.ihelp.datacapture.commons.model.ConverterArgumentsTest;
import eu.ihelp.datacapture.functions.DataRowItem;
import eu.ihelp.datacapture.functions.connectors.ConnectorAbstract;
import eu.ihelp.datacapture.functions.connectors.ConnectorData;
import eu.ihelp.datacapture.functions.connectors.ConnectorFTP;
import eu.ihelp.datacapture.functions.connectors.ConnectorFile;
import eu.ihelp.datacapture.functions.connectors.ConnectorHealthentia;
import eu.ihelp.datacapture.functions.connectors.ConnectorHealthentiaPersons;
import eu.ihelp.datacapture.functions.connectors.ConnectorRelationalDB;
import eu.ihelp.datacapture.functions.connectors.ConnectorRest;
import eu.ihelp.datacapture.functions.connectors.ConnectorTest;
import eu.ihelp.datacapture.functions.converters.ConverterAbstract;
import eu.ihelp.datacapture.functions.converters.ConverterGateway;
import eu.ihelp.datacapture.functions.converters.ConverterKafka;
import eu.ihelp.datacapture.functions.converters.ConverterKafkaAvro;
import eu.ihelp.datacapture.functions.converters.ConverterPrint;
import eu.ihelp.datacapture.functions.converters.ConverterTest;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.avro.Schema;
import org.json.JSONObject;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JobRunnableFactory {
    
    public static JobRunnable createInstance(
            String id,
            String dataProvider,
            String dataset,
            Schema schema,
            Schema schemaKey,
            JSONObject confParameters,
            int batchSize,
            ConnectorArgumentsAbstract connectorArgument,
            ConverterArgumentsAbstract converterArguments
    ) {
        ArrayBlockingQueue<DataRowItem> queue = new ArrayBlockingQueue<>(100);
        return new JobRunnable(createInstance(schema, schemaKey, connectorArgument, queue), createInstance(id, dataProvider, dataset, schema, schemaKey, confParameters, batchSize, converterArguments, queue), id, queue);
    }
    
    private static ConnectorAbstract createInstance(
            Schema schema,
            Schema schemaKey,
            ConnectorArgumentsAbstract connectorArgument,
            ArrayBlockingQueue<DataRowItem> queue) {
        
        if(connectorArgument instanceof ConnectorArgumentsTest) {
            return new ConnectorTest((ConnectorArgumentsTest) connectorArgument, schema, schemaKey, queue);
        } else if(connectorArgument instanceof ConnectorArgumentsFTP) {
            return new ConnectorFTP((ConnectorArgumentsFTP) connectorArgument, schema, schemaKey, queue);
        } if(connectorArgument instanceof ConnectorArgumentsFile) {
            return new ConnectorFile((ConnectorArgumentsFile) connectorArgument, schema, schemaKey, queue);
        } if(connectorArgument instanceof ConnectorArgumentsData) {
            return new ConnectorData((ConnectorArgumentsData) connectorArgument, schema, schemaKey, queue);
        } if(connectorArgument instanceof ConnectorArgumentsRest) {
            return new ConnectorRest((ConnectorArgumentsRest) connectorArgument, schema, schemaKey, queue);
        } if(connectorArgument instanceof ConnectorArgumentsRelationalDB) {
            return new ConnectorRelationalDB((ConnectorArgumentsRelationalDB) connectorArgument, schema, schemaKey, queue);
        } if(connectorArgument instanceof ConnectorArgumentsHealthentia) {
            return new ConnectorHealthentia((ConnectorArgumentsHealthentia) connectorArgument, schema, schemaKey, queue);
        } if(connectorArgument instanceof ConnectorArgumentsHealthentiaPersons) {
            return new ConnectorHealthentiaPersons((ConnectorArgumentsHealthentiaPersons) connectorArgument, schema, schemaKey, queue);
        } else {
            throw new IllegalArgumentException("Unkonwn connector argument type " + connectorArgument.getClass().getName());
        }
    }
    
    private static ConverterAbstract createInstance(
            String jobID,
            String dataProvider,
            String dataSet,
            Schema schema,
            Schema schemaKey,
            JSONObject confParameters,
            int batchSize,
            ConverterArgumentsAbstract converterArgument,
            ArrayBlockingQueue<DataRowItem> queue) {
        
        if(converterArgument instanceof ConverterArgumentsGateway) {
            return new ConverterGateway(jobID, schema, schemaKey, queue, (ConverterArgumentsGateway) converterArgument);
        } else if(converterArgument instanceof ConverterArgumentsKafka) {
            return new ConverterKafka(jobID, dataProvider, dataSet, schema, schemaKey, queue, (ConverterArgumentsKafka) converterArgument, confParameters, batchSize);
        } else if(converterArgument instanceof ConverterArgumentsKafkaAvro) {
            return new ConverterKafkaAvro(jobID, schema, schemaKey, queue, (ConverterArgumentsKafkaAvro) converterArgument);
        } else if(converterArgument instanceof ConverterArgumentsPrint) {
            return new ConverterPrint(jobID, schema, schemaKey, queue, (ConverterArgumentsPrint) converterArgument, confParameters, batchSize);
        } else if(converterArgument instanceof ConverterArgumentsTest) {
            return new ConverterTest(jobID, schema, schemaKey, queue, (ConverterArgumentsTest) converterArgument);
        } else {
            throw new IllegalArgumentException("Unkonwn converter argument type " + converterArgument.getClass().getName());
        }
    }
}
