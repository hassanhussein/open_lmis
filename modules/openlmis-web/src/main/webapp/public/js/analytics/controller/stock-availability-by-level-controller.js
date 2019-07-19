function StockAvailabilityByLevelController ($scope, $rootScope,StockAvailableByLevelData){


$rootScope.loadStockAvailableByLevel = function(params){

    StockAvailableByLevelData.get(params).then(function(data){

     var categories = _.pluck(data, 'facilitytype');
     var hospital = _.filter(data, {facilitytype:'HOSPITAL'});
     console.log(hospital);
     var phc = _.filter(data, {facilitytype:'Primary Health Care'});
        var dataV = [{name:'Hospital', data:[hospital[0].percentage_of_total]},
        {name:'Primary Health Care', data:[phc[0].percentage_of_total]}];
         console.log(phc);

    $scope.showTheChart(dataV,'Percentage  of stock available for '+params.periodName,'',categories);


    });

    }


    $scope.showTheChart = function (data,title, subtitle, categories) {

    Highcharts.chart('stock-by-level', {
        chart: {
            type: 'column'
        },credits: {
        enabled:false
        },
        title: {
            text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>'
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Total fruit consumption'
            }
        },
        tooltip: {
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
            shared: true
        },
        plotOptions: {
            column: {
                stacking: 'percent'
            }
        },
        series:data,
           colorByPoint: true
    });

    }
}