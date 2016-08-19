(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .factory('TelefoneSearch', TelefoneSearch);

    TelefoneSearch.$inject = ['$resource'];

    function TelefoneSearch($resource) {
        var resourceUrl =  'api/_search/telefones/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
