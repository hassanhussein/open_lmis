function stockStatusSummaryController($scope,$rootScope, StockStatusSummaryByPeriodData, StockStatusByProgramAndYearData){


$rootScope.loadStockStatusSummary = function(params) {

StockStatusSummaryByPeriodData.get(params).then(function(data){
var title = 'Percentage of health facilities that have not experienced stock out incidences of Health Commodities in '+params.periodName;

$scope.loadTheChart(data, title,'');


});

}

$rootScope.loadStockStatusByProgramAndYearData = function(params){

/*StockStatusByProgramAndYearData.get(params).then(function(data){

console.log(data);


})*/
}


$scope.loadTheChart = function loadTheChart(dataV, title,subtitle) {

var dataToShow = [];
var colors1 = Highcharts.getOptions().colors;

console.log(colors1);

  dataV.forEach(function(value){
  dataToShow = [{y: value.percentage_of_total,color: '#50B432', drilldown: {name:'Available Incidence',categories:['Over stock','Adequately stocked','Under stock','Unknown'] ,data:[value.percentage_of_os,value.percentage_of_sp,value.percentage_of_us,value.percentage_of_uk] } },
   {y: value.percentage_of_stock_out,color:'#ED561B', drilldown: {name:'Stock out Incidence', categories:['stock out'], data:[value.percentage_of_stock_out] } }];
  });

console.log(dataToShow);
var colors = Highcharts.getOptions().colors,
    categories = [
        'Available Incidence',
        'Stock out Incidence'
    ],
    data = dataToShow,
    browserData = [],
    versionsData = [],
    i,
    j,
    dataLen = data.length,
    drillDataLen,
    brightness;


// Build the data arrays
for (i = 0; i < dataLen; i += 1) {

    // add browser data
    browserData.push({
        name: categories[i],
        y: data[i].y,
        color: data[i].color
    });

    // add version data
    drillDataLen = data[i].drilldown.data.length;
    for (j = 0; j < drillDataLen; j += 1) {
        brightness = 0.2 - (j / drillDataLen) / 5;
        versionsData.push({
            name: data[i].drilldown.categories[j],
            y: data[i].drilldown.data[j],
            color: Highcharts.Color(data[i].color).brighten(brightness).get()
        });
    }
}

// Create the chart
Highcharts.chart('stock-summary-by-period-and-program', {
    chart: {
        type: 'pie'
    },
    title: {
        text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>'
    },
    subtitle: {
        text: subtitle
    },
    plotOptions: {
        pie: {
         events: {
          click: function() {

           console.log(this);

           }
         },
            shadow: false,
            center: ['50%', '50%'],
             cursor: 'pointer'
        }

    },
    credits :{
    enabled :false
    },
    tooltip: {
        valueSuffix: '%'
    },
    series: [{
        name: 'Total Incidence',
        data: browserData,
        size: '60%',
        dataLabels: {
            formatter: function () {
            console.log(this.y);
                return this.y > 5 ? this.point.name : null;
            },
            color: '#ffffff',
            distance: -30
        }
    }, {
        name: 'Occurrence',
        data: versionsData,
        size: '80%',
        innerSize: '60%',
        dataLabels: {
            formatter: function () {

                // display only if larger than 1
                return this.y > 1 ? '<b>' + this.point.name + ':</b> ' +
                    this.y + '%' : null;
            }
        },
        id: 'versions'
    }],
    responsive: {
        rules: [{
            condition: {
                maxWidth: 400
            },
            chartOptions: {
                series: [{
                }, {
                    id: 'versions',
                    dataLabels: {
                        enabled: false
                    }
                }]
            }
        }]
    }
});


}



}


