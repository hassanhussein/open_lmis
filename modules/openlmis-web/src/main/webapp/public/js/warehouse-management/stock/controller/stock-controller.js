/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function StockController($scope, $location, GetWarehouseLocations,vaccineProducts,ProductLots) {
                    console.log(vaccineProducts);


$scope.stockMovement = {};
$scope.stockMovement.products = [];

$scope.stockMovement.products = vaccineProducts;

 GetWarehouseLocations.get(function(data) {

  $scope.warehouseList = data.binLocations;
  $scope.stockMovement.warehouseList = data.binLocations;

 });

$scope.loadBinLocation = function(wareHouse) {

$scope.stockMovement.locations = wareHouse;
console.log($scope.stockMovement.locations);

}

$scope.loadProductLots = function(product) {

            $scope.lotsToDisplay = {};

            if (product !== null) {

//        console.log(product)
                ProductLots.get({
                    productId: product.product.id
                }, function(data) {
                    $scope.allLots = data.lots;
                                console.log(data.lots);
                    //                              console.log(data.lots)
                    $scope.lotsToDisplay = _.sortBy($scope.allLots,'lotCode');


                });


            }
        };

$scope.showSOH = function(data){


console.log(data);

}

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
    }

}

