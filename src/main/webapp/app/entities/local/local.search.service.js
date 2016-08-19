(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .factory('LocalSearch', LocalSearch);

    LocalSearch.$inject = ['$resource'];

    function LocalSearch($resource) {
        var resourceUrl =  'api/_search/locals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
