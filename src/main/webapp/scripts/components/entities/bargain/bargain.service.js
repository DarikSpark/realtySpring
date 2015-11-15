'use strict';

angular.module('realtyCRMmysqlApp')
    .factory('Bargain', function ($resource, DateUtils) {
        return $resource('api/bargains/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.coming_date = DateUtils.convertLocaleDateFromServer(data.coming_date);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.coming_date = DateUtils.convertLocaleDateToServer(data.coming_date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.coming_date = DateUtils.convertLocaleDateToServer(data.coming_date);
                    return angular.toJson(data);
                }
            }
        });
    });
