/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */


function StockOnHandController($scope, $window, $location, WareHouseList, GetSohReport) {

    WareHouseList.get({}, function (data) {
        $scope.warehouses = data.house;
//console.log($scope.warehouses)
    });


    $scope.getSoh = function () {


        $wareHouseId = $scope.warehouseId;
        $scope.warehouseidvalue = $wareHouseId;

        if (!$wareHouseId) {
            $storedWareHouse = sessionStorage.getItem("warehouseId");
            if ($storedWareHouse) {
                $wareHouseId = $storedWareHouse;
                $scope.warehouseId=$wareHouseId;
                $scope.disableBtn = false;
            } else {
                $scope.disableBtn = true;
            }
        } else {
            $scope.disableBtn = false;
        }


        console.log("WareHouse: " + $wareHouseId);

        GetSohReport.get({facilityId: 19075, warehouseId: $wareHouseId}, function (data) {

            console.log(data.soh);
            $scope.soh = data.soh;
            $scope.products = _.groupBy($scope.soh, 'product');


        });

    };

    $scope.printStockOnHand = function (data) {


        $wareHouseId = $scope.warehouseId;
        $window.open('/wms-reports/stockonhand-report?docType=pdf&wareHouseId=' + $wareHouseId, '_blank');

        console.log($wareHouseId + "console");
    };


    $scope.openLedger = function (product) {
    console.log(product.product);
        $wareHouseId = $scope.warehouseId;

        sessionStorage.setItem("warehouseId", $wareHouseId);

        $location.path('stock-ledger/' + $scope.warehouseId + '/' + product.productId + '/' + new Date().getFullYear()) + '/' + product.product;

    };


    $scope.calculateTotalQuantity = function (productId) {
        console.log($scope.warehouseId);
        console.log(productId);
        var sum = 0;
        var products = _.where($scope.soh, {productId: productId});

        products.forEach(function (lot) {

            sum += lot.quantityOnHand;
        });
        return sum;

    };

}

