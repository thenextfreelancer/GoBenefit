angular.module('myApp').service('Constants', function Contants() {
	return {
		appBaseUrl: '',
    	APP_ROLES: {
	    	ADMIN: 'RWA_ADMIN',
	    	OWNER: 'OWNER',
	    	SERVICE_PROVIDER: 'SERVICE_PROVIDER'
	    },
	    USER_STATUS: {
	    	ACTIVE:'ACTIVE',
	    	PENDING:'PENDING'
	    },
	    ADMIN_SERVICE_LIST: ['/dashboard', '/dashboard/towers', '/dashboard/clubs', '/dashboard/services','/dashboard/users', '/dashboard/servicerqst', '/dashboard/bookings'],
	    USER_SERVICE_LIST: ['/usrdashboard', '/usrdashboard/servicerqst', '/usrdashboard/bookings']
    };
});
