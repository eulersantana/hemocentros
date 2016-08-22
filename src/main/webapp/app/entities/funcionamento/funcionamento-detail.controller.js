(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('FuncionamentoDetailController', FuncionamentoDetailController);

    FuncionamentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Funcionamento', 'Hemocentro'];

    function FuncionamentoDetailController($scope, $rootScope, $stateParams, previousState, entity, Funcionamento, Hemocentro) {
        var vm = this;

        vm.funcionamento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timeLocationApp:funcionamentoUpdate', function(event, result) {
            vm.funcionamento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
