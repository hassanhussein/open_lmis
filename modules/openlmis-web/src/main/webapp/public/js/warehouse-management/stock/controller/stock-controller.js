/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function StockController($scope,$timeout, TransferRecords,reasonsForAdjustments, $location, GetWarehouseLocations,vaccineProducts,ProductLots) {
                    console.log(vaccineProducts);


$scope.stockMovement = {};
$scope.stockMovement.products = [];

$scope.stockMovement.products = vaccineProducts;
$scope.reasons = reasonsForAdjustments;

 console.log(reasonsForAdjustments);

 GetWarehouseLocations.get(function(data) {

  $scope.warehouseList = data.binLocations;
  $scope.stockMovement.warehouseList = data.binLocations;

 });


$scope.loadBinLocation = function(wareHouseId, warehouseList) {
var binLocations = [];
binLocations  = _.where(warehouseList, {id:wareHouseId});
$scope.stockMovement.locations = binLocations[0].locations;
console.log($scope.stockMovement.locations);

};

$scope.loadProductLots = function(product) {

            $scope.lotsToDisplay = {};

            if (product !== null) {

//        console.log(product)
                ProductLots.get({
                    productId: product
                }, function(data) {
                    $scope.allLots = data.lots;
                                console.log(data.lots);
                    //                              console.log(data.lots)
                    $scope.lotsToDisplay = _.sortBy($scope.allLots,'lotCode');


                });


            }
        };

$scope.showSOH = function(data){



};


$scope.submit = function (stockMovement) {
console.log($scope.movementForm);

   if ($scope.movementForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
//            console.log($scope.asnForm.$error)
            return;
        }


stockMovement.notify = true;
TransferRecords.save({}, stockMovement, function(data) {
console.log(data);
$scope.showMessage = true;
$scope.message = "Successiful Saved";

$timeout( function(){
 $scope.showMessage = false;
 $scope.showError = false;
 $scope.error = false;
 stockMovement.lotId = null;
 stockMovement.productId = null;
 stockMovement.soh= null;
 stockMovement.fromWarehouseId = null;
 stockMovement.toWarehouseId = null;
 stockMovement.fromBin = null;
 stockMovement.toBin= null;
 stockMovement.reason = null;
 stockMovement.quantity = null;
 }, 1000);


});

};


}


StockController.resolve =  {


    vaccineProducts: function ($q, $timeout, VaccineProgramProducts, $routeParams,$rootScope) {
        var deferred = $q.defer();
        $timeout(function () {

                VaccineProgramProducts.get({programId: 82},

                    function (data) {

                        if (!isUndefined(data.programProductList) || data.programProductList.length > 0)
                            deferred.resolve(data.programProductList);
                        });



        }, 100);

        return deferred.promise;
    },

    reasonsForAdjustments: function ($q, $timeout, GetLossAndAdjustments, $routeParams) {
        var deferred = $q.defer();
        $timeout(function () {

                GetLossAndAdjustments.get({},

                    function (data) {

                        if (!isUndefined(data.lossAdjustmentTypes) || data.lossAdjustmentTypes.length > 0)
                            deferred.resolve(data.lossAdjustmentTypes);
                        });



        }, 100);

        return deferred.promise;
    }

};

