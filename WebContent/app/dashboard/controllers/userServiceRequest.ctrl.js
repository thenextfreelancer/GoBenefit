angular.module('myApp').controller('UserServiceRequestController', 
		function ($scope, $rootScope, $uibModal, userRequestService, flatService, AppModal) {
	
	$scope.open = function () {
		var modal = $uibModal.open({
		      animation: true,
		      backdrop: 'static',
		      templateUrl: 'app/dashboard/pages/serviceRequestForm.html',
		      controller: 'UserServiceRequestFormController',
		      controllerAs: 'vmc',
		      size: 'md'
		    });
		
		modal.result.then(function(res){
			initialize();
		},function(){
			//TODO error handling
		});
	 };
	$scope.searchRequest   = '';
	
	function initialize(){
		$scope.loading = userRequestService.getServiceList().success(function(response){
			$scope.serviceList = [];//response.entityList;
		}).error(function(error){
			AppModal.error(error);
		});
	};
	
	initialize();
	
	$scope.showServiceDetails = function(serviceId) {
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