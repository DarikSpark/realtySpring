'use strict';

angular.module('realtyCRMmysqlApp')
    .factory('Comment', function ($resource, DateUtils) {
        return $resource('api/comments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
