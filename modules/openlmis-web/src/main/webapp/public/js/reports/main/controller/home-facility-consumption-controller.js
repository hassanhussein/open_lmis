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
function HomeFacilityConsumptionReportController($scope, $filter, $window, AggregateConsumptionHomeFacilityReport, localStorageService) {
    $scope.filter = {};
    $scope.reportTypes = [{
        name: 'EM',
        value: 'EM',
        label: 'Emergency'
    }, {
        name: 'RE',
        value: 'RE',
        label: 'Regular'
    }];
    $scope.isAll = false;
    $scope.facilityName;
    $scope.facilityCode;
    $scope.requisitionGroup;
    $scope.page = 1;
    $scope.pageSize = 10;
    $scope.currentPage = 1;

    $scope.selectAll = function() {

        if ($scope.isAll === false) {
            angular.forEach($scope.reportTypes, function(type) {
                type.checked = true;
            });
            $scope.isAll = true;
        } else {
            angular.forEach($scope.reportTypes, function(type) {
                type.checked = false;
            });
            $scope.isAll = false;
        }
        $scope.filter = {};
        $scope.filter.allReportType = true;
        $scope.OnFilterChanged();

    };


    $scope.toggleSingle = function() {
        var param = [];
        param = _.where($scope.reportTypes, {
            checked: true
        });
        if (parseInt(param.length, 10) === 2 || parseInt(param.length, 10) === 0) {
            $scope.allReportType = true;
            $scope.filter = {};
            $scope.filter.allReportType = true;

            $scope.OnFilterChanged();
        } else {
            $scope.filter = {};
            var param2 = _.findWhere($scope.reportTypes, {
                checked: true
            });
            if (param2.name === 'RE') {
                $scope.filter.isEmergency = false;
                $scope.allReportType = false;
                $scope.filter.allReportType = false;

            } else {
                $scope.filter.isEmergency = true;
                $scope.allReportType = false;
                $scope.filter.allReportType = false;

                // $scope.filter.allReportType = true;
            }
            $scope.OnFilterChanged();
        }
    };


    $scope.OnFilterChanged = function() {
        var selectDisggregate = $scope.filter.disaggregated;

        $scope.filter.page = $scope.page;
        $scope.filter.limit = $scope.pageSize;
        $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;

        $scope.countFactor = $scope.pageSize * ($scope.page - 1);
        var allParams = angular.extend($scope.filter, $scope.getSanitizedParameter());

         $scope.facilityName = allParams.facilityName;
         $scope.facilityCode = allParams.facilityCode;
         $scope.requisitionGroup = allParams.requisitionGroup;

        if (
            allParams.periodStart !== null &&
            allParams.periodEnd !== null &&
            allParams.program !== null
        ) {
            AggregateConsumptionHomeFacilityReport.get(allParams, function(data) {
                if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
                    $scope.pagination = data.openLmisResponse.pagination;
                    $scope.totalItems = 1000;
                    $scope.currentPage = $scope.pagination.page;
                    $scope.tableParams.total = $scope.totalItems;
                    $scope.data = data.openLmisResponse.rows;
                    $scope.paramsChanged($scope.tableParams);
                }
            });

        }


    };

    $scope.exportReport   = function (type){
            $scope.filter.pdformat = 1;
            $scope.filter.limit = 10;
            $scope.filter.max = 10000;

            var params = jQuery.param($scope.getSanitizedParameter());
            var url = '/reports/download/home_facility_aggregate_consumption/' + type + '?' + params;
            $window.open(url, '_blank');
        };

    $scope.showMoreFilters = false;

    $scope.toggleMoreFilters = function() {
        $scope.showMoreFilters = true;
    };



    //lisent to currentPage value changes then update page params and call onFilterChanged() to fetch data
    $scope.$watch('currentPage', function() {
        if ($scope.currentPage > 0) {
            $scope.page = $scope.currentPage;
            $scope.OnFilterChanged();
        }
    });
}