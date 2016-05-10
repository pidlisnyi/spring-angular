(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('PersonDeleteController',PersonDeleteController);

    PersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Person'];

    function PersonDeleteController($uibModalInstance, entity, Person) {
        var vm = this;
        vm.person = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Person.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
