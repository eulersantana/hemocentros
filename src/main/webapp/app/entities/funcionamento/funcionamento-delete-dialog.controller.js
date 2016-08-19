(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('FuncionamentoDeleteController',FuncionamentoDeleteController);

    FuncionamentoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Funcionamento'];

    function FuncionamentoDeleteController($uibModalInstance, entity, Funcionamento) {
        var vm = this;

        vm.funcionamento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Funcionamento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
