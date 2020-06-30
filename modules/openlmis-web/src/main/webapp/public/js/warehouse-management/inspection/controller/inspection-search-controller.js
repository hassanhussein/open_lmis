function InspectionSearchController($timeout,$scope,$window ,SearchInspectionByPaged,navigateBackService, $dialog,$location){

if($scope.$parent.messageFlag) {

   $timeout(function(){

        $scope.messageFlag = false;
    },4000);
}

  $scope.inspectionListFromServer=[
  {
   pagination: {
      "offset": 0,
      "limit": 10,
      "page": 1,
      "numberOfPages": 1,
      "totalRecords": 2
    },
  asnNumber:'asn1',
  asnDate:'2019-10-10',
  receiptDate:'2019-10-10',
  receiptNumber:'rec1',
  status:'DRAFT',
  poNumber:'ponumber1',
  poDate:'2019-02-10',
  blNumber:'blnumber',
  destionationPort:'Dar Es Salaam',
  vesselNumber:'6878789',
  eta:'',
  ata:'',
  shippingAgent:'Agent A',
  clearingAgent:'MSD',
  note:'This is a note',
  supplier:{
    id:1,
    name:'UNICEF'
    },
  descriptionOfProcument:'This is the Description',
  isVaccine:true,
  product:{
  name:'BCG',
  oum:'Doses',
  lots:[{
  code:'TT454',
  expiryDate:'2019-10-1',
  },
  {
    code:'YY454',
    expiryDate:'2019-10-11',
    }

  ]
  }

  }
  ];


$scope.searchOptions = [
  {value: "poNumber", name: "PO Number"},
  {value: "receiptNumber", name: "GRN Number"}
  ];


  $scope.showResults = false;

  $scope.currentPage = 1;
  $scope.selectedSearchOption = navigateBackService.selectedSearchOption || $scope.searchOptions[0];

  $scope.selectSearchType = function (searchOption) {
    $scope.selectedSearchOption = searchOption;
  };

    function getInspectionList(page, query) {

     query = query.trim();
     $scope.searchedQuery = query;
     var inspectionList = [];
      SearchInspectionByPaged.get({"searchParam": $scope.searchedQuery, "column": $scope.selectedSearchOption.value, "page": page}, function (data) {

        inspectionList = data.inspections;
        console.log(data.inspections);

        $scope.inspectionList=inspectionList;
        $scope.pagination = data.pagination;
        $scope.totalItems = $scope.pagination.totalRecords;
        $scope.currentPage = $scope.pagination.page;
        $scope.showResults = true;
        $scope.showSearchResults = true;
    });

    }

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


        $scope.print = function (inspectionId){
                  console.log(inspectionId.id);


                   var url = '/rest-api/warehouse/inspection/var/print/'+ parseInt(inspectionId.id,10);

                   $window.open(url, '_blank');



         };


      $scope.edit = function (inspection,viewMode) {

      var path = '/edit/'+parseInt(inspection.id,10);

       $location.path(path);

         // $scope.$parent.inspection=inspection;
          //$location.path('inspect/');
        };

}