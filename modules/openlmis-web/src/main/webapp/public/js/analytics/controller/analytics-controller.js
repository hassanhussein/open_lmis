function AnalyticsFunction($scope,messageService,GetLocalMap,ConsumptionTrendsData,DashboardStockStatusSummaryData,YearFilteredData,StockAvailableForPeriodData, StockAvailableByProgramAndPeriodData) {




$('ul.tabs').tabs().tabs('select_tab', 'tracer');


var params = {product:parseInt(2434,0) ,year:parseInt(2019,0), program: parseInt(1,0),period:parseInt(91,10)};

var data = [
    ['ID', 1],
    ['ID', 710],
    ['ID', 963],
    ['ID', 541],
    ['DE.HH', 622],
    ['DE.RP', 866],
    ['DE.SL', 398],
    ['DE.BY', 785],
    ['DE.SN', 223],
    ['DE.ST', 605],
    ['DE.NW', 237],
    ['DE.BW', 157],
    ['DE.HE', 134],
    ['DE.NI', 136],
    ['DE.TH', 704],
    ['DE.', 361]
];



/*$.getJSON('/public/js/reports/shared/map.json', function (geojson) {
console.log(geojson);


 Highcharts.mapChart('reportingRateMap', {
        chart: {
            map: geojson
        },
        credits:{ enabled:false},

        title: {
            text: 'Reporting Rate'
        },

        mapNavigation: {
            enabled: true,
            buttonOptions: {
                verticalAlign: 'bottom'
            }
        },

        colorAxis: {
            tickPixelInterval: 100
        },
          legend: {
                     layout: 'horizontal',
                     borderWidth: 0,
                     backgroundColor: 'rgba(255,255,255,0.85)',
                     floating: true,
                     verticalAlign: 'top',
                     y: 25
                 },

        series: [{
            data: data,
            keys: ['ID', 'ID'],
            joinBy: 'ID',
            name: 'Random data',
            states: {
                hover: {
                    color: '#a4edba'
                }
            },
            cursor: 'pointer',
            allowPointSelect: true,
            dataLabels: {
                enabled: true,
                format: '{point.properties.ADM2}'
            }
        }]
    });





});*/





$scope.consumptionTrends = [];
ConsumptionTrendsData.get(params).then(function(data){


var groupA = _.where(data, {'schedule':45});
var groupB = _.where(data, {'schedule':46});

    if(!isUndefined(data)) {

     var category = _.pluck(groupA, 'periodname');
     var tle_consumption = _.pluck(groupA, 'tle_consumption');
     var tld_consumption = _.pluck(groupA, 'tld_consumption');
     var dolutegravir_consumption = _.pluck(groupA, 'dolutegravir_consumption');

     var tle_consumptionB = _.pluck(groupB, 'tle_consumption');
     var tld_consumptionB = _.pluck(groupB, 'tld_consumption');
     var dolutegravir_consumptionB = _.pluck(groupB, 'dolutegravir_consumption');

     var chartTypeId = 'consumptionTrendsChartA';
     var chartTypeId2 = 'consumptionTrendsChartB';
     var categoryB = _.pluck(groupB, 'periodname');

     $scope.consumptionTrends = data;

     $scope.consumptionTrendsChart(chartTypeId, data,category,tle_consumption,tld_consumption,dolutegravir_consumption,'Consumption trends, 2019 - Group A');
     $scope.consumptionTrendsChart(chartTypeId2, data,categoryB,tle_consumptionB,tld_consumptionB,dolutegravir_consumptionB,'Consumption trends, 2019 - Group B');

    }


});



var stockSummary = [];
DashboardStockStatusSummaryData.get(params).then(function(data) {
$scope.stockStatuses   = [];
 console.log(data);
 if(!isUndefined(data)){
 console.log(data);
 stockSummary = data;
  var category = _.uniq(_.pluck(stockSummary,'periodname'));

               var so = _.pluck(stockSummary, 'so');
               var os = _.pluck(stockSummary, 'os');
               var sap = _.pluck(stockSummary, 'sp');
               var us = _.pluck(stockSummary, 'us');
               var uk = _.pluck(stockSummary, 'uk');
               var total = _.pluck(stockSummary, 'total');

               var summaries = [];

               var totalZeroStock = [];
               var totalLowStock = [];
               var totalOverStock = [];
               var totalUnknown = [];
               var totalSufficientStock = [];

               _.map(total,function(data, index){

             totalZeroStock.push({y:so[index],total:data});
             totalLowStock.push({y:us[index],total:data});
             totalOverStock.push({y:os[index],total:data});
             totalSufficientStock.push({y:sap[index],total:data});
             totalUnknown.push({y:uk[index],total:data});
                 return null;

               });

               summaries = [
                            {name:'Stocked Out', data:totalZeroStock, color:'#ff0d00'},
                            {name:'Understocked', data:totalLowStock, color:'#ffdb00'},
                            {name:'OverStocked', data:totalOverStock, color:'#00B2EE'},
                            {name:'Adequately stocked', data:totalSufficientStock, color:'#006600'},
                            {name:'UnKnown', data:totalUnknown, color:'gray'}

                           ];

     $scope.stockStatusesStackedColumnChart('stockStatusOverTime','column' ,'Stock Status Over Time',category, 'Count of Facilities',summaries );

 }


});



StockAvailableForPeriodData.get(params).then(function(data) {
$scope.stockAvailableForPeriodList = [];

    if(data.length > 0) {
    console.log(data);


        _.each(data, function(value){

       var totalCalculation = (parseInt(value.totalbyprogram,10) * 100)/value.total;

        $scope.stockAvailableForPeriodList.push({name:value.program_name,y:Math.round(totalCalculation),available:value.totalbyprogram,total:value.total, drilldown:value.programid });

        });
        var chartId = 'stock-available-for-program';
        var title = 'Stock Availability per program';
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

     });

     var category =_.pluck(data,'productname');
     var values = _.pluck(data,'mos');

     //$scope.availableStockByProgramModal = true;
     $scope.dataTableStockStatusChart(category,values,$scope.titleStockForProgramAvailable,chartData.color);

     // $('#availableStockByProgramModal').modal();



    });



 }

  $scope.gridOptions = { data: 'drillDownData',
              showFooter: true,
              enableGridMenu: true,
              exporterMenuCsv: true,
              showFilter: false,
              enableColumnResize: true,
              enableSorting: false,
                 exporterCsvFilename: 'myFile.csv',
                  exporterPdfDefaultStyle: {fontSize: 9},
              columnDefs: [
                {field: 'SN', displayName: '#',cellTemplate: '<div style="text-align: center !important;">{{row.rowIndex + 1}}</div>', width: 15},
                {field: 'productname', displayName: messageService.get("label.product"), width: 200},
                {field: 'soh', displayName: 'SOH'},
                {field: 'amc', displayName: 'AMC'},
                {field: 'mos', displayName: 'MOS'}

              ]
   };




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
    ],
          exporting: {
                     buttons: {
                         customButton: {
                             text: '<span style="background-color:blue"><i class="fas fa-info-circle></i>Read Description</span>',

                             symbolStroke: "red",
                                                 theme: {
                                         fill:"#28A2F3"
                                     },
                             onclick: function () {
                                 alert('You pressed the button!');
                             }
                         }
                     }
                 }

});


}


