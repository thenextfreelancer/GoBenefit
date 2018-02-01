angular.module('myApp').controller('DashboardHeaderController',
		function DashboardHeaderController($scope, $rootScope, $state, $uibModal, AuthenticationService, Constants) {
		var vm = this;		
		
		var user = $rootScope.globals.authenticationInfo.user;
		var userName = user.firstName;
		vm.user = {
			name : userName
		};
		
		if($rootScope.globals.currentRole === Constants.APP_ROLES.ADMIN){
			vm.url = 'dashboard';
			vm.isAdmin = true;
		}
		else{
			vm.url = 'userdashboard';
			vm.isAdmin = false;
		}
			
		
		
		vm.userProfile = function(){
			$uibModal.open({
			      animation: true,
			      backdrop: 'static',
			      templateUrl: 'app/dashboard/pages/userProfile.html',
			      controller: 'UserProfileController',
			      controllerAs: 'vm',
			      size: 'lg'
			    });
		};
		
		vm.userSettings = function(){
			$uibModal.open({
			      animation: true,
			      backdrop: 'static',
			      templateUrl: 'app/dashboard/pages/userSettings.html',
			      controller: 'UserSettingsController',
			      controllerAs: 'vm',
			      size: 'lg'
			    });
		};
		
		vm.logout= function(){
			AuthenticationService.ClearCredentials();
			$state.go("home",{reload: true});
		};
});