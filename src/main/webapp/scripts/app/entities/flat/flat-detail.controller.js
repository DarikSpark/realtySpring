'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('FlatDetailController', function ($scope, $rootScope, $stateParams, entity, Flat, Owner, Comment, Bargain) {
        $scope.flat = entity;
        $scope.load = function (id) {
            Flat.get({id: id}, function(result) {
                $scope.flat = result;
            });
        };
        var unsubscribe = $rootScope.$on('realtyCRMmysqlApp:flatUpdate', function(event, result) {
            $scope.flat = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
