angular.module('myApp').controller('ServiceDetailsController', 
		function ($scope, $rootScope, userRequestService, $modalInstance, AppModal) {
	
	var vm = this;
	function loadServices(){
		$scope.loading = userRequestService.getServiceDetails($rootScope.serviceId).success(function(response){
			vm.serviceRequest = response;
		}).error(function(error){
			AppModal.error(error);
		});
	};
	vm.close = function(){
		$modalInstance.close();
	};
	loadServices();
	
});