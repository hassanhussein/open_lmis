function StockOutRateController($scope, $http, $location, Program, Period, Products, $rootScope, StockOutRateData, StockStatusByLocationData, GetTzDistrictMapData, GetTzRegionMapData, GetProductById, LatestReportedStockStatusByDistrictData, ConsumptionTrendsData, GetStockOutRateTrendOfTracerProductsData, GetStockOutRateTrendOfProductsData, GetLatestStockImbalanceReportByDistrictForTracerData, GetLatestStockImbalanceReportByDistrictForProductData) {

    $scope.year = '';
    $scope.productName = '';
    $rootScope.summary = {
        content: null
    };
    $scope.productFilter = false;
    var drillDownState = false;
    var drillDownIndex = 0;

    var stockOutRateTrendForTracerData;



    //Stock Availability Map
    $rootScope.loadLatestReportedStockStatus = function(params) {
        LatestReportedStockStatusByDistrictData.get(params).then(function(data) {
            GetTzRegionMapData.get(params).then(function(regionMap) {
                GetTzDistrictMapData.get(params).then(function(districtMap) {
                    var dataWithRegion = getRegionDataFromDistrictData(data);
                    var unit = "Facilities";
                    var zeroColor = "#e74c3c"; //Red
                    var title = params.indicator === 'allTracerProducts' ? 'Percentage of Tracer Availability' : 'Percentage of Availability';
                    districtMap.feature = districtMap.features;
                    loadLatestReportedStockStatusMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                    var imbalanceData = getImbalanceData(data);
                    $scope.stockImbalanceChart('stockImbalanceByLocation', imbalanceData.imbalance);
                });

            });
        });
    };

    function computeMapData(data, indicator) {
        var data_arr = [];
        for (var x in data) {
            var obj = [];
            obj.push(data[x].region_name);
            obj.push(data[x].reported);
            obj.push(prepareValues(data, x, indicator));
            data_arr.push(obj);
        }
        return data_arr;
    }


    function loadLatestReportedStockStatusMap(regionMap, districtMap, values, params, districtValues, regionData, title, unit, zeroColor) {

        var data = Highcharts.geojson(regionMap);
        var areaName = 'Region';
        getLatestStockAvailabilityStatusSummary(regionData, params);
        $.each(data, function(i) {
            var key = this.properties['hc-key'];
            this.drilldown = key;
            var data_obj = values.filter(function(a) {
                return a[0] === key;
            });

            if (data_obj.length > 0) {
                var reg_obj = _.where(regionData, {
                    region_name: data_obj[0][0]
                })[0];
                var stockOutPercentage = Math.round((reg_obj.total_stockoutincidence / reg_obj.total_incidence) * 100);


                this.y = stockOutPercentage;
                this.value = ((100 - stockOutPercentage) <= 0) ? 1 : 100 - stockOutPercentage;
                this.stockAvailabilityPercentage = Math.round(100 - stockOutPercentage);
                this.totalIncidence = reg_obj.total_incidence;
                this.stockOutIncidence = reg_obj.total_stockoutincidence;
                this.reported = reg_obj.reported.split(" ")[2] + " " + reg_obj.reported.split(" ")[3];
            } else {
                this.y = 0;
                this.value = 1;
                this.totalIncidence = 0;
                this.stockOutIncidence = 0;
                this.stockAvailabilityPercentage = 0;
            }
        });

        var options = {
            chart: {
                renderTo: 'latestReportedCommoditiesStockStatus',
                events: {
                    drilldown: function(e) {
                        if (!e.seriesOptions) {
                            drillDownState = true;
                            var chart = this;
                            areaName = 'District';
                            chart.showLoading('<i class="icon-spinner icon-spin icon-3x"></i>');
                            var region_data = _.where(districtValues, {region_name: e.point.name });

                             $rootScope.loadStockOutRateTrendForTracer(params, e.point.name);

                              var nationalTrendData = groupDataInQuarter(stockOutRateTrendForTracerData, e.point.name);


                            var regions = _.uniq(_.pluck(data, 'drilldown'));
                            drillDownIndex = regions.indexOf(e.point.name);
                            data = Highcharts.geojson(filterMap(districtMap, e.point.name));

                            $.each(data, function(i) {
                                var sof_obj = _.where(districtValues, {
                                    region_name: e.point.name,
                                    district_name: data[i].name
                                });

                                if (sof_obj.length > 0) {
                                    var reg_obj = _.where(districtValues, {
                                        district_name: sof_obj[0].district_name
                                    })[0];
                                    this.value = sof_obj[0].stockinhand;
                                    var stockOutPercentage = Math.round((reg_obj.stockoutincidence / reg_obj.totalincidence) * 100);

                                    this.y = stockOutPercentage;

                                    this.value = ((100 - stockOutPercentage) <= 0) ? 1 : 100 - stockOutPercentage;
                                    this.totalIncidence = reg_obj.totalincidence;
                                    this.stockOutIncidence = reg_obj.stockoutincidence;
                                    this.stockAvailabilityPercentage = Math.round(100 - stockOutPercentage);
                                    this.reported = reg_obj.reported.split(" ")[2] + " " + reg_obj.reported.split(" ")[3];

                                } else {
                                    this.y = 0;
                                    this.value = 1;
                                    this.totalIncidence = 0;
                                    this.stockOutIncidence = 0;
                                    this.stockAvailabilityPercentage = 0;
                                }
                            });
                            getLatestStockAvailabilityDrillDownSummary(districtValues, params, e.point.name);

                            chart.addSeriesAsDrilldown(e.point, {
                                name: e.point.name,
                                data: data,
                                dataLabels: {
                                    enabled: true,
                                    format: '{point.name}'
                                }
                            });

                            $scope.stockAvailabilityTrend('stockAvailabilityTrend', nationalTrendData.region);
                           var imbalanceData = getImbalanceData(region_data);
                            $scope.stockImbalanceChart('stockImbalanceByLocation', imbalanceData.imbalance);
                             chart.hideLoading();
                        }
                        this.setTitle(null, {
                            text: e.point.name
                        });
                    },
                    drillup: function() {
                        areaName = 'Region';
                        drillDownState = false;
                        getLatestStockAvailabilityStatusSummary(regionData, params);
                        this.setTitle(null, {
                            text: 'Click Region  to see district'
                        });
                      var imbalanceData = getImbalanceData(districtValues);
                      $scope.stockImbalanceChart('stockImbalanceByLocation', imbalanceData.imbalance);

                        var nationalTrendData = groupDataInQuarter(stockOutRateTrendForTracerData, "Tz");
                        $scope.stockAvailabilityTrend('stockAvailabilityTrend', nationalTrendData.region);
                    }
                }
            },
            title: {
                text: "Percentage of Stock Availability by Location as Reported"
            },
            subtitle: {
                text: 'Click Region  to see district'
            },
            credits: {
                enabled: false
            },
            colorAxis: {
                min: 1,
                type: 'logarithmic',
                stops: [
                    [0, '#EC6B58'],
                    [0.2, Highcharts.color('#EC6B58').brighten(0.6).get()],
                    [0.3, Highcharts.color('#EC6B58').brighten(0.5).get()],
                    [0.4, Highcharts.color('#EC6B58').brighten(0.4).get()],
                    [0.5, Highcharts.color('#2abb9b').brighten(0.7).get()],
                    [0.6, Highcharts.color('#2abb9b').brighten(0.6).get()],
                    [0.7, Highcharts.color('#2abb9b').brighten(0.4).get()],
                    [0.8, Highcharts.color('#2abb9b').brighten(0.3).get()],
                    [0.9, Highcharts.color('#2abb9b').brighten(0.2).get()],
                    [1, '#2abb9b']
                ]
            },
            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },
            legend: {
                title: {
                    text: 'Stock Availability by Percentage(%)',
                    style: {
                        color: ( // theme
                            Highcharts.defaultOptions &&
                            Highcharts.defaultOptions.legend &&
                            Highcharts.defaultOptions.legend.title &&
                            Highcharts.defaultOptions.legend.title.style &&
                            Highcharts.defaultOptions.legend.title.style.color
                        ) || 'black'
                    }
                }
            },
            tooltip: {
                backgroundColor: 'none',
                borderWidth: 0,
                shadow: false,
                useHTML: true,
                padding: 0,
                pointFormat: '<span class="f32">' +
                    '</span></span> {point.name}<br>' +
                    '<span style="font-size:30px"> {point.stockAvailabilityPercentage}%</span><br>' +
                    '</span></span> As of {point.reported}<br>',
                positioner: function() {
                    return {
                        x: 0,
                        y: 250
                    };
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
                data: data,
                animation: {
                    duration: 1000
                },
                joinBy: ['iso-a3', 'code3'],
                name: 'Tanzania',
                dataLabels: {
                    enabled: true,
                    format: '{point.properties.postal-code}'
                }
            }],
            drilldown: {
                activeDataLabelStyle: {
                    color: '#FFFFFF',
                    textDecoration: 'none',
                    textShadow: '0 0 3px #000000'
                },
                drillUpButton: {
                    relativeTo: 'spacingBox',
                    position: {
                        x: 0,
                        y: 60
                    }
                }
            }
        };

        var chart = Highcharts.mapChart(options);
        if (drillDownState) {
            chart.series[0].data[drillDownIndex].doDrilldown(undefined, undefined, 1);
        }
    }


    function get_sum(obj, obj_name) {
        return _.pluck(obj, obj_name).reduce(function(acc, val) {
            return acc + val;
        }, 0);
    }

    function getRegionDataFromDistrictData(data) {
        var regions = _.uniq(_.pluck(data, 'region_name'));
        var reg_arry = [];
        for (var x in regions) {
            var sof_obj = _.where(data, {
                region_name: regions[x]
            });

            var total_stockoutincidence = get_sum(sof_obj, 'stockoutincidence');
            var total_incidence = get_sum(sof_obj, 'totalincidence');

            var reg_obj = {
                product: sof_obj[0].product,
                reported: sof_obj[0].reported,
                total_stockoutincidence: total_stockoutincidence,
                total_incidence: total_incidence,
                region_name: regions[x]
            };

            reg_arry.push(reg_obj);
        }

        data.region = reg_arry;

        return data;
    }

    function groupDataInQuarter(data, gzLevel) {

        var first_quater = {
            startDate: new Date(new Date().getFullYear() + "-01-01"),
            endDate: new Date(new Date().getFullYear() + "-03-31"),
            name: "March"

        };
        var second_quater = {
            startDate: new Date(new Date().getFullYear() + "-04-01"),
            endDate: new Date(new Date().getFullYear() + "-06-31"),
            name: "June"
        };
        var third_quater = {
            startDate: new Date(new Date().getFullYear() + "-07-01"),
            endDate: new Date(new Date().getFullYear() + "-09-31"),
            name: "Sept"
        };
        var fourth_quater = {
            startDate: new Date(new Date().getFullYear() + "-10-01"),
            endDate: new Date(new Date().getFullYear() + "-12-31"),
            name: "Dec"
        };

        var allQuater = [];
        allQuater.push(first_quater);
        allQuater.push(second_quater);
        allQuater.push(third_quater);
        allQuater.push(fourth_quater);

console.log(allQuater);

if(gzLevel != "Tz")
{

   data = _.where(data, {
                    region_name: gzLevel
                });
}


        var reg_arry = [];

        for (var x in allQuater) {

         //   var sof_obj = data.filter(function(a) {
                aDate = new Date(data.reported);
                var sof_obj = aDate >= allQuater[x].startDate && aDate <= allQuater[x].endDate;
         //   });

            var percentage = get_sum(sof_obj, 'percentage');
            var reg_obj;
            if (sof_obj.length > 0) {
                 reg_obj = {
                    product: sof_obj[0].product,
                    reported: allQuater[x].name,
                    percentage: Math.round(100 - (percentage/sof_obj.length))
                };
            } else {
                  reg_obj = {
                    product: "Tracer",
                    reported: allQuater[x].name,
                    total_stockoutincidence: 0,
                    total_incidence: 0,
                    percentage: 0
                };
            }
            reg_arry.push(reg_obj);
        }

        data.region = reg_arry;
        data.region.sort(comp);

        return data;

    }


    function comp(a, b) {
        return new Date(a.reported).getTime() - new Date(b.reported).getTime();
    }

    function prepareValues(data, x, ind) {

        var prepare = (ind === 'stockInHand') ? data[x].total_stockinhand :
            (ind === 'beginningBalance') ? data[x].total_beginningbalance :
            (ind === 'quantityReceived') ? data[x].total_quantityreceived :
            (ind === 'quantityIn') ? data[x].total_quantityin :
            (ind === 'quantityOut') ? data[x].total_quantityout :
            (ind === 'quantityExpiredLostStolen') ? data[x].total_quantityexpiredloststolen :
            (ind === 'consumptionQuantities') ? data[x].total_consumption :
            (ind === 'costOfReceived') ? data[x].total_costofreceived :
            (ind === 'costOfQuantityExpiredLostStolen') ? data[x].total_costofquantityexpiredloststolen :
            (ind === 'consumptionCost') ? data[x].total_costofquantitydispensed :
            (ind === 'overStockIncidence') ? data[x].total_overstockincidence :
            (ind === 'totalIncidence') ? data[x].total_incidence :
            (ind === 'overStockQuantity') ? data[x].total_overstockquantity :
            (ind === 'stockOutQuantity') ? data[x].total_stockoutquantity :
            (ind === 'stockAvailability') ? data[x].total_stockinhand :
            data[x].total_stockinhand;

        return prepare;
    }


    function filterMap(map, region_name) {
        var x = map.feature.filter(function(a) {
            return a.properties.ADM1 === region_name;
        });
        map.features = x;
        return map;

    }


    //Product Filter
    $scope.OnFilterChangeByProduct = function(filter) {

        if (filter.indicator == 'allTracerProducts') {
            $rootScope.loadLatestReportedStockStatus(filter);
            $rootScope.loadStockOutRateTrendForTracer(filter, "Tz");
         //   $rootScope.loadStockImbalance(filter);
            $scope.productFilter = false;
        } else if (filter.indicator == 'productFilter') {
            $scope.productFilter = true;
            if (filter.product && filter.program) {
                $rootScope.loadLatestReportedStockStatus(filter);
                $rootScope.loadStockOutRateTrendForTracer(filter, "Tz");
             //   $rootScope.loadStockImbalance(filter);
            }
        }
    };


    function getLatestStockAvailabilityStatusSummary(data, params) {
        var reg_arry = [];

        var total_incidence = get_sum(data, 'total_incidence');
        var total_stockoutincidence = get_sum(data, 'total_stockoutincidence');
         var availability_percentage = Math.round(100 - ((total_stockoutincidence/total_incidence ) * 100));


        var full_items = _.filter(data, function(item) {
            var percentage = ((item.total_stockoutincidence / item.total_incidence) * 100);
            if (percentage === 0) {
                return item;
            }
        });

        var partial_items = _.filter(data, function(item) {
            var percentage = ((item.total_stockoutincidence / item.total_incidence) * 100);
            if (percentage > 0 && percentage < 100) {
                return item;
            }
        });

        var total_stockout_items = _.filter(data, function(item) {
            var percentage = ((item.total_stockoutincidence / item.total_incidence) * 100);
            if (percentage === 100) {
                return item;
            }
        });

        reg_arry.push({
            key: "Full Stock Availability",
            value: full_items.length + ' Region(s)'
        });
        reg_arry.push({
            key: "Partial Stock Availability",
            value: partial_items.length + ' Region(s)'
        });
        reg_arry.push({
            key: "Total Stockout",
            value: total_stockout_items.length + ' Region(s)'
        });
        reg_arry.push({
                    key: "Availability Percentage",
                    value: availability_percentage + "%"
        });
        $rootScope.summary.content = reg_arry;
        $rootScope.$apply();
        return;
    }


    function getLatestStockAvailabilityDrillDownSummary(data, params, region_name) {


        data = _.where(data, {
            region_name: region_name
        });


var total_incidence = get_sum(data, 'totalincidence');
        var total_stockoutincidence = get_sum(data, 'stockoutincidence');
         var availability_percentage = Math.round(100 - ((total_stockoutincidence/total_incidence ) * 100));
        var reg_arry = [];


        var full_items = _.filter(data, function(item) {
            var percentage = ((item.stockoutincidence / item.totalincidence) * 100);
            if (percentage === 0) {
                return item;
            }
        });

        var partial_items = _.filter(data, function(item) {
            var percentage = ((item.stockoutincidence / item.totalincidence) * 100);
            if (percentage > 0 && percentage < 100) {
                return item;
            }
        });

        var total_stockout_items = _.filter(data, function(item) {
            var percentage = ((item.stockoutincidence / item.totalincidence) * 100);
            if (percentage === 100) {
                return item;
            }
        });

        reg_arry.push({
            key: "Full Stock Availability",
            value: full_items.length + ' Districts'
        });
        reg_arry.push({
            key: "Partial Stock Availability",
            value: partial_items.length + ' Districts'
        });
        reg_arry.push({
            key: "Total Stockout",
            value: total_stockout_items.length + ' Districts'
        });
                reg_arry.push({
                            key: "Availability Percentage",
                            value: availability_percentage + "%"
                });
        $rootScope.summary.content = reg_arry;
        $rootScope.$apply();
        return;
    }



    // Stock Out Rate Graph
    $rootScope.loadStockOutRateTrendForTracer = function(params, gzLevel) {

        params.year = new Date().getFullYear();

     //  params.year = 2017;

        if (params.indicator == 'allTracerProducts') {
            GetStockOutRateTrendOfTracerProductsData.get(params).then(function(data) {

            stockOutRateTrendForTracerData = data;
                var nationalTrendData = groupDataInQuarter(data, gzLevel);

                $scope.stockAvailabilityTrend('stockAvailabilityTrend', nationalTrendData.region);
            });
        } else if (params.indicator == 'productFilter') {
            GetStockOutRateTrendOfProductsData.get(params).then(function(data) {
            stockOutRateTrendForTracerData = data;
                var nationalTrendData = groupDataInQuarter(data, gzLevel);

                $scope.stockAvailabilityTrend('stockAvailabilityTrend', nationalTrendData.region);
            });
        }
    };

    $scope.stockAvailabilityTrend = function(chartTypeId, nationalTrendData) {
        Highcharts.chart(chartTypeId, {
            chart: {
                type: 'area'
            },
            credits: {
                enabled: false
            },
            title: {
                text: 'Stock Availability Trend'
            },
            xAxis: {
                categories: ['March', 'June', 'Sept', 'Dec'],
                title: {
                    enabled: false
                }
            },
            yAxis: {
                title: {
                    text: 'Stock Availability'
                },
                labels: {
                    formatter: function() {
                        return this.value + '%';
                    }
                }
            },
            tooltip: {
                split: true,
                valueSuffix: ' %'
            },
            plotOptions: {
                area: {
                    stacking: 'normal',
                    lineColor: '#000000',
                    color: '#78e08f',
                    lineWidth: 1,
                    marker: {
                        lineWidth: 3,
                        lineColor: '#666666'
                    }
                }
            },
            series: [{
                name: '% of Stock Availability',
                data: _.pluck(nationalTrendData, 'percentage')
            }]
        });
    };


    $scope.stockImbalanceChart = function(chartTypeId, imbalanceData) {
        Highcharts.chart(chartTypeId, {
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
                text: 'Stock Imbalance'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
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
                    },
                    showInLegend: true
                }
            },
            series: [{
                name: 'Status',
                colorByPoint: true,
                data: imbalanceData
            }]
        });

    };


    function getImbalanceData(data) {

        var imbalance_arry = [];

            var overstock_incidence = get_sum(data, 'overstockincidence');
                var overstock_incidence_obj = {
                    name: "Over Stocked",
                    y: overstock_incidence,
                    color: getStatusColor("OS")
                };

            imbalance_arry.push(overstock_incidence_obj);

            var adeliquate_stock_incidence = get_sum(data, 'adeliquatestockincidence');
                var adeliquate_stock_incidence_obj = {
                    name: "Adequately Stocked",
                    y: adeliquate_stock_incidence ,
                    color: getStatusColor("SP")
                };
            imbalance_arry.push(adeliquate_stock_incidence_obj);


      var unknown_incidence = get_sum(data, 'unknownincidence');
                  var unknown_incidence_obj = {
                      name: "Unknown",
                      y: unknown_incidence ,
                      color: getStatusColor("UK")
                  };
              imbalance_arry.push(unknown_incidence_obj);

      var understock_incidence = get_sum(data, 'understockincidence');
                  var understock_incidence_obj = {
                      name: "Under Stocked",
                      y: understock_incidence ,
                      color: getStatusColor("US")
                  };
              imbalance_arry.push(understock_incidence_obj);


      var stockout_incidence = get_sum(data, 'stockoutincidence');
                  var stockout_incidence_obj = {
                      name: "Stock Out",
                      y: stockout_incidence ,
                      sliced: true,
                      selected: true,
                      color: getStatusColor("SO")
                  };
              imbalance_arry.push(stockout_incidence_obj);

        data.imbalance = imbalance_arry;

        return data;

    }


    function getStatusColor(status) {

        switch (status) {
            case "SO":
                return '#EC6B58';
            case "UK":
                return "#bdc3c7";
            case "US":
                return "#F58F84";
            case "SP":
                return '#2abb9b';
            case "OS":
                return "#2574a9";
            default:
                return "#abb7b7";

        }
    }



    function getStatusFullName(status) {

        switch (status) {
            case "SO":
                return "Stock Out";
            case "UK":
                return "Unknown";
            case "US":
                return "Under Stocked";
            case "SP":
                return "Adequately Stocked";
            case "OS":
                return "Over Stocked";
            default:
                return "No Demand";

        }

    }

}