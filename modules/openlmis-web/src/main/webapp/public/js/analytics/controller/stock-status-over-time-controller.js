function StockStatusOverTimeFunctionController($scope,$rootScope,DashboardStockStatusSummaryData,StockStatusByProgramData,GetProductById, Program,Period,$location) {

$rootScope.loadStockStatusByProgramTrends = function (params, level) {
var stockSummary = [];

var chartId ='stockStatusOverTime';

DashboardStockStatusSummaryData.get(params).then(function(stocks) {
    console.log (stocks);
$scope.stockStatusesTitle = 'Stock Status Over Time for '+params.programName+', '+params.productName+', '+params.year;



 if(!isUndefined(stocks)){

 stockSummary = stocks;
   console.log(stocks);
  var category = _.uniq(_.pluck(stockSummary,'periodname'));


  $rootScope.drawTheChart(stocks,chartId,'',category,'DASHLET_STOCK_STATUS_OVER_TIME','Stock Status Over Time');

 }

});

};


 //Filters


    $scope.OnFilterChanged = function () {



    var programName = '';
    Program.get({id: parseInt($location.search().program,10)}, function(da){
    programName = da.program.name;

  var productName = '';
    GetProductById.get({id:parseInt($location.search().product,10)}, function(dat){
            productName = dat.productDTO.product.primaryName;





    //Period.get({id: parseInt($location.search().period,10)}, function(da){
    //periodName = da.period.name;

    $location.search().programName = programName;
    $location.search().productName = productName;

    console.log($location.search());


    $scope.$parent.params = $location.search();

    $rootScope.loadStockStatusByProgramTrends($location.search(),null);

    });

    //});


    });

    };





}