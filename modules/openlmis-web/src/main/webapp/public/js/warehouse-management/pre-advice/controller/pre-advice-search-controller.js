function PreAdviceSearchController($scope, Preadvice, $location, navigateBackService, $dialog){

  $scope.searchOptions = [
    {value: "ponumber", name: "PO Number"},
    {value: "asnumber", name: "Asn Number"},
    {value: "supplier", name: "Supplier"}
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

  $scope.edit = function (id) {
//    var data = {query: $scope.query, selectedSearchOption: $scope.selectedSearchOption};
//    navigateBackService.setData(data);
    console.log(id)
    $location.path('edit/' + id);
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
    Preadvice.get({"searchParam": $scope.searchedQuery, "column": $scope.selectedSearchOption.value, "page": page}, function (data) {
      $scope.asnList = data.asns;
      $scope.pagination = data.pagination;
      $scope.totalItems = $scope.pagination.totalRecords;
      $scope.currentPage = $scope.pagination.page;
      $scope.showResults = true;
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



  $scope.deleteAsn=function(id,index)
      {
           var callBack=function(results){
             if(results){
                $scope.asnList.splice(index,1);
             }
           };

           var options = {
                          id: "confirmDialog",
                          header: "Confirm Remove ASN",
                          body: "Are you sure you want to delete the ASN"
                      };
           OpenLmisDialog.newDialog(options, callBack, $dialog);
       };
}