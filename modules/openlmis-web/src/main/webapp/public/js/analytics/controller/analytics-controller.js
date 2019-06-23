function AnalyticsFunction($scope,DashboardStockStatusSummaryData,YearFilteredData,StockAvailableForPeriodData, StockAvailableByProgramAndPeriodData) {


$('ul.tabs').tabs().tabs('select_tab', 'tracer');


var params = {product:parseInt(2537,0) ,year:parseInt(2017,0), program: parseInt(1,0),period:parseInt(79,10)};




/*
DashboardStockStatusSummaryData.get(params).then(function(data) {
$scope.stockStatuses   = [];

 $scope.stockStatuses = data;


});
*/



StockAvailableForPeriodData.get(params).then(function(data) {
$scope.stockAvailableForPeriodList = [];

    if(data.length > 0) {
    console.log(data);


        _.each(data, function(value){

        $scope.stockAvailableForPeriodList.push({name:value.program_name,y:Math.round(parseInt(value.totalbyprogram * 100)/value.total),available:value.totalbyprogram,total:value.total, drilldown:value.programid });

        });
        var chartId = 'stock-available-for-program';
        var title = 'Stock Availability per program for June, 2019';
        var chartType = 'column';

        drillDownChart(chartId,chartType,title,$scope.stockAvailableForPeriodList);
        console.log($scope.stockAvailableForPeriodList);
        }


    });

 var drillDownSeries = $scope.drillDownData = [];
 function getAvailableDrillDownData(program,name, chartData){

     params.program = program;
    var allParams = angular.extend(params, {program:program});
      console.log(program);
    StockAvailableByProgramAndPeriodData.get(params).then(function(data){

     $scope.titleStockForProgramAvailable = 'List of Available Tracer Items for '+name +' in June 2018';
     $scope.stockColor= chartData.color;

     $scope.drillDownData = data;
      console.log(data);

     _.each(data, function(drilledData){
     // drillDownSeries.push({name:name,id:program,data:drilledData})

     })
     console.log($scope.stockColor);
     var category =_.pluck(data,'productname');
     var values = _.pluck(data,'mos');

     //$scope.availableStockByProgramModal = true;
     $scope.dataTableStockStatusChart(category,values,$scope.titleStockForProgramAvailable);
     // $('#availableStockByProgramModal').modal();

    });

 }




function drillDownChart(id,chartType, title,data){

// Create the chart
Highcharts.chart(id, {
    chart: {
        type: chartType
    },
    title: {
        text: title
    },
    subtitle: {
        text: 'Click the columns to view stock availability of each tracer items'
    },
    xAxis: {
        type: 'category'
    },
    yAxis: {
        title: {
            text: 'Total percent of tracer products'
        },
         gridLineColor: ''

    },
    credits: {
    enabled:false
    },
    legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 80,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
      events: {
    	   drillup: function (e) {
                            alert(e.seriesOptions.name);
                     }
          },

    plotOptions: {
        column: {
          point : {
             events: {

              click : function (){
                 var drilldown = this.drilldown;
                   console.log(drilldown);

                  getAvailableDrillDownData(drilldown,this.name, this);

                 console.log(this.name);

              }

             }

          }
        },
        series: {
            borderWidth: 0,
            dataLabels: {
                enabled: true,
                format: '{point.y}%'
            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}%</b> of <b>{point.available}</b> available tracer(s) of <span><b>{point.total}</b></span> total <br/>'
    },

    series: [
        {
            name: "Programs",
            colorByPoint: true,
            data: data
        }
    ]

});


}


$scope.dataTableStockStatusChart = function (category,data,title) {

Highcharts.chart('stock-available-for-program-drill-down', {
    chart: {
        type: 'bar'
    },
    title: {
        text: title
    },
    subtitle: {
        text: ''
    },
    xAxis: {
        categories: category,
        title: {
            text: null
        }
    },
    yAxis: {
        min: 0,
        title: {
            text: 'MOS',
            align: 'high'
        },
        labels: {
            overflow: 'justify'
        }
    },
    tooltip: {
        valueSuffix: ' Months'
    },
    plotOptions: {
        bar: {
            dataLabels: {
                enabled: true
            }
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'top',
        x: -40,
        y: 80,
        floating: true,
        borderWidth: 1,
        backgroundColor:
            Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
        shadow: true
    },
    credits: {
        enabled: false
    },
    series: [{
        name: 'MOS',
        data: data
    }]
});


}




}

AnalyticsFunction.resolve = {

YearFilteredData: function ($q, $timeout, OperationYears) {
        var deferred = $q.defer();
        $timeout(function () {
            OperationYears.get({}, function (data) {
                deferred.resolve(data.years);
            }, {});
        }, 100);
        return deferred.promise;
 }
 }

