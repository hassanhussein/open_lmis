function DistributionSearchController($scope,DisableAsn, localStorageService,programs,facilities, $location, VaccinePendingRequisitions,navigateBackService, $dialog){

  $scope.loadRights = function () {
            $scope.rights = localStorageService.get(localStorageKeys.RIGHT);
//            console.log($scope.rights);
     }();

     $scope.hasPermission = function (permission) {
            if ($scope.rights !== undefined && $scope.rights !== null) {
              var rights = JSON.parse($scope.rights);
              var rightNames = _.pluck(rights, 'name');
              return rightNames.indexOf(permission) > -1;
            }
            return false;
      };



        $scope.facilities = facilities;

        $scope.program =  programs;
        if($scope.program.length === 1){
            $scope.selectedProgramId = programs[0].id;
            var selectedFacilityId = facilities.id;

        }









// simulate server data structure here



// $scope.data={
// orders:[{
//                orderNumber:"IVD0001",
//                period:"Sept - Dec 2020",
//                dateSubmitted:"11/09/2020",
//                issue:false,
//                name:"Arusha RVS",
//                ordered:[{
//                  productId:343,
//                  product:"BCG",
//                  amount:35343,
//                  gap:'',
//                  given:[]
//                },
//                {
//                  productId:34,
//                  product:"PCV",
//                  amount:316,
//                  gap:'',
//                  given:[]
//                }
//
//              ],
//
//              },
//              {
//                orderNumber:"IVD0002",
//                period:"Jan - Feb 2020",
//                dateSubmitted:"11/01/2020",
//                     issue:false,
//
//                name:"Dodoma RVS",
//                ordered:[{
//                  productId:343,
//                  product:"BCG",
//                  amount:753,
//                  gap:'',
//                  given:[]
//                },
//                {ConsolidatedOrdersList
//                  productId:34,
//                  product:"PCV",
//                  amount:300,
//                  gap:'',
//                  given:[]
//                }
//
//              ],
//              },
//              {
//                              orderNumber:"IVD0003",
//                              period:"Apr- Jun 2020",
//                              dateSubmitted:"11/04/2020",
//                                              issue:false,
//
//                              name:"Tanga RVS",
//                              ordered:[{
//                                productId:343,
//                                product:"BCG",
//                                amount:853,
//                                gap:'',
//                                given:[]
//                              },
//                              {
//                                productId:34,
//                                product:"PCV",
//                                amount:456,
//                                gap:'',
//                                given:[]
//
//                              }
//
//                            ],
//
//
//                            }
//
//            ],
// pagination:{
// totalRecords:3,
// page:1,
// limit:10,
// }
//
// };

// end of server data structure simulation






  $scope.searchOptions = [
    {value: "region", name: "Region"}
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



    VaccinePendingRequisitions.get({
                facilityId: parseInt(facilities.id, 10),
                programId: parseInt(programs[0].id, 10)
            }, function (data) {
                $scope.pendingRequisition = data.pendingRequest;
                $scope.orders=[];
                console.log($scope.pendingRequisition);
                $scope.pendingRequisition.forEach(function(order){
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


  $scope.singleIssue=function(order){
        $scope.$parent.orders=[];
        $scope.$parent.orders.push(order);
        $location.path('/create/'+parseInt(facilities.id, 10));



  };


  $scope.amend=function(order){
          $scope.$parent.orders=[];
          $scope.$parent.orders.push(order);
          $location.path('/create/'+parseInt(facilities.id, 10));



    };




}


DistributionSearchController.resolve = {

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