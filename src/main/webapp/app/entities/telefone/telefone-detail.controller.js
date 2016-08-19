(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('TelefoneDetailController', TelefoneDetailController);

    TelefoneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Telefone', 'Hemocentro'];

    function TelefoneDetailController($scope, $rootScope, $stateParams, previousState, entity, Telefone, Hemocentro) {
        var vm = this;

        vm.telefone = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timeLocationApp:telefoneUpdate', function(event, result) {
            vm.telefone = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
