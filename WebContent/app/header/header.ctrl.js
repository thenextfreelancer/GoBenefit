angular
	.module('myApp').controller('HeaderController', function HeaderController($scope, $state, $uibModal, $rootScope) {
		
     // $state.transitionTo('contacts.list');
		$scope.change = function(childState){
//	        var childState = $scope.advancedSettings
//	          ? "home.advanced"
//	          : "home.basic";
	        $state.go(childState);
	    } 
		
		$scope.login =function(){
			$uibModal.open({
			      animation: true,
			      templateUrl: 'app/login/login.view.html',
			      controller: 'LoginController',
			      size: 'md'
			    });
		};
		
		$scope.register =function(){
			$rootScope.registerModal = $uibModal.open({
			      animation: true,
			      backdrop: 'static',
			      keyboard: true,
			      templateUrl: 'app/register/register.view.html',
			      controller: 'RegisterController',
			      controllerAs: 'vmr',
			      size: 'md'
			    });
			$rootScope.registerModal.result.then(function(res){
				
				var registerAdvModal = $uibModal.open({
				      animation: true,
				      backdrop: 'static',
				      templateUrl: 'app/register/register-advance.view.html',
				      controller: 'RegisterAdvanceController',
				      controllerAs: 'vm',
				      size: 'md'
				    });
			},function(){
				//TODO error handling
			});
		};
      
    });