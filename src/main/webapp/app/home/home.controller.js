(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = [
        '$scope',
        'Principal',
        'LoginService',
        '$state',
        'NgMap',
        'NavigatorGeolocation',
        'Hemocentro',
        'Endereco',
        'ParseLinks',
        'AlertService',
        'SaudeService'
      ];

    function HomeController ($scope, Principal, LoginService, $state, NgMap, NavigatorGeolocation, Hemocentro,
      Endereco, ParseLinks, AlertService, SaudeService)  {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.callbackFunc = callbackFunc;
        vm.pontos = [];
        vm.predicate = 'id';
        vm.reverse = true;
        vm.show = show;

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


        function show (e, hemocentro) {
          console.log(hemocentro);

        }

        function callbackFunc(){
          NavigatorGeolocation.getCurrentPosition()
         .then(function(position) {
           vm.lat = position.coords.latitude, vm.lng = position.coords.longitude;

           SaudeService.getSaudeMyLocation(vm.lat,vm.lng, 100, 500).then(function(data){
               vm.saude = data;
           });
         });


                // console.log(center);

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
