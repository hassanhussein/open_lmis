/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */


function StockLedgerController($scope,$routeParams, stockLedgers,$location,OperationYears,GetStockLedgerReport, GetProductById) {

$scope.ledgers = [];
$scope.ledgers = stockLedgers;

 $scope.currentYear = new Date().getFullYear();

 console.log($routeParams);

GetProductById.get({id:parseInt($routeParams.productId,10) }, function(data){

$scope.product = data.productDTO.product.primaryName;

console.log(data.productDTO.product.primaryName);

});


   OperationYears.get(function (data) {
        var array = _.sortBy(data.years, function(num) {
            return num;
        });
        $scope.startYears = array.reverse();
       // $scope.startYears.unshift('--select year-- ');
    });


    $scope.changeByYear = function() {

        var params = {};
        params.warehouseId = parseInt($routeParams.warehouseId,10);
        params.productId = parseInt($routeParams.productId,10);
        params.year = parseInt($scope.currentYear,10);

        $scope.ledgers = [];

        GetStockLedgerReport.get(params, function(data) {

                if(!isUndefined(data.ledgers) ) {

                    $scope.ledgers = data.ledgers;


                }

            }, {});



    };




}



StockLedgerController.resolve = {

    stockLedgers:function ($q, $route, $timeout, GetStockLedgerReport) {
        console.log($route.current.params);
        var deferred = $q.defer();

          $timeout(function () {

          var params = {};
          var value = [];

          params.warehouseId=parseInt($route.current.params.warehouseId,10);
          params.productId=parseInt($route.current.params.productId,10);
          params.year=parseInt($route.current.params.year,10);
          params.product = $route.current.params.product;

          GetStockLedgerReport.get(params, function(data) {

                if(!isUndefined(data.ledgers) ) {

                    console.log(data.ledgers);


                    value = data.ledgers;
                deferred.resolve(value);

                } else {

                deferred.resolve(value);

                }

            }, {});
        }, 100);
        return deferred.promise;
    }

};