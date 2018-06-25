function MonthlyStockStatusController($scope,MonthlyStockStatus) {

    $scope.exportReport = function (type) {
        $scope.filter.pdformat = 1;
        var params = jQuery.param($scope.getSanitizedParameter());
        var url = '/reports/download/monthly_stock/' + type + '?' + params;
        $window.open(url, "_BLANK");
    };

    $scope.OnFilterChanged = function () {
        // clear old data if there was any
        $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;

        MonthlyStockStatus.get($scope.getSanitizedParameter(), function (data) {
            console.log(data);
            if (data.pages !== undefined && data.pages.rows !== undefined) {
                $scope.data = data.pages.rows;
                $scope.paramsChanged($scope.tableParams);
            }
        });

    };
}