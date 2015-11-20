'use strict';

angular.module('realtyCRMmysqlApp')
	.controller('FlatDeleteController', function($scope, $modalInstance, entity, Flat) {

        $scope.flat = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Flat.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });