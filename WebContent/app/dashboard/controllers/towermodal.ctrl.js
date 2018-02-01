angular
	.module('myApp').controller('TowerModalController', function TowerModalController($scope, 
			$rootScope, towerService, $uibModal, $modalInstance) {
		
		var vm = this;
		vm.data = {
			name : "",
			flatPrefix : "",
			flatCount: "",
			totalFloor: 0
		};

		vm.ok = function () {
			var tower ={};
			tower["Tower"] = vm.data;
			towerService.addTower(tower).success(function (response) {
				vm.cancel();
				$scope.initialize();
            }).error(function(){
            	vm.cancel();
            	alert('error');
            });
			
		};
	
		vm.cancel = function () {
			$modalInstance.close();
		};
    });