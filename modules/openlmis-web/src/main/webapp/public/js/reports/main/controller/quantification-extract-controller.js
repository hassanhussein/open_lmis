function QuantificationExtractController($scope, $window , QuantificationExtractReport) {


    $scope.exportReport   = function (type){
        $scope.filter.pdformat = 1;
        var params = jQuery.param($scope.getSanitizedParameter());
        var url = '/reports/download/quantification_extract/' + type + '?' + params;
        $window.open(url, '_blank');
    };

    $scope.currentPage = 1;
    $scope.pageSize = 10;


    $scope.OnFilterChanged = function() {
        console.log($scope.filter);

        $scope.countFactor = $scope.pageSize * ($scope.page - 1 );
        $scope.filter.limit = $scope.pageSize;
        $scope.filter.page = $scope.page;

        // clear old data if there was any
        $scope.data  = $scope.xml = $scope.datarows = [];
        $scope.filter.max = 10000;
        $scope.filter.limit = 100;
        QuantificationExtractReport.get($scope.getSanitizedParameter(), function(data) {
            $scope.data = data.rows;
         if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {

             var adjustments = data.openLmisResponse.rows;
             $scope.data =_.where(adjustments,{pagination:null});

             $scope.pagination = adjustments[adjustments.length-1].pagination;
             $scope.totalItems = $scope.pagination.totalRecords;
             $scope.currentPage = $scope.pagination.page;
             $scope.tableParams.total = $scope.totalItems;
             $scope.paramsChanged($scope.tableParams);

         }
        });
        $scope.applyUrl();

    };


    $scope.$watch('currentPage', function () {
        if ($scope.currentPage > 0) {
            $scope.page = $scope.currentPage;
            $scope.OnFilterChanged();
        }
    });


}