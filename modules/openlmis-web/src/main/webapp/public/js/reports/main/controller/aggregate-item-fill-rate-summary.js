/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
function AggregateItemFillRateSummaryController($scope, $window, AggregateItemFillRateSummary, AggregateItemFillRateByOrder) {
 $scope.page = 1;
 $scope.pageSize = 10;

  $scope.exportReport = function(type) {
    $scope.filter.pdformat = 1;
    var params = jQuery.param($scope.filter);
    var url = '/reports/download/item_fill_rate_aggregate_report/' + type + '?' + params;
    $window.open(url);
  };
  $scope.calculateAverageItemFillRate = function(orderList){
  var sum = 0;
  for(var i=0; i< orderList.length;i++) {
   sum += parseInt((orderList[i].receivedQuantity/orderList[i].approvedQuantity )*100,10);
  }
   return sum/ orderList.length;
  };

  $scope.successModal = false;
  $scope.allTheData = [];


  $scope.getDetails = function(rnr, row){


   var param = angular.extend($scope.filter, {'orderId':parseInt(rnr,10)});

   AggregateItemFillRateByOrder.get(param, function(data) {
     row.items = data.openLmisResponse.rows;
     $scope.allTheData = row;

    console.log(row);

        if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
          console.log(data);
           $scope.pagination = data.openLmisResponse.pagination;
           $scope.totalItems = 1000;
           $scope.currentPage = $scope.pagination.page;
           //$scope.tableParams.total = $scope.totalItems;
           //check if this is last page and reduce totalItemSize so user can not go to next page
           if (data.openLmisResponse.rows.length !== $scope.pageSize) {
           $scope.totalItems = $scope.pageSize * $scope.page;
           }
           $scope.data = data.openLmisResponse.rows;
           $scope.paramsChanged($scope.tableParams);
          }


   });
   $scope.successModal = true;
  };
   $scope.getColor = function (color){
     return {'color':(parseInt(color,10) == 100)?'black':(parseInt(color,10) > 100)?'lightblue':'red'};
   };
    $scope.getItemFillRate = function (rate){
    console.log(rate);
     return (parseInt(rate.quantityApproved,10) > 0 )?parseInt(rate.quantityShipped,10) / parseInt(rate.quantityApproved,10) * 100:0;

    };

  $scope.OnFilterChanged = function() {

    $scope.data = $scope.datarows = [];
    $scope.filter.max = 10000;
    $scope.filter.page = 1;
    $scope.filter.limit = 10000;

    AggregateItemFillRateSummary.get($scope.filter, function(data) {


       var groupByFacilityData = _.groupBy(data.openLmisResponse.rows, 'facilityName');

       var mappedData = _.map(groupByFacilityData, function(orders, index){

           return {"facilityName":index,"district":orders[0].district,"region":orders[0].region,"msdZone":orders[0].msdZone,"orderList":orders};

       });

           console.log(mappedData);

       $scope.dataRow = mappedData;


      if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
      console.log(data);
       $scope.pagination = data.openLmisResponse.pagination;
       $scope.totalItems = 1000;
       $scope.currentPage = $scope.pagination.page;
       //$scope.tableParams.total = $scope.totalItems;
       //check if this is last page and reduce totalItemSize so user can not go to next page
       if (data.openLmisResponse.rows.length !== $scope.pageSize) {
       $scope.totalItems = $scope.pageSize * $scope.page;
       }
       $scope.data = data.openLmisResponse.rows;
       $scope.paramsChanged($scope.tableParams);
      }
    });
  };

  //lisent to currentPage value changes then update page params and call onFilterChanged() to fetch data





    $scope.loadPaginatedData = function () {

             $scope.filter.page = $scope.page;
              $scope.filter.limit = 10;
              $scope.data = $scope.datarows = [];
              $scope.filter.max = 10000;

              $scope.countFactor = $scope.pageSize * ($scope.page - 1 );


                  AggregateConsumptionReport.get($scope.getSanitizedParameter(), function (data) {
                      if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
                          $scope.pagination = data.openLmisResponse.pagination;
                          $scope.totalItems = 1000;
                          $scope.currentPage = $scope.pagination.page;
                          $scope.tableParams.total = $scope.totalItems;
                          $scope.data = data.openLmisResponse.rows;
                          $scope.paramsChanged($scope.tableParams);
                      }
                  });



      };


}
