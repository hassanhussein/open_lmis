function SearchWarehouseController($scope,navigateBackService,SearchWareHouses,$location) {

  $scope.searchOptions = [
    {value: "name", name: "Name"}
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

  console.log(id);
    var data = {query: $scope.query, selectedSearchOption: $scope.selectedSearchOption};
    navigateBackService.setData(data);
    $location.path('edit/' + id);
  };

  $scope.$watch('currentPage', function () {
    if ($scope.currentPage !== 0)
      $scope.search($scope.currentPage, $scope.searchedQuery);
  });

    $scope.search = function (page, lastQuery) {
      if (!($scope.query || lastQuery)) return;
      lastQuery ? getWareHouses(page, lastQuery) : getWareHouses(page, $scope.query);
    };

    function getWareHouses(page, query) {
      query = query.trim();
      $scope.searchedQuery = query;
      SearchWareHouses.get({"searchParam": $scope.searchedQuery, "columnName": $scope.selectedSearchOption.value, "page": page}, function (data) {
        $scope.wareHouseList = data.houses;
        $scope.pagination = data.pagination;
        $scope.totalItems = $scope.pagination.totalRecords;
        $scope.currentPage = $scope.pagination.page;
        $scope.showResults = true;
      }, {});
    }

    $scope.clearSearch = function () {
      $scope.query = "";
      $scope.totalItems = 0;
      $scope.wareHouseList = [];
      $scope.showResults = false;
      angular.element("#searchGeoZone").focus();
    };

    $scope.triggerSearch = function (event) {
      if (event.keyCode === 13) {
        $scope.search(1);
      }
    };

}