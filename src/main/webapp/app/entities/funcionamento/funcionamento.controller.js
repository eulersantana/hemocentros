(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('FuncionamentoController', FuncionamentoController);

    FuncionamentoController.$inject = ['$scope', '$state', 'Funcionamento', 'FuncionamentoSearch'];

    function FuncionamentoController ($scope, $state, Funcionamento, FuncionamentoSearch) {
        var vm = this;
        
        vm.funcionamentos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Funcionamento.query(function(result) {
                vm.funcionamentos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FuncionamentoSearch.query({query: vm.searchQuery}, function(result) {
                vm.funcionamentos = result;
            });
        }    }
})();
