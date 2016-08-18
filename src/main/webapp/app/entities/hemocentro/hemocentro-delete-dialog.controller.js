(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('HemocentroDeleteController',HemocentroDeleteController);

    HemocentroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hemocentro'];

    function HemocentroDeleteController($uibModalInstance, entity, Hemocentro) {
        var vm = this;

        vm.hemocentro = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hemocentro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
