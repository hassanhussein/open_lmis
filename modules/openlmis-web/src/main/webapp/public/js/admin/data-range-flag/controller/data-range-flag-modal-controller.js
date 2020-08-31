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

function DataRangeConfigurationModalInstanceController($scope, DataRangeConfigurations, messageService, $modalInstance, items) {
    $scope.isAddingNew = items.isAddingNew;
    $scope.menu = {};
    if ($scope.isAddingNew !== true) {
        configureForEdit();
    }
    $scope.menuList = items.menuList;

    $scope.isPage = false;
    var callback = items.callback;


    function configureForEdit() {
        $scope.menu = items.menu;

    }


    $scope.save = function (menuItem) {

        var createSuccessCallback = function (data) {
            $modalInstance.close();
            callback(data);
        };

        var errorCallback = function (data) {
            $scope.showError = true;
            $scope.errorMessage = messageService.get(data.data.error);
        };

        $scope.error = "";
        var range= "["+Number(menuItem.minvalue).toFixed(2) + "," +Number(menuItem.maxvalue).toFixed(2) +"]";
        menuItem.range=range;
        DataRangeConfigurations.save(menuItem, createSuccessCallback, errorCallback);


    };
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}
