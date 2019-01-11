function AggregateProductExpiryController($scope,$routeParams, messageService,
                                        ngTableParams, $filter,
                                        DashboardAggregateProductExpired) {
    $scope.gap = 5;
    $scope.filteredItems = [];
    $scope.groupedItems = [];
    $scope.itemsPerPage = 5;
    $scope.pagedItems = [];
    $scope.currentPage = 0;
    $scope.filteredOverStockItems = [];
    $scope.groupedOverStockedItems = [];

    $scope.pagedOverStockedItems = [];
    $scope.currentOverStockedPage = 0;

    function loadDashletData(){
        DashboardAggregateProductExpired.get({program : $scope.filter.program},function(data){
                $scope.aggregateExpiry = data.aggregateExpiry;
            });
    }

   loadDashletData();

   $scope.OnFilterChanged = function(){
        loadDashletData();
   };

    // calculate page in place
    $scope.groupToPages = function () {
        $scope.pagedItems = [];
        $scope.pagedOverStockedItems = [];

        for (var i = 0; i < $scope.filteredItems.length; i++) {
            if (i % $scope.itemsPerPage === 0) {
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.filteredItems[i] ];
                $scope.pagedOverStockedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.filteredItems[i] ];
            } else {
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.filteredItems[i]);
                $scope.pagedOverStockedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.filteredItems[i]);
            }
        }
    };

    $scope.range = function (size,start, end) {
        var ret = [];
        console.log(size,start, end);

        if (size < end) {
            end = size;
            start = size-$scope.gap;
        }
        for (var i = start; i < end; i++) {
            ret.push(i);
        }
        console.log(ret);
        return ret;
    };

    $scope.prevPage = function () {
        if ($scope.currentPage > 0) {
            $scope.currentPage--;
        }
    };

    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.pagedItems.length - 1) {
            $scope.currentPage++;
        }
    };

    $scope.setOverStockedPage = function () {
        $scope.currentOverStockedPage = this.n;
    };

    $scope.prevOverStockedPage = function () {
        if ($scope.currentOverStockedPage > 0) {
            $scope.currentOverStockedPage--;
        }
    };

    $scope.nextOverStockedPage = function () {
        if ($scope.currentOverStockedPage < $scope.pagedOverStockedItems.length - 1) {
            $scope.currentOverStockedPage++;
        }
    };

    $scope.setPage = function () {
        $scope.currentPage = this.n;
    };
}
