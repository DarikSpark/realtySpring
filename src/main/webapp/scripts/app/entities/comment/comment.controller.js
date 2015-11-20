'use strict';

angular.module('realtyCRMmysqlApp')
    .controller('CommentController', function ($scope, $state, $modal, Comment, ParseLinks) {
      
        $scope.comments = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Comment.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.comments.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.comments = [];
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
            $scope.comment = {
                text: null,
                date: null,
                id: null
            };
        };
    });
