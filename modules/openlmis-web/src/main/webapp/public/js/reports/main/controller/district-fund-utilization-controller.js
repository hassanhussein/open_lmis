function FacilityFundUtilizationReportController ($scope, $window,FacilityFundUtilizationReport) {

    $scope.OnFilterChanged = function(){
         // clear old data if there was any
         $scope.data = $scope.datarows = [];

         $scope.filter.max = 10000;
        $scope.filter.page = 1;

         FacilityFundUtilizationReport.get($scope.getSanitizedParameter(), function (data) {
         console.log(data);
             if (data.pages !== undefined && data.pages.rows !== undefined) {
                 $scope.data = data.pages.rows;
                 console.log(JSON.stringify($scope.data));
                 $scope.paramsChanged($scope.tableParams);
             }
         });
     };

     $scope.exportReport = function (type) {
         $scope.filter.pdformat = 1;
         var params = jQuery.param($scope.getSanitizedParameter());
         var url = '/reports/download/district_fund_utilization/' + type + '?' + params;
         $window.open(url);
     };

}