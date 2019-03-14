function ItemFillRateSummaryFunction($scope,GetItemFillRateSummary){

 $scope.OnFilterChanged = function () {
        // clear old data if there was any
        $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;

        GetItemFillRateSummary.get({}, function (data) {
            if (data.colors !== undefined) {
                $scope.data = data.colors;
                $scope.paramsChanged($scope.tableParams);
            }
        });

    };

}