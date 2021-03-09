function StockAvailabilityControllerFunc1(defaultYear,defaultProduct,$scope, $timeout, GetCategorizationByFacilityData, GetCoverageByRegionSummary, GetPerformanceMonitoringData,
                                          GetDistributionOfDistrictPerPerformanceData, GetFacilityClassificationDrillDownData, GetDistrictClassificationSummaryData, GetCategorizationByDistrictDrillDownData, GetCategorizationByDistrictData,
                                          GetCoverageByDistrictData, GetInventoryByMaterialFacilityListData, VaccineDashboardFacilityInventoryStockStatus, GetCoverageMapInfo, GetInventorySummaryByLocationData, GetInventorySummaryByMaterialData,
                                          StockCardsByCategory, GetDistrictInventorySummaryData, GetRegionInventorySummaryData, homeFacility, FacilityInventoryStockStatusData, GetPeriodForDashboard, YearFilteredData, ProductFilteredData,
                                          $routeParams, leafletData, ProductService, $state, VaccineProductDoseList, ReportPeriodsByYear, VimsVaccineSupervisedIvdPrograms, AvailableStockDashboard,
                                          FullStockAvailableForDashboard, GetAggregateFacilityPerformanceData,
                                          VaccineCoverageByProductData, GetCoverageByProductAndDoseData, GetCoverageByFacilityData, GetIVDReportingSummaryData,GetFacilityClassificationSummaryData,GetImmunizationSessionSummaryData,$q,
                                          GetClassificationByDistrictSummaryData,GetClassificationByDistrictDrillDownData, GetFacilityStockStatusSummaryData,GetFacilityStockStatusSummaryDataByPeriod,GetVaccineDistrictCoverageForMapData ) {


    $scope.region = true;
    $scope.showDistrict = function (d) {
        if (d === 'district') {
            $scope.region = false;
            $scope.district = true;
        } else {
            $scope.region = true;
            $scope.district = false;
        }

    };
    $('ul.tabs').tabs({
        swipeable: true,
        responsiveThreshold: 1920
    });
    $scope.showModal = function (data) {
        var colors = {'#ffdb00': 'yellow', '#ff0d00': 'red', '#00B2EE': 'blue', '#006600': 'green'};

        $scope.modal12 = true;
        $scope.modal = true;

        $scope.productName = data.category;
        $scope.level = data.level;

        var params = {level: data.level, color: colors[data.color], product: data.category};
        GetInventoryByMaterialFacilityListData.get(params).then(function (data) {
            $scope.facilityList = data;
        });
        $('#modal12').modal();

    };
    $scope.closeStockModal = function () {

        $scope.modal12 = false;
        $('#modal12').modal().modal('close');
    };


    $scope.homeFacility = homeFacility;
    $scope.homePageDate = new Date();

    var dataV = [];
    var chartIds = ['myStockVaccine', 'myStockSyringe'];
    var title = ['Vaccines', 'Syringes'];
    var name = ['Vaccines', 'Syringes'];

 /*   var compareArray = function(arra1, array2){
      var newArray = [];
      _.each(function(){

      });

    }*/

    $scope.getFacilityStockStatusSummary = function (params,level) {

     var stockSummary = [];

      GetFacilityStockStatusSummaryData.get(params).then( function (data) {

       if(!isUndefined(data)){

        stockSummary = data;

               var period = _.uniq(_.pluck(stockSummary, 'period_name'));

               var so = _.pluck(stockSummary, 'so');
               var os = _.pluck(stockSummary, 'os');
               var sap = _.pluck(stockSummary, 'sap');
               var us = _.pluck(stockSummary, 'us');
               var total = _.pluck(stockSummary, 'total');

               var summaries = [];

               var totalZeroStock = [];
               var totalLowStock = [];
               var totalOverStock = [];
               var totalSufficientStock = [];

               _.map(total,function(data, index){

               totalZeroStock.push({y:so[index],total:data});
               totalLowStock.push({y:us[index],total:data});
               totalOverStock.push({y:os[index],total:data});
               totalSufficientStock.push({y:sap[index],total:data});
                 return null;

               });

               summaries = [
                            {name:'zero stock', data:totalZeroStock, color:'#ff0d00'},
                            {name:'low stock', data:totalLowStock, color:'#ffdb00'},
                            {name:'overStock', data:totalOverStock, color:'#00B2EE'},
                            {name:'sufficient stock', data:totalSufficientStock, color:'#006600'}

                           ];
                var chartId = (level === 'cvs')?'facilityStockStatusChart':'facilityStockStatusChartForLowerLevel';

                showFacilityStockStatusChart(period, summaries,'Facility Stock Status for '+params.productName +', '+ params.periodName,params,chartId);
       }


      });

    };

    function asyncFacilityStocks(params) {

            var deferred = $q.defer();

            $timeout(function(data){

                  GetFacilityStockStatusSummaryDataByPeriod.get(params).then (
                   function(data){
                     var dataV = [];
                     if(!isUndefined(data) && data.length > 0) {
                         dataV = data;
                     return deferred.resolve(dataV);

                     }
                     }
                   );



            },100);

        return deferred.promise;
    }



     $scope.showFacilityStockList = function(indicator,params) {

     var facilityList = $scope.facilityLists = [];

       facilityList = asyncFacilityStocks(params);

        facilityList.then(function(data) {

                 if(indicator.color === '#ff0d00' && data.length > 0) {

                   facilityList = _.where(data,{'soh':0});

                   $scope.facilityLists = facilityList;

                   $scope.facilityColor ={'background-color':indicator.color};

                    $timeout(function () {
                       $('#facilityStockModal').modal();

                    },1000);


                 } else if(indicator.color === '#ffdb00' && data.length > 0) {

                    facilityList = _.filter(data,function(data){
                        return data.mos < 1;
                    });

                    $scope.facilityLists = facilityList;

                    $scope.facilityColor ={'background-color':indicator.color};

                     $timeout(function () {
                        $('#facilityStockModal').modal();

                     },1000);


                 } else if(indicator.color === '#00B2EE' && data.length > 0) {

                     facilityList = _.filter(data,function(data) {
                        return data.mos >= 1.5;

                     });

                     $scope.facilityLists = facilityList;

                     $scope.facilityColor ={'background-color':indicator.color};

                      $timeout(function () {
                         $('#facilityStockModal').modal();

                      },1000);


                 } else {

                    facilityList = _.filter(data,function(data) {
                        return (data.mos <1.5 && data.mos >1);

                     });

                     $scope.facilityLists = facilityList;

                     $scope.facilityColor ={'background-color':indicator.color};

                     $scope.stockStatusTitle = 'Facilities with Stock status ';


                     $timeout(function () {

                         $('#facilityStockModal').modal();

                     },1000);

                 }

         });




     };



    function showFacilityStockStatusChart (category, dataV, title, params,chartId){

    new Highcharts.chart(chartId, {
        chart: {
            type: 'column'
        },
        title: {
            text: title
        },

            xAxis: {
                categories:category,
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Number of Facilities'
                },
                  lineColor: '#999',
                                lineWidth: 1,
                                tickColor: '#666',
                                tickWidth: 1,
                                tickLength: 3,
                                gridLineColor: ''
            },
            tooltip: {

               formatter: function () {

                                var tooltip;
                                tooltip = '<span style="color:' + this.series.color + '">' + this.point.category + '<hr/><br/> <span>Percentage of Facilities </span> :' + Highcharts.numberFormat(this.y/this.total * 100,0) + ' % </span><br/>';
                                return tooltip;
                            }

            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0,

                      cursor: 'pointer',
                        point: {
                         events: {
                           click: function () {
                           $scope.showFacilityStockList(this,params);
                           console.log(this);
                                 }
                                  }

                                  }
                },

                series: {
                            dataLabels: {
                                enabled: true,
                                formatter:function() {
                                               console.log(this);

                                    return this.point.options.y;
                                }
                            }
                        }
            },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 50,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },

        series:dataV,

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



    }




    function populateTheChart(vaccineDataT, product, chartName, title, name) {
        dataV = [];
        vaccineDataT.forEach(function (data) {
            dataV.push({y: Math.abs(data.mos), color: data.color, soh: data.soh, uom: data.unity_of_measure});
        });
        new Highcharts.chart(chartName, {
            chart: {
                type: 'column'
            },
            credits: {
                enabled: false
            },

            legend: {
                align: 'right',
                shadow: false
            },
            title: {
                text: ''
            },
            subtitle: {
                text: title
            },
            xAxis: {
                categories: product,
                crosshair: false
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Months of Stock'
                },
                lineColor: '#999',
                lineWidth: 1,
                tickColor: '#666',
                tickWidth: 1,
                tickLength: 3,
                gridLineColor: ''
            },
            tooltip: {

                formatter: function () {
                    var tooltip;
                    tooltip = '<span style="color:' + this.series.color + '">' + this.point.category + '<hr/><br/> <span>MOS </span>' + this.y + '</span>: <b>' + this.point.soh + ' ' + this.point.uom + ' </b><br/>';

                    return tooltip;
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0,
                    dataLabels: {
                    enabled:true
                    }

                }, showLegend: false
            },

            series: [{
                name: name,
                data: dataV

            }]
        });


    }

    //Lower Level Charts
    function getVaccineStockStatusChartForLowerLevel(dataV, charts) {
        var vaccineDataT = [];
        var productT = [];
        for (var i = 0; i <= 1; i++) {
            vaccineDataT = dataV[i].dataPoints;
            productT = _.pluck(vaccineDataT, 'product');
            populateTheChart(vaccineDataT, productT, charts[i], title[i], name[i]);

        }


        /*    var vaccineData = data[0].dataPoints;
            var product = _.pluck(data[0].dataPoints, 'product');

            vaccineData.forEach(function (data) {
                dataV.push({y:data.mos,color:data.color,soh:data.soh});
            });*/

    }

    function inventorySummaryChart(chartId, title, subTitle, data) {
        Highcharts.chart(chartId, {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'

            },
            credits: {
                enabled: false
            },
            title: {
                text: title
            },
            subtitle: {
                text: subTitle
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
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
            series: data
        });


    }

    function getInventorySummarySortedData(data, color) {

        var indicatorData = _.filter(data, function (data) {

            var indicator = (color === 'red') ? data.red : (color === 'blue') ? data.blue : (color === 'yellow') ? data.yellow : data.green;
            return indicator > 0;
        });

        return _.sortBy(indicatorData, function (data) {
            return -parseInt((color === 'red') ? data.red : (color === 'blue') ? data.blue : (color === 'yellow') ? data.yellow : data.green, 10);
        });
    }

    function getByInventoryByLocation(values) {

        var colors = {'#ffdb00': 'yellow', '#ff0d00': 'red', '#00B2EE': 'blue', '#006600': 'green'};
        var customizeStatus = {
            'overstock': 'Over Stocked',
            'zero stock': 'Stocked Out',
            'sufficient stock': 'Adequately Stocked',
            'low stock': 'Under Stocked'
        };

        GetInventorySummaryByLocationData.get({
            level: values.level,
            status: colors[values.color]
        }).then(function (data) {
            var sortedData = getInventorySummarySortedData(data, (colors[values.color]));

            var byLocationPercentage = _.pluck(sortedData, (colors[values.color]));
            var byLocationFacility = _.pluck(sortedData, 'name');

            var totalProduct = _.pluck(sortedData, 'totalproduct');

            var percentageItems = [];
            var i = -1;
            angular.forEach(byLocationPercentage, function (data) {
                percentageItems.push({color: values.color, y: data, total: totalProduct[++i]});

            });
            var type = 'bar';
            var chartTitle = 'Stock Status By Location';
            var verticalTitle = 'Products';
            var chartNameId = 'byLocation';
            var name = 'By Location';
            var subTitle = '(Shows ' + (values.level).toUpperCase() + ' ' + customizeStatus[values.name] + ' with one or more Antigens)';
            var toolTip = 'Antigens';
            $scope.byLocation = true;
            loadDynamicChart2(chartNameId, type, chartTitle, subTitle, verticalTitle, toolTip, name, byLocationFacility, percentageItems);

        });

        GetInventorySummaryByMaterialData.get({
            level: values.level,
            status: colors[values.color]
        }).then(function (data) {
            var sortedData = getInventorySummarySortedData(data, (colors[values.color]));

            var byProductPercentage = _.pluck(sortedData, (colors[values.color]));
            var byProductFacility = _.pluck(sortedData, 'primaryname');

            var totalProduct = _.pluck(sortedData, 'totalproduct');

            var percentageItems = [];
            var i = -1;
            angular.forEach(byProductPercentage, function (data) {

                percentageItems.push({color: values.color, y: data, total: totalProduct[++i], level: values.level});

            });
            var type = 'bar';
            var chartTitle = 'Stock Status By Antigen';
            var verticalTitle = 'Stores';
            var chartNameId = 'byProduct';
            var name = 'By Antigen';
            var subTitle = '(Shows Antigens  ' + customizeStatus[values.name] + ' across all ' + (values.level).toUpperCase() + ')';
            var toolTip = 'Stores';
            $scope.byLocation = true;
            loadDynamicChart2(chartNameId, type, chartTitle, subTitle, verticalTitle, toolTip, name, byProductFacility, percentageItems);

        });


    }

    function summaryChartValues(blue, green, yellow, red, level) {
        return [{
            name: 'products', colorByPoint: true,
            data: [{


                name: 'overstock',
                y: blue, color: '#00B2EE', level: level
            }, {
                name: 'sufficient stock',
                y: green, color: '#006600', level: level

            }, {
                name: 'low stock',
                y: yellow, color: '#ffdb00', level: level
            }, {
                name: 'zero stock',
                y: red, color: '#ff0d00', level: level,
                sliced: true,
                selected: true
            }],
            point: {
                events: {
                    click: function (event) {
                        getByInventoryByLocation(this);
                    }
                }
            }
        }];

    }

    $scope.getVaccineInventorySummary = function () {

        GetDistrictInventorySummaryData.get().then(function (data) {

            if (!isUndefined(data)) {
                $scope.showDistrictSummary = true;
                var chartTitle = 'District Vaccine Stock Summary';
                var subTitle = '(Shows availability of all antigens across all DVS in the country)';
                var values = summaryChartValues(_.pluck(data, 'blue_total')[0], _.pluck(data, 'green_total')[0], _.pluck(data, 'yellow_total')[0], _.pluck(data, 'red_total')[0], 'dvs');
                inventorySummaryChart('districtSummary', chartTitle, subTitle, values);
            }

        });

        GetRegionInventorySummaryData.get().then(function (data) {

            if (!isUndefined(data)) {
                $scope.showDistrictSummary = true;
                var chartTitle = 'Regional Vaccine Stock Summary';
                var subTitle = '(Shows availability of all antigens across all RVS in the country)';
                var values = summaryChartValues(_.pluck(data, 'blue_total')[0], _.pluck(data, 'green_total')[0], _.pluck(data, 'yellow_total')[0], _.pluck(data, 'red_total')[0], 'rvs');
                inventorySummaryChart('regionSummary', chartTitle, subTitle, values);
            }

        });


    };


    $scope.coverageSummaryModel = false;

    $scope.getEventFunc = function (event, dataTobeFiltered) {

        $scope.showDistrictInfo = false;
        $scope.coverageSummaryDataToDisplay = [];
        (event.series.userOptions.chartId === 'regionCoverageChart') ? $scope.showRegionInfo = true : $scope.showRegionInfo = false;
        (event.series.userOptions.chartId === 'districtCoverageChart' || event.series.userOptions.chartId === 'districtCoverageChart1' ) ? $scope.showDistrictInfo = true : $scope.showDistrictInfo = false;
        (event.series.userOptions.chartId === 'facilityCoverageChart') ? $scope.showFacilityInfo = true : $scope.showFacilityInfo = false;

        $scope.covColor = event.color;
        $scope.coverageSummaryDataToDisplay = _.where(dataTobeFiltered, {coverageclassification: event.opt});

        $scope.statusCategory = event.name;
        var headerName =($scope.showRegionInfo)?'Region Name':($scope.showDistrictInfo)?'District Name':'Facility Name';
        var headers = [headerName,'Coverage %'];
        $scope.exportDataFunc(event.series.userOptions.chartId,$scope.coverageSummaryDataToDisplay,headers);

        $timeout(function () {
            $('#exampleModal').modal();
        }, 100);

    };


    $scope.exportDataFunc = function(fileName,data,headers){
        // Prepare Excel data:
        $scope.fileName = fileName;
        $scope.exportData = [];
        // Headers:
        $scope.exportData.push(headers);
        // Data:
        angular.forEach(data, function(value, key) {



            if(fileName === 'facilityCoverageChart'){
            $scope.exportData.push([value.facility_name, value.coveragepercentage]);
            }

            if(fileName === 'districtCoverageChart1'){
            $scope.exportData.push([value.district, value.value]);

            }
            if(fileName === 'regionCoverageChart'){
            $scope.exportData.push([value.region, value.value]);


            }

            if(fileName === 'facilitySummaryChart') {
            $scope.exportData.push([value.region, value.coverage]);

            }

        });

                                                                            console.log($scope);

    };


    function loadDynamicPieChart(data, title,title2, chartId, legend, periodName, productName, dose, dataTobeFiltered,name,locationName) {

        var char2 = new Highcharts.Chart({

            chart: {
                renderTo: chartId,
                type: 'pie'


            },

            credits: {enabled: false},

            plotOptions: {
                pie: {
                     innerSize: '60%',
                    size: '80%',
                    showInLegend: true,
                    allowPointSelect: true,
                    cursor: 'pointer',

                    dataLabels: {
                        enabled: true,
                        format: '<span style="text-decoration: underline !important;" class="activator">{point.y}  ' + legend + ' </span>'

                    }
                }
            },
            title: {
                text:title2
            },
            subtitle: {
                text: '<span style="font-size: 10px!important;color: #0c9083">( ' + productName + ',' + periodName + ' ) </span>'
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}  </b>'+ locationName+'<br/>'


            },

            series: [{
                colorByPoint: true,
                name: name,
                data: data,
                chartId: chartId,
                point: {
                    events: {
                        click: function (event) {

                            $scope.getEventFunc(this, dataTobeFiltered);

                        }
                    }
                }

            }]


        });

    }

    $scope.loadDistrictCoverageFunc = function (para, level) {


        if (level ==='dvs') {

            GetCoverageByFacilityData.get(para).then(function (data) {

                var badValue = _.where(data, {coverageclassification: 'BAD'});
                var goodValue = _.where(data, {coverageclassification: 'GOOD'});
                var normalValue = _.where(data, {coverageclassification: 'NORMAL'});
                var summary1 = (badValue.length > 0) ? badValue.length : 0;
                var summary3 = (goodValue.length > 0) ? goodValue.length : 0;
                var summary2 = (normalValue.length > 0) ? normalValue.length : 0;

                var title ='<span style="font-size: 17px!important;color: #0c9083">Coverage by Facilities </span>';
                var  chartId = 'facilityCoverageChart';
                var legend = 'Facilities';
                var name='Facility Coverage';
                var values = [];
                values.push({name: 'Below 80%', opt: 'BAD', y: summary1, color: 'red', sliced: true},
                    {
                        name: '80% to 89%', opt: 'NORMAL',
                        y: summary2,
                        color: 'yellow'
                    },
                    {name: '90% +', opt: 'GOOD', y: summary3, color: 'green'}
                );
                loadDynamicPieChart(values, title,title, chartId, legend, para.periodName, para.productName, para.doseId, data,name,'District');


            });

        } else {

        $scope.getRegionCoverageDataFunction = function(data){
        var filteredData = _.filter(data, function(dx){
        return !isUndefined(dx.coverageclassification);
                });

        var badValue = _.where(filteredData, {coverageclassification: 'BAD'});

        var goodValue = _.where(filteredData, {coverageclassification: 'GOOD'});
        var normalValue = _.where(filteredData, {coverageclassification: 'NORMAL'});
        var summary1 = (badValue.length > 0) ? badValue.length : 0;
        var summary3 = (goodValue.length > 0) ? goodValue.length : 0;
        var summary2 = (normalValue.length > 0) ? normalValue.length : 0;


                     var title = 'Region Coverage Summary',name='Regional Coverage',title2=null, chartId = 'regionCoverageChart', legend = 'Region(s)';
                                 var values = [];
                                 values.push({name: 'Below 80%', opt: 'BAD', y: summary1, color: 'red', sliced: true},
                                     {
                                         name: '80% to 89%', opt: 'NORMAL',
                                         y: summary2,
                                         color: 'yellow'
                                     },
                                     {name: '90% +', opt: 'GOOD', y: summary3, color: 'green'}
                                 );
                                 loadDynamicPieChart(values, title,title2, chartId, legend, para.periodName, para.productName, para.doseId, filteredData,name,'Regions');



        };


        /*    GetCoverageByRegionSummary.get(para).then(function (data) {

                var badValue = _.where(data, {coverageclassification: 'BAD'});
                var goodValue = _.where(data, {coverageclassification: 'GOOD'});
                var normalValue = _.where(data, {coverageclassification: 'NORMAL'});
                var summary1 = (badValue.length > 0) ? badValue.length : 0;
                var summary3 = (goodValue.length > 0) ? goodValue.length : 0;
                var summary2 = (normalValue.length > 0) ? normalValue.length : 0;

                var title = 'Region Coverage Summary',name='Regional Coverage',title2=null, chartId = 'regionCoverageChart', legend = 'Region(s)';
                var values = [];
                values.push({name: 'Below 80%', opt: 'BAD', y: summary1, color: 'red', sliced: true},
                    {
                        name: '80% to 89%', opt: 'NORMAL',
                        y: summary2,
                        color: 'yellow'
                    },
                    {name: '90% +', opt: 'GOOD', y: summary3, color: 'green'}
                );
                loadDynamicPieChart(values, title,title2, chartId, legend, para.periodName, para.productName, para.doseId, data,name);


            });*/

            $scope.regionLevel = level;

            GetVaccineDistrictCoverageForMapData.get(para).then(function (data) {
                             console.log(data);


                var badValue = _.where(data, {coverageclassification: 'BAD'});
                var goodValue = _.where(data, {coverageclassification: 'GOOD'});
                var normalValue = _.where(data, {coverageclassification: 'NORMAL'});
                var summary1 = (badValue.length > 0) ? badValue.length : 0;
                var summary3 = (goodValue.length > 0) ? goodValue.length : 0;
                var summary2 = (normalValue.length > 0) ? normalValue.length : 0;

                var summary ='<span style="font-size: 15px!important;color: #0c9083">District Coverage Summary </span>';
                var title = 'District Coverage Summary',title2=(level==='rvs')?summary:null,name='District Coverage', chartId = (level==='rvs')?'districtCoverageChart1':'districtCoverageChart', legend = 'District(s)';
                var values = [];
                values.push({name: 'Below 80%', y: summary1, color: 'red', opt: 'BAD', sliced: true}, {
                        name: '80% to 89%', opt: 'NORMAL',
                        y: summary2,
                        color: 'yellow'
                    },
                    {name: '90% +', y: summary3, opt: 'GOOD', color: 'green'});
                loadDynamicPieChart(values, title,title2, chartId, legend, para.periodName, para.productName, para.dose, data,name,'Districts');
            });

        }
    };
    $scope.successModal = false;

    function showCategorizationPopup(events, data,year,level) {
         //   console.log(data.point);
        var parameters = {period: data.point.category,year: parseInt(year, 10), indicator: events.name};



        GetCategorizationByDistrictDrillDownData.get(parameters).then(function (data) {



            if (!isUndefined(data)) {
                $scope.classificationData = data;

            }

        });
        $timeout(function () {
            $('#exampleModalCenter').modal();
        },1000);

        $scope.successModal = true;

        // $('#categorizationModal').modal().modal('open');

    }

    function showClassificationPopup(events, event, product, year,level) {

        var parameters = {
            period: event.point.category,
            indicator: events.name,
            product: parseInt(product, 10),
            year: parseInt(year, 10)
        };
        $scope.classificationByDistrict = [];

        var objectData =(level!=='dvs')?GetClassificationByDistrictDrillDownData: GetFacilityClassificationDrillDownData;

        $scope.showFacilityInfo = ('dvs' === level);

        objectData.get(parameters).then(function (data) {

            $scope.classificationByDistrict = data;

            var districtLevelTitle = 'List Of Facilities with Classification'+events.name+' ('+event.point.category +') ';
            var upperLevel    = 'List of Districts with Classification '+events.name+' ('+event.point.category +') ';
            $scope.classificationTitle =(level==='dvs')?districtLevelTitle:upperLevel;

            $scope.classificationColor ={'background-color':event.point.color};

            $timeout(function () {
                $('#classificationModal').modal();

            },1000);


        });


    }

    function getDynamicStackedChart(data, chartId, title, Category, chartCategory, product, year, horizontalTitle,level) {

        Highcharts.chart(chartId, {
            chart: {
                type: 'column'
            },
            credits: {enabled: false},
            title: {
                text: title
            },
            xAxis: {
                categories: Category
            },
            yAxis: {
                min: 0,
                title: {
                    text: horizontalTitle
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                },
                gridLineColor: ''
            },

            legend: {

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
                    },
                    events: {
                        click: function (event) {
                            if (chartCategory === 'classification')
                                showClassificationPopup(this, event, product, year,level);
                            else
                                showCategorizationPopup(this, event,year,level);

                        }
                    },
                    cursor: 'pointer'

                }
            },
            series: data
        });


    }

    $scope.districtCategorizationFunct = function (params, userLevel, level) {

        var colors = {'Cat_3': '#ffdb00', 'Cat_4': '#ff0d00', 'Cat_2': '#ABC9AA', 'Cat_1': '#006600'};
        var dataFunction = (userLevel === 'dvs') ? GetCategorizationByFacilityData : GetCategorizationByDistrictData;

        dataFunction.get(params).then(function (data) {

            var category = _.uniq(_.pluck(data, 'period_name'));
            var cat_1 = _.pluck(data, 'cat_1');
            var cat1_data=  {name:'cat_1', data:cat_1, color:colors.Cat_1};
            var cat_2 = _.pluck(data, 'cat_2');
           var cat2_data=  {name:'cat_2', data:cat_2, color:colors.Cat_2};
            var cat_3 = _.pluck(data, 'cat_3');
            var cat3_data=  {name:'cat_3', data:cat_3, color:colors.Cat_3};
            var cat_4 = _.pluck(data, 'cat_4');
           var cat4_data=  {name:'cat_4', data:cat_4, color:colors.Cat_4};

            var allDataToDisplay = [cat1_data,cat2_data,cat3_data,cat4_data];


            var joinTitle = (userLevel === 'dvs') ? 'Facilities' : 'Districts';
            var title = '<span style="color:#509fc5; font-size: 15px ">Categorization by ' + joinTitle + ' based on Coverage and Dropout ' + params.year + '</span>';
            var chartId = (userLevel === 'dvs') ? 'categorizationByFacility':'categorizationByDistrict';

            getDynamicStackedChart(allDataToDisplay, chartId, title, category, null, null, params.year, 'Districts');

        });

    };

    $scope.districtClassificationFunc = function (filter, level, userLevel) {
        var colors = {'Class C': '#ffdb00', 'Class D': '#ff0d00', 'Class B': '#ABC9AA', 'Class A': '#006600'};
        var parameter = {product: parseInt(filter.product, 10), year: parseInt(filter.year, 10)};
        var objectData =(level!=='dvs')?GetClassificationByDistrictSummaryData:GetDistrictClassificationSummaryData;

           objectData.get(parameter).then(function (data) {
            var category = _.uniq(_.pluck(data, 'period'));

            var groupByClassification = _.groupBy(data, function (period) {
                return period.classification;
            });

            var mappedData = _.map(groupByClassification, function (value, index) {
                return {data: value, index: index};
            });
            var classification = [];
            for (var i = 0; i < mappedData.length; i++) {
                classification.push({
                    name: mappedData[i].index,
                    data: _.pluck(mappedData[i].data, 'total'),
                    color: colors[mappedData[i].index]
                });

            }
            var header = (level!=='dvs')?'Classification by District  ('+ filter.productName +') , ':'Classification by Facility';

            var title = '<span style="color:#509fc5; font-size: 15px">' +header+ filter.year + '</span>';

            var chartId = (level === null) ? 'classificationByDistrict' : 'classificationByDistrictLowerLevel';

            var chartCategory = ' ';
            chartCategory = 'classification';

            getDynamicStackedChart(classification, chartId, title, category, chartCategory, filter.product, filter.year, 'Districts',level);
        });
    };

    $scope.facilityClassificationFunc = function (filter, level, userLevel) {
        var colors = {'Class C': '#ffdb00', 'Class D': '#ff0d00', 'Class B': '#ABC9AA', 'Class A': '#006600'};
        var parameter = {year: parseInt(filter.year, 10),product: parseInt(filter.product, 10),doseId: parseInt(filter.dose, 10)};
        GetFacilityClassificationSummaryData.get(parameter).then(function (data) {
            var category = _.uniq(_.pluck(data, 'period'));

            var groupByClassification = _.groupBy(data, function (period) {
                return period.classification;
            });

            var mappedData = _.map(groupByClassification, function (value, index) {
                return {data: value, index: index};
            });

            var classification = [];
            for (var i = 0; i < mappedData.length; i++) {
                classification.push({
                    name: mappedData[i].index,
                    data: _.pluck(mappedData[i].data, 'total'),
                    color: colors[mappedData[i].index]
                });

            }
            var title = '<span style="color:#509fc5; font-size: 15px">Classification By Facility ,' + filter.year + '</span>';

            var chartId = 'classificationByFacilityLowerLevel';

            var chartCategory = ' ';
            chartCategory = 'classification';

            getDynamicStackedChart(classification, chartId, title, category, chartCategory, filter.product, filter.year, 'Facilities',level);
        });
    };

    function returnProductRange(indicator, product) {
        return (indicator === 'BAD') ? product + ' < 50%' : (indicator === 'WARN') ? '50%<=' + product + '<80%' : (indicator === 'NORMAL') ? '80%<=' + product + '<90%' : product + ' >=90%';
    }

    $scope.districtPerformanceFunc = function (filter) {

        var colors = {'WARN': '#ffdb00', 'BAD': '#ff0d00', 'NORMAL': '#ABC9AA', 'GOOD': '#006600'};
        var params = {
            product: parseInt(filter.product, 10),
            year: parseInt(filter.year, 10),
            doseId: parseInt(filter.dose, 0)
        };
        GetDistributionOfDistrictPerPerformanceData.get(params).then(function (data) {
            var category = _.uniq(_.pluck(data, 'periodname'));

            var groupByClassification = _.groupBy(data, function (period) {
                return period.coverageclassification;
            });
            var mappedData = _.map(groupByClassification, function (value, index) {
                return {data: value, index: index};
            });
            var classification = [];
            for (var i = 0; i < mappedData.length; i++) {
                classification.push({
                    name: returnProductRange(mappedData[i].index, _.pluck(mappedData[i].data, 'product')[0]),
                    data: _.pluck(mappedData[i].data, 'total'),
                    color: colors[mappedData[i].index]
                });

            }
            var title = '<span style="color:#509fc5; font-size: 15px">Distribution of Districts per Performance  ' + filter.year + '</span>';
            var chartId = 'districtDistribution';

            var chartCategory = '';
            chartCategory = 'Districts';
            getDynamicStackedChart(classification, chartId, title, category, chartCategory, filter.product, filter.year, 'Districts');


        });


    };

    function populatePerformanceMonitoringChart(chartdata, estimate, monthlyVaccinated, cumulativeVaccinated, chartId, title, year) {
        var chartValues = [];

        new Highcharts.chart(chartId, {
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: title
            },
            credits: {
                enabled: false
            },
            subtitle: {
                text: ' '
            },
            xAxis: [{
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                crosshair: true
            }],

            yAxis: [{ // Primary yAxis
                labels: {
                    format: '{value}',
                    style: {
                        color: Highcharts.getOptions().colors[2]
                    }
                },
                title: {
                    text: '',
                    style: {
                        color: Highcharts.getOptions().colors[2]
                    }
                },
                gridLineWidth: 1,
                opposite: false,
                gridLineColor: '#197F07',
                lineWidth: 1

            }, { // Secondary yAxis
                gridLineWidth: 1,
                title: {
                    text: 'Target Vs Vaccinated',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                labels: {
                    format: '{value}',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                }

            }, { // Tertiary yAxis
                gridLineWidth: 1,
                title: {
                    text: '',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                labels: {
                    format: '{value} mb',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                opposite: true
            }],
            tooltip: {
                shared: true
            },
            legend: {},
            series: chartdata/* [{
                name: 'estimate',
                type: 'line',
                yAxis: 1,
                zIndex: 2,
                data:estimate,
                tooltip: {
                    valueSuffix: ' Target'
                }

            },*/


            /* {
             name: 'Cumulative',
             type: 'line',
             yAxis: 1,

             zIndex: 1,
             data:cumulativeVaccinated,
             tooltip: {
                 valueSuffix: ' '
             }
         }*///]
        });


    }

    function arrangeValuesInLinear(estimateValues) {
        var tat =[];
        if(estimateValues.length>0){

            estimateValues.reduce(function (previous, current) {

                var prev = (current > previous)?current:previous;
                tat.push(prev);
                return prev;
            }, []);
        }

        return tat;
    }

    $scope.performanceMonitoring = function (filter, level) {

        var param = {product: parseInt(filter.product, 10), year: parseInt(filter.year, 10)};
        GetPerformanceMonitoringData.get(param).then(function (data) {

            var byCategory = _.groupBy(data, function (p) {
                return p.doseid;
            });


            var allStockDataPointsByCategory = $.map(byCategory, function (value, index) {
                return [{"byDose": index, "dataPoints": value}];
            });
            var performanceData = [];
            var period = [];
            var estimate = [],estimateValues=[];
            var productValue = [];
            var monthlyVaccinated = [],monthlyVaccinatedValues = [];
            var cumulativeVaccinated = [],cumulativeVaccinatedValues=[];
            chartIds = (level === 'cvs') ? 'performanceMonitoring' : 'performanceMonitoring1';
            title = '<span style="color: #0c9083">Performance Monitoring, ' + filter.year + '</span>';

            period = _.pluck(allStockDataPointsByCategory[0].dataPoints, 'period');

            // productValue.push( _.zip(period,estimate));

            estimateValues = _.pluck(allStockDataPointsByCategory[0].dataPoints, 'estimate');

           estimate =  arrangeValuesInLinear(estimateValues);


            for (var i = 0; i <= allStockDataPointsByCategory.length - 1; i++) {

                monthlyVaccinatedValues= _.pluck(allStockDataPointsByCategory[i].dataPoints, 'monthlyvaccinated');
                monthlyVaccinated = arrangeValuesInLinear(monthlyVaccinatedValues);
                cumulativeVaccinatedValues= arrangeValuesInLinear( _.pluck(allStockDataPointsByCategory[i].dataPoints,'vaccinated_cumulative'));

                cumulativeVaccinated.push({
                    name: filter.productName + ' ' + allStockDataPointsByCategory[i].byDose,
                    type: 'line',
                    yAxis: 1,
                    zIndex: 1,
                    data:cumulativeVaccinatedValues,
                    tooltip: {
                        valueSuffix: ' '
                    }
                });
            }

            var chartData = [];
            var estimateValue = [];
            estimateValue = [{
                name: 'Target',
                type: 'line',
                yAxis: 1,
                zIndex: 2,
                data: estimate,
                tooltip: {
                    valueSuffix: ' Target'
                }
            }];
            chartData = cumulativeVaccinated.concat(estimateValue);

            populatePerformanceMonitoringChart(chartData, estimate, monthlyVaccinated, cumulativeVaccinated, chartIds, title, filter.year);

        });

    };

    function loadDynamicDualAxisChart(chartId, chartTitle, params, district, onTimePercentage, reportedPercentage) {

        Highcharts.chart(chartId, {
            chart: {
                zoomType: 'xy'
            },
            credits: {
                enabled: false
            },
            title: {
                text: chartTitle
            },

            subtitle: {
                text: '<span style="font-size: 10px !important;color: #0c9083;text-align: center">' + params.periodName + '</span>'
            },
            xAxis: {
                categories: district,
                crosshair: true

            },
            yAxis: [{
                lineWidth: 1,
                max: 100,
                min: 0,
                title: {
                    text: 'Reporting Percentage'
                },
                tickInterval: 20,
                labels: {
                    format: '{value} %',
                    style: {
                        color: Highcharts.getOptions().colors[4]
                    }
                },
                gridLineColor: ''
            }],
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom',
                floating: false,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            series: [{
                type: 'column',
                name: '% Reported',
                data: reportedPercentage,
                tooltip: {
                    valueSuffix: '%'
                }
            }, {
                type: 'line',
                name: '% On time',
                data: onTimePercentage,
                tooltip: {
                    valueSuffix: '%'
                },
                color: 'red'

            }]
        });
    }

    function percentage(num, per) {
        return Math.floor((num / per) * 100);

    }


    function loadDynamicDistributionChart(distributionChartId, chartTitle, params, district, distributionRate) {

        Highcharts.chart(distributionChartId, {
            chart: {
                zoomType: 'xy'
            },
            credits: {
                enabled: false
            },
            title: {
                text: chartTitle
            },

            subtitle: {
                text: '<span style="font-size: 10px !important;color: #0c9083;text-align: center">' + params.periodName + '</span>'
            },
            xAxis: {
                categories: district,
                crosshair: true

            },
            yAxis: [{
                lineWidth: 1,
                max: 100,
                min: 0,
                title: {
                    text: 'distribution %'
                },
                tickInterval: 20,
                labels: {
                    format: '{value} %',
                    style: {
                        color: Highcharts.getOptions().colors[4]
                    }
                },
                gridLineColor: ''
            }],
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom',
                floating: false,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            series: [{
                type: 'column',
                name: '% distribution',
                data: distributionRate,
                tooltip: {
                    valueSuffix: '%'
                }
            }]
        });



    }

    $scope.loadIvdReportingFunc = function (para, userLevel) {

        if (userLevel === 'rvs') {

            GetIVDReportingSummaryData.get(para).then(function (data) {
                var district = _.pluck(data, 'district_name');

                var onTimePercentage =[],
                 reportedPercentage =[],
                  distributionRate = [];

                angular.forEach(data, function (report) {
                    reportedPercentage.push(percentage(parseInt(report.reported, 10), parseInt(report.expected, 10)));
                    onTimePercentage.push(percentage(parseInt(report.ontime, 10), parseInt(report.reported, 10)));
                    distributionRate.push(percentage(parseInt(report.distributed, 10), parseInt(report.expected, 10)));
                });

                var chartId = 'ivdReportingSummary';
                var distributionChartId = 'distributionSummary';

                var chartTitle = '<span style="font-size: 16px !important;color: #0c9083;text-align: center">Completeness and Timeliness of Immunization Reports </span> ';
                var distributionChartTitle = '<span style="font-size: 16px !important;color: #0c9083;text-align: center"> % Distribution </span> ';

                loadDynamicDualAxisChart(chartId, chartTitle, para, district, onTimePercentage, reportedPercentage);
                loadDynamicDistributionChart(distributionChartId, distributionChartTitle, para, district,distributionRate);
            });
        }

    };


    function loadDynamicSessionChart(chartNameId,chartTitle, verticalTitle, verticalTitle2, periods,planned, conducted,pconducted) {

        new Highcharts.chart(chartNameId, {
            chart: {
                zoomType: 'xy'
            },
            credits: {
                enabled: false
            },
            title: {
                text: chartTitle
            },
            subtitle: {
                text: ''
            },
            xAxis: [{
                categories: periods,
                crosshair: true,
                labels: {
                    style: {
                        'font-weight': 'bold'
                    }
                }

            }],
            yAxis: [{ // Primary yAxis
                labels: {
                    format: '{value}',
                    style: {
                        color: Highcharts.getOptions().colors[1],
                        'font-weight': 'bold'
                    }
                },
                title: {
                    text: verticalTitle,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                }
               // gridLineColor: '',
                //lineWidth: 1,
               /* max: 100,
                min: 0*/



            }, { // Secondary yAxis
                title: {
                    text: verticalTitle2,
                    style: {
                        color: Highcharts.getOptions().colors[0],
                        'font-weight': 'bold'

                    }
                },
                labels: {
                    format: '{value} %',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                opposite: true,gridLineColor: ''

            }],
            tooltip: {
                shared: true
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                // x: 120,
                verticalAlign: 'bottom',
                // y: 100,
                floating: false,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            series: [{
                name: 'Number outreach sessions planned',
                type: 'column',
                data: planned,
                color:'#9FB3E6',
                tooltip: {
                    valueSuffix: ''
                }

            }, {
                name: 'Number outreach sessions conducted',
                type: 'column',
                data: conducted,
                color:'#23006E',
                tooltip: {
                    valueSuffix: ''
                }

            },{
                name: '% RI outreach sessions conducted',
                type: 'line',
                yAxis: 1,
                data: pconducted,
                color:'red',
                tooltip: {
                    valueSuffix: ' %'
                }
            }]
        });


    }

    $scope.loadSessionSummary = function (para, userLevel) {

        GetImmunizationSessionSummaryData.get(para).then(function (data) {

            var periods = _.pluck(data,'periodname');
            var planned = _.pluck(data,'planned');
            var conducted = _.pluck(data,'conducted');
            var pconducted = _.pluck(data,'pconducted');
            var chartNameId = 'sessionSummaryChart';
            var chartTitle = '<span style="font-size: 16px !important;color: #0c9083;text-align: center"> Outreach Session Planned and Conducted, '+para.year+'</span> ';
            var verticalTitle = '<span style="font-size: 10px !important;color: #0c9083;text-align: center">Number of sessions</span>';
            var verticalTitle2 = '<span style="font-size: 10px !important;color: #0c9083;text-align: center">% sessions conducted</span>';

            loadDynamicSessionChart(chartNameId,chartTitle,verticalTitle,verticalTitle2,periods,planned, conducted,pconducted);


        });




    };

    function openStockStatusForAllLevels(charts){

        var par = {
                facilityId: parseInt($scope.homeFacility.facilityid, 10),
                date: null
            };
            FacilityInventoryStockStatusData.get(par).then(function (data) {
                if (data !== null) {
                    var byCategory = _.groupBy(data, function (p) {
                        return p.product_category;
                    });
                    var allStockDataPointsByCategory = $.map(byCategory, function (value, index) {
                        return [{"productCategory": index, "dataPoints": value}];
                    });
                    getVaccineStockStatusChartForLowerLevel(allStockDataPointsByCategory,charts);

                }


            });


    }

    if ($scope.homeFacility.facilitytypecode !== 'cvs') {

        var userLevel = ($scope.homeFacility.facilitytypecode === 'dvs') ? 'dvs' : 'rvs';
         $scope.userLevel = userLevel;
        $scope.filter = {};
        var currentDate = (defaultYear === null || defaultYear === undefined)?new Date().getFullYear()-1:defaultYear;

        GetPeriodForDashboard.get(currentDate).then(function (data) {

            $scope.filter.product = defaultProduct;
            $scope.findProductToDisplay = _.where(ProductFilteredData, {'id': parseInt(defaultProduct,10)});
            $scope.years = YearFilteredData.sort(function (a, b) {
                return b - a;
            });
            $scope.filter.year = currentDate;

            $scope.products = $scope.findProductToDisplay;

            $scope.filter.period = data.id;
            var pa = {year: currentDate, product: defaultProduct, period: parseInt(data.id, 10), dose: 3};
            $scope.productToDisplay = _.findWhere($scope.products, {id: parseInt(defaultProduct, 10)});
            var para = angular.extend(pa, currentDate, {
                periodName: data.name,
                productName: $scope.productToDisplay.name
            });
            $scope.changeYear();
            $scope.performanceMonitoring(para, null);
            $scope.districtCategorizationFunct(para, userLevel, userLevel);
            $scope.districtClassificationFunc(para, userLevel, userLevel);
            $scope.facilityClassificationFunc(para, userLevel, userLevel);
            $scope.loadDistrictCoverageFunc(para, userLevel);
            $scope.loadIvdReportingFunc(para, userLevel);
            $scope.loadSessionSummary(para,userLevel);
            $scope.getFacilityStockStatusSummary(para,null);


            $scope.showfilter = false;


            $scope.showFilter = function () {
                $scope.products = ProductFilteredData;

                $scope.showfilter = true;
                $scope.myModal = true;
            };
            $scope.hideFilter = function () {
                $scope.showfilter = false;
            };



        });

        $scope.loadDashboardDataForLowerLevel = function (filter) {

            $scope.productToDisplay = _.findWhere($scope.products, {id: parseInt(filter.product, 10)});
            $scope.periodToDisplay = _.findWhere($scope.periods, {id: parseInt(filter.period, 10)});
            $scope.doseToDisplay = filter.dose;
            var prepareParams = angular.extend(filter, {
                productName: $scope.productToDisplay.name,
                periodName: $scope.periodToDisplay.name
            });

            $scope.changeYear();

            $scope.performanceMonitoring(filter, null);
            $scope.districtCategorizationFunct(filter, userLevel, userLevel);
            $scope.districtClassificationFunc(filter, userLevel, userLevel);
            $scope.facilityClassificationFunc(filter, userLevel, userLevel);
            $scope.loadDistrictCoverageFunc(filter, userLevel);
            $scope.loadIvdReportingFunc(filter, userLevel);
            $scope.loadSessionSummary(filter,userLevel);
            $scope.getFacilityStockStatusSummary(filter,null);

            $scope.showfilter = false;
        };

        $scope.changeYear = function () {

            $scope.periods = [];
            ReportPeriodsByYear.get({
                year: $scope.filter.year
            }, function (data) {
                $scope.periods = data.periods;
                $scope.filter.period = $scope.periods[0].id;

            });

        };

        $scope.periodName = [];
        $scope.filter = {};
        $scope.mans = [{'k': 181}];

        $scope.doseByProduct = function () {
            if ($scope.filter.product !== undefined)
                getDoseFilter($scope.filter.product);

        };
        if ($scope.filter.product === null || $scope.filter.product === undefined) {
            $scope.filter.product = 2412;
        }
        openStockStatusForAllLevels(['myStockVaccine', 'myStockSyringe']);

    //Removed data here

    } else {
       var charts = ['myStockVaccine1', 'myStockSyringe1'];
       // console.log(defaultYear);
        var currentDate2 = (defaultYear === null || defaultYear === undefined)?new Date().getFullYear()-1:defaultYear;
        GetPeriodForDashboard.get(currentDate2).then(function (data) {

          //  console.log(defaultProduct);
            $scope.filter.product = defaultProduct;
            $scope.findProductToDisplay = _.where(ProductFilteredData, {'id': parseInt(defaultProduct,10)});
            $scope.years = YearFilteredData.sort(function (a, b) {
                return b - a;
            });
            $scope.filter.year = currentDate2;

            $scope.products = $scope.findProductToDisplay;
            //console.log($scope.products);

            $scope.filter.period = data.id;
            var par = {year: currentDate2, product: defaultProduct, period: parseInt(data.id, 10), dose: 3};
            $scope.productToDisplay = _.findWhere($scope.products, {id: parseInt(defaultProduct, 10)});
            var para = angular.extend(par, currentDate2, {
                periodName: data.name,
                productName: $scope.productToDisplay.name
            });
            $scope.changeYear();
            openStockStatusForAllLevels(charts);
            $scope.loadCoverageMap(para);
            $scope.districtCategorizationFunct(para, null, null);
            $scope.districtClassificationFunc(para, null, null);
            $scope.districtPerformanceFunc(para);
            $scope.performanceMonitoring(para, 'cvs');
            // $scope.loadMap(par);
            $scope.loadDistrictCoverageFunc(para, null);
            $scope.getVaccineInventorySummary();
            $scope.vaccineCoverageByRegionAndProductFunc(para);
            $scope.vaccineCoverageByProductAndDoseFunc(para);
            $scope.getAggregatePerformanceFunc(para);
            $scope.getFacilityStockStatusSummary(para,'cvs');



        });

        $scope.loadDashboardData = function (filter) {
            $scope.productToDisplay = _.findWhere($scope.products, {id: parseInt(filter.product, 10)});
            $scope.periodToDisplay = _.findWhere($scope.periods, {id: parseInt(filter.period, 10)});
            $scope.doseToDisplay = filter.dose;
            var prepareParams = angular.extend(filter, {
                productName: $scope.productToDisplay.name,
                periodName: $scope.periodToDisplay.name
            });

            $scope.loadCoverageMap(filter);
            $scope.districtCategorizationFunct(filter, null, null);
            $scope.districtClassificationFunc(filter, null, null);
            $scope.loadDistrictCoverageFunc(filter, null);
            $scope.districtPerformanceFunc(filter);
            $scope.performanceMonitoring(filter, 'cvs');
            $scope.vaccineCoverageByRegionAndProductFunc(prepareParams);
            $scope.vaccineCoverageByProductAndDoseFunc(prepareParams);
            $scope.getAggregatePerformanceFunc(filter);
            $scope.showfilter = false;
            $scope.getFacilityStockStatusSummary(filter,'cvs');
        };

        $scope.changeYear = function () {

            $scope.periods = [];
            ReportPeriodsByYear.get({
                year: $scope.filter.year
            }, function (data) {
                $scope.periods = data.periods;
                $scope.filter.period = $scope.periods[0].id;

            });

        };

        $scope.periodName = [];
        $scope.filter = {};
        $scope.mans = [{'k': 181}];

        $scope.doseByProduct = function () {
            if ($scope.filter.product !== undefined)
                getDoseFilter($scope.filter.product);

        };
        if ($scope.filter.product === null || $scope.filter.product === undefined) {
            $scope.filter.product = defaultProduct;
        }


        var date = new Date();
        var year = date.getFullYear();
        year = 2017;
        var doseId = 3;
        var params = {
            productId: $scope.filter.product,
            periodId: 121,
            year: year,
            doseId: 1
        };


        $scope.getAggregatePerformanceFunc = function (params) {

            GetAggregateFacilityPerformanceData.get(params).then(function (data) {
                if (!isUndefined(data) || data.length > 0) {
                    $scope.facilityPerformance = data;
                    $scope.showNoData = false;
                }
                else
                    $scope.showNoData = true;


            });

        };


        $scope.vaccineCoverageByRegionAndProductFunc = function (params) {

            VaccineCoverageByProductData.get(params).then(function (coverage) {
                if (!isUndefined(coverage))
                    coverageByRegion(coverage, params);
            });
        };

        $scope.vaccineCoverageByProductAndDoseFunc = function (params) {
            GetCoverageByProductAndDoseData.get(params).then(function (coverage) {
              // console.log(coverage);
                if (!isUndefined(coverage))
                    coverageByProductAndDose(coverage, params);
            });
        };


        // allData();


        /*  var product = 2413,
              period = 121;
          // var params = {product:2413,doseId:1,period:121,year:2017};

          var loadOnStart = function () {
              var params = {product: 2413, doseId: 1, period: 121, year: 2017};
              $scope.fullStockAvailability(params);

          };
          //loadOnStart();

          $scope.OnFilterChanged = function () {
  /!*
              if ($scope.filter === null || $scope.filter === undefined) {
                  return;
              } else {
                  $scope.fullStockAvailability($scope.filter);

                  getDoseFilter($scope.filter);
              }*!/

          };*/


        /* var chart = Highcharts.chart('container2', {
                 chart: {
                     type: 'pie',
                     options3d: {
                         enabled: true,
                         alpha: 45
                     },
                     style: {
                         fontFamily: 'helvetica'
                     }
                 },
                 title: {
                     text: '<span style="font-size: 60px;">80 %</span> <br/> <div class="clearfix"></div><span style="font-size: 12px !important;">DTP3-HepB-Hib-3 Coverage Nov, 2017</span>',align:'center',verticalAlign: 'middle'
                 },


                 credits: {enabled: false},

                 /!* subtitle: {
                      text: '3D donut in Highcharts'
                  },*!/
                 plotOptions: {
                     pie: {
                         /!*
                                         innerSize:200,
                         *!/
                         innerSize: '80%',
                         size: '80%',
                         depth: 145,
                         shadow:false,
                         showInLegend: false,
                         dataLabels: {
                             enabled: false
                         },
                         borderWidth: 0
                     }
                 },
                 legend: {

                     layout: 'vertical',
                     align: 'right',
                     verticalAlign: 'left',
                     floating: true,
                     x: 0,
                     y: 30,
                     itemMarginTop: 10,
                     backgroundColor: '#f3f3f3',
                     useHTML: true,
                     labelFormatter: function () {
                         return '<div style="width:200px"><span style="float:left">' + this.name + '</span><span style="float:right; margin-right:1%">' + Highcharts.numberFormat(this.y, 0) + '</span></div>';
                     }
                     /!*
                                     itemMarginBottom: 10
                     *!/
                 },

                 series: [{
                     name: 'Coverage %',
                     data: [
                         ['January', 8],
                         ['February', 3],
                         ['March', 1],
                         ['April', 6],
                         {
                             name: 'May',
                             y: 9,
                             sliced: true,
                             selected: true
                         },
                         ['June', 4],

                         ['July', 4],
                         ['August', 1],
                         ['September', 1]
                     ]
                 }]
             }



         );*/

        /*
            var text = chart.renderer.text('<span style="font-size: 50px;text-align: center">89 %</span>' +
                    '<br/><br/><Strong> Coverage</Strong>').add(),
                textBBox = text.getBBox(),
                x = chart.plotLeft + (chart.plotWidth *0.4) - (textBBox.width * 0.2),
                y = chart.plotTop + (chart.plotHeight * 0.5) + (textBBox.height * 0.25);
        // Set position and styles
            text.attr({ x: x, y: y }).css({ fontSize: '20px', color: '#666666' }).add();*/


        //Coverage

        var d = new Date();
        var pastYear = d.getFullYear() - 2;
        var periodSorter = function (value) {
            return parseInt(value.id, 10);
        };


        $scope.popover = {
            "title": "Title",
            "content": "Hello Popover<br />This is a multiline message!",
            "saved": true
        };

        $scope.prod = [{id: 2, name: 'name'}, {id: 3, name: '777'}];
        $scope.showfilter = false;

        $scope.myModal = false;
        $scope.showFilter = function () {
            $scope.products = ProductFilteredData;

            $scope.showfilter = true;
            $scope.myModal = true;
        };
        $scope.hideFilter = function () {
            $scope.showfilter = false;
        };
        $(".button-collapse").sideNav();
        // $('#modal1').modal('open');

        $scope.year_slider = {
            value: parseInt(pastYear, 10),
            options: {
                floor: parseInt(pastYear, 10),
                ceil: parseInt(d.getFullYear(), 10),

                translate: function (value, sliderId, label) {
                    return value;
                }, onChange: function (sliderId, modelValue, highValue, pointerType) {
                    getPeriodByYear(modelValue);
                    return sliderId;
                },
                interval: 1,
                //ticksArray: [0, 2,3,4,5,6,7,8,9,10,11,12],
                showSelectionBar: false,
                //  showTicksValues: true,
                showTicks: true
            }
            /* value: 1,
             options: {
                 floor: 1,
                 ceil: 12,
                 showTicksValues: true,
                 translate: function(value) {
                     return value;
                 },
                 ticksValuesTooltip: function(v) {
                     return v;
                 },
                 showTicks: true

             }*/
        };


        $scope.card = {};
        $scope.showIcons = function (card) {

            $scope.showicons = true;

            /*    if(!card.displayTable && !card.displayMap && !card.displayColumn){
                    card.showicons = true;
                }else{
                    card.showicons = false;
                }*/
        };
        $scope.changeChart = function (data) {


        };
        $scope.icons = [
            /*   {name: 'table', image: 'table.jpg', action: ''},
               {name: 'column', image: 'bar.png', action: ''},
               {name: 'line', image: 'line.png', action: ''},
               {name: 'combined', image: 'combined.jpg', action: ''},
               {name: 'column', image: 'column.png', action: ''},
               {name: 'area', image: 'area.jpg', action: ''},*/
            /*
                        {name: 'pie', image: 'search.png', action: ''},
            */
            {name: 'filter', image: 'si-glyph-apron.svg', action: ''}
        ];
        $scope.hideIcons = function (card) {

            $scope.showicons = false;


            //card.showicons = false;
        };

        $(function () {
            $('#container15').highcharts({
                chart: {
                    type: 'column'
                },

                plotOptions: {
                    pie: {
                        innerSize: '70%'
                    }
                },

                title: {
                    verticalAlign: 'top',
                    floating: true,
                    text: 'Home' + '200%'
                },

                series: [{
                    data: [
                        ['Firefox', 44.2],
                        ['IE7', 26.6],
                        ['IE6', 20],
                        ['Chrome', 3.1],
                        ['Other', 5.4]
                    ]
                }]
            });
        });


        $scope.showProduct = false;
        var getProduct = function () {
            ProductService.get(parseInt($scope.filter.product, 10)).then(function (data) {
                $scope.product = data;
                $scope.showProduct = true;
            });
        };
        getProduct();


        $scope.geojson = {};

        $scope.default_indicator = "ever_over_total";

        $scope.expectedFilter = function (item) {
            return item.monthlyEstimate > 0;
        };


        $scope.$watch('period', function (newVal, oldVal) {
            // $scope.onChange();
            // $scope.$parent.OnFilterChanged();
        });
    }


    function round(value, precision) {
        var multiplier = Math.pow(10, precision || 0);
        return Math.round(value * multiplier) / multiplier;
    }


    function coverageByRegion(coverage, params) {

        var cov = _.pluck(coverage, 'coverage');
        var region = _.pluck(coverage, 'region');

        var result = [], i = -1,
            color = {Cat_1: '#52C552', Cat_2: '#509fc5', Cat_3: '#E4E44A', Cat_4: '#FF0000'};

        while (cov[++i]) {
            if (cov[i] < 50)
                result.push([{color: '#FF0000', y: cov[i]}]);
            else
                result.push([{color: '#009012', y: cov[i]}]);
        }
        var mergedArrays = [].concat.apply([], result);

        $('#container7').highcharts({
            chart: {
                type: 'bar'
            },
            title: {
                text: params.productName + '-' + params.dose + ' Coverage By Region' + ' ,' + params.periodName
            },
            credits: {enabled: false},
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: region,
                title: {
                    text: null
                }
            },
            legend: {
                shadow: false
            },
            yAxis: {
                min: 0,
                title: {
                    text: '',
                    align: 'high'
                },
                labels: {
                    overflow: 'justify'
                }
            },
            tooltip: {
                valueSuffix: ' %'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },

            series: [{
                name: 'Coverage',
                data: mergedArrays,
                valueSuffix: ' %'

            }]
        });

    }


    function showDoseSlider(dose) {

        var displayName = _.pluck(dose, 'displayName');

        $scope.dose_slider = {
            value: 1,
            options: {
                floor: 1,
                ceil: parseInt(displayName.length, 10),
                translate: function (value, sliderId, label) {
                    return displayName[value - 1];
                },
                onChange: function (sliderId, modelValue, highValue, pointerType) {
                    return sliderId;
                },
                interval: 1,
                showTicksValues: true,
                showTicks: true
            }
            /* value: 1,
             options: {
                 floor: 1,
                 ceil: 12,
                 showTicksValues: true,
                 translate: function(value) {
                     return value;
                 },
                 ticksValuesTooltip: function(v) {
                     return v;
                 },
                 showTicks: true

             }*/
        };

    }

    function getDoseFilter(product) {

        if (!isUndefined(product)) {
            VimsVaccineSupervisedIvdPrograms.get({}, function (data) {
                VaccineProductDoseList.get(
                    {
                        programId: parseInt(data.programs[0].id, 10),
                        productId: parseInt(product, 10)
                    },
                    function (result) {
                        $scope.doses = result.doses;
                        $scope.filter.dose = 'Dose 3';
                    });
            });
        }

    }

    //More Drill Down Data for the Chart 2
    function getStockAvailabilityDataView(chart2Data) {
        if (chart2Data.y !== null) {

            var indicator = (chart2Data.color === 'lightgray') ? 'availableStock' : (chart2Data.color === 'blue') ? 'CCE' : 'coverage';

            var d = {'indicator': indicator, 'total': chart2Data.y, 'period': chart2Data.category};
            $state.go('toMoreStockAvailabilityView', {
                'indicator': indicator,
                'total': chart2Data.y,
                'period': chart2Data.category
            });

        }

    }


    function getPeriodSlider(data) {

        var sortedValues = _.sortBy(data, periodSorter);
        var period_name = _.pluck(sortedValues, 'name');


        $scope.slider = {
            value: 1,
            options: {
                floor: 1,
                ceil: period_name.length,
                translate: function (value, sliderId, label) {
                    return period_name[value - 1];
                },
                interval: 10,
                // logScale:true,
                //ticksArray: [0, 2,3,4,5,6,7,8,9,10,11,12],
                // showSelectionBar: true,
                showTicksValues: true,
                showTicks: true,
                focus: true

            }
            /* value: 1,
             options: {
                 floor: 1,
                 ceil: 12,
                 showTicksValues: true,
                 translate: function(value) {
                     return value;
                 },
                 ticksValuesTooltip: function(v) {
                     return v;
                 },
                 showTicks: true

             }*/
        };


        $(function () {
            $('#sampleInput').popover();
            $('#selectb').select2();
            $('#selectb-popover').popover();
            $('select').material_select();
            $("#e1").select2();

            $("#select2insidemodal").select2({
                dropdownParent: $("#myModal")
            });
            $("#idSelect").select2({
                width: "100%"
            });
        });
    }

    function loadDynamicChart3(chartNameId, type, chartTitle, verticalTitle, verticalTitle2, name, product, cov, total, params) {

        new Highcharts.chart(chartNameId, {
            chart: {
                zoomType: 'xy'
            },
            credits: {
                enabled: false
            },
            title: {
                text: chartTitle
            },
            subtitle: {
                text: ''
            },
            xAxis: [{
                categories: product,
                crosshair: true,
                labels: {
                    style: {
                        'font-weight': 'bold'
                    }
                }

            }],
            yAxis: [{ // Primary yAxis
                labels: {
                    format: '{value}',
                    style: {
                        color: Highcharts.getOptions().colors[1],
                        'font-weight': 'bold'
                    }
                },
                title: {
                    text: verticalTitle2,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                }
            }, { // Secondary yAxis
                title: {
                    text: verticalTitle,
                    style: {
                        color: Highcharts.getOptions().colors[0],
                        'font-weight': 'bold'

                    }
                },
                labels: {
                    format: '{value} %',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                opposite: true
            }],
            tooltip: {
                shared: true
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                // x: 120,
                verticalAlign: 'bottom',
                // y: 100,
                floating: false,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            series: [{
                name: '# Of Children Vaccinated',
                type: 'column',
                data: total,
                color:'blue',
                tooltip: {
                    valueSuffix: ''
                }

            }, {
                name: 'Coverage',
                type: 'spline',
                yAxis: 1,
                data: cov,
                color:'red',
                tooltip: {
                    valueSuffix: ' %'
                }
            }]
        });


    }

    function coverageByProductAndDose(coverage, params) {
         //   console.log(coverage);
        var colors = {'WARN': '#ffdb00', 'BAD': '#ff0d00', 'NORMAL': '#ABC9AA', 'GOOD': '#006600'};
        var dataValues = [];
        var totalVaccinated = [];
        coverage.forEach(function (data) {
       // console.log(data);
            totalVaccinated.push({name: 'vaccinated', color: 'blue', y: data.total});
            dataValues.push({name: 'byChart', color: colors[data.coverageclassification], y: data.coverage});
        });

        var cov = _.pluck(coverage, 'coverage');
        var total = _.pluck(coverage, 'total');
        var product = _.pluck(coverage, 'product');

        var chartNameId = 'productByDoseChart';
        var type = 'column';
        var chartTitle = '<span style="font-size: 20px !important;color: #0c9083;text-align: center">Immunization Coverage, Tanzania ' + params.year + '</span> ';
        var name = 'Coverage';
        var verticalTitle = 'Coverage(%)';
        var verticalTitle2 = '# Of Children Vaccinated';
        loadDynamicChart3(chartNameId, type, chartTitle, verticalTitle, verticalTitle2, name, product, cov, total, params);
        //loadDynamicChart(chartNameId, type, chartTitle, verticalTitle, name, product, dataValues,params.year,params.periodName);

    }

    function dynamicChart(chartId, title) {

        new Highcharts.chart(chartId, {

            chart: {
                type: chartId,
                height: 200,
                spacingBottom: 15,
                spacingTop: 20,
                spacingLeft: 5,
                spacingRight: 15,
                borderWidth: 1,
                borderColor: '#ddd'
            },

            title: {text: title},
            legend: {padding: 0, margin: 5},
            credits: {enabled: true},
            tooltip: {enabled: false},
            plotOptions: {column: {dataLabels: {enabled: true}}},
            colors: ['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'],
            loading: {labelStyle: {top: '35%', fontSize: "2em"}},
            xAxis: {categories: ["7/12", "7/13", "7/14", "7/15", "7/16", "7/17", "7/18"]},
            series: [
                {
                    "name": "Odometer",
                    "data": [{"y": 94.98}, {"y": 182.96}, {"y": 160.97}, {"y": 18.00}, {"y": 117.97}, {"y": 6.00}, {"y": 127.97}]
                }
            ]


        });

    }


    function loadDynamicChart2(chartNameId, type, chartTitle, chartSubtitle, verticalTitle, toolTip, name, category, dataValue) {

        new Highcharts.chart(chartNameId, {
            chart: {
                type: type
            },
            credits: {
                enabled: false
            },
            title: {
                text: chartTitle
            },
            subtitle: {
                text: chartSubtitle
            },
            xAxis: {
                categories: category,
                crosshair: false
            },
            yAxis: {
                min: 0,
                title: {
                    text: verticalTitle
                },
                lineColor: '#999',
                lineWidth: 1,
                tickColor: '#666',
                tickWidth: 1,
                tickLength: 5,
                gridLineColor: '',
                tickInterval: 1
            },
            tooltip: {
                formatter: function () {
                    var tooltip;
                    tooltip = '<span style="color:' + this.color + '">' + this.y + ' Of ' + this.total + ' ' + toolTip + ' </span>';

                    return tooltip;
                }
            },
            plotOptions: {
                bar: {
                    pointPadding: 0.2,
                    borderWidth: 0,
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function () {
                                $scope.showModal(this);
                            }
                        }
                    }

                }
            },

            series: [{
                name: name,
                data: dataValue

            }]
        });


    }


    function loadDynamicChart(chartNameId, type, chartTitle, verticalTitle, name, category, dataValue, year, period) {

        new Highcharts.chart(chartNameId, {
            chart: {
                type: type
            },
            legend: {
                enabled: false,
                useHTML: true,
                symbolHeight: 14,
                symbolWidth: 14,
                symbolRadius: 3,
                style: {
                    fontSize: '9px',
                    whiteSpace: 'normal'
                },
                layout: 'vertical',
                itemMarginTop: 5,
                itemMarginBottom: 5,
                padding: 0,
                itemStyle: {
                    fontSize: '12px',
                    color: '#666',
                    fontWeight: 'bold'
                }

            },
            credits: {
                enabled: false
            },
            title: {
                text: chartTitle
            },
            subtitle: {
                text: '<span style="font-size: 11px !important; color: #0c9083">( Coverage Compararison between two Antigens ' + ', ' + period + '</span>)'
            },
            xAxis: {
                categories: category,
                crosshair: false
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Coverage %'
                },
                lineColor: '#999',
                lineWidth: 1,
                tickColor: '#666',
                tickWidth: 1,
                tickLength: 3,
                gridLineColor: ''
            },
            tooltip: {
                formatter: function () {
                    var tooltip;
                    tooltip = '<span style="color:' + this.series.color + '">' + this.series.name + '</span>: <b>' + this.y + '</b><br/>';

                    return tooltip;
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0

                }
            },

            series: [{
                name: name,
                data: dataValue

            }

            ]
        });


    }


    function getPeriodByYear(modelValue) {

        ReportPeriodsByYear.get({
            year: parseInt(modelValue, 10)
        }, function (data) {
            getPeriodSlider(data.periods);
        });
    }


    $scope.loadCoverageMap = function (params) {

        GetCoverageMapInfo.get(params).then(function (data) {
            var dataValues = [];
            Highcharts.each(data, function (code, i) {
                var colorV;
                if (parseInt(code.value, 10) <= 0 || isNaN(code.value))
                    colorV = 'black';

                dataValues.push({
                    code: code.code,
                    value: parseInt(code.value, 10),
                    color: colorV
                    // color:interpolateCoverage(code.cumulative_vaccinated,code.monthly_district_target,code.coverageclassification.toLowerCase())

                });



            });

          $scope.getRegionCoverageDataFunction(data);

            var small = $('#coverage_map').width() < 400;
            var separators = Highcharts.geojson(Highcharts.maps['countries/tz/tz-all'], 'mapline');


            Highcharts.mapChart('coverage_map', {
                chart: {
                    map: 'countries/tz/tz-all'
                }, credits: {enabled: false},

                title: {
                    text: '<span style="font-size: 15px !important;color: #0c9083;text-align: center">' + params.productName + '-' + params.dose + ' Coverage By Region, ' + params.year + '</span>'
                },

                subtitle: {
                    text: '',
                    floating: true,
                    align: 'right',
                    y: 50,
                    style: {
                        fontSize: '16px'
                    }
                },

                legend: {},

                /*   colorAxis: {
                       min: 0,
                       minColor: '#FF0000',
                       maxColor: '#52C552'
                   },*/
                colorAxis: {
                    dataClasses: [{
                        from: 0,
                        to: 80,
                        color: '#ff0d00',
                        name: 'Below 80%'
                    }, {
                        from: 80,
                        to: 90,
                        color: '#ffdb00',
                        name: '80% to 89%'
                    }, {
                        from: 90,
                        color: '#006600',
                        name: '90%+'

                    }]
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
                            /* hover: {
                                 color: '#EEDD66'
                             }*/
                        }
                    }
                },

                series: [{
                    data: dataValues,
                    joinBy: ['hc-key', 'code'],
                    name: 'Coverage',
                    dataLabels: {
                        enabled: true,
                        format: '{point.properties.postal-code}'
                    }, shadow: false
                }]
            });




        });

    };


    function mapInfo(data, MapId) {

// Create the chart
        Highcharts.mapChart(MapId, {
            chart: {
                map: 'countries/tz/tz-all'
            },

            title: {
                text: 'Highmaps basic demo'
            },

            /* subtitle: {
                 text: 'Source map: <a href="http://code.highcharts.com/mapdata/countries/tz/tz-all.js">United Republic of Tanzania</a>'
             },*/

            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },

            colorAxis: {
                min: 0
            },

            series: [{
                data: data,
                name: 'Random data',
                states: {
                    hover: {
                        color: '#BADA55'
                    }
                },
                dataLabels: {
                    enabled: true,
                    format: '{point.value}'
                }
            }]
        });
    }

}

