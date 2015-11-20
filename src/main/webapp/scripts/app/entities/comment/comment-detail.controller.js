'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('CommentDetailController', function ($scope, $rootScope, $stateParams, entity, Comment, Client, Flat) {
        $scope.comment = entity;
        $scope.load = function (id) {
            Comment.get({id: id}, function(result) {
                $scope.comment = result;
            });
        };
        var unsubscribe = $rootScope.$on('realtyCRMmysqlApp:commentUpdate', function(event, result) {
            $scope.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
