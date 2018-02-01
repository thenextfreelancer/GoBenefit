angular
	.module('myApp').controller('SelectApptController', function ClubController($scope, $rootScope, $uibModal,$stateParams) {
		
		$scope.apartments = $stateParams.apartments;
		$rootScope.predashboardModal = $uibModal.open({
			animation: true,
		    templateUrl: 'app/dashboard/pages/predashboard.html',
		    controller: 'PreDashboardController',
  			backdrop: 'static',
  			keyboard: false,
  			scope: $scope
		});
});