angular.module('myApp').service('facilitiesService',
		function facilitiesService($http, Constants) {
			return {
				getAllFacilities : function() {
					return $http.get(Constants.appBaseUrl+'/api/facilities');
				},
				getApptFacilities : function(aptId) {
					return $http.get(Constants.appBaseUrl+'/api/facilities/' + aptId);
				},
				assignFacilities : function(facilities) {
					var json = JSON.stringify(facilities);
					return $http.post(Constants.appBaseUrl+'/api/facilities/assign', json);
				},
				updateFacility : function() {
					var towers = {};
					return $http.get(Constants.appBaseUrl+'/api/clubs');
				}
			};
		});