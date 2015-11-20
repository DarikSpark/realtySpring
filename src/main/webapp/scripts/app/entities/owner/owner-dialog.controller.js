'use strict';

angular.module('realtyCRMmysqlApp').controller('OwnerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Owner', 'Flat',
        function($scope, $stateParams, $modalInstance, entity, Owner, Flat) {

        $scope.owner = entity;
        $scope.flats = Flat.query();
        $scope.load = function(id) {
            Owner.get({id : id}, function(result) {
                $scope.owner = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('realtyCRMmysqlApp:ownerUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.owner.id != null) {
                Owner.update($scope.owner, onSaveSuccess, onSaveError);
            } else {
                Owner.save($scope.owner, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
