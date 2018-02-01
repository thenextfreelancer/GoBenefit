angular.module('myApp').service('AppModal', function AppModal($rootScope, $uibModal) {
	
	function open(type, prefix, msg){
		var modalInstance = $uibModal.open({
	      template: '<div class="modal-body" style="padding:0px"><div class="alert alert-'+type+'" style="margin-bottom:0px"><button type="button" class="close" data-ng-click="close()" ><span class="glyphicon glyphicon-remove-circle"></span></button><strong>'+prefix+'</strong> '+msg+'</div></div>',
	      backdrop: true,
	      keyboard: true,
	      backdropClick: true,
	      timeout: 2000,
	      size: 'lg',
	      controller: function($scope){
	    	  $scope.close =  function(){
	  			modalInstance.close('close');
	  		};
	      }
	    }); 
	}
	
	var modalDefaults = {
        backdrop: 'static',
        keyboard: true,
        modalFade: true,
        templateUrl: 'common/tpl/confirm.html'
    };

    var modalOptions = {
        closeButtonText: 'Close',
        actionButtonText: 'OK',
        headerText: 'Proceed?',
        bodyText: 'Perform this action?'
    };

    function showModal(customModalDefaults, customModalOptions) {
        if (!customModalDefaults) customModalDefaults = {};
        customModalDefaults.backdrop = 'static';
        return show(customModalDefaults, customModalOptions);
    };
    
    function show(customModalDefaults, customModalOptions) {
        //Create temp objects to work with since we're in a singleton service
        var tempModalDefaults = {};
        var tempModalOptions = {};

        //Map angular-ui modal custom defaults to modal defaults defined in service
        angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

        //Map modal.html $scope custom properties to defaults defined in service
        angular.extend(tempModalOptions, modalOptions, customModalOptions);

        if (!tempModalDefaults.controller) {
            tempModalDefaults.controller = function ($scope, $modalInstance) {
                $scope.modalOptions = tempModalOptions;
                $scope.modalOptions.ok = function (result) {
                    $modalInstance.close();
                };
                $scope.modalOptions.close = function (result) {
                    $modalInstance.dismiss();
                };
            }
        }

        return $uibModal.open(tempModalDefaults).result;
    };
	
	return {
		
		success : function(msg) {
			open('success','Success', msg);
		},
		error : function(msg) {
			open('danger','Error', msg);
		},
		warning : function(msg) {
			open('warning','Warning', msg);
		},
		confirm : function(defaults, options) {
			return show(defaults, options);
		}
	};
	
});