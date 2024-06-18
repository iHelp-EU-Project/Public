// export const environment = {
//   production: false,
//   configFile: 'assets/config/config.dev.json'
// };

export const environment = {
  production :false,
  KEYCLOAK_SERVER: window['env']['KEYCLOAK_SERVER'] || '192.168.1.124:8080'
};

// export const environment = {
//   production: false,
//   KEYCLOAK_SERVER: 'https://keycloak.ihelp-project.eu/',
// };