angular.module('myApp').controller('UserBookingRequestController', function ($scope, $rootScope,  $uibModal, 
		userRequestService, AppModal) {
	
	var vm = this;
	vm.bookingAllowed= function(){
		return true;
	};
	vm.openRequestForm = function () {
		$uibModal.open({
		      animation: true,
		      backdrop: 'static',
		      templateUrl: 'app/dashboard/pages/bookingRequestForm.html',
		      controller:'BookingRequestFormController',
		      controllerAs:'vm',
		      size: 'md',
		      scope: $scope
		    });
	 };
	vm.searchRequest   = '';
	$scope.initialize = function(){
		$scope.loading = userRequestService.getBookingList().success(function(response){
			vm.bookingList = [];//response.entityList;
		}).error(function(error){
			AppModal.error(error);
		});
	};
	
	$scope.initialize();
	
	vm.showBookingDetails = function(bookingId) {
		$rootScope.bookingId = bookingId;
		$uibModal.open({
		      animation: true,
		      backdrop: 'static',
		      templateUrl: 'app/dashboard/pages/bookingRequestDetailsView.html',
		      controller: 'BookingDetailsController',
		      controllerAs: 'vm',
		      size: 'md'
		    });
	};
});