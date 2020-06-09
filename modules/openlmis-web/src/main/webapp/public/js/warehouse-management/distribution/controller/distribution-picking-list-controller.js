/*
 *
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *  Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
function DistributionPickingListController($scope,DisableAsn, programs,facilities, $location, VaccinePendingRequisitions,navigateBackService, $dialog) {
//$scope.orderList=[];
$scope.getPickingList=function () {

if ($scope.distributionForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
            return;
        }

//        console.log(new Date($scope.toDate));

    VaccinePendingRequisitions.get({
                facilityId: parseInt(facilities.id, 10),
                programId: parseInt(programs[0].id, 10)
            }, function (data) {
                $scope.pendingRequisition = data.pendingRequest;
//                lets filter arccoding to the date given
//                console.log($scope.pendingRequisition);
                $scope.orderList=_.filter($scope.pendingRequisition,function(order){

                return order.status!="SUBMITTED" && new Date($scope.toDate)>=new Date(order.orderDate) && new Date($scope.fromDate)<=new Date(order.orderDate);

                });

                console.log($scope.orderList);




            });
  }
}

DistributionPickingListController.resolve = {


    orders: function ($q, $timeout, UserFacilityWithViewVaccineOrderRequisition) {
        var deferred = $q.defer();
        $timeout(function () {
            UserFacilityWithViewVaccineOrderRequisition.get({}, function (data) {
                deferred.resolve(data.facilities);
//                console.log(data.facilities);
            }, {});
        }, 100);
        return deferred.promise;
    },
    programs: function ($q, $timeout, VaccineHomeFacilityPrograms) {
            var deferred = $q.defer();

            $timeout(function () {
                VaccineHomeFacilityPrograms.get({}, function (data) {
                    deferred.resolve(data.programs);
                });
            }, 100);

            return deferred.promise;
        },
        facilities: function ($q, $timeout, UserHomeFacility) {
            var deferred = $q.defer();

            $timeout(function () {
                UserHomeFacility.get({}, function (data) {
                    deferred.resolve(data.homeFacility);
                });
            }, 100);

            return deferred.promise;
        }
};
