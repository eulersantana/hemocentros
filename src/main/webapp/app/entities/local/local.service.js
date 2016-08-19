(function() {
    'use strict';
    angular
        .module('timeLocationApp')
        .factory('Local', Local);

    Local.$inject = ['$resource'];

    function Local ($resource) {
        var resourceUrl =  'api/locals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
