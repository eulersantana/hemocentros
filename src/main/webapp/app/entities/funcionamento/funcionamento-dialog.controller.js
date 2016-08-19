(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('FuncionamentoDialogController', FuncionamentoDialogController);

    FuncionamentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Funcionamento'];

    function FuncionamentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Funcionamento) {
        var vm = this;

        vm.funcionamento = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.funcionamento.id !== null) {
                Funcionamento.update(vm.funcionamento, onSaveSuccess, onSaveError);
            } else {
                Funcionamento.save(vm.funcionamento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('timeLocationApp:funcionamentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.data_inicio = false;
        vm.datePickerOpenStatus.data_fim = false;
        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
