(function() {
    'use strict';

    angular
        .module('timeLocationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hemocentro', {
            parent: 'entity',
            url: '/hemocentro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'timeLocationApp.hemocentro.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hemocentro/hemocentros.html',
                    controller: 'HemocentroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hemocentro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hemocentro-detail', {
            parent: 'entity',
            url: '/hemocentro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'timeLocationApp.hemocentro.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hemocentro/hemocentro-detail.html',
                    controller: 'HemocentroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hemocentro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hemocentro', function($stateParams, Hemocentro) {
                    return Hemocentro.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hemocentro',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hemocentro-detail.edit', {
            parent: 'hemocentro-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hemocentro/hemocentro-dialog.html',
                    controller: 'HemocentroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hemocentro', function(Hemocentro) {
                            return Hemocentro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hemocentro.new', {
            parent: 'hemocentro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hemocentro/hemocentro-dialog.html',
                    controller: 'HemocentroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                email: null,
                                movel: false,
                                status: false,
                                createdAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hemocentro', null, { reload: true });
                }, function() {
                    $state.go('hemocentro');
                });
            }]
        })
        .state('hemocentro.edit', {
            parent: 'hemocentro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hemocentro/hemocentro-dialog.html',
                    controller: 'HemocentroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hemocentro', function(Hemocentro) {
                            return Hemocentro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hemocentro', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hemocentro.delete', {
            parent: 'hemocentro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hemocentro/hemocentro-delete-dialog.html',
                    controller: 'HemocentroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hemocentro', function(Hemocentro) {
                            return Hemocentro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hemocentro', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
