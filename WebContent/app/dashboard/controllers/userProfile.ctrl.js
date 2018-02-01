angular.module('myApp').controller('UserProfileController', 
		function ($scope, $rootScope, Constants, $modalInstance, AuthenticationService) {

	var vm = this;
	vm.user = $rootScope.globals.authenticationInfo.user,
	role = $rootScope.globals.currentRole,
	loadedFile="", fullName="", displayName= "", email="", mobile="",re = /^[a-zA-Z ]+$/;
	
	if(role === Constants.APP_ROLES.ADMIN){
		vm.url = 'dashboard';
		vm.isAdmin = true;
	}
	else{
		vm.url = 'userdashboard';
		vm.isAdmin = false;
	}
	
	vm.close = function(){
		$modalInstance.close();
	};
	
	vm.resetImg = function(){
		vm.userImg =  vm.user.imagePath?vm.user.imagePath:"assets/img/defaultUser.jpg";
	};
	
	vm.resetImg();
	
	vm.loadFile = function(file){
		loadedFile = file;
		var reader = new FileReader();
	    reader.onload = function(){
	      var dataURL = reader.result;
	      vm.userImg = dataURL;
	    };
	    reader.readAsDataURL(file.files[0]);
		vm.clear();
		$scope.$applyAsync();
	};
	
	vm.clear = function () {
        $("input[type='file']").val(null);
    };
    
    vm.checkFullName = function(data){
    	if(data.match(re))
    		fullName = data;
    	else
    		alert('Wrong input');
    };
    
    vm.checkDisplayName = function(data){
    	if(data.match(re))
    		displayName = data;
    	else
    		alert('Wrong input');
    };
    
    vm.checkMobileNo = function(data){
    	var re = /^(\d+-?)+\d+$/;
    	if(data.match(re))
    		mobile = data;
    	else
    		alert('Wrong input');
    };
    
    vm.checkEmailId = function(data){
    	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    	if(data.match(re))
    		email = data;
    	else
    		alert('Wrong input');  
    };
    
    vm.saveUser = function(){
    	var data = {
    			name: fullName,
    			displayName:displayName,
    			userImg: loadedFile,
    			email: email,
    			mobile:mobile
    	};
    	
    	AuthenticationService.Update(data).then(function (auth) {
	    	var auth = response.UserAuthenticationToken;
	    	AuthenticationService.SetCredentials(auth);
  	    },function(error) {
  	    	alert(error);
  	    });
    };
	
});