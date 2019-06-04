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

function SummaryReportController($scope, SummaryReport, $timeout, $q, $window) {

    $scope.currentPage = 1;
    $scope.pageSize = 10;
    $scope.params = {};

    $scope.exportReport = function(type) {

        $scope.filter.limit = 100000;
        $scope.filter.page  = 1;

        var allow = $scope.allPrinting($scope.getSanitizedParameter());

        allow.then(function() {

            $scope.filter.pdformat = 1;
            var url = '/reports/download/summary' + (($scope.filter.disaggregated === true) ? '_disaggregated' : '') + '/' + type + '?' + jQuery.param($scope.getSanitizedParameter());
            $window.open(url, '_blank');
        });


    };


    $scope.allPrinting = function(params) {

        var deferred = $q.defer();

        SummaryReport.get(params, function(data) {

            if (data.openLmisResponse.rows.length > 0) {

                deferred.resolve();
            }

        });


        return deferred.promise;

    };
    $scope.onToggleReportTypeAll = function () {
        if ($scope.reportTypes === undefined) {
            $scope.reportTypes = {};
        }

        $scope.reportTypes.EM = $scope.reportTypes.RE = $scope.allReportType;
        $scope.onReportTypeCheckboxChanged();
    };
    $scope.onReportTypeCheckboxChanged = function () {
        var reportType = null;
        _.keys($scope.reportTypes).forEach(function (key) {
            var value = $scope.reportTypes[key];
            if (value === true && (key === 'EM' || key === 'RE')) {

                utils.isNullOrUndefined(reportType) ? reportType = key : reportType += "," + key;

            } else if (value === false) {
                $scope.allReportType = false;
                $scope.filter.allReportType = false;
            }
        });
        if ($scope.filter === undefined) {
            $scope.filter = {reportType: reportType};
        } else {
            $scope.filter.reportType = reportType;
        }

        if ($scope.filter.reportType !== null) {


            var str_array = $scope.filter.reportType.split(',');

            var type = {"RE": "false", "EM": "true"};

            if (str_array.length === 1) {
                $scope.filter.allReportType = false;
                $scope.filter.isEmergency = type[str_array[0]];

                console.log($scope.filter.isEmergency);

            } else {
                $scope.filter.allReportType = true;
                $scope.filter.isEmergency = 0;
            }

        }

        $scope.OnFilterChanged();
    };


    //triggered everytime parameter change
    $scope.OnFilterChanged = function () {

       if (angular.isUndefined($scope.getSanitizedParameter().program) || $scope.getSanitizedParameter().program === null || angular.isUndefined($scope.getSanitizedParameter().year) || $scope.getSanitizedParameter().year === null ||  angular.isUndefined($scope.getSanitizedParameter().period) || $scope.getSanitizedParameter().period === null || angular.isUndefined($scope.getSanitizedParameter().schedule) || $scope.getSanitizedParameter().schedule === null) {
            return;
        }

        // clear old data and set new
        $scope.tableParams.count = 100;
        $scope.data = $scope.datarows = [];
        $scope.filter.page = $scope.page;
        $scope.filter.limit = $scope.pageSize;
        $scope.params = angular.extend({}, $scope.getSanitizedParameter(), $scope.filter);


        //a variable to do manage rows count on UI
        $scope.countFactor = $scope.pageSize * ($scope.page - 1 );

        //fetch data by passing selected params
        SummaryReport.get($scope.params , function (data) {
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
