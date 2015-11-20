'use strict';

describe('Flat Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFlat, MockOwner, MockComment, MockBargain;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFlat = jasmine.createSpy('MockFlat');
        MockOwner = jasmine.createSpy('MockOwner');
        MockComment = jasmine.createSpy('MockComment');
        MockBargain = jasmine.createSpy('MockBargain');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Flat': MockFlat,
            'Owner': MockOwner,
            'Comment': MockComment,
            'Bargain': MockBargain
        };
        createController = function() {
            $injector.get('$controller')("FlatDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'realtyCRMmysqlApp:flatUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
