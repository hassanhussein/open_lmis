/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function SiteController($scope,siteBy,geoLevels, $location, SiteService) {
if(!isUndefined(siteBy)) {

var site = {};
site.geographicZoneId = siteBy.geographicZone.id;
site.region= siteBy.geographicZone.name;
site.name = siteBy.name;
site.code = siteBy.code;
site.id = siteBy.id;
site.active = siteBy.active;
site.longitude = siteBy.longitude;
site.latitude = siteBy.latitude;
$scope.site = site;

}
  console.log($scope.site);


  $scope.$parent.message = "";
  $scope.geoLevels = geoLevels;

  $scope.cancel = function () {
    $scope.$parent.location = undefined;
    $scope.$parent.message = "";
    $location.path('#/search');
  };

  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;
    console.log(data);
    ///$scope.$parent.locationId = data.site.id;
    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
    $scope.error = data.data.error;
    $scope.showError = true;
  };

  $scope.save = function () {
  console.log($scope.site);

    if ($scope.locationForm.$error.pattern || $scope.locationForm.$error.required) {
      $scope.showError = true;
      $scope.error = 'form.error';
      $scope.message = "";
      return;
    }
   if ($scope.site.id) {
      SiteService.update({id: $scope.site.id}, $scope.site, success, error);
    }
    else {
      SiteService.save({}, $scope.site, success, error);
    }
  };
}

SiteController.resolve = {
  locationTypes: function ($q, $timeout, LocationTypes) {
    var deferred = $q.defer();
    $timeout(function () {
      LocationTypes.get({}, function (data) {
        deferred.resolve(data.locationTypeList);
      }, {});
    }, 100);
    return deferred.promise;
  },

  siteBy: function ($q, $route, $timeout, GetSiteBy) {
    if ($route.current.params.id === undefined) return undefined;

    var deferred = $q.defer();
    var id = $route.current.params.id;

    $timeout(function () {
      GetSiteBy.get({id: id}, function (data) {
        deferred.resolve(data.sites);
      }, {});
    }, 100);
    return deferred.promise;
  } ,

  geoLevels: function ($q, $route, $timeout, GetGeoZoneByLevels) {

    var deferred = $q.defer();

    $timeout(function () {
      GetGeoZoneByLevels.get({geoLevelCode: "reg"}, function (data) {
      console.log(data);

        deferred.resolve(data.geographicZoneList);
      }, {});
    }, 100);
    return deferred.promise;
  }
};
