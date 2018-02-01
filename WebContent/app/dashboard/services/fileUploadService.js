angular.module('myApp').service('fileUploadService', function fileUploadService($http, $rootScope, Constants) {
		
	var uploadFileToUrl = function(file, towerId){
        var fd = new FormData();
        fd.append('file', file);
        return $http.post(Constants.appBaseUrl+'/api/flats/upload/'+towerId,fd,{
        	headers: {
        		'Content-Type': undefined,
        		"Authorization" : "Bearer " + $rootScope.authToken
        		}
        })
     };
	
	return {
		uploadFileToUrl: uploadFileToUrl
	};
});