import json

import streamlit
import streamlit as st
from config_ini import get_markdown
import requests
import os
import json
dns_keycloak = os.getenv("KEYCLOAK_IDP",
                         "https://keycloak.ihelp-project.eu/realms/ihelp/protocol/openid-connect/userinfo")

params = streamlit.experimental_get_query_params()

if params.get("token") is not None:
    token = params["token"][0]

    headers = {
        "Authorization": f"Bearer {token}"
    }
    response = requests.get(dns_keycloak,
                            headers=headers)
    # For unauthorized access look in the comments.
    if response.status_code == 200: # Put status_code != 200

        user_info = json.loads(response.content.decode('utf-8')) # Replace this with {"roles": ""}
        if 'ihelp Health Care Professional' in user_info['roles']: # Replace in with not in

            st.markdown(get_markdown(*('visible ' * 4).split(' ')[:-1]), unsafe_allow_html=True)

            st.header('Welcome to Bounce Mitigation of the iHelp clinical trial!')

            st.subheader('User Guide:')

            st.subheader('The application contains a select box that is used for picking which type of graph would be displayed for'
                    ' visualization:')

            st.image('./images/select_box.png', caption='Select Box')

            st.subheader('Next on the left sidebar the year and month for the data to be displayed can be selected. '
                         'The maximum percentage option helps to filter the data below the maximum percentage. '
                         'For the patients graph another option will be visible for picking the patient for '
                         'which the data should be displayed:')

            st.image('./images/sidebar.png', caption='Sidebar')

            st.subheader('Visualizing the graph:')

            st.image('./images/questionnaire_graph.png', caption='Questionnaire Graph (Displays the percentages of '
                                                                 'questionnaires responses sent by each user for the '
                                                                 'selected year and month up to the maximum'
                                                                 ' percentage value.)')

            st.image('./images/device_graph.png', caption='Device Graph (Displays the percentage of data sent from '
                                                          'devices by each patient'
                                                          'for the selected year and month up to the maximum '
                                                          'percentage value.)')

            st.image('./images/patient_graph.png', caption='Patient Graph (Displays the percentages of data sent '
                                                           'for Questionnaires '
                                                           'or Devices for a single patient, selected beforehand, '
                                                           'for the selected year and month.)')
            st.image('./images/risk_assessment_graph.png', caption='Risk Assessment Graph (This graph presents the ponderate mean'
                                                                   'between Questionnaires and Devices '
                                                                   '[70% for questionnaires, 30% for devices], throughout a year, '
                                                                   'for all the patients enrolled in the '
                                                                   'program)')
        else:
            st.header("You don't have the necessary role to view this!")
    else:
        st.header("Not authorized!")
else:
    st.header("Not authorized!")
