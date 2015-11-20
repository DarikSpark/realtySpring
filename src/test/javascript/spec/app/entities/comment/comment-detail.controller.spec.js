'use strict';

describe('Comment Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockComment, MockClient, MockFlat;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockComment = jasmine.createSpy('MockComment');
        MockClient = jasmine.createSpy('MockClient');
        MockFlat = jasmine.createSpy('MockFlat');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Comment': MockComment,
            'Client': MockClient,
            'Flat': MockFlat
        };
        createController = function() {
            $injector.get('$controller')("CommentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'realtyCRMmysqlApp:commentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
