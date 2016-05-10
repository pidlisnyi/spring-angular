(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Book', Book);

    Book.$inject = ['$resource', 'DateUtils'];

    function Book ($resource, DateUtils) {
        var resourceUrl =  'api/books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.publicationDate = DateUtils.convertLocalDateFromServer(data.publicationDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.publicationDate = DateUtils.convertLocalDateToServer(data.publicationDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.publicationDate = DateUtils.convertLocalDateToServer(data.publicationDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
