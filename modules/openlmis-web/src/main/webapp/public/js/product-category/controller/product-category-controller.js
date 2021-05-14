function productCategoryController($scope, $location, $route, ProductCategories, $modal) {
    $scope.searchMode = "contains";
    loadCategoryList();
    $scope.searchModeOptions = {
        bindingOptions: {
            value: "searchMode"
        },
        items: ["contains", "startswith"]
    };
    $scope.clear = function () {
        $scope.selectedNode = undefined;
    };
    $scope.openAddEditProductCategoryConfigItemDialog = function (isAddingNew,category) {

        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'product-category-configuration.html',
            controller: 'ProductCategoryConfigurationModalInstanceController',
            size: 'lg',
            scope: $scope,
            windowClass: 'my-modal-popup',
            resolve: {
                items: function () {
                    return {
                        isAddingNew: isAddingNew,
                        categoryList: $scope.productCategoryList,
                        category:category,
                        callback: saveCallBack
                    };
                }
            }
        });
    };
    $scope.closeModal = function () {

        $scope.regimenProductCombintionModal = false;
    };
    function loadCategoryList() {
        ProductCategories.get(function (data) {
            $scope.productCategoryList = data.productCategories;
        });

    }

    var saveCallBack = function (data) {

        loadCategoryList();
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
