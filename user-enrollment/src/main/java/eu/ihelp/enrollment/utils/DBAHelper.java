package eu.ihelp.enrollment.utils;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DBAHelper {
    private static final Logger Log = LoggerFactory.getLogger(DBAHelper.class);
    private static final String DBCONFIG_FILE = "datastore.properties";

    private String driver;
    private String protocol;
    private String port;
    private String cluster;
    private String dbName;
    private String connectionString;
    private String userName;
    private int snapshotRefreshTime;
    private boolean waitForNextSnapshot;
    private String kiviConnectionString;
    
    
    private DBAHelper() {
    }
    
    private static class DBAHelperHolder {
        private static final DBAHelper INSTANCE = buildInstance();
        
        @SuppressWarnings("UseSpecificCatch")
        static DBAHelper buildInstance() {
            DBAHelper instance = new DBAHelper();
            
            try {
                //load the configuration from file
                Properties properties = new Properties();
                ClassLoader classLoader = instance.getClass().getClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(DBCONFIG_FILE);
                properties.load(inStream);
                
                //create the instance
                instance.driver = properties.getProperty("driver");
                instance.protocol = properties.getProperty("protocol");
                instance.cluster = properties.getProperty("cluster");
                instance.dbName = properties.getProperty("dbName");
                instance.port = properties.getProperty("port");
                instance.userName = properties.getProperty("username", "pdt");
                instance.snapshotRefreshTime = Integer.valueOf(properties.getProperty("snapshot.refresh.time", "10"));
                instance.waitForNextSnapshot = Boolean.valueOf(properties.getProperty("wait.next.snapshot", "false"));
                //instance.connectionString = properties.getProperty("connection");
                
                String dataStoreHost = System.getenv("DATASTORE_HOST");
                String dataStorePort = System.getenv("DATASTORE_PORT");
                
                if(dataStorePort!=null) {
                    instance.port = dataStorePort;
                }
                
                String connectionString = "";
                if(dataStoreHost==null) {
                    connectionString = instance.protocol + instance.cluster + ":" + instance.port + "/" + instance.dbName + ";user=" + instance.userName;
                    instance.connectionString = connectionString;
                    Log.info("'{}' environment variable was not found. Will try to connect to: {}", "DATASTORE_HOST", instance.connectionString);
                } else {
                    instance.cluster = dataStoreHost;
                    connectionString = instance.protocol + instance.cluster + ":" + instance.port + "/" + instance.dbName + ";user=" + instance.userName;
                    instance.connectionString = connectionString;
                    Log.info("'{}' environment variable was found. Will try to connect to: {}", "DATASTORE_HOST", instance.connectionString);
                }
                
                connectionString = instance.protocol + instance.cluster + ":" + instance.port + "/" + instance.dbName + ";user=" + instance.userName;
                instance.connectionString = connectionString;
                System.out.println(connectionString);
                
                instance.kiviConnectionString = "lx://" + instance.cluster + ":9776/" + instance.dbName + "@" + instance.userName + ";KVPROXY=" + instance.cluster + "!9800";
                System.out.println(instance.kiviConnectionString);
                
                //load the driver
                loadDriver(instance.driver);
                
                
                Properties props = new Properties(); // connection properties
                int count = 25;  
                System.out.println("Connection string will be: " + instance.connectionString);
                while(count-- > 0) {
                    try {
                        DriverManager.getConnection(connectionString, props);
                        Log.info("Connection established with data store at {}", instance.connectionString);
                        break;
                    } catch(SQLException ex) {
                        System.err.println("Could not find datastore at " + instance.connectionString + " . will sleep and retry " + count + " times");
                        Log.warn("Could not find datastore at {}. will sleep and retry {} times", instance.connectionString, count);
                        Thread.sleep(5000);
                        
                        if(count==0) {
                            throw ex;
                        }
                    }
                }
                
            } catch(Exception ex) { //if anything fails, then break the jvm
                ex.printStackTrace();
                exit(1);
            }
            return instance;
        }
        
        
        /**
         * Loads the appropriate JDBC driver.
         */
        private static void loadDriver(String driver) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            /*
             *  The JDBC driver is loaded by loading its class.
             *  If you are using JDBC 4.0 (Java SE 6) or newer, JDBC drivers may
             *  be automatically loaded, making this code optional.
             *
             */
            try {
                Class.forName(driver).newInstance();
                Log.info("Loaded the appropriate driver");
            } catch (ClassNotFoundException cnfe) {
                Log.error("Unable to load the JDBC driver " + driver);
                Log.error("Please check your CLASSPATH.");
                cnfe.printStackTrace(System.err);
                throw cnfe;
            } catch (InstantiationException ie) {
                Log.error("Unable to instantiate the JDBC driver " + driver);
                ie.printStackTrace(System.err);
                throw ie;
            } catch (IllegalAccessException iae) {
                Log.error("Not allowed to access the JDBC driver " + driver);
                iae.printStackTrace(System.err);
                throw iae;
            }
        }
    }
    
    public static DBAHelper getInstance() {
        return DBAHelperHolder.INSTANCE;
    }
    
    public Connection getConnection() throws SQLException {
        Properties props = new Properties(); // connection properties
        Connection conn = DriverManager.getConnection(connectionString, props);
        return conn;
    }
    
    public int getSnapshotRefreshTime() {
        return snapshotRefreshTime;
    }

    public boolean isWaitForNextSnapshot() {
        return waitForNextSnapshot;
    }

    public String getDbName() {
        return dbName;
    }
    
    
    /**
     *
     * Checks if a given table already exists in the database. This will be used
     * internally by the *DBA class to check if a table needs to be created or not
     *
     * @param tableName the name of the table
     * @return true if table already exists, false otherwise
     * @throws SQLException
     */
    public boolean checkIfTableAlreadyExists(String tableName) throws SQLException {
        try(Connection conn = getConnection()) {
            return checkIfTableAlreadyExists(conn, tableName);
        } catch(Exception ex) {
            Log.error("Error while checking if table {}| exists. {}: {}", tableName, ex.getClass().getName(), ex.getMessage());
            throw new SQLException(ex);
        } 
    }
    
    /**
     *
     * Checks if a given table already exists in the database. This will be used
     * internally by the *DBA class to check if a table needs to be created or not
     *
     * @param tableName the name of the table
     * @return true if table already exists, false otherwise
     * @throws SQLException
     */
    public boolean checkIfTableAlreadyExists(Connection conn, String tableName) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        String schemaName = null;
        
        if(tableName.contains(".")) { //table name comes with a non default schema
            schemaName = tableName.substring(0, tableName.indexOf("."));
            tableName = tableName.substring(tableName.indexOf(".") + 1);
        }

        try {
            statement = conn.createStatement();
            
            String query = "SELECT * FROM SYSMETA.TABLES WHERE tableName = '" + tableName.toUpperCase() + "'";
            if(schemaName!=null) {
                query += " AND tableSchem = '" + schemaName.toUpperCase() + "'";
            } else {
                query += " AND tableSchem = '" + DBAHelper.getInstance().getDbName().toUpperCase() + "'";
            }
            rs = statement.executeQuery(query);
            if(rs.next()) {
                return true;
            }
            
        } catch(Exception ex) {
            Log.error("Error while checking if table {}| exists. {}: {}", tableName, ex.getClass().getName(), ex.getMessage());
            throw new SQLException(ex);
        } finally {
            if(rs!=null) {
                rs.close();
            }
            if(statement!=null) {
                statement.close();
            }
        }

        return false;
    }
    
    
    
    /**
     *
     * Checks if a given table already exists in the database. under a specific schema. 
     * This will be used internally by the *DBA class to check if a table needs to 
     * be created or not
     *
     * @param schemaName
     * @param schemaID
     * @param shemaID
     * @return true if table already exists, false otherwise
     * @throws SQLException
     */
    public boolean checkIfTableExistsInSchema(String schemaName, String schemaID) throws SQLException { 
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        

        try {
            conn = getConnection();
            statement = conn.createStatement(); 
            rs = statement.executeQuery("SELECT * FROM SYS.SYSSCHEMAS WHERE SCHEMAID = '" + schemaID + "' AND SCHEMANAME = '" + schemaName.toUpperCase() + "'");
            if(rs.next()) {
                return true;
            }
            
            
        } catch(SQLException ex) {
            Log.error("Error while checking if schema {}| exists. {}: {}", schemaName, ex.getClass().getName(), ex.getMessage());
            throw new SQLException(ex);
        } finally {
            if(conn!=null) {
                conn.close();
            }
            if(statement!=null) {
                statement.close();
            }
            if(rs!=null) {
                rs.close();
            }
        }

        return false;
    }
    
    

    /**
     * Returns the number of rows of a given table
     *
     * @param tableName
     * @return
     */
    public int countRows(String tableName) throws DataStoreException {
        int result = 0;
        String query = "SELECT count(*) FROM " + tableName;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBAHelper.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }

        } catch(SQLException ex) {
            Log.error("Error when tried to get the rows count from table {}. {}: {}", tableName, ex.getSQLState(), ex.getMessage());
            throw new DataStoreException(ex.getMessage(), ex);
        } finally {
            if(resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    //do nothing
                }
            }
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    //do nothing
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    //do nothing
                }
            }
        }

        return result;
    }
}
