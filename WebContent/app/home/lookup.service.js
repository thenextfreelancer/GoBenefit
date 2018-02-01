angular
    .module('myApp')
    .factory('LookUpService', function LookUpService($http, $cookieStore, $rootScope, $timeout) {
    	return {
    		lookup: function (str){
    			var appartments={};
        		$http.get('/rest/apartments?apartmentName='+str)
                .success(function (response) {
                	appartments = response;
                });
        		return appartments;
    		}
    	};
    });