angular
.module('myApp').directive('formElement', function() {
    return {
        restrict: 'E',
        transclude: true,
        scope: {
            label : "@",
            model : "="
        },
        link: function(scope, element, attrs) {
            scope.disabled = attrs.hasOwnProperty('disabled');
            scope.required = attrs.hasOwnProperty('required');
            scope.pattern = attrs.pattern || '.*';
        },
        template: '<div class="form-group"><label class="col-sm-3 control-label no-padding-right" >  {{label}}</label><div class="col-sm-7"><span class="block input-icon input-icon-right" ng-transclude></span></div></div>'
      };
        
});

angular
.module('myApp').directive('onlyNumbers', function() {
    return function(scope, element, attrs) {
        var keyCode = [8,9,13,37,39,46,48,49,50,51,52,53,54,55,56,57,96,97,98,99,100,101,102,103,104,105,110,190];
        element.bind("keydown", function(event) {
            if($.inArray(event.which,keyCode) == -1) {
                scope.$apply(function(){
                    scope.$eval(attrs.onlyNum);
                    event.preventDefault();
                });
                event.preventDefault();
            }

        });
    };
});

angular
.module('myApp').directive('focus', function() {
    return function(scope, element) {
        element[0].focus();
    }      
});

angular
.module('myApp').directive('myAlert', function($timeout){
	  return {
	    scope: {
	      message: '@',
	      isVisible: '='
	    },
	    link: link,
	    restrict: 'E',
	    replace: true,
	    template: '<div ng-if="isVisible" class="alert">{{message}}</div>'
	  }

	  function link(scope, element, attrs){
	    scope.isVisible = true;

	    $timeout(function (){
	            scope.isVisible = false;
	           }, 5000);

	  }
	});

angular
.module('myApp').directive('animateOnChange', function($animate) {
  return function(scope, elem, attr) {
      scope.$watch(attr.animateOnChange, function(nv,ov) {
        if (nv!=ov) {
              var c = 'change-up';
              $animate.addClass(elem,c, function() {
              $animate.removeClass(elem,c);
          });
        }
      });  
  }  
});

angular
.module('myApp').directive("mydatepicker", function(){
	  return {
	    restrict: "E",
	    scope:{
	      ngModel: "=",
	      dateOptions: "=",
	      opened: "=",
	    },
	    link: function($scope, element, attrs) {
	      $scope.open = function(event){
	        event.preventDefault();
	        event.stopPropagation();
	        $scope.opened = true;
	      };

	      $scope.clear = function () {
	        $scope.ngModel = null;
	      };
	    },
	    templateUrl: '/common/directives/templates/datepicker.html'
	  }
	});

angular
.module('myApp').directive("dashboardHeader", function(){
	  return {
	    restrict: "E",
	    templateUrl: '/app/dashboard/pages/dashboardHeader.html',
	    controller: 'DashboardHeaderController',
	    controllerAs:'vm'
	  }
	});

angular
.module('myApp').directive("userDashboardView", function(){
	  return {
	    restrict: "E",
	    templateUrl: '/app/dashboard/pages/dashboardView.html',
	    controller: 'AdminDashboardViewController',
	    controllerAs:'vm'
	  }
	});

angular
.module('myApp').directive("adminDashboardView", function(){
	  return {
	    restrict: "E",
	    templateUrl: '/app/dashboard/pages/dashboardView.html',
	    controller: 'UserDashboardViewController',
	    controllerAs:'vm'
	  }
	});