'use strict';

angular.module('realtyCRMmysqlApp').controller('FlatDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Flat', 'Owner', 'Comment', 'Bargain',
        function($scope, $stateParams, $modalInstance, entity, Flat, Owner, Comment, Bargain) {

        $scope.flat = entity;
        $scope.owners = Owner.query();
        $scope.comments = Comment.query();
        $scope.bargains = Bargain.query();
        $scope.load = function(id) {
            Flat.get({id : id}, function(result) {
                $scope.flat = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('realtyCRMmysqlApp:flatUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.flat.id != null) {
                Flat.update($scope.flat, onSaveSuccess, onSaveError);
            } else {
                Flat.save($scope.flat, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
