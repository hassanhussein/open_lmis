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


  $scope.range = function(n) {
        return new Array(n);
    };


  $scope.checkIssue=function(){
    if ($scope.issueAll==true){
    $scope.issueAll=false;




    }else{
        $scope.issueAll=true;


    }
  }



$scope.getOrderedQuanity=function(regionIndex,product){
var region =$scope.requstions[regionIndex];
// console.log(region.ordered)
var ordered = _.findWhere(region.ordered,{productId:product});

if (ordered!=undefined) {
  return ordered.amount
}
}

  $scope.soh=[{
    product:"BCG",
    productId:343,
    lots:[{
      number:"PGTRE",
      location:"abc",
      amount:45,
      vvm:"VVM3",
      expiry:"2018-10-10"
    },
    {
      number:"cde",
      location:"PGHDJ",
      amount:700,
      vvm:"VVM2",
      expiry:"2018-10-10"
    }
  ]
  },

  {
    product:"PCV",
    productId:34,
    lots:[{
      number:"AWPXDD",
      location:"abc",
      amount:80,
      vvm:"VVM1",
      expiry:"2018-09-10"
    }]
  }

]


}
