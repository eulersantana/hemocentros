(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('TelefoneController', TelefoneController);

    TelefoneController.$inject = ['$scope', '$state', 'Telefone', 'TelefoneSearch'];

    function TelefoneController ($scope, $state, Telefone, TelefoneSearch) {
        var vm = this;
        
        vm.telefones = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Telefone.query(function(result) {
                vm.telefones = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TelefoneSearch.query({query: vm.searchQuery}, function(result) {
                vm.telefones = result;
            });
        }    }
})();
