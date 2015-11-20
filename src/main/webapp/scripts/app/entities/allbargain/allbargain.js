'use strict';

angular.module('realtyCRMmysqlApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bargain', {
                parent: 'entity',
                url: '/bargains',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'realtyCRMmysqlApp.bargain.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bargain/bargains.html',
                        controller: 'BargainController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bargain');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bargain.detail', {
                parent: 'entity',
                url: '/bargain/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'realtyCRMmysqlApp.bargain.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bargain/bargain-detail.html',
                        controller: 'BargainDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bargain');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Bargain', function($stateParams, Bargain) {
                        return Bargain.get({id : $stateParams.id});
                    }]
                }
            })
            .state('bargain.new', {
                parent: 'bargain',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bargain/bargain-dialog.html',
                        controller: 'BargainDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    day_count: null,
                                    coming_date: null,
                                    note: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('bargain', null, { reload: true });
                    }, function() {
                        $state.go('bargain');
                    })
                }]
            })
            .state('bargain.edit', {
                parent: 'bargain',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bargain/bargain-dialog.html',
                        controller: 'BargainDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Bargain', function(Bargain) {
                                return Bargain.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bargain', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('bargain.delete', {
                parent: 'bargain',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bargain/bargain-delete-dialog.html',
                        controller: 'BargainDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Bargain', function(Bargain) {
                                return Bargain.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bargain', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
