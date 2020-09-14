function ProductListReportController($scope,$window,GetProductReport,$filter){

    $scope.exportReport = function (type) {

             $scope.filter.pdformat = 1;
                var params = jQuery.param($scope.getSanitizedParameter());
                var url = '/reports/download/product_list_report/' + type + '?' + params;
                $window.open(url, '_blank');
    };

    $scope.OnFilterChanged = function() {
        // clear old data if there was any
        $scope.data = $scope.datarows = [];
        GetProductReport.get($scope.getSanitizedParameter(), function (data) {
            if (data.pages !== undefined && data.pages.rows !== undefined) {
                $scope.data = data.pages.rows;
                $scope.paramsChanged($scope.tableParams);
            }
        });
    };
}