// export const environment = {
//   production: true,
//   configFile: 'assets/config/config.prod.json'
// };

export const environment = {
  production :true,
  KEYCLOAK_SERVER: window['env']['KEYCLOAK_SERVER'] || '192.168.1.124:8080'
  // KEYCLOAK_SERVER: 'https://keycloak.ihelp-project.eu/'
};