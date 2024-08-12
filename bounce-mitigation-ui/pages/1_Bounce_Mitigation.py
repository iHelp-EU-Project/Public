import json

import streamlit as st
import requests
import jaydebeapi
import pandas as pd
import altair as alt
from datetime import datetime
from config_ini import number_month, month_number, mt, get_markdown, get_connection
import os

dbURL, dbUser, dbPw, dbDriverClass, dbDriverPath = get_connection()
current_year = datetime.now().year
current_month = datetime.now().month
dns_keycloak = os.getenv("KEYCLOAK_IDP",
                         "https://keycloak.ihelp-project.eu/realms/ihelp/protocol/openid-connect/userinfo")




#@st.cache_data
def get_patients(pilot_name):
    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as connection:
        cursor = connection.cursor()

        patients_query = (f"""SELECT DISTINCT MED.ihelpid,hth.PATIENTID HTH_ID 
FROM
(SELECT IHELPID, PATIENTID ,PROVIDERID
FROM IHELP_PERSON 
WHERE  PROVIDERID ='{pilot_name}' 
) AS MED,
(SELECT IHELPID, PATIENTID ,PROVIDERID
FROM IHELP_PERSON 
WHERE  PROVIDERID ='HEALTHENTIA' 
) AS HTH 
WHERE med.ihelpid=hth.ihelpid AND hth.PATIENTID 
IN 
(SELECT DISTINCT PATIENTID 
FROM ANSWER 
WHERE ProviderID='{pilot_name}'
UNION 
SELECT DISTINCT PATIENTID
FROM OBSERVATION 
)
""")

        cursor.execute(patients_query)
        statement_result = cursor.fetchall()
        if statement_result:
            pt = [i[0] for i in statement_result if i[0] not in ["KBUTOI", "FO0CMG"]]
            return pt
        return []


range_ = ['gray', 'red', 'orange', 'green']
st.set_page_config(page_title='Bounce Mitigation',
                   layout='wide', menu_items={}, initial_sidebar_state='expanded')

st.markdown(get_markdown(*('visible ' * 4).split(' ')[:-1]), unsafe_allow_html=True)

params = st.experimental_get_query_params()

