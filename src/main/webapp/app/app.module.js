(function() {
    'use strict';

    angular
        .module('timeLocationApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'uiGmapgoogle-maps'
        ])
        .config(config)
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];
    config.$inject = ['uiGmapGoogleMapApiProvider'];


    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }

    function config(GoogleMapApiProviders) {
      GoogleMapApiProviders.configure({
           china: true
       });
    }
})();
