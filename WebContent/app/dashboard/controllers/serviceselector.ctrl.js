angular.module('myApp').controller('ServiceSelector',function($scope, facilitiesService, $rootScope, $filter, AppModal) {

	var vm = this;
	vm.models = {
		selected : null,
		lists : {
			"allFacilities" : [],
			"apptFacilities" : []
		}
	};
	vm.savedFacilities =[];
	
	vm.moveAllItems = function(){
		vm.models.lists.apptFacilities.push.apply(vm.models.lists.apptFacilities, vm.models.lists.allFacilities);
		vm.models.lists.allFacilities = [];
	};
	
	vm.removeAllItems = function(){
		vm.models.lists.allFacilities.push.apply(vm.models.lists.allFacilities,  vm.models.lists.apptFacilities);
		vm.models.lists.apptFacilities = [];
	};
	
	$scope.loading = facilitiesService.getAllFacilities().success(function(response) {
		var items = response.entityList;
		if (items)
			for (var ii=0; ii<items.length; ii++) {
				vm.models.lists.allFacilities.push(items[ii]);
			}
	}).error(function(error) {
		AppModal.error(error);
	}).then(function(data){
		var aptId = $rootScope.globals.authenticationInfo.user.currentApartment.id;
		$scope.loading = facilitiesService.getApptFacilities(aptId).success(function(response) {
			var items = response.entityList;
			if (items)
				for (var ii=0; ii<items.length; ii++) {
					var item = items[ii].apartmentFacility;
					 var found = $filter('filter')(vm.models.lists.allFacilities, {name: item.name}, true);
				     if (found.length) {
				    	  var arrayOfObjects = vm.models.lists.allFacilities;
				    	 for (var i = arrayOfObjects.length - 1; i >= 0; i--) {
				    		    var obj = arrayOfObjects[i];
				    		    if (obj.name.indexOf(found[0].name) !== -1) {
				    		        arrayOfObjects.splice(i, 1);
				    		    }
				    		}
				     } 
					vm.models.lists.apptFacilities.push(item);
					vm.savedFacilities.push(item);
				}
		}).error(function(error) {
			AppModal.error(error);
		});
	});

	vm.assignFacilities = function() {
		var facilities = [];
		var items = vm.models.lists.apptFacilities;
		for (var ii=0; ii<items.length; ii++) {
			var item = items[ii].code;
			facilities.push(item);
		}
		vm.savedFacilities =[];
		vm.savedFacilities.push.apply(vm.savedFacilities, vm.models.lists.apptFacilities);
		$scope.loading = facilitiesService.assignFacilities(facilities).success(
		function(response) {
			AppModal.success("Service is added successfully.");
		}).error(function(error) {
			AppModal.error(error);
		});
	};
});
