function RnRQualityCheckController ($scope,$location,$state,$window,Program,Period,$rootScope,RnrPassedQualityCheckData){

$rootScope.loadRnrPassedQualityCheckData =  function (params) {

    $scope.opentitle = 'R&R passed data quality check '+params.periodName+' ,'+params.year;

    RnrPassedQualityCheckData.get(params).then(function(data){

    if(data.length > 0){


    var percentage = Math.round((parseInt(data[0].passed_total,10) * 100/parseInt(data[0].total,10)),10);

    var values = [{name:"Total number of R&R passed data quality check ",y:percentage,color:'green',drilldown:'passed_total'},{name:"Total R&R did not pass the quality check",sliced:true,color:'red',y:100-percentage,drilldown:'details'}];

    getRnRPasseChart(' ',values,params);

    }

    });

};



function getRnRPasseChart(title,dataV,para){
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
        text: '<span style="font-size: 13px!important;color: #0c9083">Click the slices to view more details</span>'
    },
    plotOptions: {
          pie: {
                           innerSize: '70%',
                           allowPointSelect: true,
                           size: '100%',
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
                       }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
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
                           $rootScope.openDefinitionModal('RNR_QUALITY_CHECK', 'R&R passed data quality check ');
                           }
                           }
                           }
                           },
    series: [
        {
            name: "Summary",
            colorByPoint: true,
            data:dataV


        }
    ],
    drilldown: {
        series: [
            {
                name: "Chrome",
                id: "details",
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






}


function openRejectionReport(params){

 //$state.go('home2',params);



/* var url = '/public/pages/reports/other/index.html#/home?'+jQuery.param(params);
 console.log(url);
 $window.open(url, "_BLANK");*/



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

$rootScope.loadRnrPassedQualityCheckData($location.search());


});


});






};



}