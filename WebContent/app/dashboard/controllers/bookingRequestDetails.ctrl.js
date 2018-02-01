angular.module('myApp').controller('BookingDetailsController', 
		function ($scope, $rootScope, userRequestService, $modalInstance, AppModal) {
	
	var vm = this;
	function loadServices(){
		$scope.loading = userRequestService.getBookingDetails($rootScope.bookingId).success(function(response){
			vm.bookingRequest = response;
		}).error(function(error){
			AppModal.error(error);
		});
	};
	
	vm.close = function(){
		$modalInstance.close();
	};
	
	loadServices();
	
	vm.addComment = function(){
		
	};
});