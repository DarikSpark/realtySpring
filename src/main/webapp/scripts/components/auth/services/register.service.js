'use strict';

angular.module('realtyCRMmysqlApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


