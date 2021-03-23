function stockStatusSummaryController(Program,Period,$document,$scope,$location,$window,$sce,$timeout,SettingsByKey,$rootScope, StockStatusSummaryByPeriodData, StockStatusByProgramAndYearData,GetStockImbalanceSummaryData){
$scope.filteredData = false;
var title = '';
$rootScope.loadStockStatusSummary = function(params, filteredData) {
if(!filteredData) {
 title = 'Stock Availability';
 $scope.titleStockSummary = title;
loadStockSummary(params,'');
} else {

title = '#4:'+' Stock Availability of Health Commodities for  '+params.programName + ' '+params.periodName+', '+params.year+' ';

StockStatusSummaryByPeriodData.get(params).then(function(data) {

$scope.titleStockSummary = title;
$scope.loadTheChart(data, '','');
});

}

};

$scope.showSummaryStockAvailability = function(prm, indicator){
$scope.filteredData = indicator;
$scope.titleStockSummary  = 'Stock Availability';
loadStockSummary(prm,'');

};

function loadStockSummary (param,title) {

 GetStockImbalanceSummaryData.get(param).then(function(data) {

  if(!isUndefined(data)) {

  var sumStockOut=0;
  var sumTotal=0;

  angular.forEach(data,function(dx) {

      sumStockOut = sumStockOut + parseInt(dx.stockoutincidence,10);
      sumTotal = sumTotal + parseInt(dx.totalincidence,10);
 });

var dataV = [{
              name: 'Stock Out Incidences',
              y: (sumStockOut/sumTotal*100).toFixed(1),
              sliced: true,
                        selected: true
              },

              {
               name: 'Available Incidences',
               y: 100 - (sumStockOut/sumTotal*100).toFixed(1)

              }
               ];

             $scope.loadStockSummaryChart(data,title,'');


 //console.log((sumStockOut/sumTotal*100).toFixed(1));

  }

 });
 }




$rootScope.renderHtml = function (htmlCode) {
            return $sce.trustAsHtml(htmlCode);
        };
        $scope.body = '<div style="width:200px; height:200px; border:1px solid blue;"></div>';


$scope.loadTheChart = function loadTheChart(dataV, title,subtitle) {

var dataToShow = [];

 var drilldownData  = [];


  dataV.forEach(function(value){

   var os = ["OverStocked",value.percentage_of_os,"#00B2EE"];
   var us = ["Understocked", value.percentage_of_us,"#FFA500"];
   var uk = ["UnKnown",value.percentage_of_uk,"gray"];
   var sp = ["Adequately stocked", value.percentage_of_sp,"#006600"];
    drilldownData = [os,us,uk,sp];
    //50B432


    console.log(drilldownData);
  dataToShow = [{name:'Available Incidence',y: value.percentage_of_total,color: '#3C81B0', drilldown: 'Available Incidence' },
   {name:'Stock out Incidence', y: value.percentage_of_stock_out,color:'#D90C29', drilldown:'Stock out Incidence', sliced: true,selected: true }];
  /*  dataToShow = [{y: value.percentage_of_total,color: '#50B432', drilldown: {name:'Available Incidence',categories:['Over stock','Adequately stocked','Under stock','Unknown'] ,data:[value.percentage_of_os,value.percentage_of_sp,value.percentage_of_us,value.percentage_of_uk] } },
   {y: value.percentage_of_stock_out,color:'#ED561B', drilldown: {name:'Stock out Incidence', categories:['stock out'], data:[value.percentage_of_stock_out] } }];
  */});

console.log(dataToShow);
/*var colors = Highcharts.getOptions().colors,
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
}*/

// Create the chart
/*
Highcharts.chart('stock-summary-by-period-and-program', {
    chart: {
        type: 'pie'
    },
    title: {
        text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
         align: 'left'
    },
    subtitle: {
        text: '<span style="font-size: 13px!important;color: #0c9083">'+subtitle+'</span>'
    },
    plotOptions: {
        pie: {
         events: {
          click: function() {

          //$rootScope.openDefinitionModal();

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
       exporting: {
        buttons: {
         customButton: {
         text: '<span style="background-color:blue"><i class="material-icons md-18">Info</i></span>',
         symbolStroke: "red",
         theme: {
         fill:"#28A2F3"
         },
         onclick: function () {
         $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY', 'Stock Availability');
         }
         }
         }
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
*/


// Create the chart
Highcharts.chart('stock-summary-by-period-and-program', {
    chart: {
        type: 'pie'
    },
    credits: {
    enabled:false
    },
   title: {
          text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
          align:'left'

      },
      subtitle: {
          text: '<span style="font-size: 13px!important;color: #0c9083">'+subtitle+'</span>'
      },
    plotOptions: {
       pie: {
        size: '60%',
        allowPointSelect: true,
        cursor: 'pointer',

            dataLabels: {
                                       enabled: true,
                                       format: '<b>  {point.percentage:.0f} %',

                                       /*
                                                               format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                       */
                                       style: {
                                           color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                           fontFamily: '\'Lato\', sans-serif', lineHeight: '18px', fontSize: '17px'
                                       }
                                   },
                                   showInLegend: true,

         point: {
           events: {
             click: function() {
               if(this.series !== null){
                 $scope.openStockImbalanceReport(this);
                }

               }
               }
           }

       },

        series: {
            dataLabels: {
                enabled: true,
                 useHTML: true,
                    formatter: function () {
                     return '<span style="color:' + this.point.color + '">' + this.point.name + ' : '+ this.point.y+'%</span>';
                    }



            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
    }, exporting: {
              buttons: {
               customButton: {
               text: '<span style="background-color:blue"><i class="material-icons md-18">Info</i></span>',
               symbolStroke: "red",
               theme: {
               fill:"#28A2F3"
               },
               onclick: function () {
               $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY', 'Stock Availability');
               }
               }
               }
               },

    series: [
        {
            name: "Summary",
            colorByPoint: false,
            data: dataToShow
        }
    ],
    drilldown: {
        series: [
            {
                name: "Available Incidence",
                id: "Available Incidence",
                type: 'pie',
                 colors: [
                     '#00B2EE',
                     '#FFA500',
                     '#808080',
                     '#006600'

                   ],

                innerSize: '50%',
                colorByPoint: true,
                data: drilldownData
            }

        ],


            plotOptions: {

             pie:{
                 size: '100%',
              events: {
                       click: function() {

                       //$rootScope.openDefinitionModal();

                        console.log(this);

                        }
                      },

                            shadow: true,
                                   cursor: 'pointer'

             }
            }
    }
});


};

$scope.loadStockSummaryChart = function(dataV, title,subtitle) {

var dataToShow = [];

 var drilldownData  = [];

 var groupByData = _.groupBy(dataV, function(g){
       return g.reported;
 });

 var mappedData = _.map(groupByData, function(value,idx) {
  return {
       'so':value[0].stockoutincidence,
       'us':value[0].understockincidence,
       'sp':value[0].adeliquatestockincidence,
       'os':value[0].overstockincidence,
       'uk':value[0].unknownincidence,
       'total':value[0].totalincidence,
  };
 });


 var sum_so = _.reduce(_.pluck(mappedData,'so'), function(memo, num){ return memo + num; }, 0);
 var sum_us = _.reduce(_.pluck(mappedData,'us'), function(memo, num){ return memo + num; }, 0);
 var sum_sp = _.reduce(_.pluck(mappedData,'sp'), function(memo, num){ return memo + num; }, 0);
 var sum_os = _.reduce(_.pluck(mappedData,'os'), function(memo, num){ return memo + num; }, 0);
 var sum_uk = _.reduce(_.pluck(mappedData,'uk'), function(memo, num){ return memo + num; }, 0);
 var sum_total = _.reduce(_.pluck(mappedData,'total'), function(memo, num){ return memo + num; }, 0);

 var percentage_of_os = ((sum_os / sum_total) * 100).toFixed(0);
 var percentage_of_us = ((sum_us / sum_total) * 100).toFixed(0);
 var percentage_of_uk = ((sum_uk / sum_total) * 100).toFixed(0);
 var percentage_of_sp = ((sum_sp / sum_total) * 100).toFixed(0);
 var percentage_of_so = ((sum_so / sum_total) * 100).toFixed(0);
 var percentage_of_total = 100 - percentage_of_so;

 var arrayList = [{'percentage_of_os':parseInt(percentage_of_os,10), 'percentage_of_us':parseInt(percentage_of_us,10),'percentage_of_uk':parseInt(percentage_of_uk,10),'percentage_of_sp':parseInt(percentage_of_sp,10),'percentage_of_stock_out':parseInt(percentage_of_so,10) ,'percentage_of_total':parseInt(percentage_of_total,10)}];

  arrayList.forEach(function(value){

   var os = ["OverStocked",value.percentage_of_os,"#00B2EE"];
   var us = ["Understocked", value.percentage_of_us,"#FFA500"];
   var uk = ["UnKnown",value.percentage_of_uk,"gray"];
   var sp = ["Adequately stocked", value.percentage_of_sp,"#006600"];
    drilldownData = [os,us,uk,sp];
    //50B432

  dataToShow = [{name:'Available Incidence',y: value.percentage_of_total,color: '#3C81B0', drilldown: 'Available Incidence' },
   {name:'Stock out Incidence', y: value.percentage_of_stock_out,color:'#D90C29', drilldown:'Stock out Incidence', sliced: true,selected: true }];
  /*  dataToShow = [{y: value.percentage_of_total,color: '#50B432', drilldown: {name:'Available Incidence',categories:['Over stock','Adequately stocked','Under stock','Unknown'] ,data:[value.percentage_of_os,value.percentage_of_sp,value.percentage_of_us,value.percentage_of_uk] } },
   {y: value.percentage_of_stock_out,color:'#ED561B', drilldown: {name:'Stock out Incidence', categories:['stock out'], data:[value.percentage_of_stock_out] } }];
  */});


// Create the chart
Highcharts.chart('stock-summary-by-period-and-program', {
    chart: {
        type: 'pie'
    },
    credits: {
    enabled:false
    },
   title: {
          text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
          align:'left'

      },
    plotOptions: {
       pie: {
        size: '60%',
        allowPointSelect: true,
        cursor: 'pointer',

           dataLabels: {    enabled: true,
                     format:
                         '<div style="text-align:center">' +
                         '<span style="font-size:18px;color: #0c9083">{y} %</span><br></br></br>' +
                         '</div>'
                 },
                 tooltip: {
                     valueSuffix: ' %'
                 },

                 showInLegend: true,

         point: {
           events: {
             click: function() {
               if(this.series !== null){
                 $scope.openStockImbalanceReport(this);
                }

               }
               }
           }

       },

        series: {
            dataLabels: {
                enabled: true,
                 useHTML: true,
                    formatter: function () {
                     return '<span style="color:' + this.point.color + '">' + this.point.name + ' : '+ this.point.y+'%</span>';
                    }



            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
    }, exporting: {
              buttons: {
               customButton: {
               text: '<span style="background-color:blue"><i class="material-icons md-18">Info</i></span>',
               symbolStroke: "red",
               theme: {
               fill:"#28A2F3"
               },
               onclick: function () {
               $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY', 'Stock Availability');
               }
               }
               }
               },

    series: [
        {
            name: "Summary",
            colorByPoint: false,
            data: dataToShow
        }
    ],
    drilldown: {
        series: [
            {
                name: "Available Incidence",
                id: "Available Incidence",
                type: 'pie',
                 colors: [
                     '#00B2EE',
                     '#FFA500',
                     '#808080',
                     '#006600'

                   ],

                innerSize: '50%',
                colorByPoint: true,
                data: drilldownData
            }

        ],


            plotOptions: {

             pie:{
                 size: '100%',
              events: {
                       click: function() {

                       //$rootScope.openDefinitionModal();

                        console.log(this);

                        }
                      },

                            shadow: true,
                                   cursor: 'pointer'

             }
            }
    }
});


};


$scope.openStockImbalanceReport = function(data) {
console.log($scope.$parent.params);

 var names = {'OverStocked':'OS', 'Understocked': 'US','UnKnown':'UK','Adequately stocked':'SP'};

 $scope.$parent.params.reportType = 'RE';

 if(data.name === 'Stock out Incidence'){

 $scope.$parent.params.dashboardView = false;
   $scope.$parent.params.status = 'SO';

 }
  else {

 $scope.$parent.params.dashboardView = true;
$scope.$parent.params.status =names[data.name];
  }

 var url = '/public/pages/reports/main/index.html#/stock-imbalance?'+jQuery.param($scope.$parent.params);
 $window.open(url, "_BLANK");


};


$rootScope.openDefinitionModal = function (key,title) {

 SettingsByKey.get({key: key}, function (data) {

  $rootScope.content = data.settings.value;
  $rootScope.title = title;


  $timeout(function(){

  $('#definitionModal').modal();

  },100);


  });



};

//Filters



$scope.OnFilterChanged = function () {

console.log ('changed');

var programName = '';
Program.get({id: parseInt($location.search().program,10)}, function(da){
programName = da.program.name;

var periodName = '';
Period.get({id: parseInt($location.search().period,10)}, function(da){
periodName = da.period.name;

$location.search().programName = programName;
$location.search().periodName = periodName;

console.log($location.search());


$scope.$parent.params = $location.search();
$scope.filteredData = true;
$rootScope.loadStockStatusSummary($location.search(),$scope.filteredData);


});


});






};





}


