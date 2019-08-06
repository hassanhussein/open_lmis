function stockAvailabilityPerProgramController($scope,messageService,StockAvailableForPeriodData,StockAvailableByProgramAndPeriodData,$location,Program,Period,$rootScope,$window) {


$rootScope.loadStockAvailableForPeriodData = function (params){

StockAvailableForPeriodData.get(params).then(function(data) {
$scope.stockAvailableForPeriodList = [];
    $rootScope.title_stock_by_program = 'Stock Availability per program '+params.periodName+', '+params.year+'';

    if(data.length > 0) {


        _.each(data, function(value){

       var totalCalculation = (parseInt(value.totalbyprogram,10) * 100)/value.total;//#50B432

        $scope.stockAvailableForPeriodList.push({name:value.program_name,color:'#3C81B0',y:Math.round(totalCalculation),available:value.totalbyprogram,total:value.total, drilldown:value.programid });

        });
        var chartId = 'stock-available-for-program';
        var title = 'Stock Availability per program';

        var chartType = 'column';
        title ='';
        drillDownChart(chartId,chartType,title,$scope.stockAvailableForPeriodList,params);
        }


    });
};



function drillDownChart(id,chartType, title,data,para){

// Create the chart
Highcharts.chart(id, {
    chart: {
        type: chartType,
            spacingBottom: 10,
                spacingTop: 30,
                spacingLeft: 0,
                spacingRight: 10
    },
    title: {
      text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
             align:'left'
    },
    subtitle: {
        //text: 'Click the columns to view stock availability of each tracer items'
               text: ''

    },
    xAxis: {
        type: 'category'
    },
    yAxis: {
        title: {
             text: '<span style="font-size: 13px!important;color: #0c9083">Total percent of tracer products</span>'
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

                  getAvailableDrillDownData(para,drilldown,this.name, this);



              }

             }

          }
        },
        series: {
            borderWidth: 0,
              pointWidth: 20,
                         pointPadding: 0.1,
                         groupPadding: 0,
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
     text: '<span style="background-color:blue"><i class="fas fa-info-circle></i>info</span>',
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


 var drillDownSeries = $scope.drillDownData = [];
 function getAvailableDrillDownData(params, program,name, chartData){

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

       $rootScope.loadStockAvailableForPeriodData($location.search());


       });


       });

       };


}