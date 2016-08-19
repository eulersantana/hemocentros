(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .factory('FuncionamentoSearch', FuncionamentoSearch);

    FuncionamentoSearch.$inject = ['$resource'];

    function FuncionamentoSearch($resource) {
        var resourceUrl =  'api/_search/funcionamentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
