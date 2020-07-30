/*
 *
 *  * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *  *
 *  * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *  *
 *  * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

function MetabaseIntegrationModalInstanceController($scope, MetabaseMenus, MetabasePages, messageService, $modalInstance, items) {
    $scope.isAddingNew = items.isAddingNew;
    $scope.menu = {};
    if ($scope.isAddingNew === true) {
        configureForNew();
    }
    else configureForEdit();
    $scope.menuList = items.menuList;
    $scope.flatMenuList = items.flatMenuList;
    $scope.isPage = false;
    var callback = items.callback;


    function configureForEdit() {
        var findMenuById = function (id) {
            return _.findWhere(items.menuList, {id: id});
        };
        $scope.menu = items.parentMenu;
        $scope.isPage = !$scope.menu.menuItem;
        $scope.menu.isPage = $scope.isPage;
        $scope.selectedItem = $scope.menu.menuItem === false ? $scope.menu.menu : findMenuById($scope.menu.parentMenu);
    }

    function configureForNew() {

        $scope.selectedItem = items.parentMenu;
    }

    $scope.save = function (menuItem, selectedItem) {
        $scope.selectedItem = selectedItem;

        var obj = menuItem.isPage === true ? {
            id: menuItem.id,
            name: menuItem.name,
            menu: $scope.selectedItem,
            rights: menuItem.rights,
            linkUrl: menuItem.linkUrl,
            description: menuItem.description
        } :
            {
                id: menuItem.id,
                name: menuItem.name,
                parentMenu: utils.isNullOrUndefined($scope.selectedItem) ? null : $scope.selectedItem.id,
                description: menuItem.description
            };
        var createSuccessCallback = function (data) {
            $modalInstance.close();
            callback();
        };

        var errorCallback = function (data) {
            $scope.showError = true;
            $scope.errorMessage = messageService.get(data.data.error);
        };

        $scope.error = "";

        if (menuItem.isPage)
            MetabasePages.save(obj, createSuccessCallback, errorCallback);
        else
            MetabaseMenus.save(obj, createSuccessCallback, errorCallback);


    };
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}
