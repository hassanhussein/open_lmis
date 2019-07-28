function TimelinessReportingController($scope,$rootScope,TimelinessReportingData,$window,OntimeDeliveryReportData){

$rootScope.loadOnTimeDelivery = function(params){
$scope.dataToDisplay = [];

OntimeDeliveryReportData.get(params).then(function(data){

if(data.length >0) {

$scope.dataToDisplay = data;

   var display = [

   {y:100 - $rootScope.getPercentage(data[0].received,data[0].total),  val: data[0].total - data[0].received,color:'red'},
   {y:$rootScope.getPercentage(data[0].received,data[0].total),  val: data[0].received }

   ];

$scope.loadOnTimeChart(display, 'On-Time Delivery','on-time-delivery');

}

});

}

$scope.loadOnTimeChart = function (dataV, title, chartId){

new Highcharts.Chart({
    chart: {
        renderTo:chartId,
        type:'bar'
    },
    title:{

    text:'<span style="font-size: 15px!important;color: #0c9083">'+title+' </span>'

    },
    credits:{enabled:false},
    legend:{
    enabled:false

    },
    tooltip: {
        formatter: function() {
            return 'Value: '+this.point.val;
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
                       $rootScope.openDefinitionModal('DASHLET_STOCK_AVAILABILITY', 'Stock Availability');
                       }
                       }
                       }
                       },
    plotOptions: {
        series: {
            shadow:false,
            borderWidth:0,
            dataLabels:{
                enabled:true,
                formatter: function() {
                    return this.y +'%';
                }
            }
        }
    },
    xAxis:{
        lineColor:'#999',
        lineWidth:1,
        tickColor:'#666',
        tickLength:3,

        categories: ['% HF did not receive Deliveries','% HF Received Deliveries'],

            margin:0

    },
    yAxis:{
        lineColor:'#999',
        lineWidth:1,
        tickColor:'#666',
        tickWidth:1,
        tickLength:3,
        gridLineColor:'#ddd',
        title:{
            text:'percentage',
            rotation:0
        },
        labels: {
            formatter: function() {
                return (this.isLast ? this.value + '%' : this.value);
            }
        }
    },
    series: [{
        data: dataV
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
       dataToShow= [{name:'Reported On Time', y:$rootScope.getPercentage(dx.reportedontime,dx.totalexpected),drilldown: null},{name:'Reported Late',y:$rootScope.getPercentage(dx.reportedlate,dx.totalexpected),drilldown: null},{name:'Un scheduled Reporters',y:$rootScope.getPercentage(dx.unscheduled,dx.totalexpected),drilldown: null},{name:'Not reported Facilities',y:$rootScope.getPercentage(dx.not_reported,dx.totalexpected),drilldown: null}];


       //dataToShow = [dx.not_reported, dx.unscheduled, dx.reportedlate, dx.reportedontime];
    });

    $scope.loadChart(chartId,dataToShow,'Reporting Timeliness','' +params.programName+' '+params.periodName+' ',data[0].totalexpected,params.year);


    }



});

}

$scope.loadChart = function(chartId,dataV,title,subtitle,totalExpected,year){


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
          allowPointSelect: true,
                cursor: 'pointer',
                 innerSize: '60%'


      },
         series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y}%'
                        }
                    }

    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}%</b> of total<br/>'
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

    series: [
        {
            name: "Facilities",
            colorByPoint: true,
            data: dataV
        }
    ]
});



}

}