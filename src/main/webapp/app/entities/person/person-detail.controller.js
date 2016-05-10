(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Person'];

    function PersonDetailController($scope, $rootScope, $stateParams, entity, Person) {
        var vm = this;
        vm.person = entity;
        
        var unsubscribe = $rootScope.$on('jhipsterApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
