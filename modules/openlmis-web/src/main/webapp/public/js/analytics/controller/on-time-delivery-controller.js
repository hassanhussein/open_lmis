function OnTimeDeliveryController ($scope,$location,Program,Period,$rootScope,$window,OntimeDeliveryReportData){

$rootScope.loadOnTimeDelivery = function(params){
$scope.dataToDisplay = [];

OntimeDeliveryReportData.get(params).then(function(data) {

$rootScope.titleOnTimeDelivery = '#6: On-Time Delivery for '+ params.programName+' ( '+params.periodName+' ,'+params.year+' )';
if(data.length >0) {


 $scope.dataToDisplay = [

   {y:100 - $rootScope.getPercentage(data[0].received,data[0].total),  val: data[0].total - data[0].received,color:'red'},
   {y:$rootScope.getPercentage(data[0].received,data[0].total),  val: data[0].received, color:'#3C81B0' }

   ];
loadOnTimeChart($scope.dataToDisplay, '','on-time-delivery');
//loadTheChart(categories,display,'on-time-delivery','column','','','' );

}

});

};

function loadOnTimeChart (dataV, title, chartId){

new Highcharts.Chart({
    chart: {
        renderTo:chartId,
        type:'bar',
        spacingBottom: 10,
        spacingTop: 30,
        spacingLeft: 0,
        spacingRight: 10,
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
                       $rootScope.openDefinitionModal('DASHLET_ON_TIME_DELIVERY', 'On Time Delivery');
                       }
                       }
                       }
                       },
    plotOptions: {
        series: {
            shadow:false,
            borderWidth:0,
            pointWidth: 20,
             pointPadding: 0.1,
             groupPadding: 0,
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
        tickLength:1,

        categories: ['% HF did not receive Deliveries','% HF Received Deliveries'],
         labels: {
              style: {
                color: '#0c9083'
              }
            },

            margin:0

    },
    yAxis:{
        lineColor:'#999',
        lineWidth:1,
        tickColor:'#666',
        tickWidth:0.1,
        tickLength:1,
        gridLineColor:'#ddd',
        title:{
            text: '<span style="font-size: 13px!important;color: #0c9083">percentage</span>',
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

$rootScope.loadOnTimeDelivery($location.search());


});


});






};

}