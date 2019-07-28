function StockAvailabilityByLevelController ($scope, $rootScope,StockAvailableByLevelData){


$rootScope.loadStockAvailableByLevel = function(params){

    StockAvailableByLevelData.get(params).then(function(data) {

     console.log(data);

     var categories = _.pluck(data, 'tracerItems');


     var hospital = _.filter(data, {traceritems:'tracerItems'});
     var phc = _.filter(data, {traceritems:'allItems'});

     var displayData = [];

  /*    data.forEach(function(d){

       displayData.push({name:d.facilitytype,y:d.percentage_of_total,drilldown:d.facilitytype});

      });*/










        var dataV = [

        {name:'Tracer Items Availability', data:[hospital[0].percentage_of_total]},
        {name:'All Items Availability', data:[phc[0].percentage_of_total]}

        ];


          var availabileData =  [
                      {
                        name: 'not available',
                        data: [{
                        name: 'Primary Health Care',
                        y: 100 - phc[0].percentage_of_total,
                         color:'#D90C29',
                        drilldown: 'phc-not available'
                        }, {
                        name: 'Hospital',
                        y: 100 - hospital[0].percentage_of_total,
                        color:'#D90C29',
                        drilldown: 'hospital-not-available'
                        }]
                        },
        {
                                 name: 'available',
                                 data: [{
                                     name: 'Primary Health Care',
                                     y: phc[0].percentage_of_total,
                                     color:'#50B432',
                                     drilldown: 'phc-available'
                                 }, {
                                     name: 'Hospital',
                                     y: hospital[0].percentage_of_total,
                                     color:'#50B432',
                                     drilldown: 'hospital-available'
                                 }]
                             }




                        ];

         console.log(phc);

    $scope.showTheChart(availabileData,'Stock availability by Level for '+params.programName,'( '+params.periodName+' )',categories);


    });

    }


    $scope.showTheChart = function (data,title, subtitle, categories) {

/*
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
*/



/*Highcharts.chart('stock-by-level', {
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
            borderWidth: 0,
            dataLabels: {
                enabled: true,
                format: '{point.y:.1f}%'
            }
        }
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

    series: [
        {
            name: "Summary",
            colorByPoint: true,
            data: data
        }
    ],
    drilldown: {
        series: [
            {
                name: "Primary Health Care",
                id: "Primary Health Care",
                data: [
                    [
                        "v65.0",
                        0.1
                    ],
                    [
                        "v64.0",
                        1.3
                    ],
                    [
                        "v63.0",
                        53.02
                    ],
                    [
                        "v62.0",
                        1.4
                    ],
                    [
                        "v61.0",
                        0.88
                    ],
                    [
                        "v60.0",
                        0.56
                    ],
                    [
                        "v59.0",
                        0.45
                    ],
                    [
                        "v58.0",
                        0.49
                    ],
                    [
                        "v57.0",
                        0.32
                    ],
                    [
                        "v56.0",
                        0.29
                    ],
                    [
                        "v55.0",
                        0.79
                    ],
                    [
                        "v54.0",
                        0.18
                    ],
                    [
                        "v51.0",
                        0.13
                    ],
                    [
                        "v49.0",
                        2.16
                    ],
                    [
                        "v48.0",
                        0.13
                    ],
                    [
                        "v47.0",
                        0.11
                    ],
                    [
                        "v43.0",
                        0.17
                    ],
                    [
                        "v29.0",
                        0.26
                    ]
                ]
            },
            {
                name: "HOSPITAL",
                id: "HOSPITAL",
                data: [
                    [
                        "v58.0",
                        1.02
                    ],
                    [
                        "v57.0",
                        7.36
                    ],
                    [
                        "v56.0",
                        0.35
                    ],
                    [
                        "v55.0",
                        0.11
                    ],
                    [
                        "v54.0",
                        0.1
                    ],
                    [
                        "v52.0",
                        0.95
                    ],
                    [
                        "v51.0",
                        0.15
                    ],
                    [
                        "v50.0",
                        0.1
                    ],
                    [
                        "v48.0",
                        0.31
                    ],
                    [
                        "v47.0",
                        0.12
                    ]
                ]
            }
        ]
    }
});*/




    // Create the chart
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
                }

        },

         legend: {
                enabled: false
            },

        plotOptions: {
            series: {
                stacking: 'normal',
                borderWidth: 0,
                dataLabels: {
                    enabled: true
                }
            },
             column: {
                        stacking: 'percent'
                    }
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
                data: [
                    ['East', 4],
                    ['West', 2],
                    ['North', 1],
                    ['South', 4]
                ]
            }, {
                id: 'hospital-available',
                data: [
                    ['East', 6],
                    ['West', 2],
                    ['North', 2],
                    ['South', 4]
                ]
            }, {
                id: 'phc-not available',
                data: [
                    ['East', 2],
                    ['West', 7],
                    ['North', 3],
                    ['South', 2]
                ]
            }, {
                id: 'hospital-not-available',
                data: [
                    ['East', 2],
                    ['West', 4],
                    ['North', 1],
                    ['South', 7]
                ]
            }]
        }
    });









    }
}