angular
	.module('myApp').service('userService', function userService($http, Constants) {
		
		return {
    		getUsers: function (){
        		return $http.get(Constants.appBaseUrl+'/api/users');
    		},
    		approveUser: function (user){
    			var json = JSON.stringify(user);
        		return $http.put(Constants.appBaseUrl+'/api/users/approve/'+user.id, {});
    		},
    		disapproveUser: function (user){
    			return $http.put(Constants.appBaseUrl+'/api/users/disapprove/'+user.id, {});
    		},
    		deactivateUser: function (user){
    			var json = JSON.stringify({User: user});
    			return $http.put(Constants.appBaseUrl+'/api/users/deactivate/'+user.id, {});
    		},
    		bulkUploadUsers: function (users){
    			var json = JSON.stringify(users);
    			return $http.post(Constants.appBaseUrl+'/api/users/', json);
    		}
    	};
});