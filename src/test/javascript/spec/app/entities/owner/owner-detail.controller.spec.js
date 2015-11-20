'use strict';

describe('Owner Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockOwner, MockFlat;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockOwner = jasmine.createSpy('MockOwner');
        MockFlat = jasmine.createSpy('MockFlat');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Owner': MockOwner,
            'Flat': MockFlat
        };
        createController = function() {
            $injector.get('$controller')("OwnerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'realtyCRMmysqlApp:ownerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
