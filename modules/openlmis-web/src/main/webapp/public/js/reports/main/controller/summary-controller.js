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

function SummaryReportController($scope, SummaryReport) {

    $scope.currentPage = 1;
    $scope.pageSize = 50;

    $scope.$watch('currentPage', function () {
        console.log($scope.pageSize);
        if (angular.isDefined($scope.lineItems)) {
            $scope.pageLineItems();
        }
    });


    //$.extend(this, new BaseReportController($scope, SummaryReport, $scope.filter));


    $scope.exportReport = function (type, par) {
        $scope.filter.pdformat = 1;
        var params = jQuery.param(par);
        var url = '/reports/download/summary' + (($scope.filter.disaggregated === true) ? '_disaggregated' : '') + '/' + type + '?' + params;
        window.open(url, "_BLANK");
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

    $scope.params = {};

    $scope.pageLineItems = function(){

       $scope.filter.max= $scope.pageSize;
    };

    $scope.OnFilterChanged = function () {

        if (angular.isUndefined($scope.getSanitizedParameter().program) || $scope.getSanitizedParameter().program === null || angular.isUndefined($scope.getSanitizedParameter().year) || $scope.getSanitizedParameter().year === null ||  angular.isUndefined($scope.getSanitizedParameter().period) || $scope.getSanitizedParameter().period === null || angular.isUndefined($scope.getSanitizedParameter().schedule) || $scope.getSanitizedParameter().schedule === null) {
            return;
        }

        // clear old data if there was any
        $scope.tableParams.count = 10;
        $scope.data = $scope.datarows = [];
        $scope.filter.max = 100;
      //  $scope.filter.page = 10;

        $scope.numberOfPages = [{id:1,value:10,name:'10 Pages'},{id:2,value:100,name:'100 Pages'},{id:3, value:10000,name:'All Pages'}];

        $scope.params = angular.extend({}, $scope.getSanitizedParameter(), $scope.filter);
        console.log($scope.filter);

        SummaryReport.get($scope.params, function (data) {
            if (data.pages !== undefined && data.pages.rows !== undefined) {
                $scope.data = data.pages.rows;
                $scope.paramsChanged($scope.tableParams);
            }
        });
    };

}
