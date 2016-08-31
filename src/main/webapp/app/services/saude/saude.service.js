(function() {
  'use strict';
  angular
      .module('timeLocationApp')
      .factory('SaudeService', SaudeService);

      SaudeService.$inject = ['$q','$http'];

      function SaudeService($q, $http) {
        var baseUrl = 'http://mobile-aceite.tcu.gov.br/mapa-da-saude/';

        var dataPromise;

        var service = {
            getSaudeMyLocation : getSaudeMyLocation,
            getSaudeClinicas : getSaudeClinicas
        };

        return service;

        function getSaudeMyLocation(lat, long, raio, quantidade) {
            var search = '/rest/estabelecimentos/latitude/'+lat+'/longitude/'+long+'/raio/'+raio;
            var config = {
              'pagina':3,
              'quantidade':quantidade
            };

            if (angular.isUndefined(dataPromise)) {
                dataPromise = $http.get(baseUrl+search, config).then(function(result) {
                  // console.log(result.data);
                    return result.data;
                });
            }
            return dataPromise;
        }
        function getSaudeClinicas(quantidade) {
            var search = '/rest/estabelecimentos';

            if (angular.isUndefined(dataPromise)) {
              var config = {
                'quantidade': quantidade
              };

              dataPromise = $http.get(baseUrl+search,config).then(function(result){
                return result.data;
              });
            }

            return dataPromise;
        }
      }

})();
