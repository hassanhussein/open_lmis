function DashboardControllerFunction($scope,RejectionCount, leafletData,RnRStatusSummary,GetNumberOfEmergencyData,GetEmergencyOrderByProgramData,GetPercentageOfEmergencyOrderByProgramData,
                                     ExtraAnalyticDataForRnRStatus,GetTrendOfEmergencyOrdersSubmittedPerMonthData,$routeParams,messageService,GetEmergencyOrderTrendsData,
                                     ngTableParams,$filter,ReportingRate,StockStatusAvailaiblity,ItemFillRate,DailyStockStatus) {

    $scope.reportingRate={};

    $scope.loadRejectionChart = [];
//Provinces with Most Emergency Orders (Past 3 Months)
    function loadPieChart(chartId,dataValues,total) {

        var chart = new Highcharts.Chart({
            chart: {
                renderTo:chartId,
                type: 'pie'

            },
            credits:{
                enabled:false
            },  title: {
                text:'<span style="font-size:20px">'+total+' <br><span style="font-size:10px">TOTAL</span></span>',
                verticalAlign: 'middle',
                floating: true
            },

            plotOptions: {
                pie: {
                    innerSize: '60%'
                }
            },
            series: [{
                data:dataValues}]
        }/*,

            function(chart) { // on complete
                var textX = chart.plotLeft + (chart.plotWidth  * 0.5);
                var textY = chart.plotTop  + (chart.plotHeight * 0.5);

                var span = '<span id="pieChartInfoText" style="position:absolute; text-align:center;">';
                span += '<span style="font-size: 32px">Upper</span><br>';
                span += '<span style="font-size: 16px">Lower</span>';
                span += '</span>';

                $("#addText").append(span);
                span = $('#pieChartInfoText');
                span.css('left', textX + (span.width() * -0.5));
                span.css('top', textY + (span.height() * -0.5));
            }*/);



    }

    GetNumberOfEmergencyData.get(null).then(function (data) {
        var chartId = 'emergencyByRegion';
        var data1 = _.pluck(data,'Number Of EOs');
        var data2 = _.pluck(data,'Province');
        var total = 0;
        total= _.reduce(data1, function(memo, num){ return memo + num; }, 0);
        var dataValues= _.zip(data2,data1);
        loadPieChart(chartId,dataValues,total);
    });

    RejectionCount.get({}, function (data) {
        var reject = _.pluck(data.rejections,'Month');
        var rejectionCount = _.pluck(data.rejections, 'Rejected Count');
        loadTheChart(reject,rejectionCount,'rejectionCountId','line','Rejection Count','RnR Rejection Trends','Rejection Count');

    });

    GetPercentageOfEmergencyOrderByProgramData.get(null).then(function (data) {

        var chartId = 'emergencyByProgram';
        var category = _.pluck(data,'Program Name');
        var value = _.pluck(data,'Emergency');
        loadTheChart(category,value,chartId,'column','Program Name','Percentage of Emergency Orders by Program (all time)','Emergency');

    });

    GetEmergencyOrderByProgramData.get(null).then(function (data) {
        console.log(data);

        var chartId = 'emergencySubmittedByProgram';
        var category = _.pluck(data,'Program Name');
        var value = _.pluck(data,'Emergency');
        loadTheChart(category,value,chartId,'column','Program Name','Emergency Orders by Program (Past 1 Month)','Emergency');


    });
    GetTrendOfEmergencyOrdersSubmittedPerMonthData.get(null).then(function (data) {

        var chartId = 'trendOfEmergencyOrder';
        var category = _.pluck(data,'ym');
        var value = _.pluck(data,'Emergency Requisitions');
        loadTheChart(category,value,chartId,'spline','Year and Month','Trend of Emergency Orders Submitted Per Month','# of requisitions');

    });

    GetEmergencyOrderTrendsData.get(null).then(function (data) {
        var chartId = 'emergencyTrend';
        var category = _.pluck(data,'Month');
        var value = _.pluck(data,'Emergency Requisitions');
        loadTheChart(category,value,chartId,'line','Year and Month','Emergency Order Trends (past 1 year)','# of requisitions');

        console.log(data);
    });
    function loadTheChart(category, values,chartId,type,chartName,title,verticalTitle) {
        Highcharts.chart(chartId, {
            chart: {
                type: type
            },
            title: {
                text:' <h2><span style="font-size: x-small;color:#0c9083">'+title+'</span></h2>'
            }, credits:{
                enabled:false
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories:category,
                crosshair: true
            },
            yAxis: {
                lineWidth:1,
                gridLineColor: '',
                interval: 1,

                tickWidth: 1,


                min: 0,
                title: {
                    text: '<span style="font-size: x-small;color:#0c9083">'+verticalTitle+'</span>'
                }
            },
            tooltip: {
                /*
                                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                */
                headerFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.key}</b></td></tr><br/>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">'+verticalTitle+':</td>' + '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
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
            series: [{
                name: chartName,
                data: values

            }]
        });

    }

    $scope.filter={
        period: "114",
        program:"3",
        schedule: 1,
        year: "2017",
        zoneId:18
    };
    $scope.geojson = {};

    $scope.default_indicator = "period_over_expected";

    $scope.expectedFilter = function (item) {
        return item.expected > 0;
    };

    $scope.style = function (feature) {
        if ($scope.filter !== undefined && $scope.filter.indicator_type !== undefined) {
            $scope.indicator_type = $scope.filter.indicator_type;
        }
        else {
            $scope.indicator_type = $scope.default_indicator;
        }
        var color = ($scope.indicator_type === 'ever_over_total') ? interpolate(feature.ever, feature.total) : ($scope.indicator_type === 'ever_over_expected') ? interpolate(feature.ever, feature.expected) : interpolate(feature.period, feature.expected);

        return {
            fillColor: color,
            weight: 1,
            opacity: 1,
            color: 'white',
            dashArray: '1',
            fillOpacity: 0.7
        };
    };

    $scope.drawMap = function (json) {

        angular.extend($scope, {
            geojson: {
                data: json,
                style: $scope.style,
                onEachFeature: onEachFeature,
                resetStyleOnMouseout: true
            }
        });
        $scope.$apply();
    };

    function getExportDataFunction(features) {

        var arr = [];
        angular.forEach(features, function (value, key) {
            if (value.expected > 0) {
                var percentage = {'percentage': ((value.period / value.expected) * 100).toFixed(0) + ' %'};
                arr.push(angular.extend(value, percentage));
            }
        });
        $scope.exportData = arr;
    }


    $scope.loadRnRStatusSummary = function (summary) {

        var dataVal = [{name:'Status',  colorByPoint: false,data:summary}];

        Highcharts.chart('rnrSummary', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie',
                height: '350px'
            },
            credits:{
                enabled:false
            },
            title: {
                text: '<span style="font-size: x-small !important;color: #0c9083">R&R Status Summary</span>'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: false,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true
                    },
                    showInLegend: true,
                    innerSize:"70%",
                    size:150
                }
            },
            series: dataVal
        });

    };
    //
    RnRStatusSummary.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {

            var dataValues =[];
            var colors = {'RELEASED':'lightblue','IN_APPROVAL':'lightgreen','APPROVED':'#82A4EF','AUTHORIZED':'#FF558F'};
            data.rnrStatus.forEach(function (d) {
                if(d.status==='AUTHORIZED')
                    dataValues.push({sliced: true, selected: true,'name': messageService.get('label.rnr.status.summary.'+d.status),'y':d.totalStatus,color:colors[d.status]});
                else
                    dataValues.push({'name': messageService.get('label.rnr.status.summary.'+d.status),'y':d.totalStatus,color:colors[d.status]});
            });

            $scope.loadRnRStatusSummary(dataValues);
            $scope.total = 0;
            $scope.RnRStatusPieChartData = [];
            $scope.dataRows = [];
            $scope.datarows = [];

            if (!isUndefined(data.rnrStatus)) {

                $scope.dataRows = data.rnrStatus;
                if (isUndefined($scope.dataRows)) {
                    $scope.resetRnRStatusData();
                    return;
                }
                var statusData = _.pluck($scope.dataRows, 'status');
                var totalData = _.pluck($scope.dataRows, 'totalStatus');
                var color = {AUTHORIZED: '#FF0000', IN_APPROVAL: '#FFA500', APPROVED: '#0000FF', RELEASED: '#008000'};
                $scope.value = 0;
                for (var i = 0; i < $scope.dataRows.length; i++) {

                    $scope.total += $scope.dataRows[i].totalStatus;

                    var labelKey = 'label.rnr.status.summary.' + statusData[i];
                    var label = messageService.get(labelKey);
                    $scope.RnRStatusPieChartData[i] = {
                        label: label,
                        data: totalData[i],
                        color: color[statusData[i]]

                    };

                }
                $scope.rnrStatusPieChartOptionFunction();
                $scope.rnrStatusRenderedData = {
                    status: _.pairs(_.object(_.range(data.rnrStatus.length), _.pluck(data.rnrStatus, 'status')))

                };

                bindChartEvent("#rnr-status-report", "plotclick", rnrStatusChartClickHandler);
                bindChartEvent("#rnr-status-report", flotChartHoverCursorHandler);

            } else {
                $scope.resetRnRStatusReportData();
            }
            $scope.overAllTotal();
            $scope.paramsChanged($scope.tableParams);
        });

    ExtraAnalyticDataForRnRStatus.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {
            $scope.analyticData = data.analyticsData;

        });
    var data = [
        {
            y: 300,
            color:"#F7464A",
            name: "Red"
        },
        {
            y: 50,
            color: "#46BFBD",
            name: "Green"
        },
        {
            y: 100,
            color: "#FDB45C",
            name: "Yellow"
        },
        {
            y: 100,
            color: "#58ee2e",
            name: "Yellow1"
        },
        {
            y: 10,
            color: "#3066ee",
            name: "blue"
        }
    ];

    var initChart = function(data) {
        $('.chart').highcharts({
            chart: {
                animation: false,
                type: 'pie',
                backgroundColor: null
            },
            title: {
                text: 'RnR Status'
            },
            tooltip: {
                valueSuffix: '%',
                enabled: true
            },
            plotOptions: {
                pie: {
                    animation: {
                        duration: 750,
                        easing: 'easeOutQuad'
                    },
                    shadow: false,
                    center: ['50%', '50%'],
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    point:{
                        events:{
                            click: function(){
                                moveToPoint(this);
                            }
                        }
                    }
                },
                series: {
                    animation: {
                        duration: 750,
                        easing: 'easeOutQuad'
                    }
                }
            },
            series: [{
                animation: {
                    duration: 750,
                    easing: 'easeOutQuad'
                },
                name: 'Spending',
                data: data,
                size: '90%',
                innerSize: '55%',
                dataLabels: {
                    formatter: function () {
                        return this.y > 5 ? this.point.name : null;
                    },
                    color: '#ffffff',
                    distance: -30
                }
            }]
        });
    };

    var lastAngle = 0;
    var moveToPoint = function (clickPoint) {
        var points = clickPoint.series.points;
        var startAngle = 0;
        for (var i = 0; i < points.length; i++){
            var p = points[i];
            if (p === clickPoint){
                break;
            }
            startAngle += (p.percentage/100.0 * 360.0);
        }

        var newAngle = -startAngle + 90 - ((clickPoint.percentage/100.0 * 360.0)/2);

        console.log(newAngle);

        // clickPoint.series.update({
        //     //startAngle: -startAngle + 180 // start at 180
        //     startAngle: newAngle // center at 90
        // });

        $({
            angle: 0,
            target: newAngle
        }).animate({
            angle: newAngle-lastAngle
        }, {
            duration: 750,
            easing: 'easeOutQuad',
            step: function() {
                $('.chart').css({
                    transform: 'rotateZ('+this.angle+'deg)'
                });
            },
            complete: function() {
                $('.chart').css({
                    transform: 'rotateZ(0deg)'
                });
                clickPoint.series.update({
                    startAngle: newAngle // center at 90
                });
                lastAngle = newAngle;
            }
        });
    };

    initChart();

    filter();
    function filter() {


        $.getJSON('/gis/reporting-rate.json', $scope.filter, function (data) {
            $scope.features = data.map;
            var dataValues = [];
            var districts = _.pluck( $scope.features, 'name');
            var  expected= _.pluck( $scope.features, 'expected');
            var  reported= _.pluck( $scope.features, 'period');
            var expArray=[{name:'expected',data:expected},
                {name:'reported',data:reported}
            ];
            var districtMap = _.groupBy($scope.features,'name');
            getExportDataFunction($scope.features);
            angular.forEach(districtMap,function () {

            });
            angular.forEach($scope.features, function (feature) {
                feature.geometry_text = feature.geometry;
                feature.geometry = JSON.parse(feature.geometry);
                feature.type = "Feature";
                feature.properties = {};
                feature.properties.name = feature.name;
                feature.properties.id = feature.id;
                dataValues.push({
                    name:feature.name,
                    data:[ parseInt('200',feature.expected),parseInt('200',feature.period)]
                    // period: parseInt('200',feature.period),
                    // value:300,
                    // color: 'green'
                    // // color:interpolateCoverage(code.cumulative_vaccinated,code.monthly_district_target,code.coverageclassification.toLowerCase())

                });
            });
            $scope.drawMap({
                "type": "FeatureCollection",
                "features": $scope.features
            });
            zoomAndCenterMap(leafletData, $scope);
            var separators = Highcharts.geojson(Highcharts.maps['countries/zm/zm-all'], 'mapline');
            Highcharts.chart('container', {
                chart: {
                    type: 'bar'
                },
                title: {
                    text: 'Reporting Rate'
                },
                xAxis: {
                    categories: districts
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Reporting Rate'
                    }
                },
                legend: {
                    reversed: true
                },
                plotOptions: {
                    series: {
                        stacking: 'normal'
                    }
                },
                series:  expArray
            });
            Highcharts.mapChart('container1', {
                chart: {
                    map: 'countries/zm/zm-all'
                }, credits: {enabled: false},

                title: {
                    text: '<span style="font-size: 15px !important;color: #0c9083;text-align: center"> Reporting Rate, </span>'
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

                legend: {

                },

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
                        name: 'Non Reporting'
                    }, {
                        from: 80,
                        to: 90,
                        color: '#ffdb00',
                        name:'Partial Reporting'
                    }, {
                        from: 90,
                        color: '#006600',
                        name:'Fully Reporting'

                    }, {
                            from: 90,
                            color: '#000000',
                            name:'Not Expected To'

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
                    data:   dataValues ,
                    keys: ['name', 'value'],
                    joinBy: 'name',
                    name: 'Coverage',
                    dataLabels: {
                        enabled: true,
                        format: '{point.properties.postal-code}'
                    }, shadow: false
                }]
            });


        });


    }

    initiateMap($scope);

    $scope.onDetailClicked = function (feature) {
        $scope.currentFeature = feature;
        $scope.$broadcast('openDialogBox');
    };

