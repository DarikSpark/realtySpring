'use strict';

angular.module('realtyCRMmysqlApp')
    .factory('Owner', function ($resource, DateUtils) {
        return $resource('api/owners/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
