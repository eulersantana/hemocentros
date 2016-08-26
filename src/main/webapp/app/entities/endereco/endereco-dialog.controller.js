(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .controller('EnderecoDialogController', EnderecoDialogController);

    EnderecoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Endereco', 'Hemocentro', 'NgMap', 'GeoCoder'];

    function EnderecoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Endereco, Hemocentro, NgMap, GeoCoder) {
        var vm = this;

        vm.endereco = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.getLatitudeLongitude = getLatitudeLongitude;
        vm.hemocentros = Hemocentro.query({filter: 'endereco-is-null'});
        $q.all([vm.endereco.$promise, vm.hemocentros.$promise]).then(function() {
            if (!vm.endereco.hemocentro || !vm.endereco.hemocentro.id) {
                return $q.reject();
            }
            return Hemocentro.get({id : vm.endereco.hemocentro.id}).$promise;
        }).then(function(hemocentro) {
            vm.hemocentros.push(hemocentro);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        NgMap.getMap().then(function(map) {
          vm.map = map;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.endereco.id !== null) {
                Endereco.update(vm.endereco, onSaveSuccess, onSaveError);
            } else {
                Endereco.save(vm.endereco, onSaveSuccess, onSaveError);
            }
        }

        function getLatitudeLongitude () {
          var address = {address: vm.endereco.rua};
          GeoCoder.geocode(address).then(
            function(results) {
              console.log(results);
              vm.endereco.latitude  = results[0].geometry.location.lng();
              vm.endereco.longitude = results[0].geometry.location.lat();
              vm.poss = '['+vm.endereco.longitude+','+vm.endereco.latitude+']';

              console.log(vm.poss);
            },
            function(error) {
            console.log(error);
            }
          );
        }

        function onSaveSuccess (result) {
            $scope.$emit('timeLocationApp:enderecoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
