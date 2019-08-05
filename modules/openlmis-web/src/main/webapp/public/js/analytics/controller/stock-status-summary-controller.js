function stockStatusSummaryController(Program,Period,$document,$scope,$location,$window,$sce,$timeout,SettingsByKey,$rootScope, StockStatusSummaryByPeriodData, StockStatusByProgramAndYearData){


$rootScope.loadStockStatusSummary = function(params) {

StockStatusSummaryByPeriodData.get(params).then(function(data){
var title = 'Stock Availability of Health Commodities for  '+params.programName + ' '+params.periodName+', '+params.year+' ';
$scope.titleStockSummary = title;
title ='';
$scope.loadTheChart(data, title,'');

});

};

$rootScope.loadStockStatusByProgramAndYearData = function(params){

/*StockStatusByProgramAndYearData.get(params).then(function(data){

console.log(data);


})*/
};

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


$scope.openStockImbalanceReport = function(data) {
console.log(data);

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

$rootScope.loadStockStatusSummary($location.search());


});


});






};





}


