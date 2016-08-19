(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('LocalDeleteController',LocalDeleteController);

    LocalDeleteController.$inject = ['$uibModalInstance', 'entity', 'Local'];

    function LocalDeleteController($uibModalInstance, entity, Local) {
        var vm = this;

        vm.local = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Local.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
