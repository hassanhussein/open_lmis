function FacilityStockStatusMapController ($scope,$rootScope,$compile,GetGeoStockStatusForMapData,GetGeoStockStatusDetailsData,GetRegionalStockStatusSummaryData, GetGeoJsonInfoData) {

$scope.centerL = {
            lat: -6.397912857937015,
            lng: 34.911609148190784,
            zoom: 6
          };

$rootScope.facility = {"id":14994,"code":"DM520269","name":"Chahwa","description":"Dispensary","gln":"G17434","mainPhone":"2552603636","fax":"null","geographicZone":{"id":493,"code":"dom","name":"Dodoma CC","level":{"id":4,"code":"dist","name":"District","levelNumber":4},"parent":{"code":"dodo","name":"Dodoma","level":{"code":"reg","name":"Region"}},"catchmentPopulation":10000},"facilityType":{"id":1,"code":"disp","name":"Dispensary","description":"Dispensary","levelId":5,"nominalMaxMonth":6,"nominalEop":3,"displayOrder":13,"active":true},"catchmentPopulation":10000,"latitude":-6.06583,"longitude":35.98432,"altitude":531.3,"operatedBy":{"id":1,"code":"MOHCDGEC","text":"MOHCDGEC","displayOrder":1},"coldStorageGrossCapacity":9,"coldStorageNetCapacity":9,"suppliesOthers":false,"sdp":true,"hasElectricity":true,"online":true,"hasElectronicSCC":false,"hasElectronicDAR":false,"active":true,"goLiveDate":"2013-01-01","goDownDate":"2013-01-01","satellite":false,"enabled":true,"virtualFacility":false,"supportedPrograms":[{"id":9203,"facilityId":14994,"program":{"id":1,"code":"ils","name":"ILS","description":"ILS","active":true,"budgetingApplies":true,"templateConfigured":true,"regimenTemplateConfigured":false,"isEquipmentConfigured":false,"enableSkipPeriod":false,"showNonFullSupplyTab":true,"hideSkippedProducts":true,"enableIvdForm":false,"push":false,"usePriceSchedule":false,"enableMonthlyReporting":false},"active":true,"startDate":"2013-01-01","stringStartDate":"2013-01-01","editedStartDate":"2013-01-01"}],"owners":[{"displayName":"ZPCT ZPCT","owner":{"id":1,"code":"ZPCT","text":" ZPCT","displayOrder":1},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":0},{"displayName":"CIDRZ CIDRZ","owner":{"id":2,"code":"CIDRZ","text":"CIDRZ","displayOrder":2},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":1},{"displayName":"ZDF ZDF","owner":{"id":4,"code":"ZDF","text":"ZDF","displayOrder":3},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":2},{"displayName":"CHAZ CHAZ","owner":{"id":3,"code":"CHAZ","text":"CHAZ","displayOrder":4},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":3},{"displayName":"SBH SYSTEMS FOR BETTER HEALTH","owner":{"id":5,"code":"SBH","text":"SYSTEMS FOR BETTER HEALTH","displayOrder":5},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":4},{"displayName":"CRS CRS ","owner":{"id":6,"code":"CRS","text":"CRS ","displayOrder":6},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":5},{"displayName":"UM UNIVERSITY OF MARYLAND","owner":{"id":7,"code":"UM","text":"UNIVERSITY OF MARYLAND","displayOrder":7},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":6}],"stringGoLiveDate":"01-01-2013","stringGoDownDate":"01-01-2013","interfaceMappings":[]};

$rootScope.loadGeoFacilityStockMap = function(params){
   params.period = parseInt(91,10);

      createTheChart(null,params);

  /* GetGeoStockStatusForMapData.get(params).then (function(data){

   $scope.facilityDetails = data;

   console.log(data);



  initMap(data,params);


    //google.maps.event.addDomListener(window, 'load', initialize(data));



   });*/

};

var map;
var mapProp;


function initialize(data) {

 mapProp = {
    center: new google.maps.LatLng(-6.3690, 34.8888),
    zoom: 6,
    mapTypeId: google.maps.MapTypeId.TERRAIN
  };
  map = new google.maps.Map(document.getElementById("map"), mapProp);

  var infoWindow = new google.maps.InfoWindow({
    content: "<div>Hello! World</div>",
    maxWidth: 500
  });


 $.each(data, function(i, well) {
 if(!isUndefined(well.latitude)) {

    var wellCircle = new google.maps.Circle({
         strokeColor:    checkGreaterThanZero(well),
         strokeOpacity: 1,
         strokeWeight: 0,
         fillColor:    checkGreaterThanZero(well),
         fillOpacity: 1,
         map: map,
         center: new google.maps.LatLng(well.latitude, well.longitude),
         radius: 12000,
         preferCanvas: false,
         icon: {
                     path: google.maps.SymbolPath.CIRCLE,
                     scale: 10
                   },
                   draggable: true
       });

        google.maps.event.addListener(wellCircle, 'click', function(ev) {
                  infoWindow.setPosition(ev.latLng);
                  infoWindow.open(map);
      });


 }

});

}

//google.maps.event.addDomListener(window, 'load', initialize);

function checkGreaterThanZero(data) {
var color =(data.os > 0 )?'#00B2EE':(data.uk > 0)?'gray':(data.so > 0) ?'#ff0d00':(data.us > 0)?'orange':'#006600';
return color;
}

function checkGreaterThanZero2(data) {
var color =(data.os > 0 )?'os':(data.uk > 0)?'gray':(data.so > 0) ?'so':(data.us > 0)?'us':'green';
return color;
}


 function initMap(data,params) {
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 6,
          center: {lat:  -6.397912857937015, lng: 34.911609148190784}
 });
 mapFunc(map,data,params);
}


function createTheChart(data1, params) {


GetRegionalStockStatusSummaryData.get(params).then(function(dx){

//console.log(data);

   var stockSummary = [];
            Highcharts.each(dx, function (code, i) {


                 stockSummary.push({
                    code: code.region_name,
                    value: code.mos,
                    color: checkGreaterThanZero(code)

                });
   });

    startTheMap(stockSummary);


});


function startTheMap(summary) {

                          console.log(summary);

     var data = Highcharts.geojson(Highcharts.maps['countries/tz/tz-all']),
              separators = Highcharts.geojson(Highcharts.maps['countries/tz/tz-all'], 'mapline'),
              // Some responsiveness
              small = $('#facility-stock-map').width() < 400;




          // Set drilldown pointers
          $.each(data, function (i) {
              this.drilldown = this.properties['hc-key'];
              this.value = i; // Non-random bogus data
          });

          // Instantiate the map
          Highcharts.mapChart('facility-stock-map', {
              chart: {
              map: 'countries/tz/tz-all',
               credits: {
                      enabled:false
                      },
                  events: {
                      drilldown: function (e) {
                          if (!e.seriesOptions) {
                              var chart = this,
                                  mapKey = 'countries/tz/' + e.point.drilldown + '-all',
                                  // Handle error, the timeout is cleared on success
                                  fail = setTimeout(function () {
                                      if (!Highcharts.maps[mapKey]) {
                                          chart.showLoading('<i class="icon-frown"></i> Failed loading ' + e.point.name);
                                          fail = setTimeout(function () {
                                              chart.hideLoading();
                                          }, 1000);
                                      }
                                  }, 3000);

                              // Show the spinner
                              chart.showLoading('<i class="icon-spinner icon-spin icon-3x"></i>'); // Font Awesome spinner

                              // Load the drilldown map
                              $.getScript('https://code.highcharts.com/mapdata/' + mapKey + '.js', function () {

                                  data = Highcharts.geojson(Highcharts.maps[mapKey]);

                                  // Set a non-random bogus value
                                  $.each(data, function (i) {
                                      this.value = i;
                                  });

                                  // Hide loading and add series
                                  chart.hideLoading();
                                  clearTimeout(fail);
                                  chart.addSeriesAsDrilldown(e.point, {
                                      name: e.point.name,
                                      data: data,
                                      dataLabels: {
                                          enabled: true,
                                          format: '{point.name}'
                                      }
                                  });
                              });
                          }

                          this.setTitle(null, { text: e.point.name });
                      },
                      drillup: function () {
                          this.setTitle(null, { text: '' });
                      }
                  }
              },credits: {
                        enabled:false
                        },

              title: {
                           text: '<span style="font-size: 15px!important;color: #0c9083">Facility Stock Status</span>',
                           align:'left'

                       },
                       subtitle: {
                           text: '<span style="font-size: 13px!important;color: #0c9083">Click the chart to view more details</span>'
                       },


              legend: small ? {} : {
                  layout: 'vertical',
                  align: 'right',
                  verticalAlign: 'middle'
              },

              colorAxis: {
                  min: 0,
                  minColor: '#E6E7E8',
                  maxColor: '#005645'
              },

              mapNavigation: {
                  enabled: true,
                  buttonOptions: {
                      verticalAlign: 'bottom'
                  }
              },

              plotOptions: {
                  map: {
                      states: {
                          hover: {
                              color: '#EEDD66'
                          }
                      }
                  }
              },

              series: [{
                  data: summary,
                  joinBy: ['name', 'code'],
                  name: 'Region',
                  dataLabels: {
                      enabled: true,
                      format: '{point.properties.woe-name}'
                  },shadow: false
              }, {
                  type: 'mapline',
                  data: separators,
                  color: 'silver',
                  enableMouseTracking: false,
                  animation: {
                      duration: 500
                  }
              }],

              drilldown: {
                  activeDataLabelStyle: {
                      color: '#FFFFFF',
                      textDecoration: 'none',
                      textOutline: '1px #000000'
                  },
                  drillUpButton: {
                      relativeTo: 'spacingBox',
                      position: {
                          x: 0,
                          y: 60
                      }
                  }
              }
          });




}


// Create the chart
/*Highcharts.chart('facility-stock-summary', {
    chart: {
        type: 'pie'
    },
     credits: {
        enabled:false
        },
       title: {
              text: '<span style="font-size: 15px!important;color: #0c9083">Facility Stock Status</span>',
              align:'left'

          },
          subtitle: {
              text: '<span style="font-size: 13px!important;color: #0c9083">Click the chart to view more details</span>'
          },
     plotOptions: {
         pie: {
          innerSize: '50%',
          size: '60%',
          allowPointSelect: true,
          cursor: 'pointer',

              dataLabels: {
                                         enabled: true,
                                         format: '<b>  {point.percentage:.0f} %',

                                         style: {
                                             color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                             fontFamily: '\'Lato\', sans-serif', lineHeight: '18px', fontSize: '17px'
                                         }
                                     },
                                     showInLegend: true,

           point: {
             events: {
               click: function() {
                 if(this.series !== null){
                   $scope.openStockImbalanceReport(this);
                  }

                 }
                 }
             }

         }
},

    series: [
        {
            name: "Browsers",
            colorByPoint: true,
            data: [
                {
                    name: "Chrome",
                    y: 62.74,
                    drilldown: "Chrome"
                },
                {
                    name: "Firefox",
                    y: 10.57,
                    drilldown: "Firefox"
                },
                {
                    name: "Internet Explorer",
                    y: 7.23,
                    drilldown: "Internet Explorer"
                },
                {
                    name: "Safari",
                    y: 5.58,
                    drilldown: "Safari"
                },
                {
                    name: "Edge",
                    y: 4.02,
                    drilldown: "Edge"
                },
                {
                    name: "Opera",
                    y: 1.92,
                    drilldown: "Opera"
                },
                {
                    name: "Other",
                    y: 7.62,
                    drilldown: null
                }
            ]
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
            },
            {
                name: "Firefox",
                id: "Firefox",
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
            },
            {
                name: "Internet Explorer",
                id: "Internet Explorer",
                data: [
                    [
                        "v11.0",
                        6.2
                    ],
                    [
                        "v10.0",
                        0.29
                    ],
                    [
                        "v9.0",
                        0.27
                    ],
                    [
                        "v8.0",
                        0.47
                    ]
                ]
            },
            {
                name: "Safari",
                id: "Safari",
                data: [
                    [
                        "v11.0",
                        3.39
                    ],
                    [
                        "v10.1",
                        0.96
                    ],
                    [
                        "v10.0",
                        0.36
                    ],
                    [
                        "v9.1",
                        0.54
                    ],
                    [
                        "v9.0",
                        0.13
                    ],
                    [
                        "v5.1",
                        0.2
                    ]
                ]
            },
            {
                name: "Edge",
                id: "Edge",
                data: [
                    [
                        "v16",
                        2.6
                    ],
                    [
                        "v15",
                        0.92
                    ],
                    [
                        "v14",
                        0.4
                    ],
                    [
                        "v13",
                        0.1
                    ]
                ]
            },
            {
                name: "Opera",
                id: "Opera",
                data: [
                    [
                        "v50.0",
                        0.96
                    ],
                    [
                        "v49.0",
                        0.82
                    ],
                    [
                        "v12.1",
                        0.14
                    ]
                ]
            }
        ]
    },

     tooltip: {
            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
        }, exporting: {
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
                   }
});*/





/*Highcharts.chart('stock-summary-by-period-and-program', {
    chart: {
        type: 'pie'
    },
    credits: {
    enabled:false
    },
   title: {
          text: '<span style="font-size: 15px!important;color: #0c9083">'+title+'</span>',
          align:'left'

      },
      subtitle: {
          text: '<span style="font-size: 13px!important;color: #0c9083">'+subtitle+'</span>'
      },
    plotOptions: {
       pie: {
        size: '60%',
        allowPointSelect: true,
        cursor: 'pointer',

            dataLabels: {
                                       enabled: true,
                                       format: '<b>  {point.percentage:.0f} %',

                                       *//*
                                                               format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                       *//*
                                       style: {
                                           color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                           fontFamily: '\'Lato\', sans-serif', lineHeight: '18px', fontSize: '17px'
                                       }
                                   },
                                   showInLegend: true,

         point: {
           events: {
             click: function() {
               if(this.series !== null){
                 $scope.openStockImbalanceReport(this);
                }

               }
               }
           }

       },

        series: {
            dataLabels: {
                enabled: true,
                 useHTML: true,
                    formatter: function () {
                     return '<span style="color:' + this.point.color + '">' + this.point.name + ' : '+ this.point.y+'%</span>';
                    }



            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
    }, exporting: {
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
            name: "Summary",
            colorByPoint: false,
            data: dataToShow
        }
    ],
    drilldown: {
        series: [
            {
                name: "Available Incidence",
                id: "Available Incidence",
                type: 'pie',
                 colors: [
                     '#00B2EE',
                     '#FFA500',
                     '#808080',
                     '#006600'

                   ],

                innerSize: '50%',
                colorByPoint: true,
                data: drilldownData
            }

        ],


            plotOptions: {

             pie:{
                 size: '100%',
              events: {
                       click: function() {

                       //$rootScope.openDefinitionModal();

                        console.log(this);

                        }
                      },

                            shadow: true,
                                   cursor: 'pointer'

             }
            }
    }
});*/




}


function mapFunc(map,data,params){

var iconBase = '/public/images/';

var icons = {
           gray: {
           icon: iconBase + 'mm_20_black.png'
           },
           green: {

          icon: iconBase + 'green-dot.png'

           },
           os:{
                               icon: iconBase + 'blue-dot.png'
           },

          so:{
                     icon: iconBase + 'red-dot.png'
           },

          us: {
            icon: iconBase + 'orange-dot.png'
          }
   };

 var featuresData = [];
 $.each(data, function(i, info) {
 if(!isUndefined(info.latitude)) {
   featuresData.push({
   position :new google.maps.LatLng(info.latitude, info.longitude),
   type:checkGreaterThanZero2(info),
   data:info

   });
 }

 });

 var infoWindow = new google.maps.InfoWindow({
     maxWidth: 500

   });

for (var i = 0; i < featuresData.length; i++) {

 var marker = new google.maps.Marker({
 position: featuresData[i].position,
 icon: icons[featuresData[i].type].icon,
 map: map,
 title: featuresData[i].facility,
 data:featuresData[i]
 });

makeMarker(marker, i);

}


function makeMarker(marker, i) {

  google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                console.log(marker.data.data);
                infoWindow.setContent(popupFormat(marker.data.data));
                infoWindow.open(map, marker);
            };
        })(marker, i));

}

 function popupFormat(feature) {
      var totalCost = (feature.currentprice) * (feature.ordered - feature.required);

         return '<table class="table table-bordered" style="width: 310px;"><tr><td><b>Facility</b></td><td>' + feature.facility + '</td><td><b>Phone #</b></td><td>' + feature.mainphone + '</td></tr>' +
         '<tr><td><b>Product Code</b></td><td>' + feature.productcode + '</td><td><b>Product </b></td><td>' + feature.product + '</td></tr>' +
         '<tr><td><b>SOH</b></td><td>' + feature.soh + '</td><td><b>MOS </b></td><td>' + feature.mos + '</td></tr>' +
          '<tr><td><b>Price</b></td><td>' + feature.currentprice + '</td><td><b>Required </b></td><td>' + feature.required + '</td><td><b>Total Cost </b></td><td>' + totalCost + '</td></tr>' +

         '</table>';
         /*    '<tr><td><b>Region</b></td><td>' + feature.georegion + '</td><td><b>Expected Facilities</b></td><td>' + feature.expected + '</td></tr>' +
             '<tr><td><b>Zone</b></td><td>' + feature.geozone + '</td><td><b>Reported This Period</b></td><td>' + feature.period + '</td></tr></table>' +
             '<table class="table table-bordered" style="width: 310px;"><tr><th class="bold">Indicator</th><th class="bold">This Period</th><th class="bold">Previous Period</th></tr>' +
             '<tr bgcolor="#dd514c"><td class="bold">Stocked Out</td><td class="number">' + feature.stockedout + '</td><td class="number">' + feature.stockedoutprev + '</td></tr>' +
             '<tr bgcolor="#faa732"><td class="bold">Under Stocked</td><td class="number">&nbsp;&nbsp;' + feature.understocked + '</td><td class="number">' +feature.understockedprev +'</td></tr>' +
             '<tr bgcolor="#4bb1cf"><td>Over Stocked</td><td class="number">&nbsp;&nbsp;' + feature.overstocked + '</td><td class="number">' +feature.overstockedprev +'</td></tr>' +
             '<tr bgcolor="#5eb95e"><td>Adequately Stocked</td><td class="number">&nbsp;&nbsp;' + feature.adequatelystocked + '</td><td class="number">' +feature.adequatelystockedprev +'</td></tr>';
*/
     }



}
}