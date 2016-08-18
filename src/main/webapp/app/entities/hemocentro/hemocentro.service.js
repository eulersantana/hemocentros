(function() {
    'use strict';
    angular
        .module('timeLocationApp')
        .factory('Hemocentro', Hemocentro);

    Hemocentro.$inject = ['$resource', 'DateUtils'];

    function Hemocentro ($resource, DateUtils) {
        var resourceUrl =  'api/hemocentros/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertLocalDateFromServer(data.createdAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createdAt = DateUtils.convertLocalDateToServer(data.createdAt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createdAt = DateUtils.convertLocalDateToServer(data.createdAt);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
