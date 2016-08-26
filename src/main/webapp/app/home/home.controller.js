(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'NgMap',  'Hemocentro', 'Endereco'];

    function HomeController ($scope, Principal, LoginService, $state, NgMap, Hemocentro,Endereco) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.callbackFunc = callbackFunc;
        vm.pontos = [];

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        NgMap.getMap().then(function(map) {
          vm.map = map;
        });

        getAccount();
        // getHemocnetros();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }


        function register () {
            $state.go('register');
        }

        function getHemocnetros () {

        }

        function callbackFunc(param){
          vm.pontos.push(vm.map.getCenter());
          Endereco.query(function(res){
            res.$promise.then(function(data){
              data.forEach(function(pos){
                vm.pontos.push([pos.longitude,pos.latitude]);
              });
            });
          });
        }
    }
})();
