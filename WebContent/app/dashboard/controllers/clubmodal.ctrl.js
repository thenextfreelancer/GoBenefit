angular
	.module('myApp').controller('ClubModalController', function ClubModalController($scope, $rootScope, clubService, 
			$uibModal, $modalInstance) {
	var vm = this;
	vm.data = {
		name : "",
		description : ""
	};
		
	vm.ok = function () {
		var club ={};
		club["Club"] = vm.data;
		var _authToken = $rootScope.authToken;
		clubService.addClub(club).success(function (response) {
			vm.cancel();
			$scope.initialize();
        }).error(function(){
        	vm.cancel();
        });
		
	};
	
	vm.cancel = function () {
	  $modalInstance.close();
	};
  
    });