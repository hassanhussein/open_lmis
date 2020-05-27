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
function DistributionController($q,homeFacility,StockEvent,wmsSoh,all_orders,UpdateOrderRequisitionStatus,SaveDistributionList,StockCards,$window,$scope,$filter,$routeParams, $route,$location, $rootScope,SaveOnlyDistribution) {

  $scope.$parent.distributed = false;

//console.log(all_orders);
$scope.soh=wmsSoh.stocks;
$scope.requisitionsWithoutProducts=$scope.$parent.orders;
$scope.requstions=[];
$scope.requisitionsWithoutProducts.forEach(function(rwp){
 var requisitionsWithProduct=_.findWhere(all_orders,{id:rwp.id});
console.log(requisitionsWithProduct);
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


});

$scope.getLotSumPerRegion=function(lotId,productId){
 var sum=0;
   $scope.requstions.forEach(function(region){


   region.ordered.forEach(function(order){


   if(order.productId===productId){

   order.given.forEach(function(giv){
    if (giv.lotId===lotId){
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
$scope.giveLot=function(req,prod,lot,qty,regionIndex){
var region =$scope.requstions[regionIndex];
var ordered = _.findWhere(region.ordered,{productId:prod.productId});

var given= _.findWhere(ordered.given,{lotId:lot.id});
if(qty===""){
qty=0;
}

//deduct soh for this product for this lot
if(given){
//edit this lot
given.qty=qty;
//console.log('edit lot')
}else{
//push this lot
ordered.given.push({
lotId:lot.id,
qty:qty
});
}

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
lot.amount=lot.maxSoh-$scope.getLotSumPerRegion(lot.id,prod.productId);
if(Number.isNaN(lot.amount)){
lot.amount=lot.maxSoh;
}
//console.log($scope.requstions)
//console.log("Giving "+qty+"of"+prod.product+" of lot "+lot.number+" to "+req.name)
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
 $scope.$parent.distributed = true;
  $location.path('');
}



$scope.saveDistribution = function () {
$scope.distribution_list=[]

$scope.requstions.forEach(function(req){

    req.ordered.forEach(function(ord){
    ord.lots=ord.given
    })

    req.lineItems=req.ordered

    $scope.distribution_list.push(req);

});

console.log($scope.distribution_list);
















 SaveOnlyDistribution.save($scope.distribution_list, function (distribution) {

  $scope.$parent.distributed = true;
  $location.path('');
                  console.log('distributed');

                        });

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
//                  console.log(data);
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
//                        console.log(data);
                        deferred.resolve(data.pendingRequest);

                        });
                }


            }, 100);

            return deferred.promise;
        },

};
