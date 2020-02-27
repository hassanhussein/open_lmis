/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function WarehouseController($scope, siteList,warehouseBy,GetWarehouseService, $location, GetAllLocationsById) {
  $scope.types = siteList;
  $scope.$parent.message = "";

  if(!isUndefined(warehouseBy)) {
   warehouseBy.siteId = warehouseBy.site.id;
   $scope.warehouse = warehouseBy;
   loadLocations($scope.warehouse.id);
     //console.log(warehouseBy);
  }

  function loadLocations(params) {

  GetAllLocationsById.get({'id':parseInt(params,10)}, function(data){

  $scope.locationList = data.house;

  console.log(data);

  });

  }

  $scope.cancel = function () {
    $scope.$parent.location = undefined;
    $scope.$parent.message = "";
    $location.path('#/search');
  };

  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;
    //$scope.$parent.locationId = data.location.id;
    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
    $scope.error = data.data.error;
    $scope.showError = true;
  };

  $scope.save = function () {

    if ($scope.locationForm.$error.pattern || $scope.locationForm.$error.required) {
      $scope.showError = true; $scope.error = 'form.error'; $scope.message = "";
      return;
    }



    if ($scope.warehouse.id) {
      GetWarehouseService.update({id: $scope.warehouse.id}, $scope.warehouse, success, error);
    }
    else {
          console.log('reached here');
      GetWarehouseService.save({}, $scope.warehouse, success, error);
    }
  };
}

WarehouseController.resolve = {
  siteList: function ($q, $timeout, GetSiteList) {
    var deferred = $q.defer();
    $timeout(function () {
      GetSiteList.get({}, function (data) {
        deferred.resolve(data.sites);
      }, {});
    }, 100);
    return deferred.promise;
  },

  warehouseBy: function ($q, $route, $timeout, GetWarehouseBy) {
    if ($route.current.params.id === undefined) return undefined;

    var deferred = $q.defer();
    var id = $route.current.params.id;

    $timeout(function () {
      GetWarehouseBy.get({id: id}, function (data) {
      console.log(data);
        deferred.resolve(data.house);
      }, {});
    }, 100);
    return deferred.promise;
  }
};
