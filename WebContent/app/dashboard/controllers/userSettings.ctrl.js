angular.module('myApp').controller('UserSettingsController', 
		function ($scope, $rootScope, Constants, $modalInstance) {

	var vm = this;
	var user = $rootScope.globals.authenticationInfo.user,
	role = $rootScope.globals.currentRole;
	
	if(role === Constants.APP_ROLES.ADMIN){
		vm.url = 'dashboard';
		vm.isAdmin = true;
	}
	else{
		vm.url = 'userdashboard';
		vm.isAdmin = false;
	}
	
	vm.cancel = function(){
		$modalInstance.close();
	};
	
	vm.ok = function(){
		
	};
	
});