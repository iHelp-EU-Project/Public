package eu.ihelp.enrollment.managers;

import eu.ihelp.enrollment.exceptions.DataStoreException;
import eu.ihelp.enrollment.services.impl.UserEnrollmentServiceImpl;
import eu.ihelp.enrollment.utils.DBAHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DBSchemaManager {
    private static final Logger Log = LoggerFactory.getLogger(DBSchemaManager.class);
    
    private static final String iHelpTable = "CREATE TABLE IHELP_PERSON(\n" +
                        "IHELPID VARCHAR,\n" +
                        "PATIENTID VARCHAR,\n" +
                        "PROVIDERID VARCHAR, \n" +
                        "PRIMARY KEY (IHELPID, PATIENTID, PROVIDERID),\n" +
                        "FOREIGN KEY (PATIENTID) REFERENCES PATIENT(PATIENTID),\n" +
                        "FOREIGN KEY (PROVIDERID) REFERENCES PATIENT(PROVIDERID)\n" +
                        ")";
    
    private static final String patientTable = "CREATE TABLE PATIENT (\n" +
                        "PATIENTID VARCHAR,\n" +
                        "PROVIDERID VARCHAR, \n" +
                        "GENDER VARCHAR, \n" +
                        "ACTIVE BOOLEAN, \n" +
                        "BIRTHDATE TIMESTAMP,\n" +
                        "PRIMARY KEY(PATIENTID, PROVIDERID)\n" +
                        ")";
    
    private static final String messagesTable = "CREATE TABLE IF NOT EXISTS MESSAGES (\n" +
                        "TRIGGERID VARCHAR(255),\n" +
                        "STUDYID VARCHAR(255),\n" +
                        "DIALOGUEID VARCHAR(255),\n" +
                        "SUBJECTID VARCHAR(255),\n" +
                        "NOTES VARCHAR,\n" +
                        "CREATEDBY VARCHAR(255),\n" +
                        "CREATIONTIME TIMESTAMP,\n" +
                        "SENTTIME TIMESTAMP,\n" +
                        "STATUS VARCHAR(255),\n" +
                        "TOPICCODE VARCHAR(255),\n" +
                        "TOPICSYSTEM VARCHAR(255),\n" +
                        "TOPICDISPLAY VARCHAR(255),\n" +
                        "NOTIFICATION_MESSAGE VARCHAR,\n" +
                        "COACHING_ELEMENT VARCHAR(255),\n" +
                        "COACHING_DOMAIN VARCHAR(255),\n" +
                        "COACHING_INTENT VARCHAR(255),\n" +
                        "HEALTH_OUTCOME VARCHAR(255),\n" +
                        "MESSAGE_FRAMING VARCHAR(255),\n" +
                        "SUBJECT_ABILITY VARCHAR(255),\n" +
                        "HEALTH_ORIENTATION VARCHAR(255),\n" +
                        "PRIMARY KEY(TRIGGERID, STUDYID, DIALOGUEID, SUBJECTID)\n" +
                        ")";
    
    public static void initDB() throws SQLException, DataStoreException {
        Connection connection = null;
        
        try {
            connection = DBAHelper.getInstance().getConnection();
            connection.setAutoCommit(true);
            
            System.out.println("Will create the db schema");
            if(createDefaultSchema(connection, "PATIENT", patientTable)) {
                Log.info("Table PATIENT has been added");
            }
            
            Thread.sleep(100);
            
            if(createDefaultSchema(connection, "IHELP_PERSON", iHelpTable)) {
                Log.info("Table IHELP_PERSON has been added");
            }
            
            Thread.sleep(100);
            
            if(createDefaultSchema(connection, "MESSAGES", messagesTable)) {
                Log.info("Table MESSAGES has been added");
            }
            
            Thread.sleep(100);
            
            
        } catch(DataStoreException ex) {
            ex.printStackTrace();
            if(connection!=null) {
                //connection.rollback();
            }
            throw ex;
        } catch(InterruptedException ex) {
            //DO NOTHING
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(connection!=null) {
                connection.close();
            }
        }
    }
    
    private static boolean createDefaultSchema(Connection connection, String TABLE_NAME, String createTableQuery) throws DataStoreException {
        TABLE_NAME = "APP." + TABLE_NAME;
        Statement statement = null;
        try {
            if(DBAHelper.getInstance().checkIfTableAlreadyExists(TABLE_NAME)) {
                return false;
            }

            Log.info("Will create schema for {} the entity", TABLE_NAME);
            statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
        } catch(SQLException ex) {
            Log.error("Error when tried to create DB table for entity {}. {}: {}", TABLE_NAME, ex.getSQLState(), ex.getMessage());
            throw new DataStoreException(ex.getMessage(), ex);
        } finally {
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    //do nothing
                }
            }
        }       
        
        return true;
    }
}
