'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('BargainController', function ($scope, $state, $modal, Bargain, ParseLinks) {
      
        $scope.bargains = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Bargain.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.bargains.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.bargains = [];
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
            $scope.bargain = {
                day_count: null,
                coming_date: null,
                note: null,
                id: null
            };
        };
    });
