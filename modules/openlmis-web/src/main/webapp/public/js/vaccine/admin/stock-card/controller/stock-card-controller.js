function VaccineStockCardController($scope,SearchVaccineStore,StockCardByFacility) {

    $scope.searchFacility = function(){
        console.log($scope.query);
        $scope.facilities = [];
        SearchVaccineStore.get({searchParam:$scope.query}, function (data) {

            $scope.facilities = data.facilities;
            console.log(data);

        });


    };
  $scope.searchStock = function (data) {
      StockCardByFacility.get(parseInt(data.id, 10)).then(function (data) {
          console.log(data);

      });
  }

}