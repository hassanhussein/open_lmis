function AnalyticsFunction(leafletData,DefaultProgram,StockStatusByProgramData,FullProcessingPeriods,$rootScope,IndexOfAluStockAvailabilityData,RnrPassedQualityCheckData,$scope,messageService,GetLocalMap,ConsumptionTrendsData,DashboardStockStatusSummaryData,YearFilteredData,StockAvailableForPeriodData, StockAvailableByProgramAndPeriodData) {

var params;

DefaultProgram.get({}, function (data) {

        if(!isUndefined(data)){

         var program = data.program;

        FullProcessingPeriods.get({program:0}, function (data) {

        var period = data.period;

         params ={product:parseInt(2434,0) ,year:parseInt(period.stringYear,0), program: parseInt(program.id,0),programName:program.name,period:parseInt(91 ,10),periodName:period.name, schedule:period.scheduleId};
         $scope.$parent.params = params;
         //start loading functions by applying parameters

        $scope.loadStockAvailableForPeriodData(params);
        $rootScope.loadTimelinessReportingData(params);
        $rootScope.loadOnTimeDelivery(params);
        $scope.loadRnrPassedQualityCheckData(params);
         $rootScope.loadPercentageWastageData(params);
         $scope.loadMap(params);
         //$scope.loadConsumptionTrendsData(params);
         //$scope.loadStockStatusByProgram(params,'level1');
         $rootScope.loadStockStatusSummary(params);
         //$rootScope.loadStockStatusByProgramAndYearData(params);
         $rootScope.loadStockAvailableByLevel(params);






});

 }
});

  $scope.dashletSectionsLoaded = [];

    $scope.loadDashletsBySectionNames = function(sectionName) {
        switch(sectionName) {
            case 'stockStatus':
                loadStockStatusSection();
                break;
            case 'requisition':
                loadRequisitionSection();
                break;
            default:
                console.log('Dashboard section does not exist');
        }

        sectionName && $scope.dashletSectionsLoaded.push(sectionName);
    };

    $scope.sectionLoaded = function(section) {
        return $scope.dashletSectionsLoaded.includes(section);
    };

$('ul.tabs').tabs().tabs('select_tab', 'tracer');
$('.dropdown-trigger').dropdown();

$('li.dropdown.mega-dropdown').on('click', function (event) {
    $(this).parent().toggleClass('open');
});

$("dropdown-menu mega-dropdown-menu").click(function(e){
   e.stopPropagation();
});

$scope.stopPropagation = function(event,open){

    return event.stopPropagation();
};

$scope.minimizePropagation = function(event,open){

return 'dropdown-toggle';

/*
 $("dropdown-toggle").click(function() {
        $(this).dropdown("toggle");
        return false;
    });
*/


};


 $scope.toggled = function (open) {

        $scope.open = true;
        var child = $scope.$$childHead;
        while (child) {
            if (child.focusToggleElement) {
                child.isOpen = true;
                break;
            }
            child = child.$$nextSibling;
        }
    };
   $("li.show > a").click(function(){
        $("li.hide").fadeToggle();
    });



$rootScope.parameters = params;

console.log(params);

IndexOfAluStockAvailabilityData.get(params).then(function(data){
 var value1 = ['Facilities with 1 Presentation',data[0].total];
 var value2 = ['Facilities with 2 Presentation',data[1].total];
 var value3 = ['Facilities with 3 Presentation',data[2].total];
 var value4 = ['Facilities with 4 Presentation',data[3].total];


var dataV = [value1,value2,value3,value4];

$scope.indexOfStockAvailable(dataV,'Index of Availability of ACTs on the Day of Visit, June, 2018');

});


$scope.loadRnrPassedQualityCheckData =  function (params) {

    RnrPassedQualityCheckData.get(params).then(function(data){

    if(data.length > 0){

    var title = 'Percentage of Report and Requisition forms (R&R) that pass data quality check '+params.periodName+' ,'+params.year;
    var percentage = Math.round((parseInt(data[0].passed_total,10) * 100/parseInt(data[0].total,10)),10);

    var values = [{name:"Total number of R&R that passed data quality check ",y:percentage,color:'green',drilldown:'passed_total'},{name:"Total R&R did not pass the quality check",color:'red',y:100-percentage,drilldown:'total'}];

    $scope.getRnRPasseChart(title,values);

    }

    });

};





//Start of Map

   $scope.geojson = {};

    $scope.default_indicator = "period_over_expected";

    $scope.expectedFilter = function (item) {
        return item.expected > 0;
    };

    $scope.style = function (feature) {
        if ($scope.filter !== undefined && $scope.filter.indicator_type !== undefined) {
            $scope.indicator_type = $scope.filter.indicator_type;
        }
        else {
            $scope.indicator_type = $scope.default_indicator;
        }
        var color = ($scope.indicator_type === 'ever_over_total') ? interpolate(feature.ever, feature.total) : ($scope.indicator_type === 'ever_over_expected') ? interpolate(feature.ever, feature.expected) : interpolate(feature.period, feature.expected);

        return {
            fillColor: color,
            weight: 1,
            opacity: 1,
            color: 'white',
            dashArray: '1',
            fillOpacity: 0.7
        };
    };


        $scope.drawMap = function(json) {
            angular.extend($scope, {
                geojson: {
                    data: json,
                    style: $scope.style,
                    onEachFeature: onEachFeature,
                    resetStyleOnMouseout: true
                }
            });

            $scope.$apply();
        };

    function getExportDataFunction(features) {

        var arr = [];
        angular.forEach(features, function (value, key) {
            if (value.expected > 0) {
                var percentage = {'percentage': ((value.period / value.expected) * 100).toFixed(0) + ' %'};
                arr.push(angular.extend(value, percentage));
            }
        });
        $scope.exportData = arr;
    }
function interpolate(value, count) {
    var val = parseFloat(value) / parseFloat(count);
    var interpolator = chroma.interpolate.bezier(['red', 'yellow', 'green']);
    return interpolator(val).hex();
}

function initiateMap(scope) {
    angular.extend(scope, {
        layers: {
            baselayers: {
                googleTerrain: {
                    name: 'Terrain',
                    layerType: 'TERRAIN',
                    type: 'google'
                },
                googleHybrid: {
                    name: 'Hybrid',
                    layerType: 'HYBRID',
                    type: 'google'
                },
                googleRoadmap: {
                    name: 'Streets',
                    layerType: 'ROADMAP',
                    type: 'google'
                }
            }
        },
        legend: {
            position: 'bottomleft',
            colors: ['#FF0000', '#FFFF00', '#5eb95e', "#000000"],
            labels: ['Non Reporting', 'Partial Reporting ', 'Fully Reporting', 'Not expected to Report']
        }
    });

    scope.indicator_types = [
        {
            code: 'ever_over_total',
            name: 'Ever Reported / Total Facilities'
        },
        {
            code: 'ever_over_expected',
            name: 'Ever Reported / Expected Facilities'
        },
        {
            code: 'period_over_expected',
            name: 'Reported during period / Expected Facilities'
        }
    ];


    scope.viewOptins = [
        {id: '0', name: 'Non Reporting Only'},
        {id: '1', name: 'Reporting Only'},
        {id: '2', name: 'All'}
    ];

}

function popupFormat(feature) {
    return '<table class="table table-bordered" style="width: 250px"><tr><th colspan="2"><b>' + feature.properties.name + '</b></th></tr>' +
        '<tr><td>Expected Facilities</td><td class="number">' + feature.expected + '</td></tr>' +
        '<tr><td>Reported This Period</td><td class="number">' + feature.period + '</td></tr>' +
        '<tr><td>Ever Reported</td><td class="number">' + feature.ever + '</td></tr>' +
        '<tr><td class="bold">Total Facilities</b></td><td class="number bold">' + feature.total + '</td></tr>';
}

function onEachFeature(feature, layer) {
    layer.bindPopup(popupFormat(feature));
}


$scope.zoomMap = function(){

 $scope.centerL = {
            lat: -6.397912857937015,
            lng: 34.911609148190784,
            zoom: 6
          };

};

$scope.zoomMap();



    $scope.centerJSON = function () {
        leafletData.getMap().then(function (map) {


            var latlngs = [];
            for (var c = 0; c < $scope.features.length; c++) {
                if ($scope.features[c].geometry === null || angular.isUndefined($scope.features[c].geometry))
                    continue;
                if ($scope.features[c].geometry.coordinates === null || angular.isUndefined($scope.features[c].geometry.coordinates))
                    continue;
                for (var i = 0; i < $scope.features[c].geometry.coordinates.length; i++) {
                    var coord = $scope.features[c].geometry.coordinates[i];
                    for (var j in coord) {
                        var points = coord[j];
                        var latlng = L.GeoJSON.coordsToLatLng(points);

                        //this is a hack to make the tz shape files to work
                        //sadly the shapefiles for tz and zm have some areas that are in europe,
                        //which indicates that the quality of the shapes is not good,
                        //however the zoom neeeds to show the correct country boundaries.
                        if (latlng.lat < 0 && latlng.lng > 0) {
                            latlngs.push(latlng);
                        }
                    }
                }
            }
          map.fitBounds(latlngs);

        });



    };





  $scope.OnFilterChanged = function() {

     $.getJSON('/gis/reporting-rate.json', $scope.filter, function (data) {
               $scope.features = data.map;
               getExportDataFunction($scope.features);
               angular.forEach($scope.features, function (feature) {
                   feature.geometry_text = feature.geometry;
                   feature.geometry = JSON.parse(feature.geometry);
                   feature.type = "Feature";
                   feature.properties = {};
                   feature.properties.name = feature.name;
                   feature.properties.id = feature.id;
               });

               $scope.drawMap({
                   "type": "FeatureCollection",
                   "features": $scope.features
               });

               $scope.centerJSON();

              // zoomAndCenterMap1(leafletData, $scope);
           });
};


    $scope.loadMap = function(params){
    $scope.filter = params;
    initiateMap($scope);
    $scope.OnFilterChanged();

    };
 $scope.onDetailClicked = function (feature) {
        $scope.currentFeature = feature;
        $scope.$broadcast('openDialogBox');
    };

$scope.consumptionTrends = [];
$scope.loadConsumptionTrendsData = function (params){

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

};

$scope.loadStockAvailableForPeriodData = function (params){

StockAvailableForPeriodData.get(params).then(function(data) {
$scope.stockAvailableForPeriodList = [];

    if(data.length > 0) {


        _.each(data, function(value){

       var totalCalculation = (parseInt(value.totalbyprogram,10) * 100)/value.total;

        $scope.stockAvailableForPeriodList.push({name:value.program_name,y:Math.round(totalCalculation),available:value.totalbyprogram,total:value.total, drilldown:value.programid });

        });
        var chartId = 'stock-available-for-program';
        var title = 'Stock Availability per program';
        var chartType = 'column';

        drillDownChart(chartId,chartType,title,$scope.stockAvailableForPeriodList);
        }


    });
};

$scope.loadStockStatusByProgram = function (params, level){
var stockSummary = [];
var getPromiseData = (level === 'level1')?StockStatusByProgramData:DashboardStockStatusSummaryData;
var chartId =(level === 'level1')?'stock-by-program-and-period':'stockStatusOverTime';

/*StockStatusByProgramData.get(params).then(function(stocks){
var title = (level === 'level1')?'Stock Status by Program':''Stock Status Over Time for '+'Nevirapine''

var overstock = _.pluck(stocks, 'overstock');

console.log(stocks);

$scope.openStatusByProgramChart(null, 'Stock Status by Program');

});*/



var params2 = {product:parseInt(2434,0) ,year:parseInt(2018,0), program: parseInt(1,0),period:parseInt(75,10)};
getPromiseData.get(params).then(function(data) {

$scope.stockStatuses   = [];
 if(!isUndefined(data)){

 stockSummary = data;
  var category = _.uniq(_.pluck(stockSummary,'period'));

             var groupBySchedule  = _.groupBy(stockSummary, function(schedule){
                return schedule.schedule;
             });

             _.map(groupBySchedule, function(groupedData, index) {

                  chartId = 'stock-by-program-and-period.'+index[index.length -1];
                  var category = _.uniq(_.pluck(groupedData,'period'));
                  var title = (level === 'level1')?'Stock Status for  '+params.programName+' '+index+',  '+params.year:'Stock Status Over Time for '+ params.periodName +', '+params.year;
                  var dataV = [];
                  dataV  = groupedData;
                  $scope.drawTheChart(dataV,chartId,title,category);
              console.log(groupedData);
                 return null;

              });






 }


});


};


 $scope.drawTheChart = function (stockSummary,chartId,title,category) {


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

              $scope.stockStatusesStackedColumnChart(chartId,'column' ,title,category, 'Number of Incidences Reported',summaries );

              };




$scope.openStatusByProgramChart= function(dataV,title) {

Highcharts.chart('stock-by-program-and-period', {
    chart: {
        type: 'column'
    },
    title: {
        text: title
    },
    xAxis: {
        categories: ['Apples', 'Oranges', 'Pears', 'Grapes', 'Bananas']
    },
    yAxis: {
        min: 0,
        title: {
            text: 'Total fruit consumption'
        },
        stackLabels: {
            enabled: true,
            style: {
                fontWeight: 'bold',
                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
            }
        }
    },
    legend: {
        align: 'right',
        x: -30,
        verticalAlign: 'top',
        y: 25,
        floating: true,
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
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
                enabled: true,
                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
            }
        }
    },
    series: [{
        name: 'John',
        data: [5, 3, 4, 7, 2]
    }, {
        name: 'Jane',
        data: [2, 2, 3, 2, 1]
    }, {
        name: 'Joe',
        data: [3, 4, 4, 2, 5]
    }]
});



};



 var drillDownSeries = $scope.drillDownData = [];
 function getAvailableDrillDownData(program,name, chartData){

     params.program = program;
    var allParams = angular.extend(params, {program:program});
    StockAvailableByProgramAndPeriodData.get(params).then(function(data){

     $scope.titleStockForProgramAvailable = 'List of Available Tracer Items for '+name +' in '+params.periodName+', '+params.year;
     $scope.stockColor= chartData.color;

     $scope.drillDownData = data;

     _.each(data, function(drilledData){
     // drillDownSeries.push({name:name,id:program,data:drilledData})

     });

     var category =_.pluck(data,'productname');
     var values = _.pluck(data,'mos');

     //$scope.availableStockByProgramModal = true;
     $scope.dataTableStockStatusChart(category,values,$scope.titleStockForProgramAvailable,chartData.color);

     //$('#availableStockByProgramModal').modal();



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
      text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
             align:'left'
    },
    subtitle: {
        //text: 'Click the columns to view stock availability of each tracer items'
               text: '<span style="font-size: 14px!important;color: #0c9083;"> Click the columns to view more details</span>'

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

                  getAvailableDrillDownData(drilldown,this.name, this);



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
      exporting: {

        buttons: {
         customButton: {
         text: '<span style="background-color:blue"><i class="material-icons md-18">Info</i></span>',
         symbolStroke: "red",
         theme: {
         fill:"#28A2F3"
         },
         onclick: function () {
         $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY_BY_PROGRAM', 'Stock Availability by Program');
         }
         }
         }
         },

    series: [
        {
            name: "Programs",
            colorByPoint: true,
            data: data
        }
    ]//,
        //  exporting: {
                   /*  buttons: {
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
                     }*/
                 //}

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
    }],

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


 };


$scope.getRnRPasseChart = function(title,dataV){
console.log(dataV);
new Highcharts.chart('rnrPassedChart', {
    chart: {
        type: 'pie'
    },credits:{
    enabled:false

    },
    title: {
        text:'<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>'

    },
    subtitle: {
        text: 'Click the slices to view more details'
    },
    plotOptions: {
          pie: {
                           innerSize: '70%',
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
                           showInLegend: true
                       }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
    },

    series: [
        {
            name: "Requisition Forms",
            colorByPoint: true,
            data:dataV


        }
    ],
    drilldown: {
        series: [
            {
                name: "Chrome",
                id: "Chrome",
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
            }
        ]
    }
});






};



$scope.indexOfStockAvailable = function(dataV,title) {
  new Highcharts.Chart({
    chart: {
      type: 'pie',
      renderTo: 'indexOfALUAvailability'
    },credits:{
    enabled:false
    },
     title: {
            text:'<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>'

        },
   /* title: {
      verticalAlign: 'middle',
      floating: true,
      text: 'CENTERED<br>TEXT'
    },*/
    plotOptions: {
      pie: {
        innerSize: '70%'
      }
    },

    series: [{
      data: dataV
    }]
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

