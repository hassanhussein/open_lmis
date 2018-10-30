function RnrStatusChangeTimeLineController($scope,$routeParams, messageService,
                                        ngTableParams, $filter,RnRTimeLines) {
    $scope.gap = 5;
    // DashboardFacilityCommodityStatus
    $scope.filteredItems = [];
    $scope.groupedItems = [];
    $scope.itemsPerPage = 1;
    $scope.pagedItems = [];
    $scope.currentPage = 0;
    $scope.filteredOverStockItems = [];
    $scope.groupedOverStockedItems = [];
    $scope.itemsPerPage = 1;
    $scope.pagedOverStockedItems = [];
    $scope.currentOverStockedPage = 0;
    RnRTimeLines.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {

            $scope.rnrStatusTimeLines =data.rnrStatusTimeLine;


        });

}
