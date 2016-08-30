(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'NgMap', 'Hemocentro', 'Endereco', 'ParseLinks', 'AlertService'];

    function HomeController ($scope, Principal, LoginService, $state, NgMap, Hemocentro, Endereco, ParseLinks, AlertService )  {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.callbackFunc = callbackFunc;
        vm.pontos = [];
        vm.predicate = 'id';
        vm.reverse = true;
        vm.getHemocnetro = getHemocnetro;
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

        function getHemocnetro (e, hemocentro) {
          console.log(hemocentro);
        }

        function callbackFunc(param){
          // vm.pontos.push(vm.map.getCenter());
          // Endereco.query(function(res){
          //   res.$promise.then(function(data){
          //     data.forEach(function(pos){
          //       if (pos.hemocentro.movel) {
          //         vm.pontos.push({id:pos.id, pos:[pos.longitude,pos.latitude],img:'content/images/icons/bus.svg'});
          //       }else{
          //         vm.pontos.push({id:pos.id, pos:[pos.longitude,pos.latitude],img:'content/images/icons/fixo.svg'});
          //       }
          //     });
          //   });
          // });

          Endereco.query({
              page: 0,
              size: 20
          }, onSuccess, onError);

          function sort() {
              var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
              if (vm.predicate !== 'id') {
                  result.push('id');
              }
              return result;
          }

          function onSuccess(data, headers) {
              vm.links = ParseLinks.parse(headers('link'));
              vm.totalItems = headers('X-Total-Count');
              for (var i = 0; i < data.length; i++) {
                  vm.pontos.push(data[i]);
              }
          }

          function onError(error) {
              AlertService.error(error.data.message);
          }
        }
    }
})();
