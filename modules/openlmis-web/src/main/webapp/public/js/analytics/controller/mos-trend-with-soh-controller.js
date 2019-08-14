function mosTrendWithSOHController ($scope,Program,Period,$location,$rootScope,ConsumptionTrendSummaryData){



$rootScope.loadConsumptionTrendSummary = function (params) {
     console.log(params);
     //params.product = '1272';
     //params.period = parseInt(95,10);
   //  params.program = parseInt(1,10);

ConsumptionTrendSummaryData.get(params).then(function(data){
console.log(data);

   var productTitle = _.uniq(data, function(dx){
                    return dx.productname;
   });

   var str = '';

   productTitle.forEach(function(product) {

    str += "MOS and SOH Trend Of " + product.productname + ", "+params.periodName+', '+params.year;

   });
   console.log(str);
  $rootScope.mos_soh_title = str;

    if(!isUndefined(data)) {

     var categories = _.pluck(data, 'periodname');
     var amc = _.pluck(data, 'amc');
     var soh = _.pluck(data, 'soh');
     var id = 'mos-soh-chart';

     showTheChart(categories,amc,soh,id);

    }

});

};

function showTheChart(categories,amc,soh,id) {

Highcharts.chart(id, {
    chart: {
        zoomType: 'xy'
    },
    credits:{
    enabled:false
    },
    title: {
        text: ''
    },
    subtitle: {
        text: ''
    },
    xAxis: [{
        categories:categories,
        crosshair: true
    }],
    yAxis: [{ // Primary yAxis
        labels: {
            format: '{value} ',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        title: {
            text: 'AMC',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        }
    }, { // Secondary yAxis
        title: {
            text: 'SOH',
            style: {
                color: Highcharts.getOptions().colors[0]
            }
        },
        labels: {
            format: '{value} ',
            style: {
                color: Highcharts.getOptions().colors[0]
            }
        },
        opposite: true
    }],
    tooltip: {
        shared: true
    },
    legend: {
        layout: 'horizontal',
        align: 'left',
        x: 120,
        verticalAlign: 'bottom',
        y: 100,
        floating: true,
        backgroundColor:
            Highcharts.defaultOptions.legend.backgroundColor || // theme
            'rgba(255,255,255,0.25)'
    },
    series: [{
        name: 'SOH',
        type: 'column',
        yAxis: 1,
        data: soh,
        tooltip: {
            valueSuffix: ' '
        }

    }, {
        name: 'AMC',
        type: 'spline',
        data: amc,
        tooltip: {
            valueSuffix: ' '
        }
    }]
});


}


$scope.OnFilterChanged = function () {

var programName = '';
Program.get({id: parseInt($location.search().program,10)}, function(da){
programName = da.program.name;

var periodName = '';
Period.get({id: parseInt($location.search().period,10)}, function(da){
periodName = da.period.name;

$location.search().programName = programName;
$location.search().periodName = periodName;

$scope.$parent.params = $location.search();

$rootScope.loadConsumptionTrendSummary($location.search());


});


});






};






}