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


function MyRequestsController($scope, PendingRequests, SaveMaintenanceRequest, SaveAndLogMaintenanceRequest, messageService, $routeParams, $location) {
console.log('reached here');

  $scope.$parent.message = '';

  //The following variables store information for the currently selected maintenance request
  $scope.servicePerformedForCurrent = null;
  $scope.findingForCurrent = null;
  $scope.nextDateOfServiceForCurrent = null;

  PendingRequests.get(function (data) {
  console.log(data);
    $scope.list = data.logs;
  });

  $scope.respondToRequest = function (maintenanceRequest) {
    $scope.currentRequest = maintenanceRequest;
    $scope.maintenanceRequestResponseModal = true;
  };

  $scope.closeModal = function () {
    $scope.maintenanceRequestResponseModal = false;
    $scope.currentRequest = null;
  };

  $scope.saveResponse = function () {
    var successHandler = function (response) {
      $scope.error = "";
      $location.path('#/');
      $scope.closeModal();
    };

    var errorHandler = function (response) {
      $scope.error = messageService.get(response.data.error);
    };
    $scope.currentRequest.resolved = true;

      console.log( $scope.currentRequest);

    SaveMaintenanceRequest.save($scope.currentRequest, successHandler, errorHandler);
    SaveAndLogMaintenanceRequest.save($scope.currentRequest, successHandler, errorHandler);
  };

  $scope.filterLogs = function () {
    var filteredLogs = [];
    var query = $scope.query || "";

      filteredLogs = $.grep($scope.list, function (log) {
      return log.equipmentName.toLowerCase().indexOf(query.toLowerCase()) != -1;
    });

    if($scope.query === ""){
       PendingRequests.get(function (data) {
       console.log(data);
         $scope.list = data.logs;
       });
    } else
    $scope.list = filteredLogs;
  };

}