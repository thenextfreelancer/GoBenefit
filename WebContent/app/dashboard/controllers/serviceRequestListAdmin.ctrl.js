angular.module('myApp').controller('ServiceRequestListAdminController', 
		function ($scope, $rootScope, $uibModal, userRequestService, flatService, AppModal) {
	
	var vm = this;
	function initialize(){
		$scope.loading = userRequestService.getServiceList().success(function(response){
			vm.serviceListAdmin = response.entityList;
		}).error(function(error){
			AppModal.error(error);
		});
	};
	
	initialize();
	
	vm.openRequestForm = function () {
		$uibModal.open({
		      animation: true,
		      backdrop: 'static',
		      templateUrl: 'app/dashboard/pages/serviceRequestForm.html',
		      controller: 'UserServiceRequestFormController',
		      controllerAs: 'vmc',
		      size: 'md'
		    });
	 };
	 
	vm.showServiceDetails = function(serviceId) {
		$rootScope.serviceId = serviceId;
		var serviceDetailsModal = $uibModal.open({
		      animation: true,
		      backdrop: 'static',
		      templateUrl: 'app/dashboard/pages/serviceRequestDetailsView.html',
		      controller: 'ServiceDetailsController',
		      controllerAs: 'vm',
		      size: 'md'
		    });
		
		serviceDetailsModal.result.then(function(){
			$rootScope["serviceId"]=null;
			initialize();
		},function(){
			
		});
	};
});