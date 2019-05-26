function ItemFillRateSummaryController($scope,$timeout,$window,GetItemFillRateSummary){

    $scope.exportReport = function (type) {
        $scope.filter.pdformat = 1;
        var params = jQuery.param($scope.getSanitizedParameter());
        var url = '/reports/download/item_fill_rate_report/' + type + '?' + params;
        $window.open(url, "_BLANK");
    };

          $scope.currentPage = 1;

         $scope.OnFilterChanged = function () { };


         $scope.searchReport = function() {

             // clear old data if there was any
                 $scope.data = $scope.datarows = [];
                 $scope.filter.page = $scope.page;
                 console.log($scope.getSanitizedParameter());

                 GetItemFillRateSummary.get($scope.getSanitizedParameter(), function (data) {

                     if (data.openLmisResponse.rows !== undefined) {
                                  console.log(data);
                          $scope.pagination = data.openLmisResponse.pagination;
                          $scope.totalItems = 100;
                          $scope.currentPage = $scope.pagination.page;



                         $scope.data = data.openLmisResponse.rows;
                         $scope.paramsChanged($scope.tableParams);
                     }
                 });




    };


          $scope.$watch('currentPage', function () {
             if ($scope.currentPage > 0) {
               $scope.page = $scope.currentPage;
               $timeout(function(){
               $scope.searchReport();
               },100);
             }
           });


}