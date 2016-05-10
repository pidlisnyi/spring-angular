(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HelloDetailController', HelloDetailController);

    HelloDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Hello'];

    function HelloDetailController($scope, $rootScope, $stateParams, entity, Hello) {
        var vm = this;
        vm.hello = entity;
        
        var unsubscribe = $rootScope.$on('jhipsterApp:helloUpdate', function(event, result) {
            vm.hello = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
