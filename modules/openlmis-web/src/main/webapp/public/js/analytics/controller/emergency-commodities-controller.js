function EmergencyCommoditiesController($scope, $http, $location, $rootScope, messageService, StockAvailableByProgramAndPeriodData, GetCOVIDStockStatusData, getCOVIDReportByFacilityData) {



    $rootScope.loadEmergencyCommoditiesDashlets = function(params) {
        $scope.stockSection(params);
        $scope.facilityReporting(params);
        $scope.casesPerDesignatedFacilities('patientPerRegion');
        $scope.cumulativePatientTrend('cumulativePatientTrend');
        $scope.pipeline('pipeline');

    };



    $scope.gridOptions = {
        data: 'drillDownData',
        showFooter: false,
        enableGridMenu: true,
        exporterMenuCsv: true,
        showFilter: false,
        enableColumnResize: true,
        enableSorting: false,
        exporterCsvFilename: 'myFile.csv',
        exporterPdfDefaultStyle: {
            fontSize: 9
        },
        columnDefs: [{
                field: 'SN',
                displayName: '#',
                cellTemplate: '<div style="text-align: center !important;">{{row.rowIndex + 1}}</div>',
                width: 25
            },
            {
                field: 'product',
                displayName: 'Item Description',
                width: 400
            },
            {
                field: 'last_update',
                displayName: 'Last Update'
            },
            {
                field: 'stockonhand',
                displayName: 'Stock on Hand'
            },
            {
                field: 'orderd',
                displayName: 'Quantity on Order'
            }

        ]
    };



    $scope.facilityReporting = function(params) {

        if (!params.facility) {
            params.facility = 0;
        }

        var allParams = angular.extend(params);
        getCOVIDReportByFacilityData.get(params).then(function(data) {

            $rootScope.drillDownData = data;
            $rootScope.title = "Item List";
            $rootScope.items = data.length;

            $rootScope.stockOutItems = _.filter(data, function(item) {
                return item.stockonhand < 1;
            }).length;

            $rootScope.availability = Math.round((1 - ($rootScope.stockOutItems / $rootScope.items)) * 100);
            $rootScope.$apply();

        });
    };




    $scope.stockSection = function(params) {

        if (!params.startDate && !params.endDate) {
            var currentDate = new Date();
            currentDate.setDate(currentDate.getDate());
            params.endDate = currentDate.toISOString().slice(0, 10);

            var pastDate = new Date();
            pastDate.setDate(pastDate.getDate() - 21);
            params.startDate = pastDate.toISOString().slice(0, 10);

            params.product = 0;
        }

        var subTitle = "From " + params.startDate + " to " + params.endDate;

        var allParams = angular.extend(params);
        GetCOVIDStockStatusData.get(params).then(function(data) {
            $scope.stockAvailabilityByDesignatedHospital('stockAvailabilityByDesignatedHospital', data, subTitle);
            $scope.stockAvailabilityForEmergencyCommodities('stockAvailabilityForEmergencyCommodities', data, subTitle);
        });
    };


    $scope.stockAvailabilityForEmergencyCommodities = function(chartTypeId, data, subTitle) {
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
            subtitle: {
                text: subTitle
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
                    y: _.reduce(_.pluck(data, 'availabilitypercentage'), function(memo, val) {
                        return memo + val;
                    }, 0) / _.pluck(data, 'availabilitypercentage').length,
                    sliced: true,
                    selected: true
                }, {
                    name: 'StockOut',
                    y: _.reduce(_.pluck(data, 'stockoutpercentage'), function(memo, val) {
                        return memo + val;
                    }, 0) / _.pluck(data, 'stockoutpercentage').length,
                }]
            }]
        });
    };



    $scope.stockAvailabilityByDesignatedHospital = function(chartTypeId, data, subTitle) {
        Highcharts.chart(chartTypeId, {
            chart: {
                type: 'bar'
            },
            title: {
                text: 'Stock Availability By Designated Hospitals'
            },
            subtitle: {
                text: subTitle
            },
            xAxis: {
                categories: _.pluck(data, 'name'),
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
                backgroundColor: Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
                shadow: true
            },
            credits: {
                enabled: false
            },
            series: [{
                name: 'Facilities',
                data: _.pluck(data, 'availabilitypercentage')
            }]
        });
    };

    $scope.pipeline = function(chartTypeId) {
        Highcharts.chart(chartTypeId, {
            chart: {
                zoomType: 'x',
                type: 'timeline'
            },
            xAxis: {
                type: 'datetime',
                visible: false
            },
            yAxis: {
                gridLineWidth: 1,
                title: null,
                labels: {
                    enabled: false
                }
            },
            legend: {
                enabled: false
            },
            title: {
                text: 'Inbound Timeline'
            },
            credits: {
                enabled: false
            },
            tooltip: {
                style: {
                    width: 300
                }
            },
            series: [{
                dataLabels: {
                    allowOverlap: false,
                    format: '<span style="color:{point.color}">● </span><span style="font-weight: bold;" > ' +
                        '{point.x:%d %b %Y}</span><br/>{point.label}<br/>{point.status}'
                },
                marker: {
                    symbol: 'circle'
                },
                data: [{
                    x: Date.UTC(2020, 9, 4),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 5, 4),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 3, 12),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 1, 3),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 6, 20),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 3, 19),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 11, 2),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 3, 17),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 11, 4),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 1, 19),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 7, 8),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }, {
                    x: Date.UTC(2020, 10, 20),
                    name: 'Ordered by GOT',
                    label: 'Tracking #: 1XHYX044MTZ',
                    status: 'Status : SHIPPED',
                    description: "PEP: 2345 Unit, Alcohol Base Sanitizer (I Litre) (Septosider-Plus): 455 Units, N95 Facse Masks: 9000 Units"
                }]
            }]
        });
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

app.controller('TestController', function($rootScope, $scope, StockAvailableByProgramAndPeriodData) {

    $rootScope.loadPipelineReport = function(params) {
        $scope.pipelineReport(params);
    };
    $scope.pipelineReport = function(params) {
        program = 1;
        params.programName = 'ILS';
        params.program = 1;
        params.schedule = 2;
        params.year = 2017;
        params.period = 69;
        var allParams = angular.extend(params, {
            program: program
        });
        StockAvailableByProgramAndPeriodData.get(params).then(function(data) {
            $scope.titleStockForProgramAvailable = '<span style="font-size: 13px!important;color: #0c9083">List of Available Tracer Items for ' + name + ' in ' + params.periodName + ', ' + params.year + '</span>';
            $scope.pipelineData = data;
        });
    };

    $scope.gridPipelineOptions = {
        data: 'pipelineData',
        showFooter: false,
        enableGridMenu: true,
        exporterMenuCsv: true,
        showFilter: false,
        enableColumnResize: true,
        enableSorting: false,
        exporterCsvFilename: 'myFile.csv',
        exporterPdfDefaultStyle: {
            fontSize: 9
        },
        columnDefs: [{
                field: 'SN',
                displayName: '#',
                cellTemplate: '<div style="text-align: center !important;">{{row.rowIndex + 1}}</div>',
                width: 25
            },
            {
                field: 'productname',
                displayName: 'Item Name',
                width: 400
            },
            {
                field: 'soh',
                displayName: 'Last Update'
            },
            {
                field: 'amc',
                displayName: 'Description (UOM)'
            },
            {
                field: 'mos',
                displayName: 'Quantity on Order'
            },
            {
                field: 'mos',
                displayName: 'Expected Arrival Date'
            },
            {
                field: 'mos',
                displayName: 'Status'
            }

        ]
    };
});