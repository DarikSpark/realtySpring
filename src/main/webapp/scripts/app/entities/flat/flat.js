'use strict';

angular.module('realtyCRMmysqlApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('flat', {
                parent: 'entity',
                url: '/flats',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'realtyCRMmysqlApp.flat.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/flat/flats.html',
                        controller: 'FlatController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('flat');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('flat.detail', {
                parent: 'entity',
                url: '/flat/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'realtyCRMmysqlApp.flat.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/flat/flat-detail.html',
                        controller: 'FlatDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('flat');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Flat', function($stateParams, Flat) {
                        return Flat.get({id : $stateParams.id});
                    }]
                }
            })
            .state('flat.new', {
                parent: 'flat',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/flat/flat-dialog.html',
                        controller: 'FlatDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    address: null,
                                    description: null,
                                    day_cost: null,
                                    people_count: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('flat', null, { reload: true });
                    }, function() {
                        $state.go('flat');
                    })
                }]
            })
            .state('flat.edit', {
                parent: 'flat',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/flat/flat-dialog.html',
                        controller: 'FlatDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Flat', function(Flat) {
                                return Flat.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('flat', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('flat.delete', {
                parent: 'flat',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/flat/flat-delete-dialog.html',
                        controller: 'FlatDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Flat', function(Flat) {
                                return Flat.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('flat', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
