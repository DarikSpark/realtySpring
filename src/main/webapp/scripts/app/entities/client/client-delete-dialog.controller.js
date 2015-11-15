'use strict';

angular.module('realtyCRMmysqlApp')
	.controller('ClientDeleteController', function($scope, $modalInstance, entity, Client) {

        $scope.client = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Client.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });