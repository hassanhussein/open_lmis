/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function SiteController($scope, GeographicZonesAboveLevel,geoZone,Sites,$location) {
$scope.regions=_.where(geoZone,{"levelId":3});
  $scope.$parent.message = "";



  $scope.cancel = function () {
    $scope.$parent.location = undefined;
    $scope.$parent.message = "";
    $location.path('#/search');
  };

  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;
    $scope.$parent.siteId = data;
    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
    $scope.error = data.data.error;
    $scope.showError = true;
  };

  $scope.save = function () {
    if ($scope.siteForm.$error.pattern || $scope.siteForm.$error.required) {
    console.log($scope.siteForm)
    console.log($scope.siteForm.name.$error.required)
      $scope.showError = true;
      $scope.error = 'form.error';
      $scope.message = "";
      return;
    }
    $scope.site={
    regionId:$scope.regionId,
    code:$scope.code,
    name:$scope.name,
    latitude:$scope.latitude,
    longitude:$scope.longitude,
    region: {
    catchmentPopulation: 0,
    code: "string",
    id: 0,
    latitude: 0,
    level: {
    code: "string",
    id: 0,
    levelNumber: 0,
    name: "string"
    },
    longitude: 0,
    name: "string",
    parent: {}
    },
    };
    if ($scope.site.id) {
      Sites.update({id: $scope.site.id}, $scope.site, success, error);
    }
    else {
    console.log($scope.site)
      Sites.save({}, $scope.site, success, error);
    }
  };
}

SiteController.resolve = {
geoZone: function ($q, $timeout, FlatGeographicZoneList) {
    var deferred = $q.defer();
    $timeout(function () {
      FlatGeographicZoneList.get({}, function (data) {
        deferred.resolve(data.zones);
      }, {});
    }, 100);
    return deferred.promise;
  },
};
