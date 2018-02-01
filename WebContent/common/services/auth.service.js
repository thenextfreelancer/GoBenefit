angular.module('myApp').service('AuthenticationService', function AuthenticationService($http, $cookieStore, 
		$rootScope, Constants) {

    function Login(passedApartmentId, user) {
    	if(passedApartmentId)
			url = '/api/authentication?apartmentId='+passedApartmentId;
		else
			url = '/api/authentication';
    	return $http.post(Constants.appBaseUrl+url, $.param(user), {
		  	      headers : {
		  	        "content-type" : "application/x-www-form-urlencoded"
		  	      }
		  	    });
    }

    function Verify(user) {
    	return $http.post(Constants.appBaseUrl+'/api/users/sendToken', '{ "User":'+JSON.stringify(user)+'}');
    }
    
    function Register(user) {
    	return $http.post(Constants.appBaseUrl+'/api/users', '{ "User":'+JSON.stringify(user)+'}');
    }
    
    function Update(user) {
    	user.role = {};
    	user.flatId = user.flat;
    	user.role.role = 'OWNER';
    	var url = Constants.appBaseUrl+'/api/userflats';
    	var json = '{ "UserFlat":'+JSON.stringify(user)+'}';
    	return $http.post(url, json);
    }

    function SetCredentials(authObj) {
    	var auth = authObj.UserAuthenticationToken;
        $rootScope.globals = {
        	authenticationInfo: auth,
        	currentRole: auth.user.role.role
        };

        $cookieStore.put('globals', $rootScope.globals);
    }
    
    function SetDefaultAuthAttributes(auth) {
    	$http.defaults.headers.common['Authorization'] = 'Bearer ' + auth.UserAuthenticationToken.token; // jshint ignore:line
        $http.defaults.headers.common['Content-Type'] = 'application/json';
    }
    
    function ClearCredentials() {
        $rootScope.globals = {};
        $cookieStore.remove('globals');
        $http.defaults.headers.common.Authorization = 'Bearer';
    }

    var service = {};

    service.Login = Login;
    service.Update = Update;
    service.Verify = Verify;
    service.Register = Register;
    service.SetCredentials = SetCredentials;
    service.ClearCredentials = ClearCredentials;
    service.SetDefaultAuthAttributes = SetDefaultAuthAttributes;
    return service;
    
    // Base64 encoding service used by AuthenticationService
    var Base64 = {
 
        keyStr: 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=',
 
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;
 
            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
 
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
 
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
 
                output = output +
                    this.keyStr.charAt(enc1) +
                    this.keyStr.charAt(enc2) +
                    this.keyStr.charAt(enc3) +
                    this.keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);
 
            return output;
        },
 
        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;
 
            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                window.alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
 
            do {
                enc1 = this.keyStr.indexOf(input.charAt(i++));
                enc2 = this.keyStr.indexOf(input.charAt(i++));
                enc3 = this.keyStr.indexOf(input.charAt(i++));
                enc4 = this.keyStr.indexOf(input.charAt(i++));
 
                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;
 
                output = output + String.fromCharCode(chr1);
 
                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }
 
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
 
            } while (i < input.length);
 
            return output;
        }
    };
});