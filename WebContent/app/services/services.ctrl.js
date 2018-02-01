angular
	.module('myApp').controller('ServicesController', function ServicesController($stateParams, 
			$scope, $uibModal, $rootScope) {
		var svc =this;
		var apartment = $stateParams.apartment;
		svc.apartmentName = apartment.name;
		
		svc.redirect = function(serviceCode){
			$scope.serviceCode = serviceCode;
			$scope.apartmentId = apartment.id;
			$uibModal.open({
		      animation: true,
		      templateUrl: 'app/login/login.view.html',
		      controller: 'LoginController',
		      size: 'md',
		      scope: $scope
		    });
		};
});