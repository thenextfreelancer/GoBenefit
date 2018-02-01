angular.module('myApp').service('flatService', function flatService($http, Constants) {
	return {
		getFlats : function(towerId) {
			return $http.get(Constants.appBaseUrl+'/api/flats?towerId='+towerId);
		},
		getFlatsForApt : function(aptId, towerId) {
			return $http.get(Constants.appBaseUrl+'/api/flats?apartmentId='+aptId+'&towerId='+towerId);
		},
		getUserFlats : function() {
			return $http.get(Constants.appBaseUrl+'/api/flats/current');
		},
		uploadFlats : function(flats, towerId) {
			var json = JSON.stringify(flats);
			return $http.post(Constants.appBaseUrl+'/api/flats/'+towerId, json);
		},
		updateFlat : function(flats) {
			var json = JSON.stringify({Club:flats});
			return $http.put(Constants.appBaseUrl+'/api/flats/'+flats.id,json);
		},
		deleteFlat : function(flat) {
			return $http.delete(Constants.appBaseUrl+'/api/flats/'+flat.id);
		}
	};
});