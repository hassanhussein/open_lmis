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
function StockImbalanceController($scope, $window, $routeParams, StockImbalanceReport, $filter, $q, ngTableParams) {
    (function init() {
        $routeParams.reportType = "RE";
        $routeParams.status = "SO";
    })();
    if ($routeParams.status !== undefined) {
        var statuses = $routeParams.status.split(',');
        $scope.statuses = {};
        statuses.forEach(function(status) {
            $scope.statuses[status] = true;
        });
    }
    if ($routeParams.reportType !== undefined) {
        var reportTypes = $routeParams.reportType.split(',');
        $scope.reportTypes = {};
        reportTypes.forEach(function(reportType) {
            $scope.reportTypes[reportType] = true;
        });
    }

    $scope.exportReport = function(type) {

        $scope.filter.limit = 100000;
        $scope.filter.page  = 1;
        var printWindow;

        var allow = $scope.allPrinting($scope.getSanitizedParameter());

        allow.then(function() {

            $scope.filter.pdformat = 1;
            var url = '/reports/download/stock_imbalance/' + type + '?' + jQuery.param($scope.getSanitizedParameter());
            printWindow.location.href = url;
        });
            printWindow = $window.open('about:blank','_blank');

    };


    $scope.allPrinting = function(params) {

        var deferred = $q.defer();

        StockImbalanceReport.get(params, function(data) {

            if (data.openLmisResponse.rows.length > 0) {

                deferred.resolve();
            }

        });


        return deferred.promise;

    };
    $scope.onToggleReportTypeAll = function() {
        if ($scope.reportTypes === undefined) {
            $scope.reportTypes = {};
        }

        $scope.reportTypes.EM = $scope.reportTypes.RE = $scope.allReportType;
        $scope.onReportTypeCheckboxChanged();
    };
    $scope.onReportTypeCheckboxChanged = function() {
        var reportType = null;
        _.keys($scope.reportTypes).forEach(function(key) {
            var value = $scope.reportTypes[key];
            if (value === true && (key === 'EM' || key === 'RE')) {

                utils.isNullOrUndefined(reportType) ? reportType = key : reportType += "," + key;

            } else if (value === false) {
                $scope.allReportType = false;
            }
        });
        if ($scope.filter === undefined) {
            $scope.filter = {
                reportType: reportType
            };
        } else {
            $scope.filter.reportType = reportType;
        }
        $scope.applyUrl();
        $scope.OnFilterChanged();
    };

    $scope.onToggleAll = function() {
        if ($scope.statuses === undefined) {
            $scope.statuses = {};
        }

        $scope.statuses.SO = $scope.statuses.OS = $scope.statuses.US = $scope.statuses.UK = $scope.statuses.SP = $scope.all;
        $scope.onCheckboxChanged();
    };

    $scope.onCheckboxChanged = function() {
        var status = 'NS';
        _.keys($scope.statuses).forEach(function(key) {
            var value = $scope.statuses[key];
            if (value === true) {
                status += "," + key;
            } else if (value === false) {
                $scope.all = false;
            }
        });
        if ($scope.filter === undefined) {
            $scope.filter = {
                status: status
            };
        } else {
            $scope.filter.status = status;
        }
        $scope.applyUrl();
        $scope.OnFilterChanged();
    };

    $scope.currentPage = 1;
    $scope.pageSize = 10;
    $scope.OnFilterChanged = function() {

//clear old data if there was any
$scope.data = $scope.datarows = [];
$scope.filter.max = 10000;
$scope.filter.limit = $scope.pageSize;
$scope.filter.page = $scope.page;

//variable to manage counts on pagination
$scope.countFactor = $scope.pageSize * ($scope.page - 1);

if ($scope.filter.status === undefined) {
//By Default, show stocked out
$scope.statuses = {
    'SO': true
};
$scope.filter.status = 'SO';
$scope.applyUrl();
}

StockImbalanceReport.get($scope.getSanitizedParameter(), function(data) {
if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {

$scope.pagination = data.openLmisResponse.pagination;
$scope.totalItems = 1000;
$scope.currentPage = $scope.pagination.page;
$scope.tableParams.total = $scope.totalItems;
//check if this is last page and reduce totalItemSize so user can not go to next page
if (data.openLmisResponse.rows.length !== $scope.pageSize) {
$scope.totalItems = $scope.pageSize * $scope.page;
}
$scope.data = data.openLmisResponse.rows;
$scope.paramsChanged($scope.tableParams);
}
});

};
$scope.formatNumber = function(value, format) {
return utils.formatNumber(value, format);
};

$scope.$watch('currentPage', function() {
        if ($scope.currentPage > 0) {
            $scope.page = $scope.currentPage;
            $scope.OnFilterChanged();
        }
    });
}