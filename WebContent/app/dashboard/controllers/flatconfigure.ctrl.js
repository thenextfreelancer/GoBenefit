angular
	.module('myApp').controller('FlatConfigureController', function FlatViewController($scope, 
			$rootScope, flatService, fileUploadService, $uibModal, $modalInstance) {
		
		var vm = this;
		vm.uploadMessage=false;
		var loadedFile ={};
		var towerId = $scope.towerId;
		var apptId = $rootScope.globals.authenticationInfo.user.id;
		function initialize(){
			flatService.getFlatsForApt(apptId, towerId).success(function (response) {
				vm.flatList = response.entityList;
	    	}).error(function(error){
	    		alert(error);
	    	});
		};
		
		initialize();
		
		vm.loadFile = function(file){
			loadedFile = file;
			$scope.$applyAsync();
		};
		
		vm.clear = function () {
	        angular.element("input[type='file']").val(null);
	    };
	    
	    vm.cancel = function () {
	    	$modalInstance.close();
	    };
	    
	    vm.flats = [{'prefix': '', 'flatNumber': '', 'floorNumber': '', 'towerId': towerId }];
	    vm.bulk = {};
	    
	    vm.reset = function(){
	    	vm.flats = [{ 'prefix': '', 'flatNumber': '', 'floorNumber': '', 'towerId': towerId  }];
	    }
	    
	    vm.addNewFlat = function() {
	    	var flats = vm.flats;
	    	flats.push({'prefix': '', 'flatNumber': '', 'floorNumber': '', 'towerId': towerId  });
	    };
	      
	    vm.removeFlat = function(cmp) {
	    	var arr = vm.flats;
	    	for (var ii = arr.length - 1; ii >= 0; ii--) {
	    	    var obj = arr[ii];
	    	    if (obj.id == cmp.id) {
	    	        arr.splice(ii, 1);
	    	    }
	    	}
	    };
	    
	    vm.save = function(type) {
	      var arr = vm.flats;
	      for(var i=0;i<arr.length;i++){
	    	  delete arr[i]['$$hashKey'];
	      }
	      flatService.uploadFlats(arr, towerId).then(function(response){
	    	  alert("Success");
	      }).error(function(error){
	    	  alert(error);
	      });
	    };
	    
	    vm.generateFlats = function() {
	    	var start = parseInt(vm.bulk.flatNoStart);
	    	var prefix = vm.bulk.flatPre;
	    	var flatCountOnFloor = parseInt(vm.bulk.flatCountOnFloor);
	    	var end = start + parseInt(vm.bulk.floorCount) * flatCountOnFloor;
	    	var floorNumber=0;
	    	vm.flats =[];
	    	for(var ii= start,jj=0 ;ii<=end;ii++,jj++){
	    		if(jj<flatCountOnFloor+1)
	    			floorNumber=0;
	    		if(jj%flatCountOnFloor==0)
	    			floorNumber++;
	  	      	vm.flats.push({'prefix': prefix, 'flatNumber': ii, 'floorNumber': floorNumber, 'towerId': towerId  });
	    	}
	    };
	    
	    vm.clear = function () {
	        angular.element("input[type='file']").val(null);
	    };
	    
	    vm.upload = function(){
            fileUploadService.uploadFileToUrl(loadedFile, towerId).success(function(data){
            	vm.uploadMessage=true;
            }).error(function(error){
            	alert(error);
            });
	    };
	    
	    vm.deleteFlat = function(flat){
	    	flatService.deleteFlat(flat).success(function(data){
	    		initialize();
            }).error(function(error){
		    	  alert(error);
		      });
	    };
      
    });