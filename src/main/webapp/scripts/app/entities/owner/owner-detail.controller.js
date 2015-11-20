'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('OwnerDetailController', function ($scope, $rootScope, $stateParams, entity, Owner, Flat) {
        $scope.owner = entity;
        $scope.load = function (id) {
            Owner.get({id: id}, function(result) {
                $scope.owner = result;
            });
        };
        var unsubscribe = $rootScope.$on('realtyCRMmysqlApp:ownerUpdate', function(event, result) {
            $scope.owner = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
