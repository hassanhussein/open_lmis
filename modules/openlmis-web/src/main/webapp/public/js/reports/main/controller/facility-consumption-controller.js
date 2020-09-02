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
function FacilityConsumptionReportController($scope, $filter, $window, FacilityConsumptionReport, $q) {
    $scope.showDisaggregatedColumns= false;
    $scope.OnFilterChanged = function() {
        $scope.registerServerSidePagination($scope.tableParams, $scope.runReport);
    };

    $scope.runReport = function () {
        if($scope.filter.error) return;

        var deferred = $q.defer();
        var param=$scope.getSanitizedParameter();

        FacilityConsumptionReport.get(param, function (data) {
            $scope.data = [];
            if (data.pages !== undefined) {
                $scope.data = data.pages;
                $scope.periods = $scope.data.rows.length > 0 ? $scope.data.rows[0].headerPeriods : [];
                $scope.showDisaggregatedColumns  = $scope.filter.disaggregated === true || $scope.filter.disaggregated === 'true' ? true : false;
            }
            deferred.resolve();
        }, function(error){
           deferred.reject(error);
        });
        return deferred.promise;
    };


    // get the data for dynamicaly returde periods on the UI
    $scope.consumptionForPeriod = function (row, period) {
        var consumption=0;
        if (!utils.isNullOrUndefined(row)) {
            consumption = _.findWhere(row.consumptionColumnList, {header: period}).valeu;
        }
        return consumption;
    };
    $scope.getBackGroundColor = function (row, period) {
        var color="";
        if (!utils.isNullOrUndefined(row)) {
            color = _.findWhere(row.consumptionColumnList, {header: period}).flagcolor;
        }
        return color;
    };

    $scope.exportReport = function (type) {
        $scope.filter.pdformat = 1;
        var url = '/reports/download/facility_consumption' + (($scope.filter.disaggregated === true) ? '_disaggregated' : '') + '/' + type + '?' + jQuery.param($scope.getSanitizedParameter());
        $window.open(url, '_blank');
    };
    $scope.showMoreFilters = false;

    $scope.toggleMoreFilters = function () {
        $scope.showMoreFilters = true;
    };


}
