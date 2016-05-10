(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HelloDeleteController',HelloDeleteController);

    HelloDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hello'];

    function HelloDeleteController($uibModalInstance, entity, Hello) {
        var vm = this;
        vm.hello = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Hello.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
