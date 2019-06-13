function ItemFillRateSummaryController($scope,$q,$timeout,$window,GetItemFillRateSummary){

    $scope.exportReport = function (type) {

        $scope.filter.limit = 100000;
        $scope.filter.page  = 1;
        $scope.filter.pdformat = 1;
        var params = jQuery.param($scope.getSanitizedParameter());

        var allow = $scope.allPrinting($scope.getSanitizedParameter());
         allow.then(function(){

           var url = '/reports/download/item_fill_rate_report/' + type + '?' + params;
                 $window.open(url, "_BLANK");

         });


    };

   $scope.currentPage = 1;
   $scope.pageSize = 10;

    $scope.allPrinting = function(params){
    var deferred = $q.defer();

    GetItemFillRateSummary.get(params, function (data) {

    if(data.openLmisResponse.rows.length  > 0){

    deferred.resolve();
    }

    });

    return deferred.promise;


    };
    $scope.filter ={};
    $scope.OnFilterChanged = function () { };

    $scope.searchReport = function() {
        // clear old data if there was any
        $scope.data = $scope.datarows = [];
        $scope.filter.page = $scope.page;
        $scope.filter.limit = $scope.pageSize;
        $scope.filter.page = $scope.page;
        //variable to manage counts on pagination
        $scope.countFactor = $scope.pageSize * ($scope.page - 1);
         GetItemFillRateSummary.get($scope.getSanitizedParameter(), function (data) {
        if (data.openLmisResponse.rows !== undefined) {
        $scope.pagination = data.openLmisResponse.pagination;
        $scope.totalItems = 1000;
        $scope.currentPage = $scope.pagination.page;
        if (data.openLmisResponse.rows.length !== $scope.pageSize) {
        $scope.totalItems = $scope.pageSize * $scope.page;
         }
        $scope.data = data.openLmisResponse.rows;
        $scope.tableParams.total = $scope.totalItems;
        $scope.paramsChanged($scope.tableParams);
        }
        });

    };


      //listen to currentPage value changes then update page params and call onFilterChanged() to fetch data
    $scope.$watch('currentPage', function() {

    if($scope.page !== $scope.currentPage)
          {
              if ($scope.currentPage > 0) {
                  $scope.page = $scope.currentPage;
                  $timeout(function() {
                      $scope.searchReport();
                  }, 100);
              }
          }

      });

    }