StockAvailabilityControllerFunc1.resolve = {

    ProductFilteredData: function ($q, $timeout, ReportProductsWithoutDescriptionsAndWithoutProgram) {
        var deferred = $q.defer();
        $timeout(function () {
            ReportProductsWithoutDescriptionsAndWithoutProgram.get({}, function (data) {
                deferred.resolve(data.productList);
            }, {});
        }, 100);
        return deferred.promise;
    },
    YearFilteredData: function ($q, $timeout, OperationYears) {
        var deferred = $q.defer();
        $timeout(function () {
            OperationYears.get({}, function (data) {
                deferred.resolve(data.years);
            }, {});
        }, 100);
        return deferred.promise;
    },

    homeFacility: function ($q, $timeout, HomeFacilityWithType) {
        var deferred = $q.defer();
        var homeFacility = {};

        $timeout(function () {
            HomeFacilityWithType.get({}, function (data) {
                homeFacility = data.homeFacility;
                deferred.resolve(homeFacility);
            });

        }, 100);
        return deferred.promise;
    },
    defaultYear: function($q, $timeout, ConfigSettingsByKey){
        var deferred = $q.defer();
        $timeout(function () {
            ConfigSettingsByKey.get({key: 'DASHBOARD_DEFAULT_YEAR'}, function (data){
                deferred.resolve(data.settings.value);
            });
        }, 100);
        return deferred.promise;
    },


     defaultProduct: function($q, $timeout, ConfigSettingsByKey){
            var deferred = $q.defer();
            $timeout(function () {
                ConfigSettingsByKey.get({key: 'VACCINE_DASHBOARD_DEFAULT_PRODUCT'}, function (data){

                   // console.log(data);
                    deferred.resolve(data.settings.value);
                });
            }, 100);
            return deferred.promise;
        }


};