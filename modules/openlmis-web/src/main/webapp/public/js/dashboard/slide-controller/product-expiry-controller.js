function ProductExpiryController($scope,$routeParams, messageService,
                                        ngTableParams, $filter,DashboardProductExpired) {
    $scope.gap = 5;
    // DashboardFacilityCommodityStatus
    $scope.filteredItems = [];
    $scope.groupedItems = [];
    $scope.itemsPerPage = 5;
    $scope.pagedItems = [];
    $scope.currentPage = 0;
    $scope.filteredOverStockItems = [];
    $scope.groupedOverStockedItems = [];

    $scope.pagedOverStockedItems = [];
    $scope.currentOverStockedPage = 0;
    DashboardProductExpired.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {

            $scope.expiredProducts=data.expiredProducts;
            $scope.expiredProductsPivot=_.groupBy($scope.expiredProducts,"facility");
            $scope.faCommodStatusList=[];
            angular.forEach($scope.expiredProductsPivot,function (da,index) {
                var col= {
                    name: index,
                    productList: da
                };

                $scope.faCommodStatusList.push(col);
            });
            $scope.filteredItems=$scope.faCommodStatusList;
            $scope.groupToPages();
        });

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

    $scope.setOverStockedPage = function () {
        $scope.currentOverStockedPage = this.n;
    };
}
