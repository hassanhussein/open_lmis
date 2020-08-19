/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */


function StockController($scope, $timeout, WmsAdjustment, vvmList,TransferRecords, reasonsForAdjustments, $location, GetWarehouseLocationsByStorageQuarantineWithPermission,GetWarehouseLocationsByStorageQuarantine, vaccineProducts, ProductLots, GetStockProducts, GetTransferDetails) {


$scope.vvmList=vvmList;
    $scope.adjustmentReasons = [
        {
            id: 1,
            reason: 'Inventory in',
            type: 'CREDIT'
        },
        {
            id: 2,
            reason: 'Inventory out',
            type: 'DEBIT'
        },

        {
            id: 3,
            reason: 'Breakage',
            type: 'DEBIT'
        },

        {
            id: 4,
            reason: 'Label Disappeared',
            type: 'DEBIT'
        },

        {
            id: 5,
            reason: 'Frozen',
            type: 'DEBIT'
        },

        {
            id: 6,
            reason: 'Damaged',
            type: 'DEBIT'
        },

                 {
                     id: 7,
                     reason: 'VVM Change',
                     type: 'DEBIT'
                 },


    ];

    $scope.stockMovement = {};
    $scope.stockMovement.products = [];

    $scope.stockMovement.products = vaccineProducts;
    $scope.reasons = reasonsForAdjustments;


    GetWarehouseLocationsByStorageQuarantineWithPermission.get({}, function (data) {

        console.log(data);
        $scope.warehouseList = data.binLocations;
        $scope.stockMovement.warehouseList = data.binLocations;

    });


    $scope.loadBinLocation = function (wareHouseId, warehouseList) {
        var binLocations = [];
        binLocations = _.where(warehouseList, {id: wareHouseId});
        $scope.stockMovement.locations = binLocations[0].locations;

        loadToBinLocation($scope.stockMovement.locations);

    };

    var loadToBinLocation = function (toBinList) {

        $scope.stockMovement.locations2 = [];

        var binLocations = [];
        binLocations = _.filter(toBinList, function (data) {
            return parseInt(data.id, 10) != parseInt($scope.selectedBin, 10);
        });


        console.log(binLocations);

        $scope.stockMovement.locations2 = binLocations;

    };

    $scope.loadProductLotDetails = function (movement) {

        $scope.selectedBin = movement.fromBin;
        $scope.productToDisplay = [];
        $scope.lotsToDisplay = [];
        $scope.stockMovement.soh = undefined;

        GetTransferDetails.get({
            fromWarehouseId: movement.fromWarehouseId,
            fromBinLocationId: movement.fromBin
        }, function (data) {

            $scope.productToDisplay = data.products;
            console.log($scope.productToDisplay);

        });

    };


    $scope.loadProductLots = function (productId) {

        $scope.lotsToDisplay = [];

        if ($scope.productToDisplay.length > 0) {

            $scope.lotsToDisplay = _.where($scope.productToDisplay, {"productId": productId});

        }


    };

    $scope.showSOH = function (lotId) {

        $scope.stockMovement.soh = undefined;

        if ($scope.lotsToDisplay.length > 0 && !isUndefined($scope.productToDisplay) && !isUndefined(lotId)) {
            //quantityOnHand

            $scope.stockMovement.soh = _.where($scope.productToDisplay, {"lotId": lotId})[0].quantityOnHand;
        }

    };

$scope.quantityChanged=function(){
    $scope.quantityError=false;
   $scope.zeroQuantityError=false;

    if($scope.stockMovement.reason>1 && parseInt($scope.stockMovement.quantity,10)>$scope.stockMovement.soh){
        $scope.quantityError=true;
    }


     if(parseInt($scope.stockMovement.quantity,10)==0){
            $scope.zeroQuantityError=true;
     }


}

$scope.reasonChanged=function(){
$scope.disableQuantity=false;
$scope.stockMovement.quantity="";
    $scope.quantityError=false;
        $scope.zeroQuantityError=false;


    if($scope.stockMovement.reason==8){
//    check actual expiry of batch
            $scope.stockMovement.quantity=$scope.stockMovement.soh;
            $scope.disableQuantity=true;
        }




}
    $scope.showSOHa = function (lotId) {

        $scope.stockMovement.soh = undefined;
        $scope.stockMovement.vvmId = undefined;

        if ($scope.lotsToDisplay.length > 0 && !isUndefined($scope.productToDisplay) && !isUndefined(lotId)) {
            //quantityOnHand
            var queryResults = _.where($scope.productToDisplay, {"lotNumberUn": lotId})[0];
            $scope.stockMovement.vvmId = queryResults.vvmId;
            $scope.stockMovement.soh = queryResults.quantityOnHand;
            $scope.stockMovement.lotId = queryResults.lotId;
            $scope.stockMovement.stockCardId = queryResults.stockCardId;


        }

    };

    $scope.validateQuantity = function (movement) {

        $scope.isGreater = false;
        if (parseInt(movement.quantity, 10) === 0) {
            $scope.errorMessage = 'The Quantity should be greater than ZERO';
            $scope.showError = true;
            $scope.isGreater = true;
            return;
        }
        if (movement.quantity > movement.soh) {
            $scope.errorMessage = 'The Quantity should be less than SOH ';
            $scope.showError = true;
            $scope.isGreater = true;
            return;
        } else {
            return $scope.isGreater;
        }

    };

    function getReason(id) {
        return _.findWhere($scope.adjustmentReasons, {id: id});
    }


    $scope.adjust = function (stockAdjust) {
        if ($scope.movementForm.$error.required||$scope.quantityError||$scope.zeroQuantityError) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
            return;
        }
        var reason = getReason($scope.stockMovement.reason);

        var reasonType = reason.type;
        var quantity = parseInt($scope.stockMovement.quantity, 10);

        if (reasonType === "DEBIT") {
            quantity = -quantity;
        }

        var adjust = {
            "lotId": $scope.stockMovement.lotId,
            "locationid": $scope.stockMovement.fromBin,
            "productId": $scope.stockMovement.productId,
            "quantity": quantity,
            "vvmId": $scope.stockMovement.vvmId,
            "stockCardId": $scope.stockMovement.stockCardId,
            "type": "ADJUSTMENT",
            "reason": reason.reason,
            'toWarehouseId':$scope.stockMovement.toWarehouseId,
            'toBinId':$scope.stockMovement.toBin,
        };

        console.log(adjust);

        WmsAdjustment.save({}, adjust, function () {
            $scope.showMessage = false;
            $scope.showError = false;
            $scope.error = false;
            $scope.stockMovement.lotOnHandId = null;
            $scope.stockMovement.productId = null;
            $scope.stockMovement.soh = null;
            $scope.stockMovement.fromWarehouseId = null;
            $scope.stockMovement.toWarehouseId = null;
            $scope.stockMovement.fromBin = null;
            $scope.stockMovement.toBin = null;
            $scope.stockMovement.reason = null;
            $scope.stockMovement.quantity = null;
            $scope.adjustmentSuccess = true;
            $timeout(function () {
                $scope.adjustmentSuccess = false;


            }, 3000);

        });


    };

    $scope.submit = function (stockMovement) {

        stockMovement.stockCardId = $scope.productToDisplay[0].stockCardId;
        stockMovement.lotOnHandId = $scope.productToDisplay[0].lotOnHandId;


        if ($scope.isGreater) {
            $scope.errorMessage = 'The Quantity is greater than SOH';
            return;
        }

        if ($scope.movementForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
            return;
        }

        if (stockMovement == "adjustment") {
            return;
        }


        stockMovement.notify = true;
        TransferRecords.save({}, stockMovement, function (data) {

            $scope.showMessage = true;
            $scope.message = "Successiful Saved";

            $timeout(function () {
                $scope.showMessage = false;
                $scope.showError = false;
                $scope.error = false;
                stockMovement.lotId = null;
                stockMovement.productId = null;
                stockMovement.soh = null;
                stockMovement.fromWarehouseId = null;
                stockMovement.toWarehouseId = null;
                stockMovement.fromBin = null;
                stockMovement.toBin = null;
                stockMovement.reason = null;
                stockMovement.quantity = null;
            }, 3000);


        });

    };


}


StockController.resolve = {


    vaccineProducts: function ($q, $timeout, VaccineProgramProducts, $routeParams, $rootScope) {
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
     vvmList: function ($q, $route, $timeout, GetVVMStatusList) {

            var deferred = $q.defer();

            $timeout(function () {
              GetVVMStatusList.get({}, function (data) {
               console.log(data.vvms);
                deferred.resolve(data.vvms);
              }, {});
            }, 100);
            return deferred.promise;
          },

    reasonsForAdjustments: function ($q, $timeout, GetTransferReasons, $routeParams) {
        var deferred = $q.defer();
        $timeout(function () {

            GetTransferReasons.get({},

                function (data) {
                    console.log(data.reasons);
                    if (!isUndefined(data.reasons) || data.reasons.length > 0)

                        deferred.resolve(data.reasons);
                });


        }, 100);

        return deferred.promise;
    }

};

