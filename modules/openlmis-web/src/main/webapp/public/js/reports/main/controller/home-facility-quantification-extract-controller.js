function HomeFacilityQuantificationExtractController($scope, $window , QuantificationHomeFacilityExtractReport) {


    $scope.exportReport   = function (type){
        $scope.filter.pdformat = 1;
         console.log($scope.filter);
        var params = jQuery.param($scope.getSanitizedParameter());
        var url = '/reports/download/home_facility_quantification_extract/' + type + '?' + params;
        $window.open(url, '_blank');
    };

    $scope.currentPage = 1;
    $scope.pageSize = 10;
    $scope.page = 1;
    $scope.filter = {};



$scope.searchQuantificationReport = function(){

        $scope.filter.page = $scope.page;
        $scope.filter.limit = $scope.pageSize;
        $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;

        $scope.countFactor = $scope.pageSize * ($scope.page - 1);
        var allParams = angular.extend($scope.filter, $scope.getSanitizedParameter());

         $scope.facilityName = allParams.facilityName;
         $scope.facilityCode = allParams.facilityCode;
         $scope.requisitionGroup = allParams.requisitionGroup;

        QuantificationHomeFacilityExtractReport.get(allParams, function(data) {
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
            $scope.searchQuantificationReport();
        }
    });


}