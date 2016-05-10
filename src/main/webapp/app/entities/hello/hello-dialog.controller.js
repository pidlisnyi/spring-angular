(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HelloDialogController', HelloDialogController);

    HelloDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hello'];

    function HelloDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hello) {
        var vm = this;
        vm.hello = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('jhipsterApp:helloUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.hello.id !== null) {
                Hello.update(vm.hello, onSaveSuccess, onSaveError);
            } else {
                Hello.save(vm.hello, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
