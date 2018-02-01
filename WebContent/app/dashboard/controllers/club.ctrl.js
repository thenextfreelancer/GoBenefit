angular
	.module('myApp').controller('ClubController', function ClubController($scope, $rootScope, $uibModal, clubService, AppModal) {
		
		var vm = this;
		vm.open = function (size) {
			 $rootScope.clubModal = $uibModal.open({
		      animation: true,
		      templateUrl: 'app/dashboard/pages/club-modal.html',
		      controller: 'ClubModalController',
			  controllerAs: 'vm',
		      size: size,
		      scope: $scope
		    });	
		};
		
		vm.searchClub  = '';
		
		$scope.initialize = function() {
			$scope.loading = clubService.getClubs().success(function (response) {
		 		vm.clubs = response.entityList;
		 	}).error(function(error){
		 		AppModal.error(error);
		 	});
		};
		
		$scope.initialize();

		vm.saveClub = function(data) {
			$scope.loading = clubService.updateClub(data).success(function (response) {
				$scope.initialize();
		 	}).error(function(error){
		 		AppModal.error(error);
		 	});
		};
		
		vm.removeClub = function(data) {
			$scope.loading = clubService.deleteClub(data).success(function (response) {
				$scope.initialize();
		 	}).error(function(error){
		 		AppModal.error(error);
		 	});
		};
});