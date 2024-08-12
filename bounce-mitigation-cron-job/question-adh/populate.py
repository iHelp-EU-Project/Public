import datetime
import jaydebeapi
import os
import sys


def getPilotQuota(pilot: str) -> int:
    curretMonth = datetime.datetime.now().month - 1
    if pilot == 'FPG':
        return 8
    elif pilot == 'HDM':
        return 10
    elif pilot == 'MUP':
        return 29
    elif pilot == 'TMU':
        if curretMonth % 2 == 0:
            return 6
        else:
            return 5
    elif pilot == 'UNIMAN':
        return 21


def validateMonthAndYear(kwargs):
    try:
        month = int(kwargs.get("month"))
        year = int(kwargs.get("year"))

        if 1 <= month <= 12:
            if year >= 2022:
                return True
            else:
                print(f"The provided year value {year} is invalid. Year should be greater than 2022 ")

        else:
            print(f"The provided month value {month} is invalid. Month should be between 1 and 12.  ")
    except ValueError:
        print("Month and year should be number values. You've provided string either for month or year")


def createTableIfNotExists():
    ''' test from local Laptop ON the iHelp cloud
    dbURL = os.getenv("DB_URL", "jdbc:leanxcale://147.102.230.182:30001/ihelp")

    dbDriverPath = os.getenv("DB_DRIVER_PATH",
                             "../ext-lib/qe-driver-1.8-RC.9-jar-with-dependencies.jar")
    '''
    dbURL = os.getenv("DB_URL", "jdbc:leanxcale://ihelp-store-service.default:1529/ihelp")
    dbDriverPath = os.getenv("DB_DRIVER_PATH",
                             "/bounce_mitigation/qe-driver-1.8-RC.9-jar-with-dependencies.jar")
    dbUser = os.getenv("DB_USER", "app")
    dbPw = os.getenv("DB_PW", "")
    dbDriverClass = os.getenv("DB_DRIVER_CLASS", "com.leanxcale.client.Driver")

    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
        curs = conn.cursor()
        statement = """
                    CREATE TABLE IF NOT EXISTS QUEST_ADHERENCE_NEW (
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
            populateMissingQuestionnaireAnswers(**dict(arg.split('=') for arg in sys.argv[1:]))

        conn.commit()
        curs.close()


def populateMissingQuestionnaireAnswers(**kwargs):
    # 13.11.2023 the pilot should be read from the environment variable PILOT, e.g. PILOT="MUP,HDM" or PILOT="FPG"
    if 'PILOT' in os.environ:
        print("Import data for provided pilots=" + os.getenv("PILOT"))
    else:
        print("Please provide the PILOT environment variables that should in the format 'MUP,HDM' or simple 'TMU'")
        sys.exit(1)


    pilots = os.getenv("PILOT").split(",")

    ''' test from local Laptop ON the iHelp cloud
    dbURL = os.getenv("DB_URL", "jdbc:leanxcale://147.102.230.182:30001/ihelp")

    dbDriverPath = os.getenv("DB_DRIVER_PATH",
                             "../ext-lib/qe-driver-1.8-RC.9-jar-with-dependencies.jar")
    '''
    dbURL = os.getenv("DB_URL", "jdbc:leanxcale://ihelp-store-service.default:1529/ihelp")
    dbDriverPath = os.getenv("DB_DRIVER_PATH",
                             "/bounce_mitigation/qe-driver-1.8-RC.9-jar-with-dependencies.jar")
    dbUser = os.getenv("DB_USER", "app")
    dbPw = os.getenv("DB_PW", "")
    dbDriverClass = os.getenv("DB_DRIVER_CLASS", "com.leanxcale.client.Driver")

    print(
        f" The db connection params are url={dbURL}, driverPath={dbDriverPath}, dbUser={dbUser}, dbPw={dbPw},dbDriverClass={dbDriverClass}  ")

    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
        for pilot in pilots:
            for year in range(2022, 2024):
                low_range = 8 if year == 2022 else 1
                high_range = 13 if year == 2022 else 10
                for month in range(low_range, high_range):
                    totalQA = getPilotQuota(pilot=pilot)
                    sqlQuestAdh = """
            INSERT INTO QUEST_ADHERENCE_NEW (sid, pilot, perc, mon, yr)
             (SELECT sid, '{}' AS pilot, perc, {} AS mon, {} AS yr
             FROM 
            (SELECT ihelpid sid, (COUNT(*) / CAST(MAX({}) AS FLOAT)) * 100.00 perc
             FROM
              (
                SELECT DISTINCT PATIENTID subjectid , QUESTIONNAIREID qa, AUTHORED  sd
                FROM ANSWER
                WHERE MONTH(AUTHORED) = {} AND YEAR(AUTHORED) = {}   
 
              ) pat
              INNER JOIN
              (
                SELECT DISTINCT MED.ihelpid,MED.PATIENTID MEDICAL_ID,hth.PATIENTID HTH_ID 
                FROM
                  (SELECT IHELPID, PATIENTID ,PROVIDERID
                   FROM IHELP_PERSON 
                   WHERE  PROVIDERID ='{}' 
                ) AS MED,
                (SELECT IHELPID, PATIENTID ,PROVIDERID
                 FROM IHELP_PERSON 
                 WHERE  PROVIDERID ='HEALTHENTIA' 
                 ) AS HTH 
                WHERE med.ihelpid=hth.ihelpid   
              ) pat_id ON pat_id.HTH_ID = pat.SUBJECTID
              GROUP BY pat_id.HTH_ID, ihelpid,MEDICAL_ID)
    	    	) """.format(pilot, month, year, totalQA, month, year, pilot)
                    curs = conn.cursor()
                    curs.execute(sqlQuestAdh)
                    num_rows_affected = curs.rowcount
                    print(
                        "Number of inserted rows:{} for pilot {} year {} and month {}".format(num_rows_affected, pilot,
                                                                                              year,
                                                                                              month))

                    conn.commit()


if __name__ == '__main__':
    simulate = os.getenv("SIMULATE", False)
    if (simulate == False):
        createTableIfNotExists()
    else:
        print("Simulation is ON! No data database connection is required. No changes will be performed on the DB!")
