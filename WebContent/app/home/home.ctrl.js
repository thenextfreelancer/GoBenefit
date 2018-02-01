angular
	.module('myApp').controller('HomeController', function HomeController(LookUpService,$http) {
		var vmh = this;
		vmh.searchData = "";
		vmh.appartmentInfo = [];
	   	vmh.lookup = function(value){
			return $http.get('/api/apartments?lookup='+value)
            .success(function (data) {
            	vmh.appartmentInfo = data.entityList;
            });
	    }
    });