function StockAvailabilityByLevelController ($scope, $rootScope,StockAvailableByLevelData){


$rootScope.loadStockAvailableByLevel = function(params){

    StockAvailableByLevelData.get(params).then(function(data) {



     var categories = _.pluck(data, 'tracerItems');
console.log(categories);

     var tracerItems = _.filter(data, {traceritems:'tracerItems'});
     var allItems = _.filter(data, {traceritems:'allItems'});

     var displayData = [];

  /*    data.forEach(function(d){

       displayData.push({name:d.facilitytype,y:d.percentage_of_total,drilldown:d.facilitytype});

      });*/










        var dataV = [

        {name:'Tracer Items Availability', data:[tracerItems[0].percentage_of_total]},
        {name:'All Items Availability', data:[allItems[0].percentage_of_total]}

        ];

     console.log(data);
          var availabileData =  [
                      {
                        name: 'not available',
                        data: [{
                        name: 'All Items',
                        y: 100 - allItems[0].percentage_of_total,
                         color:'#D90C29',
                        drilldown: 'phc-not available'
                        }, {
                        name: 'Tracer Items',
                        y: 100 - tracerItems[0].percentage_of_total,
                        color:'#D90C29',
                        drilldown: 'hospital-not-available'
                        }]
                        },
        {
                                 name: 'available',
                                 data: [{
                                     name: 'All Items',
                                     y: allItems[0].percentage_of_total,
                                     color:'#50B432',
                                     drilldown: 'phc-available'
                                 }, {
                                     name: 'Tracer Items',
                                     y: tracerItems[0].percentage_of_total,
                                     color:'#50B432',
                                     drilldown: 'hospital-available'
                                 }]
                             }




                        ];

         console.log(tracerItems);

     $scope.title_stock_by_level = 'Stock availability by Level for '+params.programName+' '+'( '+params.periodName+', '+params.year+' )';

    $scope.showTheChart(availabileData,'','',categories);


    });

    };


    $scope.showTheChart = function (data,title, subtitle, categories) {

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









    };
}