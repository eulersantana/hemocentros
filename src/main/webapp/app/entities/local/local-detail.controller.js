(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('LocalDetailController', LocalDetailController);

    LocalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Local'];

    function LocalDetailController($scope, $rootScope, $stateParams, previousState, entity, Local) {
        var vm = this;

        vm.local = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timeLocationApp:localUpdate', function(event, result) {
            vm.local = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
