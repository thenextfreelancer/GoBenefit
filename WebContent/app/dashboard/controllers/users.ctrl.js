angular.module('myApp').controller('UsersController', function ($scope, userService, $uibModal, AppModal) {

	var vm = this;
	vm.searchUser   = '';
	
	function initialize() {
		$scope.loading = userService.getUsers().success(function (response) {
	 		vm.users = response.entityList;
	 	}).error(function(error){
	 		AppModal.error(error);
	 	});
	}; 
	
	initialize();
	
	vm.openFileUpload= function(){
		 $uibModal.open({
		      animation: true,
		      templateUrl: 'app/dashboard/pages/user-upload.html',
		      controller: 'UserUploadController',
		      controllerAs: 'vm',
		      size: 'lg'
		    });
	};
	
	vm.approveUser= function(user){
		var modalOptions = {
		        closeButtonText: 'Cancel',
		        actionButtonText: 'Approve',
		        headerText: '',
		        bodyText: 'Are you sure you want to approve this user?'
		    };
		AppModal.confirm({}, modalOptions).then(function (result) {
			$scope.loading = userService.approveUser(user).success(function (response) {
				initialize();
		 	}).error(function(error){
		 		AppModal.error(error);
		 	});
	    });
	};
	
	vm.disapproveUser= function(user){
		var modalOptions = {
		        closeButtonText: 'Cancel',
		        actionButtonText: 'Disapprove',
		        headerText: '',
		        bodyText: 'Are you sure you want to disapprove this user?'
		    };
		AppModal.confirm({}, modalOptions).then(function (result) {
			$scope.loading = userService.disapproveUser(user).success(function (response) {
				initialize();
		 	}).error(function(error){
		 		AppModal.error(error);
		 	});
		},function(){
			//TODO error handling
		});
	};
	
	vm.deactivateUser= function(user){
		var modalOptions = {
		        closeButtonText: 'Cancel',
		        actionButtonText: 'Deactivate',
		        headerText: '',
		        bodyText: 'Are you sure you want to deactivate this user?'
		    };
		AppModal.confirm({}, modalOptions).then(function (result) {
			$scope.loading = userService.deactivateUser(user).success(function (response) {
				initialize();
		 	}).error(function(error){
		 		AppModal.error(error);
		 	});
		});
	};
});