// Instantiate the map


// Prepare random data


    $.getJSON('https://cdn.rawgit.com/highcharts/highcharts/057b672172ccc6c08fe7dbb27fc17ebca3f5b770/samples/data/germany.geo.json', function (geojson) {

        // Initiate the chart

    });

//rnr status



    function calculatePercentage(data){
        var total = 0;
        angular.forEach(data,function (da,index) {
            total += da.current;
        });
        return parseInt(total/parseInt(data.length,10),10);
    }






    ReportingRate.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {
            $scope.reportingRate={"zones":data.reportingRate};
            console.log(JSON.stringify(data.reportingRate));
            $scope.dynamicPerformanceChart(data.reportingRate,'#reporting-rate','ReportingRate',calculatePercentage($scope.reportingRate.zones));

        });

    StockStatusAvailaiblity.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {
            $scope.stockAvailability={"zones":data.stockStatus};
            console.log(JSON.stringify($scope.stockAvailability));
            $scope.dynamicPerformanceChart($scope.stockAvailability,'#stock-availability','StockAvailability',calculatePercentage($scope.stockAvailability.zones));
        });
    ItemFillRate.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {
            $scope.orderFillRateByZone={"zones":data.itemFillRate};
            console.log(JSON.stringify("item fillrate" +$scope.orderFillRateByZone));
            $scope.dynamicPerformanceChart($scope.orderFillRateByZone,'#container-order-fill-rate','OrderFillRate',calculatePercentage($scope.orderFillRateByZone.zones));

        });
    function borderColor(data){
        return (data >= 80)?'green':(data<80 && data>70)?'orange':'red';

    }

    $scope.dynamicPerformanceChart = function(data,chartId,name,result)

    {

        var gaugeOptions = {

            chart: {
                type: 'solidgauge',
                margin: [0, 0, 0, 0],
                backgroundColor: 'transparent'
            },
            title: null,
            yAxis: {
                min: 0,
                max: 100,
                minColor: borderColor(result),
                maxColor: borderColor(result),
                lineWidth: 0,
                tickWidth: 0,
                minorTickLength: 0,
                // minTickInterval: 500,
                labels: {
                    enabled: false
                }
            },
            pane: {
                size: '100%',
                center: ['50%', '50%'],
                startAngle: 0,
                endAngle: 360,
                background: {
                    borderWidth: 20,
                    backgroundColor: '#DBDBDB',
                    shape: 'arch',
                    borderColor: '#DBDBDB',
                    outerRadius: '80%',
                    innerRadius: '80%'
                }
            },
            tooltip: {
                enabled: true
            },
            plotOptions: {
                solidgauge: {
                    borderColor: borderColor(result),
                    borderWidth: 18,
                    radius: 75,
                    innerRadius: '80%',
                    dataLabels: {
                        borderWidth: 0,
                        useHTML: true,
                        enable: true
                    }
                }
            },
            series: [{
                name: name,
                data: [result],
                dataLabels: {
                    format: '<div style="Width: 30px;text-align:center"><span style="font-size:20px;color:"'+borderColor(data.ofr)+'"><br>{y}%</span></div>'
                }

            }],

            credits: {
                enabled: false
            }
        };
        $(chartId).highcharts(gaugeOptions);
    };


    var dataValues = [
        ['zm-lp', 0],
        ['zm-no', 1],
        ['zm-ce', 2],
        ['zm-ea', 3],
        ['zm-ls', 4],
        ['zm-co', 5],
        ['zm-nw', 6],
        ['zm-so', 7],
        ['zm-we', 8],
        ['zm-mu', 9]
    ];

    $scope.loadStockStatusByLocation = function (params) {

        $.getJSON('/gis/stock-status-products.json', params, function (data) {
            console.log($scope.products);
            $scope.products = data.products;
        });

        Highcharts.mapChart('stock_status_map', {
            chart: {
                map: 'countries/zm/zm-all'
            }, credits: {enabled: false},

            title: {
                text: '<span style="font-size: 15px !important;color: #0c9083;text-align: center">Stock Status By Location</span>'
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
                    name: ''
                }, {
                    from: 80,
                    to: 90,
                    color: '#ffdb00',
                    name: ''
                }, {
                    from: 90,
                    color: '#006600',
                    name: ' '

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
                // joinBy: ['hc-key', 'code'],
                name: 'Coverage',
                dataLabels: {
                    enabled: true,
                    format: '{point.properties.postal-code}'
                }, shadow: false
            }]
        });


    };

    var defaultParam = {
        year: parseInt(2017, 10),
        schedule: parseInt(1, 10),
        period: parseInt(115, 10),
        program: parseInt(3, 10)
    };
    $scope.loadStockStatusByLocation(defaultParam);
    $scope.rnrStatusPieChartOptionFunction = function () {

        $scope.rnRStatusPieChartOption = {
            series: {
                pie: {
                    show: true,
                    radius: 1,
                    label: {
                        show: true,
                        radius: 2 / 4,
                        formatter: function (label, series) {
                            return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">' + Math.round(series.percent) + '%</div>';
                        },
                        threshold: 0.1
                    }

                }
            },
            legend: {
                show: true,
                container: $("#rnrStatusReportLegend"),
                noColumns: 0,
                labelBoxBorderColor: "none"
                //width: 20

            },
            grid: {
                hoverable: true,
                clickable: true,
                borderWidth: 1,
                borderColor: "#d6d6d6",
                backgroundColor: {
                    colors: ["#FFF", "#CCC", "#FFF", "#CCC"]
                }
            },
            tooltip: true,
            tooltipOpts: {
                content: "%p.0%, %s",
                shifts: {
                    x: 20,
                    y: 0
                },
                defaultTheme: false
            }
        };


    };
