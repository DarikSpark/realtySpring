'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('BargainDetailController', function ($scope, $rootScope, $stateParams, entity, Bargain, Client, Flat) {
        $scope.bargain = entity;
        $scope.load = function (id) {
            Bargain.get({id: id}, function(result) {
                $scope.bargain = result;
            });
        };
        var unsubscribe = $rootScope.$on('realtyCRMmysqlApp:bargainUpdate', function(event, result) {
            $scope.bargain = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
