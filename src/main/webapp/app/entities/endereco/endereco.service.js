(function() {
    'use strict';
    angular
        .module('timeLocationApp')
        .factory('Endereco', Endereco);

    Endereco.$inject = ['$resource', 'DateUtils'];

    function Endereco ($resource, DateUtils) {
        var resourceUrl =  'api/enderecos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
