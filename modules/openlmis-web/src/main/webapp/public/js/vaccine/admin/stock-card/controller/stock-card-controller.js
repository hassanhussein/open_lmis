function VaccineStockCardController($scope,SearchVaccineStore,StockCardByFacility,updateTotalStockOnHand) {

    $scope.searchFacility = function(){
        console.log($scope.query);
        $scope.facilities = [];
        SearchVaccineStore.get({searchParam:$scope.query}, function (data) {

            $scope.facilities = data.facilities;

        });


    };

    $scope.searchStock = function (data) {
        $scope.stockCards = [];
      StockCardByFacility.get(parseInt(data.id, 10)).then(function (data) {

          $scope.stockCards = data;

      });
  };
    $scope.updateStockOnHand = function(stock){
        var totalQuantity = 0;
        angular.forEach(stock.lotsOnHand,function (q) {
            totalQuantity += q.quantityOnHand;
        });
        updateTotalStockOnHand.update({id:parseInt(stock.id,10),total:parseInt(totalQuantity,10)},function (data) {

        });

    };
    $scope.changeColor = function(color){
        if(color)
            return 'red';
        else
            return 'blue';
    };

$scope.getTotalRange = function (lotOnHand,total2) {
   var total = 0;
    angular.forEach(lotOnHand,function (q) {
        total += q.quantityOnHand;
    });
    return parseInt(total, 10) !== parseInt(total2, 10);
};
}