'use strict';

angular.module('realtyCRMmysqlApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('owner', {
                parent: 'entity',
                url: '/owners',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'realtyCRMmysqlApp.owner.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/owner/owners.html',
                        controller: 'OwnerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('owner');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('owner.detail', {
                parent: 'entity',
                url: '/owner/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'realtyCRMmysqlApp.owner.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/owner/owner-detail.html',
                        controller: 'OwnerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('owner');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Owner', function($stateParams, Owner) {
                        return Owner.get({id : $stateParams.id});
                    }]
                }
            })
            .state('owner.new', {
                parent: 'owner',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/owner/owner-dialog.html',
                        controller: 'OwnerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    phone: null,
                                    bank_account: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('owner', null, { reload: true });
                    }, function() {
                        $state.go('owner');
                    })
                }]
            })
            .state('owner.edit', {
                parent: 'owner',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/owner/owner-dialog.html',
                        controller: 'OwnerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Owner', function(Owner) {
                                return Owner.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('owner', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('owner.delete', {
                parent: 'owner',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/owner/owner-delete-dialog.html',
                        controller: 'OwnerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Owner', function(Owner) {
                                return Owner.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('owner', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
