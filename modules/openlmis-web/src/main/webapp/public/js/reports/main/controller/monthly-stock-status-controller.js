function MonthlyStockStatusController($scope, MonthlyStockStatus, $timeout) {

    $scope.pages = 1;
    $scope.pageSize = 10;

    $scope.exportReport = function(type) {
        $scope.filter.pdformat = 1;
        var params = jQuery.param($scope.getSanitizedParameter());
        var url = '/reports/download/monthly_stock/' + type + '?' + params;
        $window.open(url, "_BLANK");
    };

    $scope.OnFilterChanged = function() {
        // clear old data if there was any
        $scope.filter.page = $scope.page;
        $scope.filter.limit = $scope.pageSize;
        $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;



        MonthlyStockStatus.get($scope.getSanitizedParameter(), function(data) {
            if (data.openLmisResponse !== undefined || data.openLmisResponse.rows !== undefined || data.openLmisResponse.rows.length === 0) {
                $scope.pagination = data.openLmisResponse.pagination;
                $scope.totalItems = 1000;
                $scope.currentPage = $scope.pagination.page;
                $scope.tableParams.total = $scope.totalItems;
                //check if this is last page and reduce totalItemSize so user can not go to next page
                if (data.openLmisResponse.rows.length !== $scope.pageSize) {
                    $scope.totalItems = $scope.pageSize * $scope.page;
                }
                $scope.data = data.openLmisResponse.rows;
                $scope.paramsChanged($scope.tableParams);
            }
        });

    };


    //lisent to currentPage value changes then update page params and call onFilterChanged() to fetch data
    $scope.$watch('currentPage', function() {
        if ($scope.currentPage > 0) {
            $scope.page = $scope.currentPage;
            $timeout(function() {
                $scope.OnFilterChanged();
            }, 100);
        }
    });

}