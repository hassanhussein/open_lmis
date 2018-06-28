function DailyStockStatusController($scope,$routeParams, messageService,
                                        ngTableParams, $filter,DailyStockStatus,DashboardCommodityStatus) {
    $scope.gap = 5;
    // DashboardFacilityCommodityStatus
    $scope.filteredDailyStatusItems = [];
    $scope.groupedDailyStatusItems = [];
    $scope.itemsPerPage = 1;
    $scope.pagedDailyStatusItems = [];
    $scope.currentDailyStatusPage = 0;
    DashboardCommodityStatus.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {

            $scope.commoditiesStausData=data.commodity;
            $scope.commoditiesStausPivot=_.groupBy($scope.commoditiesStausData,"regionName");
            $scope.commodStatusList=[];
            angular.forEach($scope.commoditiesStausPivot,function (da,index) {
                var col= {
                    name: index,
                    osList: _.filter(da, function (row) {
                        return row.status != "OS";
                    }),
                    usList: _.filter(da, function (row) {
                        return row.status != "US";
                    })
                };

                $scope.commodStatusList.push(col);
            });

        });
    DailyStockStatus.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {

            $scope.dailyStockStatusData=data.dailyStockStatus;
            $scope.dailyStockStatusHeader=_.unique( _.pluck($scope.dailyStockStatusData,"programCode"));
            $scope.dailyStockStatusPivot=_.groupBy($scope.dailyStockStatusData,"name");
            $scope.dataList=[];
            angular.forEach($scope.dailyStockStatusPivot,function (da,index) {
                var col={name:index};
                angular.forEach(da,function (p,i) {
                    col[p.programCode]=p.countOfSubmissions;
                });
                $scope.dataList.push(col);
            });




        });

}
