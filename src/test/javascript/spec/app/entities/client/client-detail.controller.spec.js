'use strict';

describe('Client Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockClient, MockBargain;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockClient = jasmine.createSpy('MockClient');
        MockBargain = jasmine.createSpy('MockBargain');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Client': MockClient,
            'Bargain': MockBargain
        };
        createController = function() {
            $injector.get('$controller')("ClientDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'realtyCRMmysqlApp:clientUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
