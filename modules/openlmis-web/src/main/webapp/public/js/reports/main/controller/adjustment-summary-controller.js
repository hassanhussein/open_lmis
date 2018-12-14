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

function AdjustmentSummaryReportController($scope, $window , AdjustmentSummaryReport) {

  $scope.exportReport = function(type) {

    $scope.filter.pdformat = 1;
    var params = jQuery.param($scope.getSanitizedParameter());
    var url = '/reports/download/adjustment_summary/' + type + '?' + params;
    $window.open(url, '_blank');
  };

  $scope.currentPage = 1;
  $scope.pageSize = 10;
  $scope.OnFilterChanged = function() {
    // clear old data if there was any
    $scope.data = $scope.datarows = [];
    $scope.filter.max = 10000;
    $scope.filter.limit = $scope.pageSize;
    $scope.filter.page = $scope.page;
    AdjustmentSummaryReport.get($scope.getSanitizedParameter(), function(data) {

        if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
        var adjustments = data.openLmisResponse.rows;
        $scope.data =_.where(adjustments,{pagination:null});
        $scope.pagination = adjustments[adjustments.length-1].pagination;
        $scope.totalItems = $scope.pagination.totalRecords;
        $scope.currentPage = $scope.pagination.page;
        $scope.tableParams.total = $scope.totalItems;
        $scope.paramsChanged($scope.tableParams);
      }
    });
  };


    $scope.$watch('currentPage', function () {
        if ($scope.currentPage > 0) {
            $scope.page = $scope.currentPage;
            $scope.OnFilterChanged();
        }
    });


}
