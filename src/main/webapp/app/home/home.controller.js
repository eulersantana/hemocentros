(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'NgMap'];

    function HomeController ($scope, Principal, LoginService, $state, NgMap) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.callbackFunc = callbackFunc;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        NgMap.getMap().then(function(map) {
          vm.map = map;
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function callbackFunc(param){
          console.log('You are at '+vm.map.getCenter());
          vm.positionMy = vm.map.getCenter();
        }
    }
})();
