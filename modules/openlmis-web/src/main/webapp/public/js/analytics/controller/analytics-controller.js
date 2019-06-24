function AnalyticsFunction($scope,messageService,DashboardStockStatusSummaryData,YearFilteredData,StockAvailableForPeriodData, StockAvailableByProgramAndPeriodData) {




$('ul.tabs').tabs().tabs('select_tab', 'tracer');


var params = {product:parseInt(2434,0) ,year:parseInt(2018,0), program: parseInt(1,0),period:parseInt(91,10)};



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

    console.log(data);


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
    ]

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
     series:data
 });




 };


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

