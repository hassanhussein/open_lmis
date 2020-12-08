/*
 *
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *  Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
function DistributionController($q,$timeout,homeFacility,StockEvent,wmsSoh,all_orders,UpdateOrderRequisitionStatus,SaveDistributionList,StockCards,$window,$scope,$filter,$routeParams, $route,$location, $rootScope,SaveOnlyDistribution, UpdateDistributionOrderStatus,localStorageService,ApproveOnlyDistribution,StockEventWms) {


$scope.qty=[];
$scope.vialPresentationErrorList=[];
$scope.allFieldsFieldErrorList=[];
     $scope.loadRights = function () {
            $scope.rights = localStorageService.get(localStorageKeys.RIGHT);
     }();

     $scope.hasPermission = function (permission) {
            if ($scope.rights !== undefined && $scope.rights !== null) {
              var rights = JSON.parse($scope.rights);
              var rightNames = _.pluck(rights, 'name');
              return rightNames.indexOf(permission) > -1;
            }
            return false;
      };


  $scope.$parent.distributed = false;

//console.log(all_orders);
$scope.soh=wmsSoh.stocks;







//console.log($scope.requisitionsWithoutProducts);
$scope.requisitionsWithoutProducts=$scope.$parent.orders;
$scope.requstions=[];

$scope.requisitionsWithoutProducts.forEach(function(rwp){
 var requisitionsWithProduct=_.findWhere(all_orders,{id:rwp.id});
//console.log(all_orders);
$scope.requstions.push({
                             fromFacilityId:19075,
                             toFacilityId:requisitionsWithProduct.facilityId,
                             programId:parseInt(82,10),
                             orderId:requisitionsWithProduct.id,
                             periodId: requisitionsWithProduct.periodId,
                             remarks:'Add some remarks',
                             orderNumber: requisitionsWithProduct.orderNumber,
                             period: requisitionsWithProduct.period,
                             dateSubmitted: requisitionsWithProduct.dateSubmitted,
                             issue: true,
                             name:requisitionsWithProduct.facilityName,
                             ordered:requisitionsWithProduct.ordered

});

//console.log($scope.requstions);


});


$scope.initializeQty=function (){





$scope.requstions.forEach(function(region){
var productArray=[];
    $scope.soh.forEach(function(product){
        var lotArray=[];
        product.lots.forEach(function(lot){
            lotArray.push('');
        });


        productArray.push(lotArray);

    });
$scope.qty.push(productArray);

});




};

$scope.initializeQty();


$scope.checkVialPresentation=function(){
$scope.vialPresentationErrorList=[];
    _.each($scope.requstions,function(region){
        _.each(region.ordered,function(order){
           _.each(order.given,function(lot){
                if(!Number.isInteger(parseInt(lot.qty,10)/parseInt(lot.packSize,10))){
                        $scope.vialPresentationErrorList.push('Batch: '+lot.number+' ('+lot.vvmId+') Quantity Issued should be multiple of '+lot.packSize);
                }
           });
        });
    });

};

$scope.checkAllFields=function(){
$scope.allFieldsFieldErrorList=[];
 _.each($scope.requstions,function(region){
        _.each(region.ordered,function(order){
           _.each(order.given,function(lot){
                if(lot.qty && parseInt(lot.qty,10)>=0){
                        $scope.allFieldsFieldErrorList.push('Please fill all quantity issued for '+region.name);
                }
           });
        });
    });
};



$scope.getQuantity=function(req,product,lot,regionIndex){
var region =$scope.requstions[regionIndex];
var ordered = _.findWhere(region.ordered,{productId:product.productId});
var given = _.findWhere(ordered.given,{lotId:lot.lotId,vvmId:lot.vvmId,locationId:lot.locationId});
if(given===undefined){
    return '';
}

return given.qty;



};

$scope.getLotSumPerRegion=function(lotId,locationId,productId){
 var sum=0;
   $scope.requstions.forEach(function(region){


   region.ordered.forEach(function(order){


   if(order.productId===productId){

   order.given.forEach(function(giv){
    if (giv.lotId===lotId&&giv.locationId===locationId){
    qty=giv.qty;
    if(giv.qty===""){
    qty=0;
    }
    sum+=parseInt(qty,10);
    }
   });
   }
   });
   });
return sum;
};

$scope.getGap=function(regionIndex,product){
var region =$scope.requstions[regionIndex];
var ordered = _.findWhere(region.ordered,{productId:product.productId});
if(Number.isNaN(ordered.gap)){
return '';
}
return ordered.gap;

};
$scope.giveLot=function(req,prod,lot,qty,regionIndex,productIndex,lotIndex){











var region =$scope.requstions[regionIndex];
var ordered = _.findWhere(region.ordered,{productId:prod.productId});

var given= _.findWhere(ordered.given,{lotId:lot.lotId,vvmId:lot.vvmId,locationId:lot.locationId});
if(qty==="" ||qty===0){
qty=0;
}



//check vialPresentaion here
//if(qty>0){
//    $scope.vialPresentationError=!isInteger(qty/lot.lot.packSize)?true:false
//}

//deduct soh for this product for this lot
var lotQuantity=0;
if(given){
 if(qty){
      lotQuantity=parseInt(qty,10);
  }
//edit this lot
given.qty=qty;
given.quantity=lotQuantity/lot.packSize;


}else{
//push this lot
//var lotQuantity=0;
 if(qty){
      lotQuantity=parseInt(qty,10);
  }

ordered.given.push({
lotId:lot.lotId,
qty:qty,
locationId:lot.locationId,
number:lot.number,
stockCardId:lot.stockCardId,
vvmId:lot.vvmId,
packSize:lot.packSize,
binLocation:lot.binLocation,
transferLogs:lot.binLocation+"-"+req.name,
quantity:lotQuantity/lot.packSize

});
}

$scope.checkVialPresentation();

//find lot sum
var sum=0;
ordered.given.forEach(function(given){
sum+=parseInt(given.qty,10);
});
ordered.gap=ordered.amount-sum;

//update the soh accordingly
//sum all the requisitions for this product for this lot
var all_requisitions=_.filter($scope.requstions,function(req){
return;
}
);

//reset other inputs when qty<lot.maxSoh
if(qty<lot.maxSoh){

$scope.qty.forEach(function(region,qtyRegionIndex){

    region.forEach(function(product,qtyProductIndex){
        product.forEach(function(lot,qtyLotIndex){
        if(qtyLotIndex>lotIndex && qtyProductIndex==productIndex ){
        console.log('reset');

        $scope.qty[qtyRegionIndex][qtyProductIndex][qtyLotIndex]='';
        }

        });
    });
});

}





lot.amount=lot.maxSoh-$scope.getLotSumPerRegion(lot.lotId,lot.locationId,prod.productId);
if(Number.isNaN(lot.amount)){
lot.amount=lot.maxSoh;
}
};


  $scope.range = function(n) {
        return new Array(n);
    };


  $scope.checkIssue=function(){
    if ($scope.issueAll===true){
    $scope.issueAll=false;




    }else{
        $scope.issueAll=true;


    }
  };



$scope.getOrderedQuantity=function(regionIndex,product){
var region =$scope.requstions[regionIndex];
// console.log(region.ordered)
var ordered = _.findWhere(region.ordered,{productId:product});
//console.log(ordered)
if (ordered!==undefined) {
  return ordered.amount;
}
};


$scope.cancel=function(){
 $scope.$parent.distributed = false;
  $location.path('');
};



$scope.releaseForPickingDistribution=function(){

$scope.checkAllFields();
if($scope.vialPresentationErrorList.length||$scope.allFieldsFieldErrorList.length){
return;


}
$scope.saveDistribution();
};


$scope.saveDistribution = function () {



//if ($scope.distributionForm.$error.required) {
//            $scope.showError = true;
//            $scope.error = 'form.error';
//            $scope.message = "";
////            console.log('dfas')
//            return;
//        }
$scope.distribution_list=[];
//console.log($scope.)

$scope.requstions.forEach(function(req){

    req.status='PENDING';
    req.ordered.forEach(function(ord){
    ord.lots=ord.given;
    delete ord.product;
    });

    req.lineItems=req.ordered;
    req.distributionDate=$scope.distributionDate;
    req.distributionType=$scope.distributionType;


    $scope.distribution_list.push(req);

});



 SaveOnlyDistribution.save($scope.distribution_list, function (distribution) {

  $scope.$parent.distributed = true;
  $scope.$parent.message="Distribution updated successfully";

  $timeout(function(){

        $scope.$parent.distributed = false;

        },4000);

  $location.path('');


                        });

  UpdateDistributionOrderStatus.update($scope.distribution_list, function(distribution){

  });

};















$scope.approveDistribution=function(){


var events = [];
var distributionLineItemList = [];

angular.forEach($scope.requstions, function (facility) {

var distribution = {};

distribution.fromFacilityId = facility.fromFacilityId;
distribution.toFacilityId = facility.toFacilityId;
distribution.distributionDate = "2020-05-21";
distribution.periodId = facility.periodId;
distribution.orderId = facility.orderId;
distribution.status = "PICKED";
distribution.distributionType = "SCHEDULED";
distribution.remarks = facility.remarks;
distribution.programId = facility.programId;
distribution.lineItems = [];

 angular.forEach(facility.ordered, function (product) {

var lineItem = {};
        if (product.amount > 0) {





             lineItem.productId = product.productId;
             lineItem.quantity = product.totalQuantity;
              var totalQuantity=0;


            var l = {};
                          lineItem.lots = [];

             angular.forEach(product.given, function(lot) {

              console.log(lot);

              var lotQuantity=0;

              if(lot.qty){
              lotQuantity=parseInt(lot.qty,10);
              }


             if (lotQuantity !== null && lotQuantity > 0) {

                totalQuantity=totalQuantity+lotQuantity;

                     var event = {};
                     event.type = "ISSUE";
                     event.productCode = product.productCode;
                     event.facilityId = facility.toFacilityId;
                     event.occurred = distribution.distributionDate;
                     event.quantity = lotQuantity;
                     event.customProps = {};
                     event.customProps.occurred = distribution.distributionDate;
                     event.customProps.issuedto = facility.name;

                     event.lotId = lot.lotId;
                     event.quantity = lotQuantity;


                     l.lotId = lot.lotId;
                     l.vvmStatus = lot.vvmStatus;
                     l.locationId=lot.locationId;
                     l.lotNumber=lot.number;
                     l.stockCardId=lot.stockCardId;
                     l.vvmId=lot.vvmId;
                     l.packSize=lot.packSize;

                      l.facilityName=facility.name;

                      var packSize=1;

                      if(lot.packSize){
                      packSize=lot.packSize;
                      }


                     l.quantity = lotQuantity/packSize;
                     event.lots=lot;
                     lineItem.lots.push(lot);
                     events.push(event);

             }

             });

                lineItem.quantity=totalQuantity;
                }

                if (lineItem.quantity > 0) {
                    distribution.lineItems.push(lineItem);
                }

            });



           distributionLineItemList.push(distribution);

});

//console.log(events);




StockEventWms.save({facilityId: homeFacility}, events, function (data) {

 if (data.success) {
$scope.distribution_list=[];
//console.log($scope.)

$scope.requstions.forEach(function(req){

    req.ordered.forEach(function(ord){
    ord.lots=ord.given;
    delete ord.product;
    });

    req.lineItems=req.ordered;
    req.status="PICKED";


    $scope.distribution_list.push(req);

});


ApproveOnlyDistribution.save($scope.distribution_list, function (distribution) {



  $scope.$parent.distributed = true;
    $scope.$parent.message="Distribution approved successfully!";

    $timeout(function(){

            $scope.$parent.distributed = false;

            },4000);

  $location.path('');


                        });



UpdateDistributionOrderStatus.update($scope.distribution_list, function(distribution){

  });

                    }
 });





//console.log('reached here');

};




}

DistributionController.resolve = {


    homeFacility: function ($q, $timeout, UserHomeFacility) {
        var deferred = $q.defer();

        $timeout(function () {

            UserHomeFacility.get({}, function (data) {
                deferred.resolve(data.homeFacility.id);
            });

        }, 100);

        return deferred.promise;
    },wmsSoh: function ($q, $timeout, GetCurrentStock) {
              var deferred = $q.defer();

              $timeout(function () {

                  GetCurrentStock.get({}, function (data) {


                      deferred.resolve(data);
                  });

              }, 100);

              return deferred.promise;
          },
    all_orders: function ($q, $timeout, VaccinePendingRequisitionsForCVS, $route) {
            var deferred = $q.defer();
            $timeout(function () {
                if (isUndefined($route.current.params.facilityId)) {


                    return null;
                } else {
                    VaccinePendingRequisitionsForCVS.get({

                            facilityId: $route.current.params.facilityId
                        },
                        function (data) {

                        deferred.resolve(data.pendingRequest);

                        });
                }


            }, 100);

            return deferred.promise;
        },

};

