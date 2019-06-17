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

function DistrictConsumptionReportController($scope,$window,$q,  DistrictConsumptionReport, $timeout) {

        $scope.allPrinting = function(params){

                 var deferred = $q.defer();

                   DistrictConsumptionReport.get(params, function (data) {

                   if(data.openLmisResponse.rows.length  > 0){

                        deferred.resolve();
                   }

                   });


          return deferred.promise;

     };




    //filter form data section

    $scope.page = 1;
    $scope.pageSize = 10;
    $scope.filter = {};
    $scope.OnFilterChanged = function(){
      $scope.data = $scope.datarows = [];
      $scope.filter.max = 10000;
      $scope.filter.limit = $scope.pageSize;
      $scope.filter.page = $scope.page;
      $scope.countFactor = $scope.pageSize * ($scope.page - 1 );



      DistrictConsumptionReport.get($scope.getSanitizedParameter(), function(data) {

        if(data.districtData !== undefined && data.districtData.rows !== undefined){
          $scope.data = chainParentChildReport(data);//.districtData.rows;
          $scope.pagination = data.districtData.pagination;
          $scope.totalItems = 1000;
          $scope.currentPage = $scope.pagination.page;
          $scope.tableParams.total = $scope.totalItems;

          //check if this is last page and reduce totalItemSize so user can not go to next page
          if(data.districtData.rows.length !== $scope.pageSize)
          {
          $scope.totalItems = $scope.pageSize * $scope.page;
          }
          $scope.paramsChanged($scope.tableParams);
        }
      });
    };

    var chainParentChildReport = function(data){

        _.each(data.districtData.rows, function(row){
            row.facilityConsumption = _.where(data.facilityData.rows, {district_id: row.district_id});
        });
        return data.districtData.rows;
    };

    var removeRowsWithNoPercentage = function (data){
        return data
            .filter(function (el) {
                return el.totalPercentage !== null && el.totalPercentage !== 0;
            });
    };

   $scope.exportReport   = function (type){

       $scope.filter.limit = 100000;
       $scope.filter.page  = 1;
       var printWindow;
       var allow = $scope.allPrinting($scope.getSanitizedParameter());

       allow.then(function(){

             $scope.filter.pdformat =1;
             var params = jQuery.param($scope.getSanitizedParameter());
             var url = '/reports/download/district_consumption/' + type +'?' + params;
             printWindow.location.href = url;
       });

            printWindow = $window.open('about:blank','_BLANK');

    };



     //lisent to currentPage value changes then update page params and call onFilterChanged() to fetch data
     $scope.$watch('currentPage', function () {
                    if ($scope.currentPage > 0) {
                      $scope.page = $scope.currentPage;
                      $timeout(function(){
                      $scope.OnFilterChanged();
                      },100);
                    }
      });

}
