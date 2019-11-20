function StockOutRateController($scope, $http,$location, Program, Period, $rootScope, StockOutRateData, StockStatusByLocationData, GetTzDistrictMapData, GetTzRegionMapData, GetStockOutRateByProductData,GetProductById) {

   $scope.year ='';
   $scope.productName ='';



    $rootScope.loadStockOutRate = function(params) {
    console.log(params);
        StockStatusByLocationData.get(params).then(function(data) {

           console.log($rootScope.stockIndicator);

            GetTzRegionMapData.get(params).then(function(regionMap) {

                GetTzDistrictMapData.get(params).then(function(districtMap) {
                               console.log(districtMap);
                    var dataWithRegion = addRegionData(data);



                    if(params.indicator === 'Stock Availability By Location') {

                    var prepareColors = ($rootScope.stockIndicator === 'overstockpercentage')?'#4bb1cf':($rootScope.stockIndicator==='stockoutpercentage')?'#dd514c':($rootScope.stockIndicator ==='adequatelystockpercentage')?'#5eb95e':($rootScope.stockIndicator === 'understockpercentage')?'#faa732':'gray';


                    loadStockOutRateChart(regionMap, districtMap, computeMapData(dataWithRegion.region,$rootScope.stockIndicator), params, data, prepareColors);

                   } else{

                   loadStockOutRateChart(regionMap, districtMap, computeMapData(dataWithRegion.region, undefined), params, data);

                   }
                });

            });
        });
    };


    $rootScope.loadStockOutRateTrend = function(params) {
    var productName= "";
           GetProductById.get({id:parseInt(params.product,10)}, function(productData){
           productName=productData.productDTO.product.primaryName;
        });

         GetStockOutRateByProductData.get(params).then(function(data) {
                    loadStockOutRateTrend(data, params, productName);
          });
    };

    function loadStockOutRateTrend(data, params, productName) {
        Highcharts.chart('stockOutRateTrend', {

            title: {
                text:  'Stock Out Rate for '+productName+' on '+params.year+'',
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
                min : 0,
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
                series: {
                    label: {
                        connectorAllowed: false
                    }
                }
            },


            series: [{
                name: productName,
                data:  _.uniq(_.pluck(data, 'percentage'))
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

    function loadStockOutRateChart(map, districtMap, values, params, districtValues,colorSelection) {

        var data = Highcharts.geojson(map);
        $.each(data, function(i) {
            var key = this.properties['hc-key'];
            this.drilldown = key;
            var data_obj = values.filter(function(a) {
                return a[0] === key;
            });
            if (data_obj.length > 0) {
                this.value = data_obj[0][1];
            }
        });

        $('#stockOutRate').highcharts('Map', {
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
                                    console.log(e.point.name);
                                    console.log(data[i].name);
                                    console.log(sof_obj);
                                    if (sof_obj.length > 0) {
                                        this.value = sof_obj[0].stockoutpercentage;
                                    } else {
                                        this.value = 0;
                                    }



                                });
                                console.log(data);
                                chart.hideLoading();
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
                text: 'Health Commodities Map'
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
                minColor: '#E6E7E8',
                maxColor: (colorSelection === undefined)?'#005645':colorSelection
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

            var total_understockpercentage = get_sum(sof_obj, 'understockpercentage');

            var average_understockpercentage = total_understockpercentage / sof_obj.length;


            var total_overstockpercentage = get_sum(sof_obj, 'overstockpercentage');
            var average_overstockpercentage = total_overstockpercentage / sof_obj.length;



            var total_adequatelystockpercentage = get_sum(sof_obj, 'adequatelystockpercentage');
            var average_adequatelystockpercentage = total_adequatelystockpercentage / sof_obj.length;



            var total_unknownstockpercentage = get_sum(sof_obj, 'unknownstockpercentage');
            var average_unknownstockpercentage = total_unknownstockpercentage / sof_obj.length;


            var total_stockoutpercentage = get_sum(sof_obj, 'stockoutpercentage');
            var average_stockoutpercentage = total_stockoutpercentage / sof_obj.length;




            var reg_obj = {
                product: sof_obj[0].product,
                understockpercentage: average_understockpercentage,
                overstockpercentage: average_overstockpercentage,
                adequatelystockpercentage: average_adequatelystockpercentage,
                unknownstockpercentage: average_unknownstockpercentage,
                stockoutpercentage: average_stockoutpercentage,
                region_name: regions[x]
            };

            reg_arry.push(reg_obj);
        }

        console.log(reg_arry);

        data.region = reg_arry;

        return data;
    }

    function computeMapData(data,indicator) {
        var data_arr = [];
        var ind;
        if(indicator === undefined) {
        ind = 'stockoutpercentage';
        } else {
        ind = indicator;
        }

        for (var x in data) {
            var obj = [];
            obj.push(data[x].region_name);
            obj.push(prepareValues(data,x,ind));
            data_arr.push(obj);
        }

                console.log(data_arr);
        return data_arr;
    }

    function prepareValues(data,x,ind){
       var prepare = (ind === 'overstockpercentage')?data[x].overstockpercentage:(ind==='stockoutpercentage')?data[x].stockoutpercentage:(ind ==='adequatelystockpercentage')?data[x].adequatelystockpercentage:(ind === 'understockpercentage')?data[x].understockpercentage:data[x].unknownstockpercentage;

       return prepare;
    }

    function filterMap(map, region_name) {

        var x = map.features.filter(function(a) {
            return a.properties.ADM1 === region_name;
        });

        map.features = x;

        return map;

    }


    //    function manuver(params) {
    //
    //        GetTzDistrictMapData.get(params).then(function(data) {
    //
    //            for (var x in data.features) {
    //                data.features[x].properties["hc-key"] = data.features[x].properties.ADM2;
    //                data.features[x].properties["name"] = data.features[x].properties.ADM2;
    //            }
    //        });
    //
    //    }

    $scope.onSelected = function(option) {


    $scope.showStockStatus = false;

     if(option === 'Stock Availability By Location') {

     $scope.showStockStatus = true;

     }

      if (option === 'Stock out rate') {

      }
    };

      $scope.$watch('indicator.value', function(value) {

        $rootScope.stockIndicator = value;

      });


    $scope.onFilterChange = function(filter) {
        $rootScope.loadStockOutRate(filter);
    };

    $scope.OnFilterChangeByProduct = function(filter) {
        $rootScope.loadStockOutRateTrend(filter);
    };


}