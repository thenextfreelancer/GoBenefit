angular
	.module('myApp', ['dndLists','ui.bootstrap','ui.router', 'ui.router.stateHelper','ngCookies','xeditable','cgBusy'])
	.config(function($stateProvider, $urlRouterProvider){
		
		$urlRouterProvider.otherwise('/');
		$stateProvider
		.state('home', {
			url: '/',
            views: {
                'header': {
                    templateUrl: 'app/header/view/publicView.html'
                },
                'content': {
                	name: 'search',
        			url: '/search',
                    templateUrl: 'app/home/home.view.html',
                    controller: 'HomeController',
                    controllerAs: 'vmh',
                }
            }
        })
		.state('home.about', {
			name: 'about',
			url: 'about',
			controller: 'AboutController',
			templateUrl: 'app/about/view.html'
		})
		.state('home.contact', {
			name: 'contact',
			url: 'contact',
			controller: 'ContactController',
			templateUrl: 'app/contact/view.html'
		})
		.state('home.services', {
			name: 'services',
			url: 'services',
			controller: 'ServicesController',
			templateUrl: 'app/services/view.html',
			controllerAs: 'svc',
			params: { 
		        apartment: null
			}
		})
		.state('dashboard', {
			name: 'dashboard',
			url: '/dashboard',
			templateUrl: 'app/dashboard/pages/dashboard-admin.html',
			resolve: { authenticate: authenticate },
			params: { 
		        apartment: null,
		        service: null
			}
		})
		.state('dashboard.towers', {
			name: 'towers',
			url: '/towers',
			controller: 'TowerController',
			controllerAs: 'vm',
			templateUrl: 'app/dashboard/pages/tower.html',
			resolve: { authenticate: authenticate }
		})
		.state('dashboard.clubs', {
			name: 'clubs',
			url: '/clubs',
			controller: 'ClubController',
			controllerAs: 'vm',
			templateUrl: 'app/dashboard/pages/club.html',
			resolve: { authenticate: authenticate }
		})
		.state('dashboard.services', {
			name: 'services',
			url: '/services',
			templateUrl: 'app/dashboard/pages/services-drag-drop.html',
			controller: 'ServiceSelector',
			controllerAs: 'vm',
			resolve: { authenticate: authenticate }
		})
		.state('dashboard.users', {
			name: 'users',
			url: '/users',
			controller: 'UsersController',
			controllerAs: 'vm',
			templateUrl: 'app/dashboard/pages/users.html',
			resolve: { authenticate: authenticate }
		})
		.state('dashboard.servicerequest', {
			name: 'servicerequest',
			url: '/servicerequest',
			templateUrl: 'app/dashboard/pages/serviceRequestListAdmin.html',
			controller: 'ServiceRequestListAdminController',
			controllerAs: 'vm',
			resolve: { authenticate: authenticate }
		})
		.state('dashboard.bookings', {
			name: 'bookingrequest',
			url: '/bookingrequest',
			templateUrl: 'app/dashboard/pages/bookingRequestListAdmin.html',
			controller: 'BookingRequestListAdminController',
			controllerAs: 'vm',
			resolve: { authenticate: authenticate }
		})
		.state('userdashboard', {
			name: 'userdashboard',
			url: '/userdashboard',
			templateUrl: 'app/dashboard/pages/dashboard-user.html',
			resolve: { authenticate: authenticate },
			params: { 
		        apartment: null,
		        service: null
			}
		})
		.state('userdashboard.servicerequest', {
			name: 'servicerqst',
			url: '/servicerequest',
			controller: 'UserServiceRequestController',
			controllerAs: 'vm',
			templateUrl: 'app/dashboard/pages/user-service-rqst.html',
			resolve: { authenticate: authenticate }
		})
		.state('userdashboard.bookings', {
			name: 'bookings',
			url: '/bookings',
			controller: 'UserBookingRequestController',
			controllerAs: 'vm',
			templateUrl: 'app/dashboard/pages/user-booking-rqst.html',
			resolve: { authenticate: authenticate }
		})
		.state('404', {
			name: '404',
			url: '/404.html',
			templateUrl: 'app/login/404.html'
		})
		.state('selectappt', {
			name: 'selectappt',
			url: '/',
			controller: 'SelectApptController',
			template: '<div></div>',
			params: { 
		        apartments: null
			}
		});
		
		function authenticate($q, $rootScope, $state, $timeout, $location, Constants) {
			//user.isAuthenticated()
			var hasNoAccess = false;
			
		    if ($rootScope.globals.authenticationInfo) {
		    	var location = $location.path();
	    		var restrictedPage = $.inArray(location, Constants.ADMIN_SERVICE_LIST) !== -1 && location !== '/404';
	            if(restrictedPage)
	                hasNoAccess = !($rootScope.globals.currentRole === Constants.APP_ROLES.ADMIN);
		    } else 
		    	hasNoAccess = true;
		    
		    
		    if(hasNoAccess){
		    	// The next bit of code is asynchronously tricky.
		        $timeout(function() {
		          // This code runs after the authentication promise has been rejected.
		          // Go to the log-in page
		          $state.go('404');
		        })

		        // Reject the authentication promise to prevent the state from loading
		        return $q.reject();
		    }
		    return $q.when();
		 }
	}).run(function($rootScope, $location, $cookieStore, $http, $state, Constants){
		// keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.authenticationInfo) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $rootScope.globals.authenticationInfo.token; // jshint ignore:line
        }
        
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
        	 if ($rootScope.globals.authenticationInfo) {
 		    	var location = $location.path();
 		    	if(location === '/'){
 		    		if($rootScope.globals.currentRole === Constants.APP_ROLES.ADMIN)
 		    			$location.path('/dashboard');//$state.go('dashboard');
 		    		else 
 		    			$location.path('/userdashboard');//$state.go('userdashboard');
 		    	} 
        	 }
        	 
        	 if($rootScope.loginModal)
	            	$rootScope.loginModal.close('close');
	            if($rootScope.register)
	            	$rootScope.register.close('close');
        });
        
	});