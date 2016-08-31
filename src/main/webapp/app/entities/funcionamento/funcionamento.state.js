(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('funcionamento', {
            parent: 'entity',
            url: '/funcionamento',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'timeLocationApp.funcionamento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/funcionamento/funcionamentos.html',
                    controller: 'FuncionamentoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('funcionamento');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('funcionamento-detail', {
            parent: 'entity',
            url: '/funcionamento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'timeLocationApp.funcionamento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/funcionamento/funcionamento-detail.html',
                    controller: 'FuncionamentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('funcionamento');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Funcionamento', function($stateParams, Funcionamento) {
                    return Funcionamento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'funcionamento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('funcionamento-detail.edit', {
            parent: 'funcionamento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funcionamento/funcionamento-dialog.html',
                    controller: 'FuncionamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Funcionamento', function(Funcionamento) {
                            return Funcionamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('funcionamento.new', {
            parent: 'funcionamento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funcionamento/funcionamento-dialog.html',
                    controller: 'FuncionamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dia: null,
                                hora_inicio: null,
                                hora_fim: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('funcionamento', null, { reload: true });
                }, function() {
                    $state.go('funcionamento');
                });
            }]
        })
        .state('funcionamento.edit', {
            parent: 'funcionamento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funcionamento/funcionamento-dialog.html',
                    controller: 'FuncionamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Funcionamento', function(Funcionamento) {
                            return Funcionamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('funcionamento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('funcionamento.delete', {
            parent: 'funcionamento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funcionamento/funcionamento-delete-dialog.html',
                    controller: 'FuncionamentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Funcionamento', function(Funcionamento) {
                            return Funcionamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('funcionamento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
