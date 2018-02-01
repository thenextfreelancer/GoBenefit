angular
	.module('myApp').service('userRequestService', function userRequestService($http, Constants) {
		
		return {
			getServiceList: function (){
        		return $http.get(Constants.appBaseUrl+'/api/serviceRequests');
    		},
    		getServiceDetails: function (serviceId){
        		return $http.get(Constants.appBaseUrl+'/api/serviceRequests/'+serviceId);
    		},
    		requestService: function (request){
    			var json = JSON.stringify(request);
        		return $http.post(Constants.appBaseUrl+'/api/serviceRequests', json);
    		},
    		getBookingList: function (){
        		return $http.get(Constants.appBaseUrl+'/api/bookings');
    		},
    		getBookingDetails: function (bookingId){
        		return $http.get(Constants.appBaseUrl+'/api/bookings/'+bookingId);
    		},
    		requestBooking: function (request){
    			var json = JSON.stringify(request);
    			return $http.post(Constants.appBaseUrl+'/api/bookings', json);
    		}
    	};
});