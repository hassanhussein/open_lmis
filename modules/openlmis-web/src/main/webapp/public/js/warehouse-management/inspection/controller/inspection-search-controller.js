function InspectionSearchController($scope,navigateBackService, $dialog,$location){
$scope.problem=true;

$scope.searchOptions = [
  {value: "asnNumber", name: "ASN Number"},
     {value: "receiptNumber", name: "Receipt Number"},
  ];

  $scope.inspectionListFromServer=[
  {
   pagination: {
      "offset": 0,
      "limit": 10,
      "page": 1,
      "numberOfPages": 1,
      "totalRecords": 1
    },
  inspectionDate:'2019-12-10',
  asnNumber:'asn1',
  supplierId:1,
  portId:1,
  asnDate:'2019-10-10',
  receiptDate:'2019-10-10',
  receiptNumber:'rec1',
  status:'DRAFT',
  poNumber:'ponumber1',
  poDate:'2019-02-10',
  blNumber:'blnumber',
  destionationPort:'Dar Es Salaam',
  vesselNumber:'6878789',
  eta:'2019-02-10',
  ata:'2019-02-10',
  shippingAgent:'Agent A',
  clearingAgent:'MSD',
  note:'This is a note',

  supplier:{
    id:1,
    name:'UNICEF'
    },
  descriptionOfProcurement:'This is the Description',
  isVaccine:true,
  product:{
  name:'BCG',
  oum:'Vials',
  shippedQty:1000,
  countedQty:0,
  passedQty:0,
  failedQty:0,
  lots:[{
  code:'TT454',
  quantity:500,
  expiryDate:'2019-10-1',
  passedQty:0,
  failedQty:0,
  passedLocation:0,
  failedLocation:0,
  vvm:0
  },
  {
    code:'YY454',
    quantity:500,
    expiryDate:'2019-10-11',
    passedQty:0,
    failedQty:0,
    passedLocation:0,
    failedLocation:0,
    vvm:0
    }

  ]
  }

  }
  ]





    function getInspectionList(page, query) {
//      query = query.trim();
//      $scope.searchedQuery = query;
//      Preadvice.get({"searchParam": $scope.searchedQuery, "column": $scope.selectedSearchOption.value, "page": page}, function (data) {
//        $scope.asnList = data.asns;
//        $scope.pagination = data.pagination;
//        $scope.totalItems = $scope.pagination.totalRecords;
//        $scope.currentPage = $scope.pagination.page;
//        $scope.showResults = true;
//        $scope.showSearchResults = true;
//      }, {});


    $scope.inspectionList=$scope.inspectionListFromServer;
    $scope.pagination = $scope.inspectionList[0].pagination;
    $scope.totalItems = $scope.pagination.totalRecords;
    $scope.currentPage = $scope.pagination.page;
    $scope.showResults = true;
    $scope.showSearchResults = true;
    }
  $scope.showResults = false;
  $scope.currentPage = 1;
  $scope.selectedSearchOption = navigateBackService.selectedSearchOption || $scope.searchOptions[0];

  $scope.selectSearchType = function (searchOption) {
    $scope.selectedSearchOption = searchOption;
  };

    // column to sort
     $scope.column = 'asnNumber';

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


      $scope.clearSearch = function () {
         $scope.query = "";
         $scope.totalItems = 0;
         $scope.inspectionList = [];
         $scope.showResults = false;
         angular.element("#searchPreadive").focus();
       };

       $scope.triggerSearch = function (event) {
           if (event.keyCode === 13) {
             $scope.search(1);
           }
         };



  $scope.$on('$viewContentLoaded', function () {
    $scope.query = navigateBackService.query;
  });
   $scope.$watch('currentPage', function () {
      if ($scope.currentPage !== 0)
        $scope.search($scope.currentPage, $scope.searchedQuery);
    });


     $scope.search = function (page, lastQuery) {
//     console.log($scope.query)
        if (!($scope.query || lastQuery)) return;
        lastQuery ? getInspectionList(page, lastQuery) : getInspectionList(page, $scope.query);
      };



      $scope.edit = function (inspection,viewMode) {

          $scope.$parent.inspection=inspection;
          $location.path('inspect/');
        };

}