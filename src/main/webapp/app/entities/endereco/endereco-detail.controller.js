(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('EnderecoDetailController', EnderecoDetailController);

    EnderecoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Endereco', 'Hemocentro'];

    function EnderecoDetailController($scope, $rootScope, $stateParams, previousState, entity, Endereco, Hemocentro) {
        var vm = this;

        vm.endereco = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timeLocationApp:enderecoUpdate', function(event, result) {
            vm.endereco = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
