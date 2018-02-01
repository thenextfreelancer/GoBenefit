describe('Controller: AboutController',function(){
	var $controller, $rootScope;
	beforeEach(angular.module('myApp'));
	
	beforeEach(inject(function(_$controller_){
		$controller = _$controller_;
		
	}))
		
	describe('$scope.change', function(){
		var $scope, controller;
		beforeEach(function(){
			$scope ={};
			controller = $controller('AboutController', { $scope: $scope});
		});
		
		it('should navigate to child state', function(){
			var $scope;
			var controller = $controller('AboutController', { $scope: $scope});
			$scope.password = 'jhatu';
			$scope.change('/');
//			expect($scope.strength).isEqual('weak');
			
		});
	});
});