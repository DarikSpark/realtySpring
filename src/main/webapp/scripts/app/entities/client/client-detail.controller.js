'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('ClientDetailController', function ($scope, $rootScope, $stateParams, entity, Client, Bargain, Comment) {
        $scope.client = entity;
        $scope.load = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
            });
        };
        var unsubscribe = $rootScope.$on('realtyCRMmysqlApp:clientUpdate', function(event, result) {
            $scope.client = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
