function CommoditiesComparisonController($scope, $location, Program, Period, $rootScope, GetTLEAndTLDData) {

//$scope.year = 2019;


    $rootScope.loadCommoditiesComparison = function(params) {
        $scope.year = params.year;
        GetTLEAndTLDData.get(params).then(function(data) {
            loadCommoditiesComparisonTLEvsTLDChart(getConsumptionData(data), data);
        });
    };


    function loadCommoditiesComparisonTLEvsTLDChart(data, ogData) {
    console.log(Highcharts.charts);

        ['mousemove', 'touchmove', 'touchstart'].forEach(function(eventType) {
            document.getElementById('common').addEventListener(
                eventType,
                function(e) {
                    var chart,
                        point,
                        i,
                        event;
                    var container_charts = [];
                    for (var x in Highcharts.charts) {
                    if(Highcharts.charts[x].userOptions) {
                        if (Highcharts.charts[x].userOptions.title.text == 'TLD' || Highcharts.charts[x].userOptions.title.text == 'TLE') {
                            container_charts.push(Highcharts.charts[x]);
                        }
                    }}

                    console.log(container_charts);

                    for (i = 0; i < container_charts.length; i = i + 1) {
                        chart = container_charts[i];

                        // Find coordinates within the chart
                        event = chart.pointer.normalize(e);
                        // Get the hovered point
                        point = chart.series[0].searchPoint(event, true);

                        if (point) {
                            point.highlight(e);
                        }
                    }
                }
            );
        });

        /**
         * Override the reset function, we don't need to hide the tooltips and
         * crosshairs.
         */
        Highcharts.Pointer.prototype.reset = function() {
            return undefined;
        };

        /**
         * Highlight a point by showing tooltip, setting hover state and draw crosshair
         */
        Highcharts.Point.prototype.highlight = function(event) {
            event = this.series.chart.pointer.normalize(event);
            this.onMouseOver(); // Show the hover marker
            this.series.chart.tooltip.refresh(this); // Show the tooltip
            this.series.chart.xAxis[0].drawCrosshair(event, this); // Show the crosshair
        };

        /**
         * Synchronize zooming through the setExtremes event handler.
         */
        function syncExtremes(e) {
            var thisChart = this.chart;

            if (e.trigger !== 'syncExtremes') { // Prevent feedback loop
                Highcharts.each(Highcharts.charts, function(chart) {
                    if (chart !== thisChart) {
                        if (chart.xAxis[0].setExtremes) { // It is null while updating
                            chart.xAxis[0].setExtremes(
                                e.min,
                                e.max,
                                undefined,
                                false, {
                                    trigger: 'syncExtremes'
                                }
                            );
                        }
                    }
                });
            }
        }

        // Get the data. The contents of the data file can be viewed at

        data.datasets.forEach(function(dataset, i) {

            // Add X values
            dataset.data = Highcharts.map(dataset.data, function(val, j) {
                return [data.xData[j], val];
            });

            var chartDiv = document.createElement('div');
            chartDiv.className = 'chart';
            document.getElementById('common').appendChild(chartDiv);


            Highcharts.chart(chartDiv, {
                chart: {
                    marginRight: 40, // Keep all charts left aligned
                    spacingTop: 20,
                    spacingBottom: 20,
                    height: 200, // added height and scroll
                    width: 800,
                    scrollablePlotArea: {
                        minWidth: 200,
                        scrollPositionX: 0
                    }
                },
                title: {
                    text: dataset.name,
                    align: 'left',
                    margin: 0,
                    x: 30
                },
                credits: {
                    enabled: false
                },
                legend: {
                    enabled: false
                },
                xAxis: {
                    crosshair: true,
                    events: {
                        setExtremes: syncExtremes
                    },
                    categories: _.uniq(_.pluck(ogData, 'name'))
                },
                yAxis: {
                    title: {
                        text: null
                    }
                },
                tooltip: {
                    positioner: function() {
                        return {
                            // right aligned
                            x: this.chart.chartWidth - this.label.width - 40,
                            y: 10 // align to title
                        };
                    },
                    borderWidth: 0,
                    backgroundColor: 'none',
                    pointFormat: '{point.y}',
                    headerFormat: '',
                    shadow: false,
                    style: {
                        fontSize: '18px'
                    },
                    valueDecimals: dataset.valueDecimals
                },
                series: [{
                    data: dataset.data,
                    name: dataset.name,
                    type: dataset.type,
                    color: Highcharts.getOptions().colors[i],
                    fillOpacity: 0.3,
                    tooltip: {
                        valueSuffix: ' ' + dataset.unit
                    }
                }]
            });
        });



    }


    function getConsumptionData(data) {

        var period_arr = _.uniq(_.pluck(data, 'name'));


        var tld = _.where(data, {
            productcode: '10010164AB'
        });

        var tld_obj = {
            "name": "TLD",
            "data": _.pluck(tld, 'totalconsumption'),
            "unit": "Units",
            "type": "line",
            "valueDecimals": 1
        };

        var tle = _.where(data, {
            productcode: '10010022AB'
        });


        var tle_obj = {
            "name": "TLE",
            "data": _.pluck(tle, 'totalconsumption'),
            "unit": "Units",
            "type": "line",
            "valueDecimals": 1
        };

        var dataArry = {};
        dataArry.xData = period_arr;

        var datasets = [];
        datasets.push(tld_obj);
        datasets.push(tle_obj);
        dataArry.datasets = datasets;
        return dataArry;
    }


    $scope.onFilterChange = function(filter) {

        for (var x in Highcharts.charts) {
         if(Highcharts.charts[x].userOptions) {
            if (Highcharts.charts[x].userOptions.title.text == 'TLD' || Highcharts.charts[x].userOptions.title.text == 'TLE') {
                Highcharts.charts[x].destroy();
            }
        }
        }
        Highcharts.charts.shift();
        Highcharts.charts.shift();
        Highcharts.charts.shift();
        console.log(Highcharts.charts);
        $rootScope.loadCommoditiesComparison(filter);
    };



}