(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hello', {
            parent: 'entity',
            url: '/hello',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hello.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hello/hellos.html',
                    controller: 'HelloController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hello');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hello-detail', {
            parent: 'entity',
            url: '/hello/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hello.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hello/hello-detail.html',
                    controller: 'HelloDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hello');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hello', function($stateParams, Hello) {
                    return Hello.get({id : $stateParams.id});
                }]
            }
        })
        .state('hello.new', {
            parent: 'hello',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hello/hello-dialog.html',
                    controller: 'HelloDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                title: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hello', null, { reload: true });
                }, function() {
                    $state.go('hello');
                });
            }]
        })
        .state('hello.edit', {
            parent: 'hello',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hello/hello-dialog.html',
                    controller: 'HelloDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hello', function(Hello) {
                            return Hello.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('hello', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hello.delete', {
            parent: 'hello',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hello/hello-delete-dialog.html',
                    controller: 'HelloDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hello', function(Hello) {
                            return Hello.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('hello', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
