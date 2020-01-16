function StockAvailabilityByLevelController ($scope,$location,Program,Period, $rootScope,StockAvailableByLevelData){

function calculatePercentage (num, den) {

 var total = (parseInt(num,10) / parseInt(den, 10) * 100);
 return Math.round(total,0);

}

$rootScope.loadStockAvailableByLevel = function(params){

    StockAvailableByLevelData.get(params).then(function(data) {

if(data.length > 0 && !isUndefined(data)) {

     var categories = _.pluck(data, 'tracerItems');


     var tracerItems = _.filter(data, {traceritems:'tracerItems'});
     console.log(tracerItems);

/*
      var os = ["OverStocked",value.percentage_of_os,"#00B2EE"];
        var us = ["Understocked", value.percentage_of_us,"#FFA500"];
        var uk = ["UnKnown",value.percentage_of_uk,"gray"];
        var sp = ["Adequately stocked", value.percentage_of_sp,"#006600"];
*/
 var totalTracerAvailable = tracerItems[0].percentage_of_os + tracerItems[0].percentage_of_us + tracerItems[0].percentage_of_sp + tracerItems[0].percentage_of_uk;

var availableTracer = [
                         {name:'OverStocked',y: calculatePercentage(tracerItems[0].percentage_of_os,totalTracerAvailable), color:"#00B2EE"},
                         {name:'Understocked', y:calculatePercentage(tracerItems[0].percentage_of_us,totalTracerAvailable),color:"#FFA500"},
                         {name:'Adequately Stocked', y:calculatePercentage(tracerItems[0].percentage_of_sp,totalTracerAvailable),color:"#006600"},
                         {name:'Unknown',y:calculatePercentage(tracerItems[0].percentage_of_uk,totalTracerAvailable),color:"gray"}
                     ];


 var allItems = _.filter(data, {traceritems:'allItems'});

 var totalAllAvailable = allItems[0].percentage_of_os + allItems[0].percentage_of_us + allItems[0].percentage_of_sp + allItems[0].percentage_of_uk;

 var availableAll = [
 {name:'OverStocked',y: calculatePercentage(allItems[0].percentage_of_os,totalAllAvailable), color:"#00B2EE"},
 {name:'Understocked', y:calculatePercentage(allItems[0].percentage_of_us, totalAllAvailable),color:"#FFA500"},
 {name:'Adequately Stocked', y:calculatePercentage(allItems[0].percentage_of_sp,totalAllAvailable),color:"#006600"},
 {name:'Unknown',y:calculatePercentage(allItems[0].percentage_of_uk,totalAllAvailable),color:"gray"}
                         ];
        var dataV = [

        {name:'Tracer Items Availability', data:[tracerItems[0].percentage_of_total]},
        {name:'All Items Availability', data:[allItems[0].percentage_of_total]}

        ];

var availableData =  [
                      {
                        name: 'not available',
                        color:'#D90C29',
                        data: [{
                        name: 'All Items',
                        y: 100 - allItems[0].percentage_of_total,

                        drilldown: null
                        }, {
                        name: 'Tracer Items',
                        color:'#D90C29',
                        y: 100 - tracerItems[0].percentage_of_total,

                        drilldown: null
                        }]
                        },
                        {
                                 name: 'available',
                                 color:'#3C81B0',
                                 data: [{
                                     name: 'All Items',
                                     y: allItems[0].percentage_of_total,

                                     drilldown: 'phc-available'
                                 }, {
                                     name: 'Tracer Items',
                                     y: tracerItems[0].percentage_of_total,
                                     color:'#3C81B0',
                                     drilldown: 'hospital-available'
                                 }]
                         }

                        ];

    $scope.title_stock_by_level = 'Stock availability for '+params.programName+' '+'( '+params.periodName+', '+params.year+' )';

    $scope.showTheChart(availableData,'','',categories,availableTracer,availableAll);
     }

    });

    };


    $scope.showTheChart = function (data,title, subtitle, categories,availableTracer,availableAll) {

      (function (H) {

            //For X-axis labels
            H.wrap(H.Point.prototype, 'init', function (proceed, series, options, x) {
                var point = proceed.call(this, series, options, x),
                    chart = series.chart,
                    tick = series.xAxis && series.xAxis.ticks[x],
                    tickLabel = tick && tick.label;
                //console.log("series");
                //console.log(series);

                if (point.drilldown) {

                    if (tickLabel) {
                        if (!tickLabel._basicStyle) {
                            tickLabel._basicStyle = tickLabel.element.getAttribute('style');
                        }
                        tickLabel.addClass('highcharts-drilldown-axis-label')          .css({
                            'text-decoration': 'none',
                            'font-weight': 'normal',
                            'cursor': 'auto'
                            }).on('click', function () {
                                if (point.doDrilldown) {
                                    return false;
                                }
                            });//remove this "on" block to make axis labels clickable
                    }
                }
                else if (tickLabel && tickLabel._basicStyle)
                {
                }

                return point;
            });
        })(Highcharts);


 $('#stock-by-level').highcharts({
        chart: {
            type: 'column'
        },
       title: {
                   text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
                   align:'left'
               },
               credits :{
                enabled:false
               },
               subtitle: {
                  text: '<span style="font-size: 14px!important;color: #0c9083;">'+subtitle+' </span>'

               },
              xAxis: {
                  type: 'category'
              },
               yAxis: {
                              title: {
                                  text: '<span style="font-size: 10px!important;color: #0c9083">Total Percentage Incidences Occurred </span>'
                              },

                       min:0,
                       max: 100

                      },


        plotOptions: {
            column : {
                stacking : 'normal',
                    borderWidth: 0,
                                  pointWidth: 30,
                                   pointPadding: 0.2,
                                dataLabels: {
                                    enabled: true
                   }
            }
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
                       $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY_BY_LEVEL','Stock Availability By Level');
                       }
                       }
                       }
                       },
        series:data,
        drilldown: {

        series: [{      type: 'pie',
                        size: '60%',
                        id: 'phc-available',

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

                        data:availableAll
                    }, {
                        type: 'pie',
                        size: '60%',
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
                        id: 'hospital-available',
                        data: availableTracer

                    }, {
                        id: 'phc-not available',
                        data: []
                    }, {
                        id: 'hospital-not-available',
                        data: []
                    }]


        }
    });








    // Create the chart
    /*$('#stock-by-level').highcharts({
       chart: {
             type: 'column'
         },
         title: {
             text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
             align:'left'
         },
         credits :{
          enabled:false
         },
         subtitle: {
            text: '<span style="font-size: 14px!important;color: #0c9083;">'+subtitle+' </span>'

         },
        xAxis: {
            type: 'category'
        },
        yAxis: {
                title: {
                    text: '<span style="font-size: 10px!important;color: #0c9083">Total Percentage Incidences Occurred </span>'
                }

        },

         legend: {
                enabled: false
            },

        plotOptions: {
            series: {
                stacking: 'normal',
                borderWidth: 0,
                  pointWidth: 30,
                    pointPadding: 0.2,
                dataLabels: {
                    enabled: true
                }
            },
             column: {
                        stacking: 'percent'

                    },

        },
         tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
            },   exporting: {
                      buttons: {
                       customButton: {
                       text: '<span style="background-color:blue"><i class="material-icons md-18">Info</i></span>',
                       symbolStroke: "red",
                       theme: {
                       fill:"#28A2F3"
                       },
                       onclick: function () {
                       $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY_BY_LEVEL','Stock Availability By Facility Level');
                       }
                       }
                       }
                       },

        series: data,
        drilldown: {
            series: [{
                id: 'phc-available',

                data:availableAll
            }, {
                id: 'hospital-available',
                data: availableTracer
            }, {
                id: 'phc-not available',
                data: []
            }, {
                id: 'hospital-not-available',
                data: []
            }]
        }
    });*/

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

    $rootScope.loadStockAvailableByLevel($location.search());


    });


    });

    };





}