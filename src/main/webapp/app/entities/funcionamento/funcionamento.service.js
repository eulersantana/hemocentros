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
                        data.data_inicio = DateUtils.convertDateTimeFromServer(data.data_inicio);
                        data.data_fim = DateUtils.convertDateTimeFromServer(data.data_fim);
                        data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
