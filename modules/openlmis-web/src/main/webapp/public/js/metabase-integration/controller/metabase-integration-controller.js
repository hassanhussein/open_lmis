function ConfigureMetabaseIntegrationController($scope, $location, $route, MetabaseMenus, MetabaseFlatMenus, $modal) {
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
    $scope.openAddEditMetabaseItemDialog = function (isAddingNew) {

        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'metabase-integration.html',
            controller: 'MetabaseIntegrationModalInstanceController',
            size: 'lg',
            scope: $scope,
            windowClass: 'my-modal-popup',
            resolve: {
                items: function () {
                    return {
                        isAddingNew: isAddingNew,
                        menuList: $scope.menus,
                        flatMenuList: $scope.flatMenuList,
                        parentMenu: $scope.selectedNode,
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
        MetabaseFlatMenus.get(function (data) {
            $scope.flatMenuList = data.menus;
        });
        MetabaseMenus.get(function (data) {
            $scope.menus = data.menus;
            $scope.treeViewOptions = {
                bindingOptions: {
                    searchMode: "searchMode",
                },
                itemTemplate: function(item) {
                    var icon = item.menuItem ? 'folder-open' : 'file';
                    return "<div>" +
                        "<i class=\"icon-" + icon +"\"></i>" +
                        // "<i class=\"icon-folder-open\"></i>" +
                        "<span> "  + item.name + "</span></div>";
                },
                displayExpr: "name",
                keyExpr: "name+id",
                onItemClick: function (o) {
                    $scope.selectedNode = o.itemData;
                },
                reload: function (items) {

                },
                items: $scope.menus,
                width: 500,
                searchEnabled: true
            };
        });

    }

    var saveCallBack = function (data) {

        loadMenuList();
        $route.reload();
        $scope.message = data.success;

    };
}
ConfigureMetabaseIntegrationController.resolve = {
    regimenTree: function ($q, RegimenTree, $location, $route, $timeout) {
        var deferred = $q.defer();
        var id = 3;

        $timeout(function () {
            RegimenTree.get({programId: id}, function (data) {
                deferred.resolve(data.regimen_tree);
            }, function () {
                $location.path('select-program');
            });
        }, 100);

        return deferred.promise;
    }

};