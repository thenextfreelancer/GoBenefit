angular
	.module('myApp')
		.controller('RegisterAdvanceController', function RegisterAdvanceController($scope, $rootScope, $uibModal,
						AuthenticationService, apartmentService, towerService, flatService, AppModal, $modalInstance) {
		var vm = this;
		vm.initialize = function(){
			$scope.loadingA = apartmentService.getApartmentList().then(function(response){
				var list = response.data.entityList;
				if(list){
					var items = [{ name: 'Select Apartment', id : 'default'}];
					for(var ii=0; ii<list.length; ii++){
						items.push({ name: list[ii].name, id : list[ii].id});
					}
					vm.userapartment='default';
					vm.aptList = items;
				}
			},function(error){
				AppModal.error('Error in loading apartments. Please contact support.');
				$modalInstance.dismiss();
			});
		};
		
		vm.initialize();
		
		vm.getTowers = function(){
			$scope.loadingT = towerService.getTowersByAptId(vm.userapartment).then(function(response){
				var list = response.data.entityList;
				if(list){
					var items = [{ name: 'Select Tower', id : 'default'}];
					for(var ii=0; ii<list.length; ii++){
						items.push({ name: list[ii].name, id : list[ii].id});
					}
					vm.usertower='default';
					vm.towerList = items;
				}
			},function(error){
				AppModal.error('Error in loading towers. Please contact support.');
				$modalInstance.dismiss();
			});
		};
		
		vm.getFlats = function(){
			$scope.loadingF = flatService.getFlatsForApt(vm.userapartment, vm.usertower).then(function(response){
				var list = response.data.entityList;
				if(list){
					var items = [{ name: 'Select Flat', id : 'default'}];
					for(var ii=0; ii<list.length; ii++){
						items.push({ name: list[ii].flatNumber, id : list[ii].id});
					}
					vm.userflat='default';
					vm.flatList = items;
				}
			},function(error){
				AppModal.error('Error in loading flats. Please contact support.');
				$modalInstance.dismiss();
			});
		}
	    
	    vm.update = function() {
	    	if(vm.userapartment==='default'){
	    		AppModal.warning('Please select apartment.');
	    		return;
	    	} else if(vm.usertower==='default'){
	    		AppModal.warning('Please select tower.');
	    		return;
	    	} else if(vm.userflat==='default'){
	    		AppModal.warning('Please select flat.');
	    		return;
	    	}
	    	var user = {
	    			apartment:vm.userapartment,
	    			tower: vm.usertower,
	    			flat: vm.userflat,
	    			emailId: $rootScope.userRegistrationInfo.emailId,
	    			mobileNo: $rootScope.userRegistrationInfo.mobileNo
	    	};
	        
	    	$scope.loadingP = AuthenticationService.Update(user)
	            .then(function (response) {
	            		AppModal.success('Registration Successful. Please wait till admin verifies your account.');
	                    $location.path('/');
	                }, function(){
	                	AppModal.error('Error while saving your data. Please contact support.');
	                	$modalInstance.dismiss();
	             });
	    }
		
		
	});