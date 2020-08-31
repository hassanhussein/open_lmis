function ConfigureDataRangeFlagController($scope, $location, $route, DataRangeConfigurations, $modal) {
    $scope.searchMode = "contains";
    loadMenuList();
    $scope.searchModeOptions = {
        bindingOptions: {
            value: "searchMode"
        },
        items: ["contains", "startswith"]
    };
    $scope.clear = function () {
        $scope.selectedNode = undefined;
    };
    $scope.openAddEditDataRangeConfigItemDialog = function (isAddingNew,type) {

        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'data-range-configuration.html',
            controller: 'DataRangeConfigurationModalInstanceController',
            size: 'lg',
            scope: $scope,
            windowClass: 'my-modal-popup',
            resolve: {
                items: function () {
                    return {
                        isAddingNew: isAddingNew,
                        menuList: $scope.flatMenuList,
                        menu:type,
                        callback: saveCallBack
                    };
                }
            }
        });
    };
    $scope.closeModal = function () {

        $scope.regimenProductCombintionModal = false;
    };
    function loadMenuList() {
        DataRangeConfigurations.get(function (data) {
            $scope.flatMenuList = data.dataRangeConfigurations;
        });

    }

    var saveCallBack = function (data) {

        loadMenuList();
        $route.reload();
        $scope.message = data.success;

    };
    /*

     */

    $scope.hexPicker = {
        color: ''
    };

    $scope.rgbPicker = {
        color: ''
    };

    $scope.rgbaPicker = {
        color: ''
    };

    $scope.nonInput = {
        color: ''
    };

    $scope.resetColor = function() {
        $scope.hexPicker = {
            color: '#ff0000'
        };
    };

    $scope.resetRBGColor = function() {
        $scope.rgbPicker = {
            color: 'rgb(255,255,255)'
        };
    };

    $scope.resetRBGAColor = function() {
        $scope.rgbaPicker = {
            color: 'rgb(255,255,255, 0.25)'
        };
    };

    $scope.resetNonInputColor = function() {
        $scope.nonInput = {
            color: '#ffffff'
        };
    };

}
