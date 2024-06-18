(function (window) {
    window['env'] = window['env'] || {};
    // Environment variables
    window['env']['KEYCLOAK_SERVER'] = '${KEYCLOAK_SERVER}';
})(this);