angular.module('myApp').controller('BookingRequestFormController', 
		function ($scope, $rootScope, userRequestService, facilitiesService, $modalInstance, AppModal) {
	
	var vm = this;
	var selectedFlatId = null;
	
	vm.dateOptionsFrom = {
		'year-format': "'yy'",
		'show-weeks' : false
	};
	vm.openedFrom = false;
	
	vm.dateOptionsTo = {
		'year-format': "'yy'",
		'show-weeks' : false
	};
	vm.openedTo =false;
	vm.flats = $rootScope.globals.authenticationInfo.user.flats;	
	vm.loadFacilities = function(){
		var aptId = $rootScope.globals.authenticationInfo.user.currentApartment.id;
		
		facilitiesService.getApptFacilities(aptId).success(function(response) {
			var list = response.entityList;
			if(list){
				var items = [];
				for(var ii=0; ii<list.length; ii++){
					items.push(list[ii].apartmentFacility);
				}
				vm.facilities = items;
				vm.facilityCode = items[0].code;
				vm.flatId = vm.flats[0].id;
			}
		}).error(function(error) {
			AppModal.error(error);
		});
	};
	
	vm.loadFacilities();
	
	var populatePeriodList = function(){
		var d =  [];
		for(var ii=0; ii< 24;ii++) {
			if(ii===0){
				d.push('00:00');
				d.push('00:30');
				vm.toTime = vm.fromTime = '00:00';
			} else {
				if(ii<10){
					d.push('0'+ii+':00');
					d.push('0'+ii+':30');
				}else{
					d.push(ii+':00');
					d.push(ii+':30');
				}
			}
		}
		vm.periodList = d;
	};
	populatePeriodList();
	
	vm.close = function(){
		$modalInstance.dismiss();
	};

	var getFormattedDate= function(d){
		var dd = d.getDate();
		var mm = d.getMonth()+1; //January is 0!

		var yyyy = d.getFullYear();
		if(dd<10){
		    dd='0'+dd
		} 
		if(mm<10){
		    mm='0'+mm
		} 
		return yyyy+'-'+mm+'-'+dd+' 00:00:00';
	};
	var getFormattedTime= function(time){
		return time+":00";
	};
	
	vm.requestBooking = function(){
		var userId = $rootScope.globals.authenticationInfo.user.id;
		var fd = getFormattedDate(vm.frombdate);
		var td = getFormattedDate(vm.tobdate);
		var request = {
				EventBooking: {
					eventStartDate: fd,
					eventEndDate: td,
					eventStartTime: getFormattedTime(vm.fromTime),
					eventEndTime: getFormattedTime(vm.toTime),
					apartmentFacility: {
						code: vm.facilityCode
					},
					subject: vm.subject,
					description: vm.description,
					user: {
						id: userId
					},
					flat: {
						id: vm.flatId
					}
				}
			};
		userRequestService.requestBooking(request).success(function(response){
			$modalInstance.close();
		}).error(function(error) {
			AppModal.error(error);
			$modalInstance.dismiss();
		});
		};
		
});