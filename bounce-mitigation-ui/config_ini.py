import os
import streamlit as st
from datetime import datetime

current_year = datetime.now().year
current_month = datetime.now().month

number_month = {
    1: "JAN",
    2: "FEB",
    3: "MAR",
    4: "APR",
    5: "MAY",
    6: "JUN",
    7: "JUL",
    8: "AUG",
    9: "SEP",
    10: "OCT",
    11: "NOV",
    12: "DEC"
}

mt = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
]

month_number = {
    "January": 1,
    "February": 2,
    "March": 3,
    "April": 4,
    "May": 5,
    "June": 6,
    "July": 7,
    "August": 8,
    "September": 9,
    "October": 10,
    "November": 11,
    "December": 12
}


def get_markdown(vis_year: str, vis_month: str, vis_scale: str, vis_patient: str, url: str = '') -> str:
    year_index = 0 if vis_year == 'visible' else -1
    year_opacity = 1 if vis_year == 'visible' else 0.5

    month_index = 0 if vis_month == 'visible' else -1
    month_opacity = 1 if vis_month == 'visible' else 0.5

    scale_index = 0 if vis_scale == 'visible' else -1
    scale_opacity = 1 if vis_scale == 'visible' else 0.5

    patient_index = 0 if vis_patient == 'visible' else -1
    patient_opacity = 1 if vis_patient == 'visible' else 0.5

    style = """
    <style>
    #MainMenu {{
    visibility: hidden;
    }}
    footer {{
    visibility: hidden;
    }}
    div[data-testid="stStatusWidget"] {{
    visibility: hidden;
    height: 0px;
    position: fixed;
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(1) > div:nth-child(1) > div:nth-child(2) {{
    z-index: {};
    opacity: {};
    }}
    section.st-emotion-cache-1o14730 eczjsme11 {{
    z-index: 9999999999999999999;
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(2) > div:nth-child(1) > div:nth-child(2) {{
    z-index: {};
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(3) > div:nth-child(1) > div:nth-child(2) {{
    z-index: {};
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(5) > div:nth-child(1) > div:nth-child(2) {{
    z-index: {};
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(1) > div:nth-child(1) > label:nth-child(1) > div:nth-child(1) >
    p:nth-child(1){{
    font-size: 18px;
    font-weight: bold;
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(2) > div:nth-child(1) > label:nth-child(1) > div:nth-child(1) >
    p:nth-child(1) {{
    font-size: 18px;
    font-weight: bold;
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(3) > div:nth-child(1) > label:nth-child(1) > div:nth-child(1) > 
    p:nth-child(1) {{
    font-size: 18px;
    font-weight: bold;
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(5) > div:nth-child(1) > label:nth-child(1) > div:nth-child(1) > 
    p:nth-child(1) {{
    font-size: 18px;
    font-weight: bold;
    opacity: {};
    }}
    div.st-emotion-cache-1p1nwyz:nth-child(4) > div:nth-child(1) > div:nth-child(1) {{
    opacity: {};
    }}
    div.st-emotion-cache-j6dhcq:nth-child(4) > div:nth-child(1) > label:nth-child(1) > div:nth-child(1) > p:nth-child(1)
    {{
       font-size: 12px;
       font-weight: bold;
    }}
    div.element-container:nth-child(4) > div:nth-child(1) > div:nth-child(1) {{
        font-size: 2012pxpx;
        font-weight: bold;
    }}
    div.element-container:nth-child(6) > div:nth-child(1) > div:nth-child(1) {{
        font-size: 12px;
        font-weight: bold;
    }}
    div.element-container:nth-child(8) > div:nth-child(1) > div:nth-child(1) {{
        font-size: 12px;
        font-weight: bold;
    }}
    #middle > div > span {{
    margin-left: 50%;
    transform: translate(-20%);
    }}
    #welcome-to-bounce-mitigation-of-the-ihelp-clinical-trial > div {{
    margin-left: 50%;
    overflow-x: hidden;
    transform: translate(-35%);
    font-size: 45px;
    }}
    div[data-testid='caption'] {{
    font-size: 20px;
    }}
    </style>
    {}
    """.format(year_index, year_opacity, month_index, month_opacity, scale_index, scale_opacity, patient_index,
               patient_opacity, year_opacity, month_opacity, scale_opacity, patient_opacity, scale_opacity, url)

    return style


def get_connection():
    db_url = os.getenv("DB_URL", "jdbc:leanxcale:///147.102.230.182:30001/ihelp")
    db_user = os.getenv("DB_USER", "app")
    db_pw = os.getenv("DB_PW", "")
    db_driver_class = os.getenv("DB_DRIVER_CLASS", "com.leanxcale.client.Driver")
    db_driver_path = os.getenv(
        "DB_DRIVER_PATH", './driver/qe-driver-1.7.10-jar-with-dependencies.jar')

    return db_url, db_user, db_pw, db_driver_class, db_driver_path


def get_select_boxes():
    year = st.selectbox(
        'Select the Year (Works for all graphs)', [i for i in range(2022, current_year + 1)])

    months = mt[:current_month - 1] if current_year == year else mt

    month = st.selectbox('Select the Month', months)

    scale = st.selectbox(
        'Select the Maximum Percentage', ('100%', '90%', '80%', '70%', '60%', '50%', '40%', '30%', '20%', '10%'))

    scale_label = st.text('(Scale default value 100%)')

    return year, month, scale, scale_label
