(function() {
    'use strict';
    angular
        .module('timeLocationApp')
        .factory('Funcionamento', Funcionamento);

    Funcionamento.$inject = ['$resource', 'DateUtils'];

    function Funcionamento ($resource, DateUtils) {
        var resourceUrl =  'api/funcionamentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.hora_inicio = DateUtils.convertDateTimeFromServer(data.hora_inicio);
                        data.hora_fim = DateUtils.convertDateTimeFromServer(data.hora_fim);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
