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
function WmsReportController($scope,$filter,$window, $location, reports, $routeParams, $timeout, GetReportDataValue) {

  $scope.categories = ['WMS reports'];
  $scope.report_list = [
                     {'id':1,code:'preAdvice', name:'Pre-Advice'},
                     {'id':2,code:'grn', name:'GRN'},
                     {'id':3,code:'inspect', name:'Vaccine Arrival Report'},
                     {'id':4,code:'par', name:'Product Arrival Report'},
                     {'id':5,code:'var', name:'Inspection Report'}
                     ];
  $scope.report  = {};
  $scope.report.currentFilters  = [{name:'program'},{name:'dateRange2'},{name:'product'},{name:'search1'},{name:'year00'},{name:'custom'}];

  var allMonths = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  function findMonth(columnValues, indx) {

    return _.find(columnValues, function (a) {
      return a.startsWith(allMonths[indx]);
    });
  }

  $scope.reports = reports;
  $scope.displayReports = _.groupBy(reports, 'category');
  $scope.categories = _.uniq(_.pluck(reports, 'category'));

  $scope.OnReportTypeChanged = function (value) {
    $scope.filter.report_key = value;
    $scope.selectedKey = _.findWhere($scope.report_list,{code:value});

    console.log($scope.filter);
    $scope.OnFilterChanged();
  };

  $scope.onLinkClicked = function (col, row) {
    for (var i = 0; i < col.link.rowParams.length; i++) {
      var valueField = _.values(col.link.rowParams[i])[0];
      var key = _.keys(col.link.rowParams[i])[0];
      $scope.filter[key] = row[valueField];
    }
    $scope.filter.report_key = col.link.report;
    $location.search($scope.filter);
    $scope.$broadcast('filter-changed');
  };

  function updateFilterSection($scope) {

    // avoid having the blinking effect if the report has not been changed.
    if ($scope.previous_report_key !== $scope.filter.report_key) {
      $scope.previous_report_key = $scope.filter.report_key;

      $scope.report = _.findWhere($scope.reports, {reportkey: $scope.filter.report_key});

      $scope.report.columns = angular.fromJson($scope.report.columnoptions);
      if ($scope.report.filters !== null && $scope.report.filters !== '') {
        $scope.report.currentFilters = angular.fromJson($scope.report.filters);
        var required = _.pluck($scope.report.currentFilters, 'name');
        $scope.requiredFilters = [];
        angular.forEach(required, function (r) {
          $scope.requiredFilters[r] = r;
        });
      } else {
        $scope.report.currentFilters = [];
        $scope.requiredFilters = [];
      }
    }
  }


  $scope.postProcess = function (d) {
    var rows = [];
    var columnOptions = JSON.parse($scope.report.columnoptions);
    $scope.report.trusted_meta = $sce.trustAsHtml($scope.report.meta);
    $scope.report.pivotRowColumn = _.findWhere(columnOptions, {"pivotRow": "true"});
    $scope.report.pivotColumnDetail = _.findWhere(columnOptions, {"pivotColumn": "true"});
    $scope.report.pivotValueColumn = _.findWhere(columnOptions, {"pivotValue": "true"});
    $scope.report.pivotSummary = [];

    if (!isUndefined($scope.report.pivotRowColumn) && !isUndefined($scope.report.pivotColumnDetail) && !isUndefined($scope.report.pivotValueColumn)) {
      $scope.report.pivot = true;
      var rowName = $scope.report.pivotRowColumn.name;
      var columnName = $scope.report.pivotColumnDetail.name;

      $scope.report.hasPivot = true;
      var columns = [];
      $scope.report.pivotSummary = {};
      for (var i = 0; i < d.length; i++) {
        var current = d[i];

        if (columns[current[columnName]] === undefined) {
          columns[current[columnName]] = current[columnName];
        }

        if (rows[current[rowName]] === undefined) {
          current.p = [];
          rows[current[rowName]] = current;
        }

        //classify here. &&
        if ($scope.report.pivotValueColumn.classification !== undefined) {
          if ($scope.report.pivotSummary[current[$scope.report.pivotValueColumn.classification]] === undefined) {
            $scope.report.pivotSummary[current[$scope.report.pivotValueColumn.classification]] = {
              classification: current[$scope.report.pivotValueColumn.classification],
              p: {}
            };
          }
          if ($scope.report.pivotSummary[current[$scope.report.pivotValueColumn.classification]].p[current[columnName]] === undefined) {
            $scope.report.pivotSummary[current[$scope.report.pivotValueColumn.classification]].p[current[columnName]] = 0;
          }

          $scope.report.pivotSummary[current[$scope.report.pivotValueColumn.classification]].p[current[columnName]] += 1;

        }


        var row = rows[current[rowName]];
        row.p[current[columnName]] = current;
      }

      $scope.report.pivotSummaryArray = _.values($scope.report.pivotSummary);


      $scope.report.pivotColumns = [];
      var columnVals = _.values(columns);

      if ($scope.report.pivotColumnDetail.pivotType != 'custom') {
        for (i = 0; i < allMonths.length; i++) {
          // try to find
          var mont = findMonth(columnVals, i);
          if (!mont) {
            $scope.report.pivotColumns.push(allMonths[i] + ' ' + $scope.filter.year);
          } else {
            $scope.report.pivotColumns.push(mont);
          }
        }
      } else {
        $scope.report.pivotColumns = columnVals.sort();
      }

      $scope.data = _.sortBy(_.values(rows), $scope.report.columns[0].name);
    }
    else {
      $scope.data = d;
    }
  };

  $scope.OnFilterChanged = function () {



     var starDate = $filter('date')(new Date($scope.filter.periodStartDate), 'yyyy-MM-dd');
      var endDate = $filter('date')(new Date($scope.filter.periodEndDate), 'yyyy-MM-dd');

      $scope.filter.startDate = starDate;
      $scope.filter.endDate = endDate;

      console.log(endDate);


/*    if (angular.isUndefined($scope.filter) || angular.isUndefined($scope.filter.report_key) || !$scope.isReady) {
      return;
    }
    $scope.applyUrl();
    updateFilterSection($scope);

    //clear existing data
    $scope.data = [];
    $scope.meta = undefined;
    CustomReportValue.get($scope.getSanitizedParameter(), function (data) {
      $scope.meta = data;
      $scope.postProcess(data.values);
    });*/
  };


/*  $scope.dateOptions = {
    changeYear: true,
    changeMonth: true,
    yearRange: '1900:-0'
    };*/

  $scope.changeFilter = function (x) {

  console.log($scope.field.value);
  console.log(x);

  };

  $scope.searchReport = function () {

      console.log($scope.getSanitizedParameter());
/*
      $scope.reportList = [{"lotflag":true,"nocoolantflag":false,"productid":2418,"createddate":1578036904213,"icepackflag":false,"modifieddate":1578036904213,"boxcounted":0,"receiveid":16,"isshipped":true,"inspectionid":3,"isshippedprovided":true,"quantitycounted":0,"id":3,"status":"DRAFT"},{"nocoolantflag":true,"productid":2415,"createddate":1578371533682,"conditionofbox":"mammmaa","modifieddate":1578402699713,"inspectiondate":1574456400000,"receiveid":21,"inspectionid":5,"descriptionofinspection":"sasasa","passquantity":1200,"dryiceflag":true,"electronicdeviceflag":true,"id":5,"receiptnumber":"200","lotflag":true,"icepackflag":true,"shippedprovidedcomment":"no comment","labelattachedcomment":"ammmmaa","boxcounted":0,"shippedcomment":"comment","isshipped":true,"vvmflag":true,"cccardflag":true,"isshippedprovided":false,"modifiedby":307,"quantitycounted":1200,"inspectedby":"Vims - Admin","othermonitor":"type","status":"DRAFT","failquantity":0},{"nocoolantflag":true,"productid":2413,"createddate":1576738543247,"modifieddate":1578296657536,"inspectiondate":1576011600000,"receiveid":9,"inspectionid":1,"descriptionofinspection":"successiful updated","inspectionnote":"updateda","passquantity":600,"dryiceflag":true,"electronicdeviceflag":true,"id":1,"receiptnumber":"10000","lotflag":false,"icepackflag":true,"boxcounted":100,"isshipped":true,"vvmflag":true,"cccardflag":true,"isshippedprovided":true,"modifiedby":307,"inspectedby":"Vims - Admin","othermonitor":"6000L","status":"DRAFT","failquantity":500},{"lotflag":false,"nocoolantflag":false,"productid":2412,"createddate":1577188429586,"icepackflag":false,"modifieddate":1578305536077,"inspectiondate":1576702800000,"boxcounted":1009,"receiveid":7,"isshipped":true,"inspectionid":2,"vvmflag":false,"descriptionofinspection":"deacsacacaa","inspectionnote":"notes","cccardflag":false,"isshippedprovided":true,"dryiceflag":false,"modifiedby":307,"quantitycounted":0,"electronicdeviceflag":false,"id":2,"receiptnumber":"7991dda","inspectedby":"Vims - Admin","status":"DRAFT"},{"nocoolantflag":true,"productid":2413,"createddate":1578038414606,"modifieddate":1578329829558,"inspectiondate":1576357200000,"receiveid":15,"inspectionid":4,"passquantity":300,"dryiceflag":true,"electronicdeviceflag":true,"id":4,"receiptnumber":"3000","lotflag":true,"icepackflag":true,"boxcounted":0,"isshipped":true,"vvmflag":true,"cccardflag":true,"isshippedprovided":true,"modifiedby":307,"quantitycounted":500,"inspectedby":"Vims - Admin","othermonitor":"400K","status":"DRAFT","failquantity":200},{"nocoolantflag":true,"productid":2416,"createddate":1578404479593,"conditionofbox":"Yes","modifieddate":1578406189321,"inspectiondate":1578171600000,"receiveid":24,"inspectionid":7,"descriptionofinspection":"Inspection IN progress","passquantity":3000,"dryiceflag":true,"electronicdeviceflag":false,"id":7,"receiptnumber":"1277L","lotflag":true,"icepackflag":false,"shippedprovidedcomment":"some comments","labelattachedcomment":"No","boxcounted":0,"shippedcomment":"Some Comments","isshipped":false,"vvmflag":true,"cccardflag":false,"isshippedprovided":true,"modifiedby":307,"quantitycounted":4000,"inspectedby":"Vims - Admin","othermonitor":"some other type","status":"DRAFT","failquantity":1000}];
*/

    GetReportDataValue.get($scope.getSanitizedParameter(), function(data) {

       $scope.reportList = data.reportList;
      console.log(JSON.stringify(data.reportList));

    });


  };


   $scope.print = function (inspectionId){
            console.log(inspectionId.id);
             var url = '/rest-api/warehouse/inspection/var/print/'+ parseInt(inspectionId.ins_id,10);

             $window.open(url, '_blank');
   };



  $scope.exportExcel = function () {
    var params = jQuery.param($scope.getSanitizedParameter());
    var url = '/report-api/excel.xlsx?' + params;
    $window.open(url, '_blank');
  };

  $scope.isReady = true;
  if (!angular.isUndefined($scope.filter) && angular.isUndefined($scope.filter.report_key)) {
    $scope.OnFilterChanged();
  }

  if (angular.isUndefined($scope.filter)) {
    $scope.filter = {};
  }

  $scope.loadReportFromExternalUrl = function () {
    if (!angular.isUndefined($routeParams.report_key))
      $scope.filter.report_key = $routeParams.report_key;
    $scope.OnFilterChanged();
  };

  $timeout($scope.loadReportFromExternalUrl, 0);
}

WmsReportController.resolve = {
  reports: function ($q, $timeout) {
    var deferred = $q.defer();
    $timeout(function () {
     // CustomReportList.get(function (data) {
        deferred.resolve([]);
     // });
    }, 100);
    return deferred.promise;
  }
};
