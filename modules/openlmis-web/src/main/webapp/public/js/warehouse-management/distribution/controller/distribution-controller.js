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
function DistributionController($window,$scope,$filter,$routeParams, $route,$location, $rootScope) {

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

  $scope.soh=[{
    product:"BCG",
    productId:343,
    lots:[{
       id:1,
      number:"PGTRE",
      location:"abc",
      amount:45,
      maxSoh:45,
      vvm:"VVM3",
      expiry:"2018-10-10"
    },
    {
    id:2,
      number:"cde",
      location:"PGHDJ",
      amount:700,
      maxSoh:700,
      vvm:"VVM2",
      expiry:"2018-10-10"
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
      amount:80,
      maxSoh:80,
      vvm:"VVM1",
      expiry:"2018-09-10"
    }]
  }

];


}
