angular
	.module('myApp').controller('FlatViewController', function FlatViewController($scope, $rootScope, flatService, $modalInstance) {
		
		var vm = this;
		vm.flatListSample = [{
			desc: "Residents",
			address: "1001",
			isOwnerResident: true,
			vacant: false,
			floorNo: "1",
			isMember: false
		},{
			desc: "Tanents",
			address: "1002",
			isOwnerResident: false,
			vacant: false,
			floorNo: "1",
			isMember: false
		},{
			desc: "Vacent",
			address: "1003",
			isOwnerResident: false,
			vacant: true,
			floorNo: "1",
			isMember: false
		},{
			desc: "Resident and Member",
			address: "1004",
			isOwnerResident: true,
			vacant: false,
			floorNo: "1",
			isMember: true
		}];
		
		var towerId = $scope.towerId;
		var apptId = $rootScope.globals.authenticationInfo.user.id;
		function initialize(){
			flatService.getFlatsForApt(apptId, towerId).success(function (response) {
				vm.flatList = response.entityList;
	    	}).error(function(){
	    		alert("Error");
	    	});
		};
		
		initialize();
	    vm.cancel = function () {
	    	$modalInstance.close();
	    };
      
    });