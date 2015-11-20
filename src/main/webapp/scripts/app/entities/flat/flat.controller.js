'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('FlatController', function ($scope, $state, $modal, Flat, ParseLinks) {
      
        $scope.flats = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Flat.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.flats.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.flats = [];
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
            $scope.flat = {
                address: null,
                description: null,
                day_cost: null,
                people_count: null,
                id: null
            };
        };
    });
