(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('EnderecoDialogController', EnderecoDialogController);

    EnderecoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Endereco', 'Hemocentro'];

    function EnderecoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Endereco, Hemocentro) {
        var vm = this;

        vm.endereco = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.hemocentros = Hemocentro.query({filter: 'endereco-is-null'});
        $q.all([vm.endereco.$promise, vm.hemocentros.$promise]).then(function() {
            if (!vm.endereco.hemocentro || !vm.endereco.hemocentro.id) {
                return $q.reject();
            }
            return Hemocentro.get({id : vm.endereco.hemocentro.id}).$promise;
        }).then(function(hemocentro) {
            vm.hemocentros.push(hemocentro);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.endereco.id !== null) {
                Endereco.update(vm.endereco, onSaveSuccess, onSaveError);
            } else {
                Endereco.save(vm.endereco, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('timeLocationApp:enderecoUpdate', result);
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
