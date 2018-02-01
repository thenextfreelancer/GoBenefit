angular
	.module('myApp').controller('AboutController', function AboutController($scope, $state) {
		
     // $state.transitionTo('contacts.list');
		$scope.change = function(childState){
//	        var childState = $scope.advancedSettings
//	          ? "home.advanced"
//	          : "home.basic";
	        $state.go(childState);
	    } 
      
    });