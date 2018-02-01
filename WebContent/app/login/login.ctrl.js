angular
	.module('myApp')
		.controller(
				'LoginController',
				function LoginController(Constants, $state, $scope, $rootScope, $uibModal,
						$http, AuthenticationService, AppModal,
						apartmentService, $modalInstance) {
		
		var serviceCode = $scope.serviceCode;
		var passedApartmentId = $scope.apartmentId;
		$scope.user = {};
		$scope.login = function() {

			$scope.loading = AuthenticationService.Login(passedApartmentId, $scope.user).success(function (response) {
				$modalInstance.close();
				AuthenticationService.SetDefaultAuthAttributes(response);
				var apartments =  response.UserAuthenticationToken.user.apartments;
    	    	
    	    	if(passedApartmentId){ //if logged in from service page
    	    		var aptExists =false;
        	    	for(var ii=0; ii<apartments.length;ii++){
        	    		var id = apartments[ii].id;
        	    		if(id == passedApartmentId){
        	    			aptExists = true;
        	    			break;
        	    		}
        	    	}
        	    	
        	    	//if user does not belong to selected apartment, give alert and redirect to sign up
        	    	if(!aptExists) 
        	    	{
        	    		var modalInstanceWarn = $uibModal.open({
              		      template: '<div class="modal-body" style="padding:0px"><div class="alert alert-warning" style="margin-bottom:0px"><strong>Error!</strong> You are not registered for the selected apartment. Do you wish to continue with login for your apartments?  <button type="button" style="margin-left: 40px;" class="btn btn-info" data-ng-click="redirect()" ><span class="glyphicon glyphicon-play-circle"> Continue</span></button><button type="button" class="close" data-ng-click="close()" ><span class="glyphicon glyphicon-remove-circle"></span></button></div></div>',
              		      backdrop: 'static',
              		      keyboard: true,
              		      backdropClick: true,
              		      timeout: 500,
              		      size: 'lg'
              		    });
        	    		
        	    		modalInstanceWarn.result.then(
	    		            //close
	    		            function (result) {
	    		            	AuthenticationService.ClearCredentials();
	    		            },
	    		            //dismiss
	    		            function (result) {
	    		            	AuthenticationService.ClearCredentials();
	    		        });

        	    	} else { //user has selected one of his own apartment; passing apartment object
        	    		redirect();
        	    	}
    	    	} else { //if direct login
    	    		
    	    		if(apartments.length===1){
    	    			passedApartmentId = apartments[0].id;
    	    			redirect();
    	    		} else {
    	    			$state.go('selectappt', {apartments: apartments});
    	    		}
    	    		
    	    	}
    	    }).error(function(data) {
    	    	AppModal.error('Login Failed. Please check User Name or Password.');
	  	    });
			
        	function redirect(){
        		AppModal.success('Login Successful.');
        		apartmentService.authApartment(passedApartmentId).success(function(auth){
        			$modalInstance.close();
    				AuthenticationService.SetCredentials(auth);
    				
    				if($rootScope.globals.currentRole === Constants.APP_ROLES.ADMIN)
    					$state.go('dashboard', {reload: true});
    				else
    					$state.go('userdashboard', {reload: true});
    				
    			}).error(function(error){
    				AppModal.error('Not able to get apartment associated. Please contact admin.');
    			});
        	}
        };
});