/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */
function PutawaySearchController($scope, $location,putaway, navigateBackService, $dialog,SearchPutAway){


      $scope.putawayList =putaway.aways ;
      $scope.pagination = putaway.pagination;
      $scope.totalItems = $scope.pagination.totalRecords;
      $scope.currentPage = $scope.pagination.page;
      $scope.showResults = true;
      $scope.showSearchResults = true;




  $scope.searchOptions = [
    {value: "poNumber", name: "PO Number"},

  ];

  $scope.$parent.putawayViewMode=false;


//  $scope.showResults = false;
  $scope.currentPage = 1;
  $scope.selectedSearchOption = navigateBackService.selectedSearchOption || $scope.searchOptions[0];

  $scope.selectSearchType = function (searchOption) {
    $scope.selectedSearchOption = searchOption;
  };


 // column to sort
 $scope.column = 'poNumber';

 // sort ordering (Ascending or Descending). Set true for desending
 $scope.reverse = false;

 // called on header click
 $scope.sortColumn = function(col){
  $scope.column = col;
  if($scope.reverse){
   $scope.reverse = false;
   $scope.reverseclass = 'arrow-up';
  }else{
   $scope.reverse = true;
   $scope.reverseclass = 'arrow-down';
  }
 };

 // remove and change class
 $scope.sortClass = function(col){
  if($scope.column == col ){
   if($scope.reverse){
    return 'arrow-down';
   }else{
    return 'arrow-up';
   }
  }else{
   return '';
  }
 };


  $scope.$on('$viewContentLoaded', function () {
    $scope.query = navigateBackService.query;
  });



   $scope.edit = function (id) {

          $location.path('create/'+id);
    };



  $scope.$watch('currentPage', function () {
    if ($scope.currentPage !== 0)
      $scope.search($scope.currentPage, $scope.searchedQuery);
  });

  $scope.search = function (page, lastQuery) {
   console.log('am here');
    if (!($scope.query || lastQuery)) return;
    lastQuery ? getPutaway(page, lastQuery) : getPutaway(page, $scope.query);
  };

  function getPutaway(page, query) {
    query = query.trim();
    $scope.searchedQuery = query;
    SearchPutAway.get({"searchParam": $scope.searchedQuery, "column": $scope.selectedSearchOption.value, "page": page}, function (data) {
      $scope.putawayList = data.aways;
      $scope.pagination = data.pagination;
      $scope.totalItems = $scope.pagination.totalRecords;
      $scope.currentPage = $scope.pagination.page;
      $scope.showResults = true;
      console.log($scope.putawayList);

      $scope.showSearchResults = true;
    }, {});
  }

  $scope.clearSearch = function () {
    $scope.query = "";
    $scope.totalItems = 0;
    $scope.asnList = [];
    $scope.showResults = false;
    angular.element("#searchPreadive").focus();
  };


  $scope.triggerSearch = function (event) {
    if (event.keyCode === 13) {
      $scope.search(1);
    }
  };


}


PutawaySearchController.resolve = {

    putaway: function($q, $route, $timeout, SearchPutAway) {


        var deferred = $q.defer();


        $timeout(function() {
            SearchPutAway.get({"searchParam":'%', "column": 'poNumber', "page": 1}, function(data) {

                deferred.resolve(data);
                console.log(data);

            }, {});
        }, 100);
        return deferred.promise;
    }

};