angular.module('myApp').controller('BookingRequestListAdminController', 
		function ($scope, $rootScope,  $uibModal, userRequestService, AppModal) {
	
	var vm = this;

	function initialize(){
		$scope.loading = userRequestService.getBookingList().success(function(response){
			vm.bookingList = response.entityList;
			vm.bookingListAdmin = response.entityList;
		}).error(function(error){
			AppModal.error(error);
		});
	};
	
	initialize();
	
	vm.openRequestForm = function () {
		$uibModal.open({
		      animation: true,
		      backdrop: 'static',
		      templateUrl: 'app/dashboard/pages/bookingRequestForm.html',
		      controller:'BookingRequestFormController',
		      controllerAs:'vm',
		      size: 'md'
		    });
	 };
	
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