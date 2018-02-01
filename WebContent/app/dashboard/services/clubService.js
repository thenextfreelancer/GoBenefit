angular.module('myApp').service('clubService', function clubService($http, $rootScope, Constants) {
	return {
		getClubs : function() {
			return $http.get(Constants.appBaseUrl+'/api/clubs');
		},
		addClub : function(club) {
			var json = JSON.stringify(club);
			return $http.post(Constants.appBaseUrl+'/api/clubs', json);
		},
		updateClub : function(club) {
			var json = JSON.stringify({Club:club});
			return $http.put(Constants.appBaseUrl+'/api/clubs/'+club.id,json);
		},
		deleteClub : function(club) {
			return $http.delete(Constants.appBaseUrl+'/api/clubs/'+club.id);
		}
	};
});