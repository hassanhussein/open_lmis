function WarehouseManagementController($scope,GetSitesData,warehouse, $location) {


$scope.sites = [];

GetSitesData.get({"regionId":447}).then(function(data) {

$scope.sites  = data.sites;

});
warehouse.lineItems =  [
                           {
                                      "id": 11,
                                      "warehouseId": 3,
                                      "zone": {
                                          "id": 1,
                                          "code": "Test 1",
                                          "name": "Test 1",
                                          "description": null,

                                         "aisleList": [{"id":1,

                                          "aisleCode": "teST1",
                                                                               "binLocationFrom": "1000",
                                                                               "binLocationTo": "100",
                                                                               "beamLevelFrom": "100",
                                                                               "beamLevelTo": "100"
                                           },

                                           {"id":2,

                                            "aisleCode": "teST",
                                            "binLocationFrom": "10",
                                            "binLocationTo": "100",
                                            "beamLevelFrom": "1",
                                            "beamLevelTo": "10"
                                           }


                                         ]


                                      }

                                  }

                              ];

console.log(warehouse);
$scope.warehouse = warehouse;


$scope.productTypes = [{'id':1,'name':'Vaccines/Associated Products'}, {'id':1,'name':'Other Items'}];


  $scope.cancel = function () {
    $scope.$parent.geoZoneId = undefined;
    $scope.$parent.message = "";
    $location.path('#/search');
  };

  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;
    $scope.$parent.geoZoneId = data.geoZone.id;
    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
    $scope.error = data.data.error;
    $scope.showError = true;
  };

  $scope.save = function () {
    console.log($scope.warehouse.site);

    if ($scope.warehouseForm.$error.pattern || $scope.warehouseForm.$error.required || !$scope.warehouse.site.name || $scope.warehouse.productType.name === null) {
      $scope.showError = true;
      $scope.error = 'form.error';
      $scope.message = "";
      return;
    }

    $scope.warehouse.productTypeId = $scope.warehouse.productType.name;

  console.log($scope.warehouse);

    if (!$scope.parentLevels || $scope.parentLevels.length === 0) {
      $scope.geoZone.parent = undefined;
    }
    if ($scope.geoZone.id) {
      GeographicZones.update({id: $scope.geoZone.id}, $scope.geoZone, success, error);
    }
    else {
      GeographicZones.save({}, $scope.geoZone, success, error);
    }
  };


}

WarehouseManagementController.resolve = {

  warehouse: function ($q, $route, $timeout, Warehouses) {
    if ($route.current.params.id === undefined) return undefined;

    var deferred = $q.defer();
    var warehouseId = $route.current.params.id;

    $timeout(function () {
      Warehouses.get({id: warehouseId}, function (data) {

        deferred.resolve(data.house);
      }, {});
    }, 100);
    return deferred.promise;
  }
};