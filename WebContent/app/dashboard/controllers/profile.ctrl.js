(function () {
    'use strict';
 
    function ProfileController($window, $scope, $location, AuthenticationService, FlashService) {
    	var vm=this;
    	$scope.editProfile=false;
    	$scope.editExp=false;
    	$scope.projects = [{
			"title" : "ALumni",
			"duration" : "mar 2013 to apr 2015",
			"role" : "Programmer",
			"skills" : "Java, Angualar JS",
			"Team Size" : "10"
		},{
			"title" : "ALumni",
			"duration" : "mar 2013 to apr 2015",
			"role" : "Programmer",
			"skills" : "Java, Angualar JS",
			"Team Size" : "10"
		}];
		
    	$scope.user = {
			"designation" : "Senior Associate",
			"currCompany" : "Genpact HeadStrong",
			"currLocation" : "Noida",
			"preferredLocation" : "Noida, Chandigarh",
			"totalExp" : "3 Years and 5 Months",
			"annualSalary" : "5 Lacs 80 Thousands",
			"role" : "Software Development",
			"industry" : "Software / IT Services",
			"industry" : "Software / IT Services",
			"dob" : "22/02/1988",
			"gender" : "Male",
			"phone" : "8802160491",
			"email" : "kasheesh31@gmail.com",
			"permanantAddress" : "A-144 AVAS VIKAS COLONY BUDAUN",
			"homeTown" : "Budaun",
			"pin" : "243601",
			"email" : "kasheesh31@gmail.com",
			"maritalStatus" : "Unmarried",
			"highestDegree" : "UG Education B.TECH [CSE]"
		};
    	$scope.workExp = [ {
			"designation" : "Senior Associate",
			"employer" : "Genpact HeadStrong",
			"status" : "currentEmployer"
		}, {
			"designation" : "Programmer",
			"employer" : "CampusLabs Pvt. Ltd.",
			"status" : "previousEmployer"
		}];
    	
    	$scope.roles = [ "Programmer", "Sr. Programmer", "Test Engineer",
				"Project Leader", "Sr. Project Leader", "Domain Expert",
				"Sollution Architect", "Quality Analyst",
				"Database Architect/DBA", "Network/System Administrator",
				"Others" ];
		
    	$scope.edit=function(flag) {
    		$scope.editProfile=flag;
    		$scope.expYears = ["Fresher",0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30];
    		$scope.expMonths = [0,1,2,3,4,5,6,7,8,9,10,11];
    		$scope.salaryThousands = [0,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95];
    		var range1 = [];
    		for(var i=1;i<51;i++) {
    		  range1.push(i);
    		}
    		$scope.salaryLacs=range1;
    		$scope.maritalStatusList=["Single/Unmarried","Married","Widowed","Divoced","Separated","Other"];
    		$scope.genderList=["Male","Female"];
    	};
    	
    	$scope.editExpDtls=function(flag){
    		$scope.editExp=flag;
    		$scope.months = [{"name" :"Jan","code" : "1"},
    		               {"name" :"Feb","code" : "2"},
    		               {"name" :"Mar","code" : "3"},
    		               {"name" :"Apr","code" : "4"},
    		               {"name" :"May","code" : "5"},
    		               {"name" :"Jun","code" : "6"},
    		               {"name" :"Jul","code" : "7"},
    		               {"name" :"Aug","code" : "8"},
    		               {"name" :"Sep","code" : "9"},
    		               {"name" :"Oct","code" : "10"},
    		               {"name" :"Nov","code" : "11"},
    		               {"name" :"Dec","code" : "12"}];
    		var year = new Date().getFullYear();
    		var range = [];
    		range.push(year);
    		for(var i=1;i<20;i++) {
    		  range.push(year - i);
    		}
    		$scope.years = range;
    	};
      
    }
    
    ProfileController.$inject = ['$window','$scope', '$location', 'AuthenticationService', 'FlashService'];
    
    angular
    .module('myApp')
    .controller('ProfileController', ProfileController);
})();