import datetime
import jaydebeapi
import os
import sys


def validateMonthAndYear(kwargs):
    try:
        month = int(kwargs.get("month"))
        year = int(kwargs.get("year"))

        if 1 <= month <= 12:
            if year >= 2022:
                return True
            else:
                print(
                    f"The provided year value {year} is invalid. Year should be greater than 2022 ")

        else:
            print(
                f"The provided month value {month} is invalid. Month should be between 1 and 12.  ")
    except ValueError:
        print("Month and year should be number values. You've provided string either for month or year")


def createTableIfNotExists():
    dbURL = os.getenv(
        "DB_URL", "jdbc:leanxcale://147.102.230.182:30001/ihelp")

    dbDriverPath = os.getenv("DB_DRIVER_PATH",
                             "../ext-lib/qe-driver-1.8-RC.9-jar-with-dependencies.jar")
    dbUser = os.getenv("DB_USER", "app")
    dbPw = os.getenv("DB_PW", "")
    dbDriverClass = os.getenv("DB_DRIVER_CLASS", "com.leanxcale.client.Driver")

    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
        curs = conn.cursor()
        statement = """
                    CREATE TABLE IF NOT EXISTS DEVICE_ADHERENCE_NEW (
	                    SID VARCHAR(50),
	                    PILOT VARCHAR(50),
	                    PERC DOUBLE,
	                    MON INTEGER,
	                    YR INTEGER
                    )
                    """
        try:
            curs.execute(statement)
        except:
            populateDeviceData(
                **dict(arg.split('=') for arg in sys.argv[1:]))

        conn.commit()
        curs.close()


def populateDeviceData(**kwargs):
    now = datetime.datetime.now()
    current_month = now.month - 1 if (now.month != 1) else 12
    current_year = now.year if (now.month != 1) else now.year - 1
    pilots = ['HDM', 'MUP', 'FPG', 'TMU', 'UNIMAN']

    # This will be run on the 15th day of the month
    print(
        f"Report will run for the  month={current_month} and current year={current_year}")

    dbURL = os.getenv(
        "DB_URL", "jdbc:leanxcale://147.102.230.182:30001/ihelp")

    dbDriverPath = os.getenv("DB_DRIVER_PATH",
                             "../ext-lib/qe-driver-1.8-RC.9-jar-with-dependencies.jar")
    dbUser = os.getenv("DB_USER", "app")
    dbPw = os.getenv("DB_PW", "")
    dbDriverClass = os.getenv("DB_DRIVER_CLASS", "com.leanxcale.client.Driver")

    print(
        f" The db connection params are url={dbURL}, driverPath={dbDriverPath}, dbUser={dbUser}, dbPw={dbPw},dbDriverClass={dbDriverClass}  ")

    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
        for pilot in pilots:
            sqlQuestAdh = """
            INSERT INTO DEVICE_ADHERENCE_NEW(sid, pilot, perc, mon, yr)
            (SELECT DISTINCT ihelpid sid, '{}' AS pilot, pat.perc, '{}' AS mon, '{}' AS yr 
            FROM
              (
                SELECT SUBJECTID,(count(SUBJECTID)*100.00/MAX(DM)) AS perc
                 FROM (
                        SELECT PATIENTID SUBJECTID , MAX(VALUESTRING) mvalue,MAX(EXTRACT(DAY FROM LAST_DAY(EFFECTIVEDATETIME))) AS DM
                        FROM  OBSERVATION 
                        WHERE VALUESTRING >0 AND MONTH(EFFECTIVEDATETIME)={}  AND YEAR(EFFECTIVEDATETIME)={} AND OBSERVATIONCATEGORYDISPLAY  LIKE 'Physiological action'
                         GROUP BY PATIENTID ,EFFECTIVEDATETIME)
                 GROUP  BY SUBJECTID) pat
                 INNER JOIN
                 (
                   SELECT DISTINCT MED.ihelpid,MED.PATIENTID MEDICAL_ID,hth.PATIENTID HTH_ID 
                    FROM (SELECT IHELPID, PATIENTID ,PROVIDERID
                          FROM IHELP_PERSON 
                          WHERE  PROVIDERID ='{}' 
                        ) AS MED,
                       (SELECT IHELPID, PATIENTID ,PROVIDERID
                        FROM IHELP_PERSON 
                        WHERE  PROVIDERID ='HEALTHENTIA' 
                       ) AS HTH 
                     WHERE med.ihelpid=hth.ihelpid ) pat_id 
             ON pat_id.HTH_ID = pat.SUBJECTID
            )
    	    """.format(pilot, current_month, current_year, current_month, current_year, pilot)
            print(sqlQuestAdh)
            curs = conn.cursor()
            curs.execute(sqlQuestAdh)
            num_rows_affected = curs.rowcount
            print("Number of inserted rows {} for pilot {}".format(num_rows_affected, pilot))

            conn.commit()


if __name__ == '__main__':
    simulate = os.getenv("SIMULATE", False)
    if (simulate == False):
        createTableIfNotExists()
    else:
        print("Simulation is ON! No data database connection is required. No changes will be performed on the DB!")