if params.get("token") is not None and params.get("pilot") is not None:
    token = params["token"][0]
    pilot = params["pilot"][0]

    headers = {
        "Authorization": f"Bearer {token}"
    }

    response = requests.get(dns_keycloak,
                            headers=headers)

    if response.status_code == 200:  # The same as in Start Page
        user_info = json.loads(response.content.decode('utf-8'))
        if 'ihelp Health Care Professional' in user_info["roles"]:
            year = st.sidebar.selectbox(
                'Year *', [i for i in range(2022, current_year + 1)])

            month = st.sidebar.selectbox('Month *', mt[:current_month] if current_year == year else mt)

            scale = st.sidebar.selectbox(
                'Maximum Percentage Response', ('100%', '90%', '80%', '70%', '60%', '50%', '40%', '30%', '20%', '10%'))

            scale_label = st.sidebar.text('(Scale default value 100%)')

            patient_id = st.sidebar.selectbox('Patient ID', get_patients(pilot))

            st.header('Bounce Mitigation', anchor='middle')

            tabs = st.selectbox('Select visualization Type', ('Questionnaire', 'Device', 'Patient', 'Risk Assessment'))


            def questionnaire_tab():
                st.text('To view the questionnaire graph please select the Year and Month')
                st.markdown(get_markdown(*('visible ' * 3).split(' ')[:-1], 'hidden'), unsafe_allow_html=True)
                with st.spinner('Fetching Data...'):
                    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
                        curs = conn.cursor()
                        questionnaire_query = (
                            "SELECT SID, PERC FROM QUEST_ADHERENCE_NEW WHERE mon={} AND YR={} AND PILOT='{}'").format(
                            month_number[month], year, pilot)
                        curs.execute(questionnaire_query)
                        result = curs.fetchall()
                        curs.close()
                        if result:
                            dff = pd.DataFrame(
                                result, columns=['Patient ID', 'Percentage'])
                            dff = dff[dff['Percentage'] <= float(
                                scale.split('%')[0])].drop_duplicates()
                            bar_chart = alt.Chart(dff).mark_bar(size=22).encode(
                                x=alt.X('Patient ID', title='Patient Identifier'),
                                y=alt.Y('Percentage', title='Percentage (%)'),
                                color=alt.Color("Percentage", title='Percentage (%)', scale=alt.Scale(
                                    domain=[0, 50, 100], range=range_))
                            ).properties(
                                title=f'Response to questionnaires per patient in {month} {year}',
                                height=500,
                            )
                            text = bar_chart.mark_text(align="center", baseline="bottom", fontSize=20).encode(
                                text=alt.Text("Percentage", format=",.0f"))
                            final_chart = alt.layer(bar_chart, text).configure_title(
                                fontSize=20,
                                anchor="middle"
                            ).configure_axisX(
                                labelAngle=90,
                                labelFontSize=20,
                                titleFontSize=20
                            ).configure_axisY(
                                labelFontSize=20,
                                titleFontSize=20
                            ).configure_legend(
                                labelFontSize=20,
                                titleFontSize=20
                            )
                            st.header('Questionnaires Graph')
                            st.altair_chart(final_chart, theme="streamlit",
                                            use_container_width=True)
                        else:
                            st.header('Data unavailable for {} {}'.format(month, year))


            def device_tab():
                st.text('To view the device graph please select the Year and Month')
                st.markdown(get_markdown(*('visible ' * 3).split(' ')[:-1], 'hidden'), unsafe_allow_html=True)
                with st.spinner('Fetching Data...'):
                    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
                        curs = conn.cursor()
                        device_query = "SELECT SID, PERC FROM DEVICE_ADHERENCE_NEW WHERE mon={} AND YR={} AND PILOT='{}'".format(
                            month_number[month], year, pilot)
                        curs.execute(device_query)
                        result = curs.fetchall()
                        curs.close()
                        if result:
                            dff = pd.DataFrame(
                                result, columns=['Patient ID', 'Percentage'])
                            dff = dff[dff['Percentage'] <= float(
                                scale.split('%')[0])].drop_duplicates()
                            bar_chart = alt.Chart(dff).mark_bar(size=22).encode(
                                x=alt.X('Patient ID', title='Patient Identifier'),
                                y=alt.Y('Percentage', title='Percentage (%)'),
                                color=alt.Color("Percentage", scale=alt.Scale(
                                    domain=[0, 50, 100], range=range_))
                            ).properties(
                                title=f'Data from devices transmitted per patient in {month} {year}',
                                height=500,
                            )
                            text = bar_chart.mark_text(align="center", baseline="bottom", fontSize=20).encode(
                                text=alt.Text("Percentage", format=",.0f"))
                            final_chart = alt.layer(bar_chart, text).configure_title(
                                fontSize=24,
                                anchor="middle"
                            ).configure_axisX(
                                labelAngle=90,
                                labelFontSize=20,
                                titleFontSize=20
                            ).configure_axisY(
                                labelFontSize=20,
                                titleFontSize=20
                            ).configure_legend(
                                labelFontSize=20,
                                titleFontSize=20
                            )
                            st.header('Devices Graph')
                            st.altair_chart(final_chart, theme="streamlit",
                                            use_container_width=True)
                        else:
                            st.header('Data unavailable for {} {}'.format(month, year))


            def patient_tab():
                months = [mnt for mnt in range(1, 13)]
                st.text('To view the patient graph please select the Year and Month and Patient ID')
                st.markdown(get_markdown('visible', 'hidden', 'hidden', 'visible'), unsafe_allow_html=True)
                with st.spinner('Fetching Data...'):
                    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
                        curs = conn.cursor()
                        device_query = (
                            "SELECT MON, PERC FROM APP.DEVICE_ADHERENCE_NEW WHERE SID='{}' AND YR={}").format(
                            patient_id, year)
                        if patient_id:
                            curs.execute(device_query)
                        result = curs.fetchall()
                        if result:
                            available_months = [res[0] for res in result]
                            for mnt in months:
                                if mnt not in available_months:
                                    result.append((mnt, 0.0))
                            result = sorted(result, key=lambda x: x[0])
                            dff = pd.DataFrame(
                                result, columns=['Months', 'Percentage']).drop_duplicates()
                            dff['Months'] = sorted(
                                [mt[i - 1] for i in dff['Months']], key=lambda x: month_number[x])
                            bar_chart = alt.Chart(dff).mark_line(size=22, point=True, strokeWidth=5).encode(
                                x=alt.X('Months', sort=dff['Months'].to_list()),
                                y=alt.Y('Percentage', title='Percentage (%)'),
                            ).properties(
                                title='Evolution of data transmitted from devices for patient with ID {} in {}'.format(
                                    patient_id, year),
                                height=500,
                            )
                            text = bar_chart.mark_text(align="center", baseline="bottom", fontSize=20).encode(
                                text=alt.Text("Percentage", format=",.0f"))
                            final_chart = alt.layer(bar_chart, text).configure_title(
                                fontSize=24,
                                anchor="middle"
                            ).configure_axisX(
                                labelAngle=90,
                                labelFontSize=20,
                                titleFontSize=20
                            ).configure_axisY(
                                labelFontSize=20,
                                titleFontSize=20
                            ).configure_point(
                                size=150
                            ).configure_legend(
                                labelFontSize=20,
                                titleFontSize=20
                            )
                            st.header(f'Patient {patient_id}')
                            st.header('Devices Graph')
                            st.altair_chart(final_chart, theme="streamlit",
                                            use_container_width=True)

                            questionnaire_query = (
                                "SELECT MON, PERC FROM QUEST_ADHERENCE_NEW WHERE SID='{}' AND YR={}").format(
                                patient_id, year)
                            if patient_id:
                                curs.execute(questionnaire_query)
                            result = curs.fetchall()
                            curs.close()
                            if result:
                                available_months = [res[0] for res in result]
                                for mnt in months:
                                    if mnt not in available_months:
                                        result.append((mnt, 0.0))
                                result = sorted(result, key=lambda x: x[0])
                                dff = pd.DataFrame(
                                    result, columns=['Months', 'Percentage'])
                                dff['Months'] = sorted(
                                    [mt[i - 1] for i in dff['Months']], key=lambda x: month_number[x])
                                bar_chart = alt.Chart(dff).mark_line(size=22, point=True, strokeWidth=5).encode(
                                    x=alt.X('Months', sort=dff['Months'].to_list()),
                                    y=alt.Y('Percentage', title='Percentage (%)'),
                                ).properties(
                                    title='Evolution of responses to the questionnaires for patient with ID {} in {}'.format(
                                        patient_id, year),
                                    height=500
                                )
                                text = bar_chart.mark_text(align="center", baseline="bottom", fontSize=20).encode(
                                    text=alt.Text("Percentage", format=",.0f"))
                                final_chart = alt.layer(bar_chart, text).configure_title(
                                    fontSize=24,
                                    anchor="middle"
                                ).configure_axisX(
                                    labelAngle=90,
                                    labelFontSize=20,
                                    titleFontSize=20
                                ).configure_axisY(
                                    labelFontSize=20,
                                    titleFontSize=20
                                ).configure_point(
                                    size=150
                                ).configure_legend(
                                    labelFontSize=20,
                                    titleFontSize=20
                                )
                                st.header('Questionnaires Graph')
                                st.altair_chart(final_chart, theme="streamlit",
                                                use_container_width=True)
                        else:
                            st.header('Data unavailable for patient {} in {}'.format(patient_id, year))


            def risk_tab():
                st.text('To view the risk assessment graph please select the Year')
                st.markdown(get_markdown('visible', 'hidden', 'hidden', 'hidden'), unsafe_allow_html=True)
                patients = get_patients(pilot)
                if year == datetime.now().year:
                    last_month = datetime.now().month
                else:
                    last_month = 13
                with st.spinner('Fetching Data...'):
                    with jaydebeapi.connect(dbDriverClass, dbURL, [dbUser, dbPw], dbDriverPath) as conn:
                        curs = conn.cursor()
                        device_query = """SELECT SID, PERC, MON FROM APP.DEVICE_ADHERENCE_NEW WHERE YR = {} AND PILOT = '{}' 
                            """.format(year, pilot)
                        curs.execute(device_query)
                        result = curs.fetchall()
                        if result:
                            for patient in patients:
                                found_month = []
                                for res in result:
                                    if patient in res:
                                        found_month.append(res[2])

                                if len(found_month) > 0:
                                    for mnt in range(1, last_month):
                                        if mnt in found_month:
                                            continue
                                        else:
                                            result.append((patient, -1.0, mnt))
                                else:
                                    for mnt in range(1, last_month):
                                        result.append((patient, -1.0, mnt))

                        aux = []
                        for patient in patients:
                            for i, res in enumerate(result):
                                if res[0] == patient:
                                    aux.append(res)

                        result = aux

                        dff_dev = pd.DataFrame(
                            result, columns=['Patient ID', 'Percentage', 'Month'])

                        dff_dev['Month'] = dff_dev['Month'].apply(
                            lambda lmnt: number_month[lmnt])

                        questionnaire_query = """SELECT SID, PERC, MON FROM APP.QUEST_ADHERENCE_NEW WHERE YR = {} AND PILOT = '{}'
                            """.format(year, pilot)
                        curs.execute(questionnaire_query)
                        result = curs.fetchall()
                        curs.close()
                        if result:
                            for patient in patients:
                                found_month = []
                                for res in result:
                                    if patient in res:
                                        found_month.append(res[2])

                                if len(found_month) > 0:
                                    for mnt in range(1, last_month):
                                        if mnt in found_month:
                                            continue
                                        else:
                                            result.append((patient, -1.0, mnt))
                                else:
                                    for mnt in range(1, last_month):
                                        result.append((patient, -1.0, mnt))

                        aux = []
                        for patient in patients:
                            for i, res in enumerate(result):
                                if res[0] == patient:
                                    aux.append(res)

                        result = aux

                        dff_quest = pd.DataFrame(
                            result, columns=['Patient ID', 'Percentage', 'Month'])

                        dff_quest['Month'] = dff_quest['Month'].apply(
                            lambda lmnt: number_month[lmnt])

                        dff = pd.merge(dff_quest, dff_dev, on=[
                            'Patient ID', 'Month'], suffixes=('_dff_quest', '_dff_dev'))

                        dff['Percentage'] = 0.7 * dff['Percentage_dff_quest'] + 0.3 * dff['Percentage_dff_dev']

                        dff.drop(
                            ['Percentage_dff_quest', 'Percentage_dff_dev'], axis=1, inplace=True)

                        dff.sort_values(['Month'], inplace=True)
                        if not dff.empty:
                            heat_map = alt.Chart(dff).mark_rect(size=22, strokeWidth=1.5).encode(
                                x=alt.X('Month:O', title='Month', sort=[
                                    'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG',
                                    'SEP',
                                    'OCT', 'NOV', 'DEC']),
                                y=alt.Y('Patient ID:O', title='Patient Identifier'),
                                color=alt.Color("Percentage:Q", scale=alt.Scale(
                                    domain=[-1, 0, 50, 100], range=range_))
                            ).properties(
                                height=1150
                            )

                            x_labels_chart = alt.Chart(dff).mark_text(dy=-498, size=15, color='#ffffff').encode(
                                x=alt.X('Month:O', sort=[
                                    'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG',
                                    'SEP',
                                    'OCT', 'NOV', 'DEC']),
                                text='Month:N'
                            )

                            final_chart = alt.layer(heat_map, x_labels_chart).configure_axisX(
                                labelAngle=90,
                                labelFontSize=20,
                                titleFontSize=20,
                                titleY=100,
                                tickSize=10
                            ).configure_axisY(
                                labelFontSize=20,
                                titleFontSize=20,
                                tickSize=10,
                            ).configure_legend(
                                labelFontSize=20,
                                titleFontSize=20
                            )

                            st.header(f'Risk Assessment Graph Year {year}')
                            st.altair_chart(final_chart, theme="streamlit",
                                            use_container_width=True)
                        else:
                            st.header('Data unavailable')


            if tabs == 'Questionnaire':
                questionnaire_tab()
            elif tabs == 'Device':
                device_tab()
            elif tabs == 'Patient':
                patient_tab()
            elif tabs == 'Risk Assessment':
                risk_tab()
        else:
            st.header("You don't have the necessary role to view this!")
    else:
        st.header("You have been logged in for a long time and for security reasons your access has been denied.  Please log out and log in again.  Apologies for this inconvenience!")
else:
    st.header("You have been logged in for a long time and for security reasons your access has been denied.  Please log out and log in again. Apologies for this inconvenience!")
