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
function AggregateConsumptionReportController($scope, $filter, $window, AggregateConsumptionReport, ngTableParams, $q) {

    var disaggregationProductsMinCount = 1;
    var disaggregationProductsMaxCount = 5;
    $scope.reportTypes = [
        {name: 'All', value: 'ALL', label: 'All', checked: true},
        {name: 'EM', value: 'EM', label: 'Emergency'},
        {name: 'RE', value: 'RE', label: 'Regular'}
    ];

    $scope.showDisaggregatedColumns= false;

    $scope.OnFilterChanged = function() {
        $scope.registerServerSidePagination($scope.tableParams, $scope.runReport);
    };

    $scope.runReport = function (pageSize, page, sortBy, sortDirection) {
        var disaggregatedSelectedProductCount = $scope.filter.products.filter(function(item){ return item !=="0" && item!=="-1"; }).length;
        var disaggregationWrongProductSelection = (disaggregatedSelectedProductCount > disaggregationProductsMaxCount ||
            disaggregatedSelectedProductCount < disaggregationProductsMinCount);

        var disaggregated  = $scope.filter.disaggregated === true || $scope.filter.disaggregated === 'true' ? true : false;

        //During disaggregating, at least 1 and at most 5 products needs to be selected
        if(disaggregated && disaggregationWrongProductSelection) {
            $scope.filter.error = 'During disaggregation at least 1 or at most 5 products should be selected';
            return;
        }

        var deferred = $q.defer();

        AggregateConsumptionReport.get($scope.getSanitizedParameter(), function (data) {
            $scope.data = [];
            if (data.pages !== undefined) {
                $scope.data = data.pages;
                $scope.showDisaggregatedColumns = $scope.filter.disaggregated ? true : false;
                deferred.resolve(data);
            }
        });
        return deferred.promise;
    };

    $scope.exportReport = function (type) {
        $scope.filter.pdformat = 1;
        var url = '/reports/download/aggregate_consumption' + (($scope.filter.disaggregated === true) ? '_disaggregated' : '') + '/' + type + '?' + jQuery.param($scope.getSanitizedParameter());
        $window.open(url, '_blank');
    };

    $scope.showMoreFilters = false;
    $scope.toggleMoreFilters = function () {
        $scope.showMoreFilters = true;
    };
}
