'use strict';

describe('Bargain Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockBargain, MockClient;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockBargain = jasmine.createSpy('MockBargain');
        MockClient = jasmine.createSpy('MockClient');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Bargain': MockBargain,
            'Client': MockClient
        };
        createController = function() {
            $injector.get('$controller')("BargainDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'realtyCRMmysqlApp:bargainUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
