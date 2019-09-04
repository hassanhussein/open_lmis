function TimelinessReportingController($scope,$location,Program,Period,$rootScope,TimelinessReportingData,$window,OntimeDeliveryReportData){



   function loadTheChart(category, values, chartId, type, chartName, title, verticalTitle) {
          Highcharts.chart(chartId, {
              chart: {
                  type: type
              },

              title: {
                  text: ' <h2><span style="font-size: x-small;color:#0c9083">' + title + '</span></h2>'
              }, credits: {
                  enabled: false
              },
              subtitle: {
                  text: ''
              },
              xAxis: {
                  categories: category,
                  crosshair: true
              },
              yAxis: {
                  lineWidth: 1,
                  gridLineColor: '',
                  interval: 1,

                  tickWidth: 1,


                  min: 0,
                  title: {
                      text: '<span style="font-size: x-small;color:#0c9083">' + verticalTitle + '</span>'
                  }
              },
              tooltip: {
                  /*
                   headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                   */
                  headerFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.key}</b></td></tr><br/>',
                  pointFormat: '<tr><td style="color:{series.color};padding:0">' + verticalTitle + ':</td>' + '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                  footerFormat: '</table>',
                  shared: true,
                  useHTML: true
              },

              plotOptions: {
                  column: {
                      pointPadding: 0.2,
                      borderWidth: 0
                  }
              },
              series: [{
                  name: chartName,
                  data: values

              }]
          });

      }


$rootScope.loadTimelinessReportingData = function (params) {

TimelinessReportingData.get(params).then(function(data){
   console.log(data);

    if(data.length > 0) {

    var chartId = 'timeliness-reporting';

     var dataToShow = [];

   data.forEach(function(dx){
       dataToShow= [{name:'Reported On Time',color:'#50B432', y:$rootScope.getPercentage(dx.reportedontime,dx.totalexpected),drilldown: null},{color:'#D90C29',name:'Reported Late',y:$rootScope.getPercentage(dx.reportedlate,dx.totalexpected),drilldown: null},{name:'Unscheduled',y:$rootScope.getPercentage(dx.unscheduled,dx.totalexpected),drilldown: null},{name:'Not reported',color:'black',y:$rootScope.getPercentage(dx.not_reported,dx.totalexpected),drilldown: null}];


       //dataToShow = [dx.not_reported, dx.unscheduled, dx.reportedlate, dx.reportedontime];
    });

    $rootScope.title_reporting_timeliness =   'Reporting Timeliness for '+params.programName+' ('+params.periodName+' ,'+params.year+')';
    console.log($scope.title_reporting_timeliness);
    loadChart(chartId,dataToShow,' ',' ',data[0].totalexpected,' ');


    }



});

};

function loadChart(chartId,dataV,title,subtitle,totalExpected,year){


// Create the chart
Highcharts.chart(chartId, {
    chart: {
        type: 'pie'
    },
    credits:{
    enabled:false
    },
    title: {
        text:'<span style="font-size: 15px!important;color: #0c9083">'+title+', '+year+' </span>',
        align:'left'
    },
    subtitle: {
        text: '<span style="font-size: 12px!important;color: #0c9083">'+subtitle+'</span>',
        align:'left'
    },
    plotOptions: {
      pie:{
               innerSize: '70%',
                                      allowPointSelect: true,
                                      size: '70%',
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

                                       events:{
                                         click: function (event) {

                                         if(event.point.color !== 'green'){
                                        // console.log(para);
                                         //openRejectionReport(para);

                                         }else {



                                         }

                                          //$scope.openAdjustmentReport(this);

                                             }
                                       }

      },
    series: {
           dataLabels: {

           enabled: true,
           formatter: function () {
           return '<span style="color:' + this.point.color + '">' + this.point.name + ' : '+ this.point.y+'%</span>';
           }
           }
    }
    },

    tooltip: {
       // headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        //pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}%</b> of total<br/>'
         pointFormat: '{point.name}: <b>{point.name:.0f} %<br>total: {point.y}'
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
                                    $rootScope.openDefinitionModal('DASHLET_REPORTING_TIMELINESS', 'Reporting Timeliness');
                                    }
                                    }
                                    }
                                    },

    series: [
        {
            name: "Facilities",
            colorByPoint: true,
            animation: true,
            data: dataV
        }
    ]
});



}




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

$rootScope.loadTimelinessReportingData($location.search());


});


});






};


}