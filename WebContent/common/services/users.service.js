(function () {
    'use strict';
 
   
    function UserService($http) {
        var service = {};
 
        service.GetAll = GetAll;
        service.GetById = GetById;
        service.GetByUsername = GetByUsername;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;
 
        return service;
 
        function GetAll() {
            return $http.get('/api/user/get').then(handleSuccess, handleError('Error getting all users'));
        }
 
        function GetById(id) {
            return $http.get('/api/user/get?id=' + id).then(handleSuccess, handleError('Error getting user by id'));
        }
 
        function GetByUsername(username) {
            return $http.get('/api/user/get?uname=' + username).then(handleSuccess, handleError('Error getting user by username'));
        }
 
        function Create(user) {
            return $http.post('/api/user/register', user).then(handleSuccess, handleError('Error creating user'));
        }
 
        function Update(user) {
            return $http.put('/api/user/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }
 
        function Delete(id) {
            return $http.delete('/api/user/delete?id=' + id).then(handleSuccess, handleError('Error deleting user'));
        }
 
        // private functions
 
        function handleSuccess(res) {
            return res.data;
        }
 
        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }
 
    UserService.$inject = ['$http'];
    
    angular
    .module('myApp')
    .factory('UserService', UserService);

    
    
})();