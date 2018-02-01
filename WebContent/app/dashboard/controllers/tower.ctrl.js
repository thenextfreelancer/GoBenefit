angular.module('myApp').controller('TowerController', function ($scope, $rootScope,  $uibModal, towerService, AppModal) {

	var vm = this;
	vm.open = function (size) {
		$uibModal.open({
	      animation: true,
	      templateUrl: 'app/dashboard/pages/tower-modal.html',
	      controller: 'TowerModalController',
	      controllerAs: 'vm',
	      size: 'md',
	      scope: $scope
	    });
	 };
	vm.searchTower   = '';
	
	vm.showFlatView = function(tower) {
		$scope.towerId = tower.id;
		$uibModal.open({
	      animation: true,
	      templateUrl: 'app/dashboard/pages/flatview.html',
	      controller: 'FlatViewController',
	      controllerAs: 'vm',
	      size: 'lg',
	      scope: $scope
	    });
	};
	
	vm.configureFlats = function(tower){
		$scope.towerId = tower.id;
		$uibModal.open({
	      animation: true,
	      templateUrl: 'app/dashboard/pages/flat-configure-view.html',
	      controller: 'FlatConfigureController',
	      controllerAs: 'vm',
	      size: 'lg',
	      scope: $scope
	    });
	};

	$scope.initialize = function(){
		$scope.loading = towerService.getTowers().success(function (response) {
	 		vm.towers = response.entityList;
	 	}).error(function(error){
	 		AppModal.error(error);
	 	});
	};
	
	$scope.initialize();
	//Edit Grid Code
	vm.checkData = function(a,b) {
		
	};
	
	vm.checkNumeric = function(a,b) {
		var pattern = /^\d+$/;
        if(!pattern.test(a))
        	return "Only numeric values allowed.";
	};
	
	vm.saveTower = function(data) {
		$scope.loading = towerService.updateTower(data).success(function (response) {
			$scope.initialize();
	 	}).error(function(error){
	 		AppModal.error(error);
	 	});
	};
	
});