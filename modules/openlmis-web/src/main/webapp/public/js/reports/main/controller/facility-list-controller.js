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


function ListFacilitiesController($scope, FacilityList) {
    $scope.data = "";
    $scope.totalFacilities = 0;
    $scope.unfilteredFacilities = "";
    $scope.statuses = [
        {
            'name': 'Active',
            'value': 'true'
        },
        {
            'name': 'Inactive',
            'value': 'false'
        }
    ];


    $scope.filterFacilities = function () {
        $scope.data =  _.filter($scope.unfilteredFacilities, function(item){
            return item.province === ($scope.filter.provinces === "" ? item.province : $scope.filter.provinces) &&
                item.district === ($scope.filter.districts === "" ? item.district : $scope.filter.districts) &&
                item.groupName === ($scope.filter.requisitionGroups === "" ? item.groupName : $scope.filter.requisitionGroups) &&
                item.active === ($scope.filter.status === "" ? item.active : (String($scope.filter.status) === "true"));
        });
        $scope.totalFacilities = $scope.data.length;
        $scope.paramsChanged($scope.tableParams);
        return;
    };


    var generateParentChildReport = function(data) {
        val = _.uniq(data, function(item, key, a) {
            return item.id;
        });

        _.each(val, function(row) {
            row.facilityProgramReportList = _.chain(data).where({
                id: row.id
            }).map(function(row) {
                return {
                    'name': row.name,
                    'startDate': row.startDate
                };
            }).value();
        });
        return val;
    };


    $scope.init = function () {
        FacilityList.get(null, function(data) {
            $scope.totalFacilities = data.pages.total;
            $scope.data = generateParentChildReport(data.pages.rows);
            $scope.unfilteredFacilities = $scope.data;
            $scope.requisitionGroups = _.uniq(_.pluck($scope.data, 'groupName'));
            $scope.provinces = _.uniq(_.pluck($scope.data, 'province'));
            $scope.districts = _.uniq(_.pluck($scope.data, 'district'));
            $scope.paramsChanged($scope.tableParams);
        });
    };

    $scope.init();

    $scope.exportReport = function(type) {
        var url = '/reports/download/facility-list/' + type ;
        if (type === "mailing-list") {
            url = '/reports/download/facility_mailing_list/pdf';
        }
        window.open(url, '_BLANK');
    };
}