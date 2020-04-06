function EmergencyCommoditiesController($scope, $http, $location, $rootScope, messageService, StockAvailableByProgramAndPeriodData) {


    $rootScope.loadEmergencyCommoditiesDashlets = function(params) {
        $scope.table(params);
        $scope.stockAvailabilityByDesignatedHospital('stockAvailabilityByDesignatedHospital');
        $scope.stockAvailabilityForEmergencyCommodities('stockAvailabilityForEmergencyCommodities');
        $scope.casesPerDesignatedFacilities('patientPerRegion');
        $scope.cumulativePatientTrend('cumulativePatientTrend');
    };

       $scope.table = function(params){
                  program = 1;
                  params.programName = 'ILS';
                  params.program = 1;
                  params.schedule = 2;
                  params.year = 2017;
                  params.period = 69;
                 var allParams = angular.extend(params, {program:program});
                 StockAvailableByProgramAndPeriodData.get(params).then(function(data){

                  $scope.titleStockForProgramAvailable = '<span style="font-size: 13px!important;color: #0c9083">List of Available Tracer Items for '+name +' in '+params.periodName+', '+params.year+'</span>';

                  $scope.drillDownData = data;
                 });
       };

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

       $scope.cumulativePatientTrend = function(chartTypeId) {
      Highcharts.chart(chartTypeId, {

          title: {
              text: 'Trend of Cumulative Reported Cases'
          },
         credits: {
              enabled: false
         },
          subtitle: {
              text: 'March 2020'
          },

          yAxis: {
              title: {
                  text: 'Cases'
              }
          },

          xAxis: {
              accessibility: {
                  rangeDescription: 'Timeline'
              }
          },

          legend: {
              layout: 'vertical',
              align: 'right',
              verticalAlign: 'middle'
          },

          plotOptions: {
              series: {
                  label: {
                      connectorAllowed: false
                  },
                  pointStart: 2010
              }
          },

          series: [{
              name: 'COVID-19 Cases',
              data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
          }]

      });
       };
       $scope.stockAvailabilityByDesignatedHospital = function(chartTypeId) {
              Highcharts.chart(chartTypeId, {
                  chart: {
                      type: 'bar'
                  },
                  title: {
                      text: 'Stock Availability By Designated Hospitals'
                  },
                  subtitle: {
                      text: 'Source: <a href="https://en.wikipedia.org/wiki/World_population">Wikipedia.org</a>'
                  },
                  xAxis: {
                      categories: ['Mt. Meru ', 'Mloganzila', 'Tumbi RRH (Kibaha)', 'Kagera RRH', 'Temeke Hospital', 'Mloganzila', 'Tumbi RRH (Kibaha)', 'Kagera RRH', 'Temeke Hospital'],
                      title: {
                          text: null
                      }
                  },
                  yAxis: {
                      min: 0,
                      title: {
                          text: 'Population (millions)',
                          align: 'high'
                      },
                      labels: {
                          overflow: 'justify'
                      }
                  },
                  tooltip: {
                      valueSuffix: ' millions'
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
                      name: 'Year 1800',
                      data: [107, 31, 635, 203, 2, 31, 635, 203, 2]
                  }]
              });
              };
       $scope.stockAvailabilityForEmergencyCommodities = function(chartTypeId) {
  Highcharts.chart(chartTypeId, {
       chart: {
                      plotBackgroundColor: null,
                      plotBorderWidth: null,
                      plotShadow: false,
                      type: 'pie'
      },
      title: {
          text: 'Overall Availability'
      },
      tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      credits: {
                      enabled: false
                  },
      accessibility: {
          point: {
              valueSuffix: '%'
          }
      },
      plotOptions: {
          pie: {
              allowPointSelect: true,
              cursor: 'pointer',
              dataLabels: {
                  enabled: true,
                  format: '<b>{point.name}</b>: {point.percentage:.1f} %'
              }
          }
      },
      series: [{
          name: 'Brands',
          colorByPoint: true,
          innerSize: '40%',
          data: [{
              name: 'Available',
              y: 8,
              sliced: true,
              selected: true
          }, {
              name: 'StockOut',
              y: 2
          }]
      }]
  });
};
       $scope.casesPerDesignatedFacilities = function(chartTypeId) {
  Highcharts.chart(chartTypeId, {
       chart: {
                      plotBackgroundColor: null,
                      plotBorderWidth: null,
                      plotShadow: false,
                      type: 'pie'
      },
      title: {
          text: 'Cases Per Designated Facilities'
      },
      tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      credits: {
                      enabled: false
                  },
      accessibility: {
          point: {
              valueSuffix: '%'
          }
      },
      plotOptions: {
          pie: {
              allowPointSelect: true,
              cursor: 'pointer',
              dataLabels: {
                  enabled: true,
                  format: '<b>{point.name}</b>: {point.percentage:.1f} %'
              }
          }
      },
      series: [{
          name: 'Brands',
          colorByPoint: true,
          innerSize: '40%',
          data: [{
              name: 'Mt Meru',
              y: 8,
              sliced: true,
              selected: true
          }, {
              name: 'Mloganzila',
              y: 12
          },
           {
                name: 'Tumbi',
                 y: 32
           },
            {
                 name: 'Kagera RRH',
                 y: 22
            }

          ]
      }]
  });
 };





}