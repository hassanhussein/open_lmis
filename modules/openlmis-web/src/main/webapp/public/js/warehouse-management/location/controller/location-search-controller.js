/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function LocationSearchController($location,$timeout,$scope,WareHouseList,navigateBackService,SaveBinLocation,updateBinLocation,SearchBinByPaged) {

console.log( $scope.newLocation);
$scope.locationCreated="false";


WareHouseList.get({},function(data){

//console.log(data.house);
$scope.warehouses=data.house;
});
 $scope.searchOptions = [
    {value: "code", name: "Code"}
  ];

  $scope.showResults = false;
  $scope.currentPage = 1;
  $scope.selectedSearchOption = navigateBackService.selectedSearchOption || $scope.searchOptions[0];

  $scope.selectSearchType = function (searchOption) {
    $scope.selectedSearchOption = searchOption;
  };

  $scope.$on('$viewContentLoaded', function () {
    $scope.query = navigateBackService.query;
  });

  $scope.$watch('currentPage', function () {
      if ($scope.currentPage !== 0)
        $scope.search($scope.currentPage, $scope.searchedQuery);
    });

    $scope.search = function (page, lastQuery) {
    console.log($scope.warehouseId);
      if (!($scope.query || lastQuery)) return;
      lastQuery ? getLocations(page, lastQuery) : getLocations(page, $scope.query);
    };

    function getLocations(page, query) {
      query = query.trim();
      var warehouseId = $scope.warehouseId;
      $scope.searchedQuery = query;
      SearchBinByPaged.get({"searchParam": $scope.searchedQuery, "columnName": $scope.selectedSearchOption.value, "page": page,"wareHouseId":warehouseId}, function (data) {
        $scope.locationList = data.bins;
        $scope.pagination = data.pagination;
        $scope.totalItems = $scope.pagination.totalRecords;
        $scope.currentPage = $scope.pagination.page;
        $scope.showResults = true;
      }, {});
    }

    $scope.clearSearch = function () {
      $scope.query = "";
      $scope.totalItems = 0;
      $scope.locationList = [];
      $scope.showResults = false;
      angular.element("#searchLocation").focus();
    };

    $scope.triggerSearch = function (event) {
      if (event.keyCode === 13) {
        $scope.search(1);
      }
    };

      $scope.showNewLocationModal = function(product) {
            $scope.newLocationModal = true;
            $scope.newLot = {};
            $scope.newLot.product = product;
            $scope.error = "";

        };

        $scope.closeNewLocationModal = function() {
            $scope.newLoocation = {};
            $scope.newLocationModal = false;
        };

      $scope.updateLocations=function(){

      };

      $scope.clearEditMode=function(location){

      location.editMode=false;
      };

       $scope.enterEditMode=function(location){

            location.editMode=true;
            };

       var success = function (data) {
          $scope.error = "";
          $scope.$parent.message = data.success;
          //$scope.$parent.locationId = data.location.id;
          $scope.showError = false;
         // $location.path('');
        };

        var error = function (data) {

        if(data.status === 400) {
            console.log(data);
                  $scope.$parent.message = "";
                  $scope.error = data.data.error;
                  $scope.showError = true;
        } else {

        success(data);
        }

        };


      $scope.editBin=function(row){
      var newLocation = {};
                    newLocation.code = row.code;
                    newLocation.name = row.name;
                    newLocation.displayOrder = parseInt(row.displayOrder,10);
                    newLocation.active=row.active;
                    newLocation.warehouseId=parseInt($scope.warehouseId,10);
                    updateBinLocation.update({id:parseInt(row.id,10)},newLocation ,function(data) {
                        console.log(data);
                        row.editMode=false;
                    });

      };


       $scope.createLocation = function() {
       console.log('cacae');
              var newLocation = {};
              newLocation.code = $scope.newLocation.code;
              newLocation.name = $scope.newLocation.name;
              newLocation.displayOrder = parseInt($scope.newLocation.type,10);
              newLocation.active=true;
              newLocation.warehouseId=parseInt($scope.warehouseId,10);
              SaveBinLocation.save(newLocation, function(data) {
                  $scope.newLocationModal = false;
                  console.log(data);
                  $scope.locationCreated=true;

                  $timeout(function(){
                   $scope.locationCreated=false;
                  },3000);
                   $scope.newLocation.code="";
                   $scope.newLocation.name="";
                   $scope.newLocation.type="";
              }, error);
          };
}
