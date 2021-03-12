function RnRQualityCheckController (GetRejectedRnRByZoneReport,$scope,$location,$state,$window,Program,Period,$rootScope,RnrPassedQualityCheckData){

$rootScope.loadRnrPassedQualityCheckData =  function (param) {
      console.log(param);

        $scope.opentitle = '#7: R&R passed data quality check '+param.periodName+' ,'+param.year;

         param.status='INITIATED';
         param.max=10000;
         param.page=1;
         var dataV = [];
        GetRejectedRnRByZoneReport.get(param, function (data) {
                      console.log(data.pages.rows);
           if(data.pages !== undefined && !isUndefined(data.pages.rows)){
           // dataValue=data.pages.rows;

            dataV = $scope.getRejectionRate(data.pages.rows);

           loadChartSummary(param,dataV);

           } else {

            loadChartSummary(param,null);

          }

            //  $scope.pagination = data.pagination;
          //  $scope.totalItems = $scope.pagination.totalRecords;
          //  $scope.currentPage = $scope.pagination.page;

            return dataV;
        });


      console.log(param);

};


function loadChartSummary(params,drillDownData) {




    RnrPassedQualityCheckData.get(params).then(function(data){

    if(data.length > 0){


    var percentage = Math.round((parseInt(data[0].passed_total,10) * 100/parseInt(data[0].total,10)),10);

    var values = [{name:"Total number of R&R passed data quality check ",y:percentage,color:'green',drilldown:'passed_total'},{name:"Total R&R did not pass the quality check",sliced:true,color:'red',y:100-percentage,drilldown:'details'}];

     $scope.dataValue = [];


    getRnRPasseChart(' ',values,params, drillDownData);

    }

    });



}





$scope.loadRejectionByZone = function (params) {

         var param = jQuery.param(params);
         console.log(param);
         params.status='INITIATED';
         params.max=10000;
         params.page=1;
         var dataV = [];
        GetRejectedRnRByZoneReport.get(params, function (data) {
                      console.log(data.pages.rows);
           if(data.pages !== undefined && !isUndefined(data.pages.rows)){
           // dataValue=data.pages.rows;

            dataV = $scope.getRejectionRate(data.pages.rows);
            console.log( $scope.getRejectionRate(data.pages.rows));
           }

            //  $scope.pagination = data.pagination;
          //  $scope.totalItems = $scope.pagination.totalRecords;
          //  $scope.currentPage = $scope.pagination.page;

            return dataV;
        });

};

$scope.getRejectionRate = function(rows) {

        var groupByZone = _.groupBy(rows, 'zoneName');

        var data = _.map(groupByZone, function (value, key) {

            var total = 0;
            for (var i = 0; i < value.length; i++) {
                var rejectedCount = value[i].rejectionCount;
                total += (rejectedCount);
            }
            return {'key': key, 'total': total};
        });
        var totalValues = _.pluck(data, 'total');
        var key = _.pluck(data, 'key');

        var maximumValue = Math.max.apply(null, totalValues);

        var array1 = key, array3 = totalValues, result = [], i = -1;

        while (array1[++i]) {
            if (array3[i] === maximumValue)
                result.push({
                    name: array1[i], y: array3[i], sliced: true,
                    selected: true,color:'red'
                });
            else
                result.push([array1[i], array3[i]]);
        }

       return result;
        //console.log(JSON.stringify(dataValue));
       // functionalData(result);
    };

function getRnRPasseChart(title,dataV,para,drillDownData){

//dataValue = [{"name":"Dar Es Salaam Zone","y":34,"sliced":true,"selected":true,"color":"red"},["Dodoma Zone",27],["Iringa Zone",12],["Mbeya Zone",31],["Moshi Zone",8],["Mtwara Zone",7],{"name":"Tabora Zone","y":34,"sliced":true,"selected":true,"color":"red"},["Tanga Zone",3]];

console.log(drillDownData);
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
                data: drillDownData
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