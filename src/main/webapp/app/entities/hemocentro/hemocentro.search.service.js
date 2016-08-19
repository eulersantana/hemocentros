(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .factory('HemocentroSearch', HemocentroSearch);

    HemocentroSearch.$inject = ['$resource'];

    function HemocentroSearch($resource) {
        var resourceUrl =  'api/_search/hemocentros/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
