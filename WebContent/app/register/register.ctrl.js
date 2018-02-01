angular.module('myApp').controller(
		'RegisterController',
		function RegisterController($scope, $rootScope, $uibModal,
				AuthenticationService, $modalInstance, AppModal) {
			var vmr = this;
			vmr.requestOTP = function(){
				vmr.otp = false;
				$scope.loadingV = AuthenticationService.Verify(vmr.newuser).then(function(response) {
					vmr.otp = true;
					AppModal.success('An OTP is sent to your email id:'+vmr.newuser.emailId+'. Please use OTP to proceed further.');
				}, function(error) {
					AppModal.error('Your email id is either already registered or not valid. Please contact support.');
					$modalInstance.dismiss();
				});
			};
			
			vmr.register = function() {
				$scope.loadingP = AuthenticationService.Register(vmr.newuser).then(function(response) {
					$rootScope.userRegistrationInfo = response.data.User;
					$modalInstance.close();
				}, function(error) {
					AppModal.error('OTP is not valid. Please try again.');
					$modalInstance.dismiss();
				});
			}

		});