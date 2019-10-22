function fundUtilizationController($scope, $rootScope, GetDistrictFundUtilizationData, $location, Program, Period, GetSourceOfFundsByLocationData) {

    $rootScope.loadDistrictFundUtilization = function(params) {

        GetDistrictFundUtilizationData.get(params).then(function(data) {
            var category = _.pluck(data, 'district');

            var other = _.pluck(data, 'other');
            var userFees = _.pluck(data, 'userFees');
            var dataV = [];
            dataV = [{
                name: 'Other',
                data: other
            }, {
                name: 'userFees',
                data: userFees
            }];

            console.log(dataV);

            loadChart(category, dataV);



        });


    };


    function loadChart(category, dataV) {


        new Highcharts.chart('district-fund-ultilization', {
            chart: {
                type: 'column'
            },
            credits: {
                enabled: false
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
                pointFormat: '<td><b>{point.y:.1f}</b></td></tr>'

            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: dataV
        });


    }



    $scope.OnFilterChanged = function() {
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

                $location.search().programName = programName;
                $location.search().periodName = periodName;

                console.log($location.search());


                $scope.$parent.params = $location.search();

                $rootScope.loadHealthCommoditiesFinancing($location.search());


            });


        });

    };


    $rootScope.loadHealthCommoditiesFinancing= function(params) {
         GetSourceOfFundsByLocationData.get(params)
                .then(function(data) {
                console.log(data);
                var title = 'Breakdown of source of funds used to purchase health commodities';
                var value = _.pluck(data, 'Breakdown of source of funds used to purchase health commodities');
                loadHealthCommoditiesFinancingChart(data, title, value, 'spline', 'Year and Month', '# of requisitions');});
        };


        function loadHealthCommoditiesFinancingChart(data, title, values, type, chartName, verticalTitle) {
                Highcharts.chart('sourceOfFundsByZones', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Breakdown of source of funds used to purchase health commodities'
                    },
                    subtitle: {
                        text: 'Click on bar to see more'
                    },
                    xAxis: {
                       type: 'category'
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Tsh'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                            '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0
                        }
                    },
                    series: createSeriesForSourceOfFundsByZones(data),
                    drilldown: {
                        series: getDrillDownsRegionSeries(data)
                    },
                });

            }


            function get_sum(obj) {
                return _.pluck(obj, 'total').reduce(function(acc, val) { return acc + val; }, 0);
                }

                function createSeriesForSourceOfFundsByZones(data) {
                    var zones = _.uniq(_.pluck(data, 'zone_name'));
                    var source_funds = _.uniq(_.pluck(data, 'name'));

                    var fund_array = [];
                    for (var x in source_funds) {
                        var zones_array = [];
                        for (var y in zones) {
                            var sof_obj = _.where(
                                data, {
                                    name: source_funds[x],
                                    zone_name: zones[y]
                                });

                           // var sof_total = _.pluck(sof_obj, 'total').reduce((a, b) => a + b, 0);
                            var sof_total = get_sum(sof_obj);

                            var zone_obj = {
                                name: zones[y],
                                y: sof_total,
                                drilldown: zones[y] + '-' + source_funds[x]
                            };
                            zones_array.push(zone_obj);

                        }

                        var fund_obj = {
                            name: source_funds[x],
                            data: zones_array
                        };
                        fund_array.push(fund_obj);
                    }

                    return fund_array;

                }

                function getDrillDownsRegionSeries(data) {
                    var zones = _.uniq(_.pluck(data, 'zone_name'));
                    var source_funds = _.uniq(_.pluck(data, 'name'));
                    var regions = _.uniq(_.pluck(data, 'region_name'));

                    var fund_array = [];

                    for (var x in source_funds) {
                        var zone_array = [];
                        for (var y in zones) {
                            var region_array = [];
                            for (var z in regions) {
                                var sof_objs = _.where(
                                    data, {
                                        name: source_funds[x],
                                        zone_name: zones[y],
                                        region_name: regions[z]
                                    });

                                if (sof_objs.length > 0) {

                                   // var sof_total = _.pluck(sof_objs, 'total').reduce((a, b) => a + b, 0);
                                    var sof_total = get_sum(sof_obj);

                                    var region_obj = {
                                        name: regions[z],
                                        y: sof_total,
                                        drilldown: regions[z] + '-' + source_funds[x]
                                    };
                                    region_array.push(region_obj);
                                }

                            }
                            if(region_array.length > 0)
                            {
                            var fund_obj = {
                                name: source_funds[x],
                                id: zones[y] + '-' + source_funds[x],
                                data: region_array
                            };
                            fund_array.push(fund_obj);
                            }
                        }
                    }
                    return fund_array.concat(getDrillDownsDistrictSeries(data));

                }


                function getDrillDownsDistrictSeries(data) {
                    var zones = _.uniq(_.pluck(data, 'zone_name'));
                    var regions = _.uniq(_.pluck(data, 'region_name'));
                    var districts = _.uniq(_.pluck(data, 'district_name'));
                    var source_funds = _.uniq(_.pluck(data, 'name'));

                    var fund_array = [];

                    for (var x in source_funds) {
                        var zone_array = [];
                        for (var y in zones) {
                            var region_array = [];
                            for (var z in regions) {
                                var district_array = [];
                                for (var a in districts) {
                                    var sof_objs = _.where(
                                        data, {
                                            name: source_funds[x],
                                            zone_name: zones[y],
                                            region_name: regions[z],
                                            district_name: districts[a]
                                        });

                                    if (sof_objs.length > 0) {

                                       // var sof_total = _.pluck(sof_objs, 'total').reduce((a, b) => a + b, 0);
                                       var sof_total = get_sum(sof_obj);

                                        var district_obj = {
                                            name: districts[a],
                                            y: sof_total
                                        };
                                        district_array.push(district_obj);
                                    }

                                }
                                var fund_obj = {
                                    name: source_funds[x],
                                    id: regions[z] + '-' + source_funds[x],
                                    data: district_array
                                };

                                if (fund_obj.data.length > 0) {
                                    fund_array.push(fund_obj);
                                }
                            }
                        }
                    }

             return fund_array;
                }

    }