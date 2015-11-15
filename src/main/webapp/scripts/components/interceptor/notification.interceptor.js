 'use strict';

angular.module('realtyCRMmysqlApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-realtyCRMmysqlApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-realtyCRMmysqlApp-params')});
                }
                return response;
            }
        };
    });