$scope.dataTableStockStatusChart = function (category,data,title,color) {

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
        color:color,
        data: data
    }]
    ,
                  exporting: {
                             buttons: {
                                 customButton: {
                                     text: '<span style="background-color:blue"><i class="fas fa-info-circle></i>Read Description</span>',

                                     symbolStroke: "red",
                                                         theme: {
                                                 fill:"#28A2F3"
                                             },
                                     onclick: function () {
                                         alert('You pressed the button!');
                                     }
                                 }
                             }
                         }
});


};



 $scope.stockStatusesStackedColumnChart = function (id,chartType, title,category,yAxisTitle,data){

 Highcharts.chart(id, {
     chart: {
         type: chartType
     },
     title: {
         text: title
     },
     xAxis: {
         categories: category,
         labels: {
                     align: 'right'
                 },
             title: {
                     text: "Processing Periods"
                 }
     },
     yAxis: {
     gridLineColor: '',
         min: 0,
         title: {
             text: yAxisTitle
         },
         stackLabels: {
             enabled: true,
             style: {
                 fontWeight: 'bold',
                 color: ( // theme
                     Highcharts.defaultOptions.title.style &&
                     Highcharts.defaultOptions.title.style.color
                 ) || 'gray'
             }
         }
     }, credits: {
               enabled: false
           },
     legend: {
         align: 'right',
         x: -30,
         verticalAlign: 'top',
         y: 25,
         floating: true,
         backgroundColor:
             Highcharts.defaultOptions.legend.backgroundColor || 'white',
         borderColor: '#CCC',
         borderWidth: 1,
         shadow: false
     },
     tooltip: {
         headerFormat: '<b>{point.x}</b><br/>',
         pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
     },
     plotOptions: {
         column: {
             stacking: 'normal',
             dataLabels: {
                 enabled: true
             }
         }
     },
     series:data,
        exporting: {
                 buttons: {
                     customButton: {
                         text: '<span style="background-color:blue"><i class="fas fa-info-circle></i>Read Description</span>',

                         symbolStroke: "red",
                                             theme: {
                                     fill:"#28A2F3"
                                 },
                         onclick: function () {
                             alert('You pressed the button!');
                         }
                     }
                 }
             }
 });




 };


 $scope.consumptionTrendsChart = function (chartTypeId,data,category,tle_consumption,tld_consumption,dolutegravir_consumption,group){

 Highcharts.chart(chartTypeId, {
     chart: {
         type: 'line'
     }, credits: {
           enabled:false
           },
     title: {
         text: group
     },
     subtitle: {
         text: ''
     },
     xAxis: {
         categories: category
     },
     yAxis: {
         title: {
             text: 'Average Monthly Consumption'
         }
     },
     plotOptions: {
         line: {
             dataLabels: {
                 enabled: true
             },
             enableMouseTracking: false
         }
     },
     series: [{
                     name: 'TLE',
                     data: tle_consumption
                 }, {
                     name: 'TLD',
                     data: tld_consumption
                 }, {
                     name: 'Dolutegravir',
                     data: dolutegravir_consumption
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
 };