//    new

    DailyStockStatus.get({zoneId: $scope.filter.zoneId,
            periodId: $scope.filter.period,
            programId: $scope.filter.program
        },
        function (data) {

            $scope.dailyStockStatusData=data.dailyStockStatus;
            $scope.dailyStockStatusHeader=_.unique( _.pluck($scope.dailyStockStatusData,"programCode"));
            $scope.dailyStockStatusPivot=_.groupBy($scope.dailyStockStatusData,"name");
            $scope.dataList=[];
            angular.forEach($scope.dailyStockStatusPivot,function (da,index) {
                var col={name:index};
                angular.forEach(da,function (p,i) {
                    col[p.programCode]=p.countOfSubmissions;
                });
                $scope.dataList.push(col);
            });




        });
    Highcharts.chart('equipmentStatusContainer', {
        chart: {
            type: 'spline'
        },
        title: {
            text: 'Lab Equipment Status'
        },

        xAxis: {
            categories: ['Eq1', 'Eq2', 'Eq3', 'Eq4', 'Eq5', 'Eq6',
                'Eq7', 'Eq8', 'Eq9', 'Equ10', 'Eq11', 'Eq12']
        },
        yAxis: {
            title: {
                text: 'Value'
            },
            labels: {
                formatter: function () {
                    return this.value ;
                }
            }
        },
        tooltip: {
            crosshairs: true,
            shared: true
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        series: [{
            name: 'Functional',
            marker: {
                symbol: 'square'
            },
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, {
                y: 26.5,

            }, 23.3, 18.3, 13.9, 9.6]

        }, {
            name: 'Non-Functional',
            marker: {
                symbol: 'diamond'
            },
            data: [{
                y: 3.9,

            }, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
        }]
    });


}

DashboardControllerFunction.resolve = {};