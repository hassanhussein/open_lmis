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

function ProductListReportController($scope,$window,GetProductReport){

    $scope.data = "";
    $scope.totalProducts = 0;
    $scope.unfilteredProducts = "";
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
    $scope.tracer = [
        {
            'name': 'Yes',
            'value': 'true'
        },
        {
            'name': 'No',
            'value': 'false'
        }
    ];

    $scope.fullSupply = [
        {
            'name': 'Yes',
            'value': 'true'
        },
        {
            'name': 'No',
            'value': 'false'
        }
    ];


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

    $scope.filterFacilities = function () {
        $scope.data =  _.filter($scope.unfilteredProducts, function(item){
            return item.programName === ($scope.filter.programs === "" ? item.programName : $scope.filter.programs) &&
                item.facilityType === ($scope.filter.facilityType === "" ? item.facilityType : $scope.filter.facilityType) &&
                item.category === ($scope.filter.category === "" ? item.category : $scope.filter.category) &&
                item.isActive === ($scope.filter.status === "" ? item.isActive : (String($scope.filter.status) === "true")) &&
                item.fullSupply === ($scope.filter.fullSupply === "" ? item.fullSupply : (String($scope.filter.fullSupply) === "true")) &&
                item.tracer === ($scope.filter.tracer === "" ? item.tracer : (String($scope.filter.tracer) === "true"));
        });
        $scope.data = generateParentChildReport($scope.data);
        $scope.totalProducts = $scope.data.length;
        $scope.paramsChanged($scope.tableParams);
        return;
    };

    $scope.init = function () {
        GetProductReport.get(null, function(data) {
            $scope.data = generateParentChildReport(data.pages.rows);
            $scope.unfilteredProducts = data.pages.rows;
            $scope.totalProducts = $scope.data.length;
            $scope.programs = _.uniq(_.pluck($scope.data, 'programName'));
            $scope.facilityType = _.uniq(_.pluck($scope.data, 'facilityType'));
            $scope.category = _.uniq(_.pluck($scope.data, 'category'));
            $scope.paramsChanged($scope.tableParams);
        });
    };

    $scope.init();

    $scope.exportReport = function (type) {

        $scope.filter.pdformat = 1;
        var url = '/reports/download/product_list_report/' + type ;
        $window.open(url, '_blank');
    };

}