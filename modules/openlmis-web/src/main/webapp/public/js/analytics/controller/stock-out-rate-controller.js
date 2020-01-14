function StockOutRateController($scope, $http, $location, Program, Period, Products, $rootScope, StockOutRateData, StockStatusByLocationData, GetTzDistrictMapData, GetTzRegionMapData, GetStockOutRateByProductData, GetProductById, LatestReportedStockStatusByDistrictData) {

    $scope.year = '';
    $scope.productName = '';
    $rootScope.summary = {
        content: null
    };
    $scope.productFilter = false;
    var drillDownState = false;
    var drillDownIndex = 0;


    $rootScope.loadLatestReportedStockStatus = function(params) {
        LatestReportedStockStatusByDistrictData.get(params).then(function(data) {
            GetTzRegionMapData.get(params).then(function(regionMap) {
                GetTzDistrictMapData.get(params).then(function(districtMap) {
                    var dataWithRegion = addRegionData(data);
                    var unit = "Facilities";
                    var zeroColor = "#e74c3c"; //Red
                    var title = params.indicator === 'allTracerProducts' ? 'Percentage of Tracer Availability' : 'Percentage of Availability';
                    districtMap.feature = districtMap.features;
                    loadLatestReportedStockStatusMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                });

            });
        });
    };



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
                this.value = stockOutPercentage;
                this.stockAvailabilityPercentage = 100 - stockOutPercentage;
                this.totalIncidence = reg_obj.total_incidence;
                this.stockOutIncidence = reg_obj.total_stockoutincidence;
                this.reported = reg_obj.reported;


                if (stockOutPercentage == 100) {
                    this.color = '#EC6B58'; //Red
                } else if (stockOutPercentage < 100 && stockOutPercentage > 50) {
                    this.color = '#F58F84'; //Mid-Red
                } else if (stockOutPercentage <= 50 && stockOutPercentage > 20) {
                    this.color = '#65c6bb'; //Mid Green
                } else if (stockOutPercentage <= 20 ) {
                    this.color = '#2abb9b'; //Green
                }
//                else if (stockOutPercentage === 0) {
//                    this.color = '#1BBB9C'; //Green
//                }
            } else {
                this.y = 0;
                this.color = '#b2bec3';
                this.value = 0;
                this.totalIncidence = 0;
                this.stockOutIncidence = 0;

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

                                    this.value = stockOutPercentage;
                                    this.totalIncidence = reg_obj.totalincidence;
                                    this.stockOutIncidence = reg_obj.stockoutincidence;
                                    this.stockAvailabilityPercentage = 100 - stockOutPercentage;
                                    this.reported = reg_obj.reported;

                              if (stockOutPercentage == 100) {
                                   this.color = '#EC6B58'; //Red
                               } else if (stockOutPercentage < 100 && stockOutPercentage > 50) {
                                   this.color = '#F58F84'; //Mid-Red
                               } else if (stockOutPercentage <= 50 && stockOutPercentage > 20) {
                                   this.color = '#65c6bb'; //Mid Green
                               } else if (stockOutPercentage <= 20 ) {
                                   this.color = '#2abb9b'; //Green
                               } }else {
                                    this.y = 0;
                                    this.color = '#b2bec3';
                                    this.value = 0;
                                    this.totalIncidence = 0;
                                    this.stockOutIncidence = 0;
                                }
                            });
                            getLatestStockAvailabilityDrillDownSummary(districtValues, params, e.point.name);
                            chart.hideLoading();

                            chart.addSeriesAsDrilldown(e.point, {
                                name: e.point.name,
                                data: data,
                                dataLabels: {
                                    enabled: true,
                                    format: '{point.name}'
                                }
                            });

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
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom'
            },
            colorAxis: {
                dataClasses: [{
                        color: '#EC6B58',
                        name: 'Total Stockout'
                    }, {
                        color: '#F58F84',
                        name: '<50 % Stock Availability'
                    },
                    {
                        color: '#4ecdc4',
                        name: '51% - 80% Stock Availability'
                    },
                    {
                        color: '#2abb9b', //'#5DBCD2',
                        name: '>80% Stock Availability'
                    }
                ]
            },
            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
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




    $rootScope.loadStockOutRate = function(params) {
        StockStatusByLocationData.get(params).then(function(data) {
            GetTzRegionMapData.get(params).then(function(regionMap) {
                GetTzDistrictMapData.get(params).then(function(districtMap) {
                    var dataWithRegion = addRegionData(data);
                    switch (params.indicator) {
                        case "stockAvailability":
                            var title = params.productName + " Stock Avaibility by Location, " + params.periodName + ", " + params.year;
                            var unit = "Facilities";
                            var zeroColor = "#e74c3c"; //Red
                            loadCommoditiesMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        case "consumptionQuantities":
                            title = params.productName + " Stock Consumption Quantity by Location, " + params.periodName + ", " + params.year;
                            unit = "Units";
                            zeroColor = "#b2bec3"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit), zeroColor;
                            return;
                        case "stockInHand":
                            title = params.productName + " Stock on Hand Quantity by Location, " + params.periodName + ", " + params.year;
                            unit = "Units";
                            zeroColor = "#e74c3c"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        case "consumptionCost":
                            title = params.productName + " Cost of Reported consumption by Location, " + params.periodName + ", " + params.year;
                            unit = "Tsh";
                            zeroColor = "#b2bec3"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        case "quantityReceived":
                            title = params.productName + " Quantity Received from MSD by Location, " + params.periodName + ", " + params.year;
                            unit = "Unit";
                            zeroColor = "#b2bec3"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        case "quantityExpiredLostStolen":
                            title = params.productName + " Quantity Lost/Expired/Stolen by Location, " + params.periodName + ", " + params.year;
                            unit = "Unit";
                            zeroColor = "#b2bec3"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        case "costQuantityExpiredLostStolen":
                            title = params.productName + " Cost of Quantity Lost/Expired/Stolen by Location, " + params.periodName + ", " + params.year;
                            unit = "Tsh";
                            zeroColor = "#b2bec3"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        case "overStockQuantity":
                            title = params.productName + " OverStock Quantity by Location, " + params.periodName + ", " + params.year;
                            unit = "Unit";
                            zeroColor = "#b2bec3"; //normal
                            loadCommoditiesConsumptionMap(regionMap, districtMap, computeMapData(dataWithRegion.region, params.indicator), params, data, dataWithRegion.region, title, unit, zeroColor);
                            return;
                        default:
                            return;
                    }
                });

            });
        });
    };


    $rootScope.loadStockOutRateTrend = function(params) {
        var productName = "";
        GetProductById.get({
            id: parseInt(params.product, 10)
        }, function(productData) {
            productName = productData.productDTO.product.primaryName;
        });

        GetStockOutRateByProductData.get(params).then(function(data) {
            loadStockOutRateTrend(data, params, productName);
        });
    };

    function loadStockOutRateTrend(data, params, productName) {
        Highcharts.chart('stockOutRateTrend', {

            title: {
                text: 'Stock Out Rate for ' + productName + ' on ' + params.year + '',
            },

            subtitle: {
                text: ''
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: _.uniq(_.pluck(data, 'processing_period_name'))
            },
            yAxis: {
                title: {
                    text: 'Percentage'
                },
                min: 0,
                //                                        plotLines: [{
                //                                                     color: '#FF0000',
                //                                                     width: 2,
                //                                                     value: .5 // Need to set this probably as a var.
                //                                                 }]
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },

            plotOptions: {
                mappie: {
                    borderColor: 'rgba(255,255,255,0.4)',
                    borderWidth: 1,
                    tooltip: {
                        headerFormat: ''
                    }
                },
                series: {
                    label: {
                        connectorAllowed: false
                    }
                }
            },


            series: [{
                name: productName,
                data: _.uniq(_.pluck(data, 'percentage'))
            }],

            responsive: {
                rules: [{
                    condition: {
                        maxWidth: 500
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
            }

        });

    }

    function loadCommoditiesMap(regionMap, districtMap, values, params, districtValues, regionData, title, unit, zeroColor) {

        var data = Highcharts.geojson(regionMap);
        getSummary(regionData, params.indicator);
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
                if (data_obj[0][1] > 0) {
                    this.y = data_obj[0][1];
                    this.color = '#16a085';
                    this.value = data_obj[0][1];
                    this.totalIncidence = reg_obj.total_incidence;
                    this.stockOutIncidence = reg_obj.total_stockoutincidence;
                } else {
                    this.y = 0;
                    this.color = zeroColor;
                    this.value = 0;
                    this.totalIncidence = reg_obj.total_incidence;
                    this.stockOutIncidence = reg_obj.total_stockoutincidence;
                }
            } else {
                this.y = 0;
                this.color = '#b2bec3';
                this.value = 0;
                this.totalIncidence = 0;
                this.stockOutIncidence = 0;
            }
        });

        $('#commodities').highcharts('Map', {
            chart: {
                events: {
                    drilldown: function(e) {
                        if (!e.seriesOptions) {
                            var chart = this;
                            chart.showLoading('<i class="icon-spinner icon-spin icon-3x"></i>');
                            GetTzDistrictMapData.get(params).then(function(mapDis) {

                                data = Highcharts.geojson(filterMap(mapDis, e.point.name));
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
                                        if (sof_obj[0].stockinhand > 0) {
                                            this.y = sof_obj[0].stockinhand;
                                            this.color = '#16a085';
                                            this.value = sof_obj[0].stockinhand;
                                            this.totalIncidence = reg_obj.totalincidence;
                                            this.stockOutIncidence = reg_obj.stockoutincidence;
                                        } else {
                                            this.y = 0;
                                            this.color = '#e74c3c';
                                            this.value = 0;
                                            this.totalIncidence = reg_obj.totalincidence;
                                            this.stockOutIncidence = reg_obj.stockoutincidence;
                                        }
                                    } else {
                                        this.y = 0;
                                        this.color = '#b2bec3';
                                        this.value = 0;
                                        this.totalIncidence = 0;
                                        this.stockOutIncidence = 0;
                                    }
                                });
                                chart.hideLoading();
                                getDrillDownSummary(districtValues, params.indicator, e.point.name);
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
                        this.setTitle(null, {
                            text: e.point.name
                        });
                    },
                    drillup: function() {
                        this.setTitle(null, {
                            text: 'Tanzania'
                        });
                    }
                }
            },
            title: {
                text: title
            },
            credits: {
                enabled: false
            },
            subtitle: {
                text: 'Tanzania',
                floating: true,
                align: 'right',
                y: 50,
                style: {
                    fontSize: '16px'
                }
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'middle'
            },
            colorAxis: {
                dataClasses: [{
                        color: '#e74c3c',
                        name: 'Stock out'
                    }, {
                        color: '#16a085',
                        name: 'Available'
                    },
                    {
                        color: '#b2bec3',
                        name: 'Not Reported'
                    }
                ]
                //                min: 0,
                //                minColor: '#F4C2C2',
                //                maxColor: (colorSelection === undefined) ? '#FF0000' : colorSelection
            },
            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },
            tooltip: {
                formatter: function() {
                    return 'Region: ' + this.point.name +
                        '<br/>Reported Facilities: ' + this.point.totalIncidence +
                        '<br/>Stock out Facilities: ' + this.point.stockOutIncidence;
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
        });
    }

    function loadCommoditiesConsumptionMap(regionMap, districtMap, values, params, districtValues, regionData, title, unit, zeroColor) {

        var data = Highcharts.geojson(regionMap);
        getSummary(regionData, params.indicator);
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
                if (data_obj[0][1] > 0) {
                    this.value = data_obj[0][1];
                } else {
                    this.value = 0;
                    this.color = zeroColor;
                }
            } else {
                this.value = 0;
                this.color = '#E6E7E8';
            }
        });
        $('#commodities').highcharts('Map', {
            chart: {
                events: {
                    drilldown: function(e) {
                        if (!e.seriesOptions) {
                            var chart = this;
                            chart.showLoading('<i class="icon-spinner icon-spin icon-3x"></i>');
                            GetTzDistrictMapData.get(params).then(function(mapDis) {

                                data = Highcharts.geojson(filterMap(mapDis, e.point.name));
                                $.each(data, function(i) {
                                    var sof_obj = _.where(districtValues, {
                                        region_name: e.point.name,
                                        district_name: data[i].name
                                    });
                                    if (sof_obj.length > 0) {
                                        this.value = prepareValuesForDrillDown(sof_obj, 0, params.indicator);
                                        if (this.value === 0) {
                                            this.color = '#e74c3c';
                                        }
                                    } else {
                                        this.value = 0;
                                        this.color = '#E6E7E8';
                                    }
                                });
                                chart.hideLoading();
                                getDrillDownSummary(districtValues, params.indicator, e.point.name);
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
                        this.setTitle(null, {
                            text: e.point.name
                        });
                    },
                    drillup: function() {
                        getSummary(regionData, params.indicator);
                        this.setTitle(null, {
                            text: 'Tanzania'
                        });
                    }
                }
            },
            title: {
                text: title
            },
            credits: {
                enabled: false
            },
            subtitle: {
                text: 'Tanzania',
                floating: true,
                align: 'right',
                y: 50,
                style: {
                    fontSize: '16px'
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },
            colorAxis: {
                min: 0,
                minColor: '#4b8e8d',
                maxColor: '#396362'
            },
            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },
            tooltip: {
                formatter: function() {
                    return this.point.name +
                        '<br/>' + this.point.value + ' ' + unit;
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
        });
    }

    function createSeries(data) {
        var pp_name = _.uniq(_.pluck(data, 'processing_period_name'));
        var products_group_name = _.uniq(_.pluck(data, 'name'));

        var series_array = [];

        for (var y in products_group_name) {
            var sof_obj = _.where(
                data, {
                    name: products_group_name[y]
                });

            var data_arr = [];
            for (var z in sof_obj) {

                var data_series_obj = {
                    name: sof_obj[z].processing_period_name,
                    y: sof_obj[z].percentage
                };

                data_arr.push(data_series_obj);
            }
            var series_obj = {
                name: products_group_name[y],
                data: data_arr
            };
            series_array.push(series_obj);
        }

        return series_array;

    }

    function get_sum(obj, obj_name) {
        return _.pluck(obj, obj_name).reduce(function(acc, val) {
            return acc + val;
        }, 0);
    }

    function addRegionData(data) {
        var regions = _.uniq(_.pluck(data, 'region_name'));
        var reg_arry = [];
        for (var x in regions) {
            var sof_obj = _.where(data, {
                region_name: regions[x]
            });

            var total_stockinhand = get_sum(sof_obj, 'stockinhand');
            var total_beginningbalance = get_sum(sof_obj, 'beginningbalance');
            var total_quantityreceived = get_sum(sof_obj, 'quantityreceived');
            var total_quantityin = get_sum(sof_obj, 'quantityin');
            var total_quantityout = get_sum(sof_obj, 'quantityout');
            var total_quantityexpiredloststolen = get_sum(sof_obj, 'quantityexpiredloststolen');
            var total_consumption = get_sum(sof_obj, 'consumption');
            var total_costofreceived = get_sum(sof_obj, 'totalcostofreceived');
            var total_costofquantityexpiredloststolen = get_sum(sof_obj, 'totalcostofquantityexpiredloststolen');
            var total_costofquantitydispensed = get_sum(sof_obj, 'totalcostofquantitydispensed');
            var total_stockoutincidence = get_sum(sof_obj, 'stockoutincidence');
            var total_overstockincidence = get_sum(sof_obj, 'overstockincidence');
            var total_incidence = get_sum(sof_obj, 'totalincidence');
            var total_overstockquantity = get_sum(sof_obj, 'overstockquantity');
            var total_stockoutquantity = get_sum(sof_obj, 'stockoutquantity');


            var reg_obj = {
                product: sof_obj[0].product,
                reported: sof_obj[0].reported,
                total_stockinhand: total_stockinhand,
                total_beginningbalance: total_beginningbalance,
                total_quantityreceived: total_quantityreceived,
                total_quantityin: total_quantityin,
                total_quantityout: total_quantityout,
                total_quantityexpiredloststolen: total_quantityexpiredloststolen,
                total_consumption: total_consumption,
                total_costofreceived: total_costofreceived,
                total_costofquantityexpiredloststolen: total_costofquantityexpiredloststolen,
                total_costofquantitydispensed: total_costofquantitydispensed,
                total_stockoutincidence: total_stockoutincidence,
                total_overstockincidence: total_overstockincidence,
                total_incidence: total_incidence,
                total_overstockquantity: total_overstockquantity,
                total_stockoutquantity: total_stockoutquantity,
                region_name: regions[x]
            };

            reg_arry.push(reg_obj);
        }

        data.region = reg_arry;

        return data;
    }



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


    function prepareValuesForDrillDown(data, x, ind) {

        var prepare = (ind === 'stockInHand') ? data[x].stockinhand :
            (ind === 'consumptionQuantities') ? data[x].consumption :
            (ind === 'quantityReceived') ? data[x].total_quantityreceived :
            (ind === 'quantityIn') ? data[x].total_quantityin :
            (ind === 'quantityOut') ? data[x].total_quantityout :
            (ind === 'quantityExpiredLostStolen') ? data[x].total_quantityexpiredloststolen :
            (ind === 'consumptionQuantities') ? data[x].total_consumption :
            (ind === 'costOfReceived') ? data[x].total_costofreceived :
            (ind === 'costQuantityExpiredLostStolen') ? data[x].total_costofquantityexpiredloststolen :
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

    $scope.onSelected = function(option) {

        $scope.showStockStatus = false;

        if (option === 'Stock Availability By Location') {

            $scope.showStockStatus = true;

        }
        if (option === 'Stock out rate') {

        }
    };

    $scope.$watch('indicator.value', function(value) {

        $rootScope.stockIndicator = value;

    });


    $scope.onFilterChange = function(filter) {

        var programName = '';
        Program.get({
            id: parseInt($location.search().program, 10)
        }, function(da) {
            programName = da.program.name;

            var periodName = '';
            Period.get({
                id: parseInt($location.search().period, 10)
            }, function(da) {

                periodName = da.period.name;

                Products.get({
                    id: parseInt($location.search().product, 10)
                }, function(da) {

                    filter.productName = da.productDTO.product.fullName;
                    filter.programName = programName;
                    filter.periodName = periodName;

                    $scope.$parent.params = $location.search();

                    $rootScope.loadStockOutRate(filter);
                    //  $rootScope.loadLatestReportedStockStatus(filter);
                });

            });
        });

    };

    $scope.OnFilterChangeByProduct = function(filter) {

        if (filter.indicator == 'allTracerProducts') {
            $rootScope.loadLatestReportedStockStatus(filter);
            $scope.productFilter = false;
        } else if (filter.indicator == 'productFilter') {
            $scope.productFilter = true;
            if (filter.product && filter.program) {
                $rootScope.loadLatestReportedStockStatus(filter);
            }
        }
    };


    function getLatestStockAvailabilityStatusSummary(data, params) {
        var reg_arry = [];
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
        $rootScope.summary.content = reg_arry;
        $rootScope.$apply();
        return;
    }


    function getSummary(data, indicator) {
        var reg_arry = [];
        var total_stockoutincidence;
        var total_incidence;
        var max_obj;
        var min_obj;
        switch (indicator) {
            case "stockAvailability":

                total_stockoutincidence = _.uniq(_.pluck(data, 'total_stockoutincidence'));
                total_incidence = _.uniq(_.pluck(data, 'total_incidence'));
                max_obj = {
                    key: "Total Facilities with stockout",
                    value: _.reduce(total_stockoutincidence, function(a, b) {
                        return a + b;
                    }, 0)
                };
                min_obj = {
                    key: "Total reported Facilities",
                    value: _.reduce(total_incidence, function(a, b) {
                        return a + b;
                    }, 0)
                };

                reg_arry.push(max_obj);
                reg_arry.push(min_obj);
                $rootScope.summary.content = reg_arry;
                $rootScope.$apply();
                return;
            case "consumptionQuantities":


                maxItem = _.max(data, function(data) {
                    return data.total_consumption;
                });
                minItem = _.min(data, function(data) {
                    return data.total_consumption;
                });
                total_consumption = _.uniq(_.pluck(data, 'total_consumption'));

                max_obj = {
                    key: "Max Consumption",
                    value: maxItem.region_name + " ( " + maxItem.total_consumption + " Units)"
                };
                min_obj = {
                    key: "Min Consumption",
                    value: minItem.region_name + " (" + minItem.total_consumption + " Units)"
                };
                total_obj = {
                    key: "Total Reported Consumption",
                    value: _.reduce(total_consumption, function(a, b) {
                        return a + b;
                    }, 0) + " Units"
                };

                reg_arry.push(max_obj);
                reg_arry.push(min_obj);
                reg_arry.push(total_obj);
                $rootScope.summary.content = reg_arry;
                $rootScope.$apply();
                return;
            case "stockInHand":

                maxItem = _.max(data, function(data) {
                    return data.total_stockinhand;
                });
                minItem = _.min(data, function(data) {
                    return data.total_stockinhand;
                });
                total_stockinhand = _.uniq(_.pluck(data, 'total_stockinhand'));

                max_obj = {
                    key: "Max Stock on hand",
                    value: maxItem.region_name + " ( " + maxItem.total_stockinhand + " Units)"
                };
                min_obj = {
                    key: "Min stock on hand",
                    value: minItem.region_name + " (" + minItem.total_stockinhand + " Units)"
                };
                total_obj = {
                    key: "Total Reported Consumption",
                    value: _.reduce(total_stockinhand, function(a, b) {
                        return a + b;
                    }, 0) + " Units"
                };

                reg_arry.push(max_obj);
                reg_arry.push(min_obj);
                reg_arry.push(total_obj);
                $rootScope.summary.content = reg_arry;
                $rootScope.$apply();
                return;
            default:
                return;
        }
    }


    function getLatestStockAvailabilityDrillDownSummary(data, params, region_name) {


        data = _.where(data, {
            region_name: region_name
        });

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
        $rootScope.summary.content = reg_arry;
        $rootScope.$apply();
        return;
    }


    function getDrillDownSummary(data, indicator, region_name) {

        var reg_arry = [];
        var total_stockoutincidence;
        var total_incidence;
        var max_obj;
        var min_obj;
        var maxItem;
        var minItem;
        var total_ob;
        data = _.where(data, {
            region_name: region_name
        });
        switch (indicator) {
            case "stockAvailability":


                total_stockoutincidence = _.uniq(_.pluck(data, 'stockoutincidence'));
                total_incidence = _.uniq(_.pluck(data, 'totalincidence'));
                max_obj = {
                    key: "Total Facilities with stockout",
                    value: _.reduce(total_stockoutincidence, function(a, b) {
                        return a + b;
                    }, 0)
                };
                min_obj = {
                    key: "Total reported Facilities",
                    value: _.reduce(total_incidence, function(a, b) {
                        return a + b;
                    }, 0)
                };

                reg_arry.push(max_obj);
                reg_arry.push(min_obj);
                $rootScope.summary.content = reg_arry;
                return;
            case "consumptionQuantities":


                maxItem = _.max(data, function(data) {
                    return data.consumption;
                });
                minItem = _.min(data, function(data) {
                    return data.consumption;
                });
                total_consumption = _.uniq(_.pluck(data, 'consumption'));

                max_obj = {
                    key: "Max Consumption",
                    value: maxItem.district_name + " ( " + maxItem.consumption + " Units)"
                };
                min_obj = {
                    key: "Min Consumption",
                    value: minItem.district_name + " (" + minItem.consumption + " Units)"
                };
                total_obj = {
                    key: "Total Reported Consumption",
                    value: _.reduce(total_consumption, function(a, b) {
                        return a + b;
                    }, 0) + " Units"
                };

                reg_arry.push(max_obj);
                reg_arry.push(min_obj);
                reg_arry.push(total_obj);
                $rootScope.summary.content = reg_arry;
                return;
            case "stockInHand":


                maxItem = _.max(data, function(data) {
                    return data.stockinhand;
                });
                minItem = _.min(data, function(data) {
                    return data.stockinhand;
                });
                total_consumption = _.uniq(_.pluck(data, 'stockinhand'));

                max_obj = {
                    key: "Max Stock on Hand",
                    value: maxItem.district_name + " ( " + maxItem.consumption + " Units)"
                };
                min_obj = {
                    key: "Min Stock on Hand",
                    value: minItem.district_name + " (" + minItem.consumption + " Units)"
                };
                total_obj = {
                    key: "Total Reported Stock on Hand",
                    value: _.reduce(total_consumption, function(a, b) {
                        return a + b;
                    }, 0) + " Units"
                };

                reg_arry.push(max_obj);
                reg_arry.push(min_obj);
                reg_arry.push(total_obj);
                $rootScope.summary.content = reg_arry;
                return;
            default:
                return;
        }
    }

}