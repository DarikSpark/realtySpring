'use strict';

angular.module('realtyCRMmysqlApp')
	.controller('OwnerDeleteController', function($scope, $modalInstance, entity, Owner) {

        $scope.owner = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Owner.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });