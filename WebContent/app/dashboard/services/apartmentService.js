angular.module('myApp').service('apartmentService', function apartmentService($http, $rootScope, Constants) {
	return {
		authApartment : function(apptId) {
			return $http.put(Constants.appBaseUrl+'/api/authentication/'+apptId,{});
		},
		getApartmentList : function(){
			return $http.get(Constants.appBaseUrl+'/api/apartments');
		}
	};
});