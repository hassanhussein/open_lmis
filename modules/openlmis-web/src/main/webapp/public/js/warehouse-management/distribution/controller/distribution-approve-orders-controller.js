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
function DistributionApproveOrdersController($scope,DisableAsn,$window, programs,facilities, $location, VaccinePendingRequisitions,navigateBackService, $dialog) {



$scope.pod = function (order) {
var url = '/wms-reports/proof-delivery-report/'+order.id;
$window.open(url, '_blank');
};


$scope.invoice = function (order) {
var url = '/wms-reports/invoice-report/'+order.id;
$window.open(url, '_blank');
};




  $scope.searchOptions = [
    {value: "region", name: "Region"},
    {value: "picklistid", name: "Pick List ID"},
    {value: "orderid", name: "Order ID"}
  ];

//  $scope.$parent.asnViewMode=false;



  $scope.showResults = false;
  $scope.currentPage = 1;
  $scope.selectedSearchOption = navigateBackService.selectedSearchOption || $scope.searchOptions[0];

  $scope.selectSearchType = function (searchOption) {
    $scope.selectedSearchOption = searchOption;
  };


 // column to sort
 $scope.column = 'asnnumber';

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

  $scope.edit = function (id,viewMode) {
//    var data = {query: $scope.query, selectedSearchOption: $scope.selectedSearchOption};
//    navigateBackService.setData(data);
//    console.log(id)
    if(!viewMode){
                $scope.$parent.asnViewMode=false;
        $location.path('edit/' + id);

    }else{
            $scope.$parent.asnViewMode=true;
        $location.path('view/' + id);

    }
  };

  $scope.$watch('currentPage', function () {
    if ($scope.currentPage !== 0)
      $scope.search($scope.currentPage, $scope.searchedQuery);
  });

  $scope.search = function (page, lastQuery) {
    if (!($scope.query || lastQuery)) return;
    lastQuery ? getPreadvice(page, lastQuery) : getPreadvice(page, $scope.query);
  };

  function getPreadvice(page, query) {
    query = query.trim();
    $scope.searchedQuery = query;

//          $scope.orderList = $scope.data.orders;
//          $scope.pagination = $scope.data.pagination;
//          $scope.totalItems = $scope.pagination.totalRecords;
//          $scope.currentPage = $scope.pagination.page;
//          $scope.showResults = true;
//          $scope.showSearchResults = true;
//    Preadvice.get({"searchParam": $scope.searchedQuery, "column": $scope.selectedSearchOption.value, "page": page}, function (data) {
//      $scope.asnList = data.asns;
//      $scope.pagination = data.pagination;
//      $scope.totalItems = $scope.pagination.totalRecords;
//      $scope.currentPage = $scope.pagination.page;
//      $scope.showResults = true;
//      $scope.showSearchResults = true;
//    }, {});

//alert("done"+query);


    VaccinePendingRequisitions.get({"searchParam": $scope.searchedQuery,"column": $scope.selectedSearchOption.value,
                facilityId: parseInt(facilities.id, 10),
                programId: parseInt(programs[0].id, 10)
            }, function (data) {
                $scope.pendingRequisition = data.pendingRequest;
                $scope.orders=[];
                console.log($scope.pendingRequisition);
                $scope.pendingRequisition.forEach(function(order){

                 if(order.status!='SUBMITTED'){

                 $scope.orders.push({
                                                     id:order.id,
                                                     orderNumber:"IVD"+order.id,
                                                     period:order.periodName,
                                                     dateSubmitted:order.orderDate,
                                                     issue:false,
                                                     status:order.status,
                                                     name:order.facilityName,
                                                     ordered:[]
                                 });
                 }


                });
    $scope.data={};
    $scope.data.orders=$scope.orders;
    $scope.data.pagination={};
    $scope.data.pagination.totalRecords=$scope.orders.length;
    $scope.data.pagination.limit=$scope.orders.length;

    $scope.orderList = $scope.data.orders;

              $scope.pagination = $scope.data.pagination;
              $scope.totalItems = $scope.pagination.totalRecords;
              $scope.currentPage = $scope.pagination.page;
              $scope.showResults = true;
              $scope.showSearchResults = true;
            });
  }

  getPreadvice(1,"%");

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


  $scope.issue=function(){
    $scope.$parent.orders=[];

   angular.forEach($scope.orderList,function(order){
   if(order.issue===true){
   $scope.$parent.orders.push(order);
   }
   });

   $location.path('/create/'+parseInt(facilities.id, 10));

//   $window.location = '/public/pages/warehouse-management/distribution/index.html#/create/'+ parseInt(programs[0].id, 10)+'/'+parseInt(facilities.id, 10);


  };


  $scope.singleApprove=function(order){
        $scope.$parent.orders=[];
        $scope.$parent.orders.push(order);
        $location.path('/create/'+parseInt(facilities.id, 10));



  };




}




DistributionApproveOrdersController.resolve = {


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
