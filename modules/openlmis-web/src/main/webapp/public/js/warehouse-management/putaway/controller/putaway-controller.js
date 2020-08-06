/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function PutawayController($scope, $location,GetInspectionById,putaway,GetWarehouseLocations,SavePutAwayDetails) {

 GetWarehouseLocations.get(function(data) {
               console.log(data.binLocations);
                $scope.warehouseList=data.binLocations;
                });
$scope.movementQueue=[];
$scope.lineItem=putaway.lineItems[0];



$scope.addToQueue=function(){





//return;
$scope.quantityError=false;


//validate if putaway quantity is less than passed quantity
if(parseInt($scope.toLot.passQuantity,10)>parseInt($scope.fromLot.passQuantity,10)){
$scope.quantityError=true;
 return;
}

$scope.movementQueue.push(
    {

    inspectionId:$scope.lineItem.inspectionId,
    productId:$scope.lineItem.product.id,
    lotNumber:$scope.fromLot.lotNumber,
    lotId:$scope.fromLot.id,
    quantity:$scope.toLot.passQuantity,
    fromWarehouse:$scope.fromLot.location.house.name,
    fromWareHouseId:$scope.fromLot.location.house.id,
    fromBinLocationId:$scope.fromLot.location.id,
    fromBinLocation:$scope.fromLot.location.code,
    toWarehouse:$scope.toLot.warehouse.name,
    toWareHouseId:$scope.toLot.warehouse.id,
    toBinLocation:$scope.toLot.binLocation.code,
    toBinLocationId:$scope.toLot.binLocation.id

}
);

console.log($scope.movementQueue);

//deduct the quantity
$scope.fromLot.passQuantity-=parseInt($scope.toLot.passQuantity,10);
//if quantity == 0 remove that lot from list
if($scope.fromLot.passQuantity===0){
    $scope.lineItem.lots.splice($scope.lineItem.lots.indexOf($scope.fromLot),1);
    $scope.fromLot='';
    $scope.toLot.warehouse='';
    $scope.toLot.binLocation='';

}
    $scope.toLot.warehouse="";
    $scope.toLot.binLocation="";
    $scope.toLot.passQuantity="";
    $scope.toLot.passQuantity='';
};


$scope.generateMovement = function(movement){
console.log(JSON.stringify(movement));

 SavePutAwayDetails.save({}, $scope.movementQueue, success,error);


};



  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;

    $location.path('');
      $scope.$parent.messageToDisplay = "Batch Moved Successfully";
      $scope.$parent.messageFlag = true;
      $scope.messageToDisplay = "Batch Moved Successfully";
      $scope.messageFlag = true;
      $scope.showError = false;
  };

  var error = function (data) {
    $scope.$parent.message = "";
     console.log(data);
    $scope.error = data.data.error;
    $scope.showError = true;
  };



}




PutawayController.resolve = {

  putaway: function ($q, $route, $timeout, GetInspectionById) {
    if ($route.current.params.id === undefined) return undefined;

    var deferred = $q.defer();
    var inspectionId = $route.current.params.id;

    $timeout(function () {
      GetInspectionById.get({id: inspectionId}, function (data) {
      console.log(data.inspection);
        deferred.resolve(data.inspection);
      }, {});
    }, 100);
    return deferred.promise;
  }

};

