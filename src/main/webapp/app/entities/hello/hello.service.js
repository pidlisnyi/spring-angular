(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Hello', Hello);

    Hello.$inject = ['$resource'];

    function Hello ($resource) {
        var resourceUrl =  'api/hellos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
