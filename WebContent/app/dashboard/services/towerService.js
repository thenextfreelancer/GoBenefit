angular
	.module('myApp').service('towerService', function towerService($http, Constants) {
		
		return {
    		getTowers: function (){
        		return $http.get(Constants.appBaseUrl+'/api/towers');
    		},
    		getTowersByAptId: function (aptId){
        		return $http.get(Constants.appBaseUrl+'/api/towers?apartmentId='+aptId);
    		},
    		addTower: function (tower){
    			var json = JSON.stringify(tower);
        		return $http.post(Constants.appBaseUrl+'/api/towers', json);
    		},
    		updateTower: function (tower){
    			var json = JSON.stringify({Tower: tower});
    			return $http.put(Constants.appBaseUrl+'/api/towers/'+tower.id, json);
    		}
    	};
});