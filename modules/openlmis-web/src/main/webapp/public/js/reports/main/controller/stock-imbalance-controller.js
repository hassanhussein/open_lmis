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
function StockImbalanceController($scope, $window, $routeParams, StockImbalanceReport, $filter, ngTableParams) {
  ( function init(){
    $routeParams.reportType="RE";
    $routeParams.status="SO";
  })();
  if ($routeParams.status !== undefined) {
    var statuses = $routeParams.status.split(',');
    $scope.statuses =  {};
    statuses.forEach(function(status){
      $scope.statuses[status] = true;
    });
  }
  if ($routeParams.reportType !== undefined) {
    var reportTypes = $routeParams.reportType.split(',');
    $scope.reportTypes =  {};
    reportTypes.forEach(function(reportType){
      $scope.reportTypes[reportType] = true;
    });
  }
  $scope.exportReport = function (type) {
    $scope.filter.pdformat = 1;
    var params = jQuery.param($scope.getSanitizedParameter());
    var url = '/reports/download/stock_imbalance/' + type + '?' + params;
    $window.open(url, '_blank');
  };
  $scope.onToggleReportTypeAll = function () {
    if ($scope.reportTypes === undefined) {
      $scope.reportTypes =  {};
    }

    $scope.reportTypes.EM = $scope.reportTypes.RE = $scope.allReportType;
    $scope.onReportTypeCheckboxChanged();
  };
  $scope.onReportTypeCheckboxChanged = function () {
    var reportType = null;
    _.keys($scope.reportTypes).forEach(function (key) {
      var value = $scope.reportTypes[key];
      if (value === true && (key==='EM'|| key==='RE')) {

        utils.isNullOrUndefined(reportType)? reportType=key:  reportType += "," + key;

      }else if(value===false){
        $scope.allReportType=false;
      }
    });
    if($scope.filter === undefined){
      $scope.filter = {reportType: reportType};
    }else{
      $scope.filter.reportType = reportType;
    }
    $scope.applyUrl();
    $scope.OnFilterChanged();
  };

  $scope.onToggleAll = function () {
    if ($scope.statuses === undefined) {
      $scope.statuses = {};
    }

    $scope.statuses.SO = $scope.statuses.OS = $scope.statuses.US = $scope.statuses.UK = $scope.statuses.SP = $scope.all;
    $scope.onCheckboxChanged();
  };

  $scope.onCheckboxChanged = function () {
    var status = 'NS';
    _.keys($scope.statuses).forEach(function (key) {
      var value = $scope.statuses[key];
      if (value === true) {
        status += "," + key;
      }else if(value===false){
        $scope.all=false;
      }
    });
    if($scope.filter === undefined){
      $scope.filter = {status: status};
    }else{
      $scope.filter.status = status;
    }
    $scope.applyUrl();
    $scope.OnFilterChanged();
  };

 $scope.currentPage = 1;
  $scope.pageSize = 10;
  $scope.OnFilterChanged = function () {
    $scope.data = $scope.datarows = [];
    $scope.filter.max = 10000;
    $scope.countFactor = $scope.pageSize * ($scope.page - 1 );
    $scope.filter.limit = $scope.pageSize;
    $scope.filter.page = $scope.page;
    if($scope.filter.status === undefined){
      //By Default, show stocked out
      $scope.statuses = { 'SO' : true };
      $scope.filter.status = 'SO';
      $scope.applyUrl();
    }

     StockImbalanceReport.get($scope.getSanitizedParameter(), function(data) {
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

  $scope.formatNumber = function (value, format) {
    return utils.formatNumber(value, format);
  };


  $scope.$watch('currentPage', function () {
        if ($scope.currentPage > 0) {
            $scope.page = $scope.currentPage;
            $scope.OnFilterChanged();
        }
    });
}
