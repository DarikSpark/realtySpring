'use strict';

angular.module('realtyCRMmysqlApp').controller('BargainDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Bargain', 'Client',
        function($scope, $stateParams, $modalInstance, entity, Bargain, Client) {

        $scope.bargain = entity;
        $scope.clients = Client.query();
        $scope.load = function(id) {
            Bargain.get({id : id}, function(result) {
                $scope.bargain = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('realtyCRMmysqlApp:bargainUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.bargain.id != null) {
                Bargain.update($scope.bargain, onSaveSuccess, onSaveError);
            } else {
                Bargain.save($scope.bargain, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
