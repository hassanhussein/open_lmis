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
function DistributionController($q,homeFacility,StockEvent,UpdateOrderRequisitionStatus,SaveDistributionList,StockCards,$window,$scope,$filter,$routeParams, $route,$location, $rootScope) {







//test
//$scope.data={
// orders:[{
//                orderNumber:"IVD0001",
//                period:"Sept - Dec 2020",
//                dateSubmitted:"11/09/2020",
//                issue:false,
//                name:"Arusha RVS",
//                ordered:[{
//                  productId:343,
//                  product:"BCG",
//                  amount:353,
//                  gap:'',
//                  given:[]
//                },
//                {
//                  productId:34,
//                  product:"PCV",
//                  amount:316,
//                  gap:'',
//                  given:[]
//                }
//
//              ],
//
//              },
//              {
//                orderNumber:"IVD0002",
//                period:"Jan - Feb 2020",
//                dateSubmitted:"11/01/2020",
//                     issue:false,
//
//                name:"Dodoma RVS",
//                ordered:[{
//                  productId:343,
//                  product:"BCG",
//                  amount:753,
//                  gap:'',
//                  given:[]
//                },
//                {
//                  productId:34,
//                  product:"PCV",
//                  amount:300,
//                  gap:'',
//                  given:[]
//                }
//
//              ],
//              },
//              {
//                              orderNumber:"IVD0003",
//                              period:"Apr- Jun 2020",
//                              dateSubmitted:"11/04/2020",
//                                              issue:false,
//
//                              name:"Tanga RVS",
//                              ordered:[{
//                                productId:343,
//                                product:"BCG",
//                                amount:853,
//                                gap:'',
//                                given:[]
//                              },
//                              {
//                                productId:34,
//                                product:"PCV",
//                                amount:456,
//                                gap:'',
//                                given:[]
//
//                              }
//
//                            ],
//
//
//                            }
//
//            ],
// pagination:{
// totalRecords:3,
// page:1,
// limit:10,
// }
//
// };
//
//  $scope.requstions=[];
// $scope.orderList = $scope.data.orders;
//    angular.forEach($scope.orderList,function(order){
//
//    $scope.requstions.push(order);
//
//    });
//endtest

var currentStock = $scope.soh = lots = [];

  var wait = function() {
    var deferred = $q.defer();
    setTimeout(function() {

     StockCards.get({facilityId:19075}, function(data){
          deferred.resolve(data.stockCards);

     });


    }, 1000);
    return deferred.promise;
  };

wait()
  .then(function(stocks) {


   var sohAvailable = {};
    var currentS = {};

   if(stocks.length > 0 ) {

   angular.forEach(stocks, function(stock, indx){

            currentS.product=stock.product.primaryName;
            currentS.productId=stock.product.id;

            var newLots = [];

            angular.forEach(stock.lotsOnHand, function(lot,idx){
              newLots.push({id:idx + 1, lotNumber:lot.lot.lotCode,
              amount:lot.quantityOnHand, maxSoh:lot.quantityOnHand, expirationDate:lot.lot.expirationDate,
              vvm:lot.customProps.vvmstatus

              });
             });
             currentS.lots = newLots;

        $scope.soh.push(currentS);

       console.log($scope.soh);

   });

   }

  });


$scope.requstions=$scope.$parent.orders;


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
/*

  $scope.soh=[{
    product:"BCG",
    productId:343,
    lots:[{
       id:1,
      number:"ABDSE",
      location:"abc",
      amount:445,
      maxSoh:445,
      vvm:"VVM2",
      expiry:"2028-10-10"
    },
    {
    id:2,
      number:"PH0YUT",
      location:"PGHDJ",
      amount:700,
      maxSoh:700,
      vvm:"VVM2",
      expiry:"2028-10-10"
    }
  ]
  },

  {
    product:"PCV",
    productId:34,
    lots:[{
    id:4,
      number:"AWPXDD",
      location:"abc",
      amount:280,
      maxSoh:280,
      vvm:"VVM1",
      expiry:"2028-09-10"
    }]
  }

];
*/


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
    }

};
