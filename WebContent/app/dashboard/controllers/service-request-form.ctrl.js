angular.module('myApp').controller('UserServiceRequestFormController', 
		function ($scope, $rootScope, userRequestService, facilitiesService, $modalInstance, AppModal) {
	
	var vmc = this;
	var selectedFlatId = null;
	vmc.flats = $rootScope.globals.authenticationInfo.user.flats;
	
	vmc.loadFacilities = function(){
		var aptId = $rootScope.globals.authenticationInfo.user.currentApartment.id;
		
		facilitiesService.getApptFacilities(aptId).success(function(response) {
			var list = response.entityList;
			if(list){
				var items = [];
				for(var ii=0; ii<list.length; ii++){
					items.push(list[ii].apartmentFacility);
				}
				vmc.facilities = items;
				vmc.facilityCode = items[0].code;
				
				vmc.flatId = vmc.flats[0].id;
			}
		}).error(function(error) {
			AppModal.error(error);
		});
		
	};
	
	vmc.loadFacilities();
	vmc.close = function(){
		$modalInstance.close();
	};
	
	vmc.requestService = function(){
		var request = {
				ServiceRequest : {
					description: vmc.message,
					subject: vmc.subject,
					apartmentFacility: {
						code: vmc.facilityCode
					},
					flat:{
						id: vmc.flatId //TODO hard coded flat id
					}
				}
		};
		
		userRequestService.requestService(request).success(function(response){
			$modalInstance.close();
		}).error(function(error) {
			AppModal.error(error);
			$modalInstance.dismiss();
		});
	};
	
});