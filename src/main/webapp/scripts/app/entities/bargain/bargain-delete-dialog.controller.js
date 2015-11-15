'use strict';

angular.module('realtyCRMmysqlApp')
	.controller('BargainDeleteController', function($scope, $modalInstance, entity, Bargain) {

        $scope.bargain = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Bargain.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });