function fundUtilizationController($scope,$rootScope,GetDistrictFundUtilizationData) {

  $rootScope.loadDistrictFundUtilization = function(params){

   GetDistrictFundUtilizationData.get(params).then(function(data) {
   var category = _.pluck(data,'district');

   var other = _.pluck(data,'other');
   var userFees = _.pluck(data,'userFees');
   var dataV  = [];
    dataV = [{name:'Other', data:other}, {name:'userFees', data:userFees}];

       console.log(dataV);

   loadChart(category,dataV);



    });


  }


function loadChart (category,dataV) {


new Highcharts.chart('district-fund-ultilization', {
             chart: {
                 type: 'column'
             },
             credits: {
             enabled:false
             },
             title: {

                 text: '<span style="font-size: 15px!important;color: #0c9083">Fund Utilization by District</span>'
             },
             subtitle: {
                 text: ' '
             },
             xAxis: {
                 categories: category,
                 crosshair: true
             },
             yAxis: {
                 min: 0,
                 title: {
                     text: 'Millions'
                 }
             },
             tooltip: {
             /*    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                 pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                     '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                 footerFormat: '</table>',
                 shared: true,
                 useHTML: true*/

                 headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                 pointFormat:                      '<td><b>{point.y:.1f}</b></td></tr>'

             },
             plotOptions: {
                 column: {
                     pointPadding: 0.2,
                     borderWidth: 0
                 }
             },
             series:dataV
         });


}








}