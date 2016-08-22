(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('FuncionamentoDialogController', FuncionamentoDialogController);

    FuncionamentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Funcionamento', 'Hemocentro'];

    function FuncionamentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Funcionamento, Hemocentro) {
        var vm = this;

        vm.funcionamento = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.hemocentros = Hemocentro.query();

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

        vm.datePickerOpenStatus.hora_inicio = false;
        vm.datePickerOpenStatus.hora_fim = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
