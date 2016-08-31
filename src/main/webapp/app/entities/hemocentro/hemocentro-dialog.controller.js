(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('HemocentroDialogController', HemocentroDialogController);

    HemocentroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hemocentro', 'Funcionamento'];

    function HemocentroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hemocentro, Funcionamento) {
        var vm = this;

        vm.hemocentro = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.funcionamentos = Funcionamento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hemocentro.id !== null) {
                Hemocentro.update(vm.hemocentro, onSaveSuccess, onSaveError);
            } else {
                Hemocentro.save(vm.hemocentro, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('timeLocationApp:hemocentroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
