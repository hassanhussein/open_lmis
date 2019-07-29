function PercentageWastageController ($scope,$rootScope, PercentageWastageData,$window){

$rootScope.getPercentage = function (percentFor,percentOf)
{
    return Math.round((parseInt(percentFor,10)/parseInt(percentOf,10) *100));
}


$scope.percentageWastage = [];

$rootScope.loadPercentageWastageData = function (params) {

PercentageWastageData.get(params).then(function(data) {

if(!isUndefined(data) && data.length > 0) {

var totalAdjustment = 0;
var total = 0;
console.log(data);

data.forEach(function(d){

var highest = _.max(data, function(o){return o.adjustment_qty;});
totalAdjustment = totalAdjustment + parseInt(d.adjustment_qty,10);
total = total + parseInt(d.total,10);

if(parseInt(highest.adjustment_qty,10) === d.adjustment_qty ) {
$scope.percentageWastage.push({ name:d.adjustmentname,y:d.adjustment_qty,sliced: true, selected: true, color:'red'});
}else{
$scope.percentageWastage.push({ name:d.adjustmentname,y:d.adjustment_qty});
}



});
$scope.category = _.pluck(data,'adjustmentname');

$scope.percentageValue = $rootScope.getPercentage (totalAdjustment,total);

$rootScope.wastageTitle = 'Percentage of wastage for '+params.programName+' reported on the period of '+params.periodName+', '+params.year;

$scope.loadPercentageWastageChart($scope.percentageValue,'');


/*  data: [{
            name: 'Chrome',
            y: 61.41,
            sliced: true,
            selected: true
        }*/

}



});

};




$scope.loadPercentageWastageChart = function(dataV,title) {

var gaugeOptions = {

    pane: {
        center: ['50%', '80%'],
        size: '130%',
        startAngle: -90,
        endAngle: 90,
        background: {
            backgroundColor:
                Highcharts.defaultOptions.legend.backgroundColor || '#EEE',
            innerRadius: '60%',
            outerRadius: '100%',
            shape: 'arc'
        }
    },

    tooltip: {
        enabled: false
    },

    // the value axis
    yAxis: {
        stops: [
            [0.1, '#55BF3B'], // green
            [0.5, '#DDDF0D'], // yellow
            [0.9, '#DF5353'] // red
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
            y: -70
        },
        labels: {
            y: 16
        }
    },

    plotOptions: {
        solidgauge: {
            events: {
                        click: function() {
                            document.getElementById('back').style.display = "block";
                            Highcharts.chart('container-speed', columnsOptions);
                        }
                    },
            dataLabels: {
                y: 5,
                borderWidth: 0,
                useHTML: true,
                  enabled: true
            },
             cursor: 'pointer'
           /*       events: {
                    click: function() {
                                              // document.getElementById('back').style.display = "block";
              Highcharts.chart('container-speed', columnsOptions);

                    }
              }*/




        }
    }
};

// The speed gauge
 /*Highcharts.chart('container-speed', Highcharts.merge(gaugeOptions,*/

 var solidGaugeOptions =
 {

     chart: {
         type: 'solidgauge'
     },
title: {
        text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>'


    },
    yAxis: {
        min: 0,
        max: 100,


            labels: {
            distance: 20,
                style: {
                    'font-size': 18,
                    'color':'#0c9083'
                },valueSuffix: ' %'
            },

                lineWidth: 0,
                minorTicks: false,
                tickWidth: 2,
                tickPosition: 'outside'
    },

    credits: {
        enabled: false
    },

     series: [{
         name: 'Wastage',
         colorByPoint: true,
         data: [{y:dataV, drilldown: "A" }],
         dataLabels: {
             format:
                 '<div style="text-align:center">' +
                 '<span style="font-size:25px;color: #0c9083">{y} %</span><br></br></br>' +
                 '</div>'
         },
         tooltip: {
             valueSuffix: ' %'
         }
     }],

     drilldown: {

      series: [
                 {
                     name: "wastage",
                     id: "A",
                     data: {}
                 }]

     }




};

Highcharts.chart('container-speed', Highcharts.merge(gaugeOptions,solidGaugeOptions));


document.getElementById('back').addEventListener('click', function() {
    this.style.display = "none";
    Highcharts.chart('container-speed', Highcharts.merge(gaugeOptions,solidGaugeOptions));
});

var titleV = 'Percentage wastage by losses and adjustment Type';

 var columnsOptions = {

     chart: {
         type: 'pie',
           plotBackgroundColor: null,
               plotBorderWidth: null,
               plotShadow: false
       },credits:{
       enabled:false
       },
        title: {
               text:'<span style="font-size: 15px!important;color: #0c9083">'+titleV+'</span>'

           },
             tooltip: {
             pointFormat: '{point.name}: <b>{point.percentage:.0f} %<br>total: {point.total}'

             },

       plotOptions: {
         pie: {
           allowPointSelect: true,
                                    cursor: 'pointer',
           innerSize: '70%',
              dataLabels: {  useHTML: true,
                            enabled: true,
/*
                            format: '<b>{point.name}</b>:<br>{point.percentage:.1f} %',
*/

                            formatter: function () {
                                return '<span style="color:' + this.point.color + '"<b>'+this.point.name+'</b>: '+Math.round(this.point.percentage)+' % </span>';


                            }


                          }

         }

       },

       series: [{
        animation: true,
        colorByPoint: true,
         data: $scope.percentageWastage,

                       point:{
                           events:{
                               click: function (event) {

                               $scope.openAdjustmentReport(this);

                               }
                           }
                       }

       }],

         exporting: {

                         buttons: {
                                customButton: {
                                    text: '<span style="background-color:blue;display: none;"> <a id="back" style="display: none;" class="waves-effect waves-light btn"><i class="material-icons left">arrow_back</i> ◁ Back to Wastage Percentage</a></span>',

                                    symbolStroke: "red",
                                                        theme: {
                                                fill:"#28A2F3"
                                            },
                                    onclick: function () {
                                         Highcharts.chart('container-speed', Highcharts.merge(gaugeOptions,solidGaugeOptions));
                                    }
                                }
                            }
                        }

        };


};

$scope.openAdjustmentReport = function(data) {
 console.log(data);

 $scope.$parent.params.adjustmentType = data.name;
 $scope.$parent.params.color = data.color;

 var url = '/public/pages/reports/main/index.html#/adjustment-summary?'+jQuery.param($scope.$parent.params);
 $window.open(url, "_BLANK");

};

}