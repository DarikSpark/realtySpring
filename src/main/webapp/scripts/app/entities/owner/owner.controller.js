'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('OwnerController', function ($scope, $state, $modal, Owner, ParseLinks) {
      
        $scope.owners = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Owner.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.owners.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.owners = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.owner = {
                name: null,
                phone: null,
                bank_account: null,
                id: null
            };
        };
    });
