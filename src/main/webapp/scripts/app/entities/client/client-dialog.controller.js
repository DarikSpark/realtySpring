'use strict';

angular.module('realtyCRMmysqlApp').controller('ClientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Client', 'Bargain', 'Comment',
        function($scope, $stateParams, $modalInstance, entity, Client, Bargain, Comment) {

        $scope.client = entity;
        $scope.bargains = Bargain.query();
        $scope.comments = Comment.query();
        $scope.load = function(id) {
            Client.get({id : id}, function(result) {
                $scope.client = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('realtyCRMmysqlApp:clientUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.client.id != null) {
                Client.update($scope.client, onSaveSuccess, onSaveError);
            } else {
                Client.save($scope.client, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
