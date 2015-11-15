'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('ClientController', function ($scope, $state, $modal, Client, ParseLinks) {
      
        $scope.clients = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Client.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.clients.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.clients = [];
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
            $scope.client = {
                name: null,
                passport: null,
                city: null,
                id: null
            };
        };
    });
