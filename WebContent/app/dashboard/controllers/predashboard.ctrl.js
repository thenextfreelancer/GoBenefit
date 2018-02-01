angular
	.module('myApp').controller('PreDashboardController', function PreDashboardController($scope, $rootScope, $state,
			apartmentService, AuthenticationService, Constants, $modalInstance) {
		
		$scope.loadApt = function(apt){
			$scope.loading = apartmentService.authApartment(apt.id).success(function(auth){
				$modalInstance.close();
				AuthenticationService.SetCredentials(auth);
				
				if($rootScope.globals.currentRole === Constants.APP_ROLES.ADMIN)
					$state.go('dashboard', {reload: true});
				else
					$state.go('userdashboard', {reload: true});
				
			}).error(function(error){
				alert("bad request:"+error);
			});
		};
});