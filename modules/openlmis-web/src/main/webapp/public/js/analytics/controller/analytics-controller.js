function AnalyticsFunction($scope,DashboardStockStatusSummaryData,YearFilteredData){



var params = {product:parseInt(2537,0) ,year:parseInt(2017,0), program: parseInt(1,0)};

DashboardStockStatusSummaryData.get(params).then(function(data){
$scope.stockStatuses   = [];

 $scope.stockStatuses = data;

  console.log(data);
});

}

AnalyticsFunction.resolve = {

YearFilteredData: function ($q, $timeout, OperationYears) {
        var deferred = $q.defer();
        $timeout(function () {
            OperationYears.get({}, function (data) {
                deferred.resolve(data.years);
            }, {});
        }, 100);
        return deferred.promise;
 }
 }

