/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

function RequisitionStatusSummaryController($scope,Program,Period,$rootScope, messageService, EmergencyRnRStatusSummary,ExtraAnalyticDataForRnRStatus,$timeout, $filter, RnRStatusSummaryData, $location) {



   $rootScope.initializeRequisitionSummary = function (params) {
        params.zone = 437;
        $scope.showProductsFilter = false;
        $scope.$parent.currentTab = "RNR-STATUS-SUMMARY";

       RnRStatusSummaryData.get(params).then(function(data){

                 $scope.values = data;

                               $scope.total = 0;
                               $scope.RnRStatusPieChartData = [];
                               $scope.dataRows = [];
                               $scope.datarows = [];

                               if (!isUndefined(data)) {

                                   $scope.dataRows = data;
                                   if (isUndefined($scope.dataRows)) {
                                       $scope.resetRnRStatusData();
                                       return;
                                   }

                                   var statusData = _.pluck($scope.dataRows, 'status');
                                   var totalData = _.pluck($scope.dataRows, 'totalStatus');

                                                                     console.log(statusData);
                                   var color = {AUTHORIZED: '#FF0000', IN_APPROVAL: '#FFA500', APPROVED: '#3C81B0', RELEASED: '#008000'};
                                   $scope.value = 0;
                                   for (var i = 0; i < $scope.dataRows.length; i++) {

                                       $scope.total += $scope.dataRows[i].totalStatus;

                                       var labelKey = 'label.rnr.status.summary.' + statusData[i];
                                       var label = messageService.get(labelKey);
                                       $scope.RnRStatusPieChartData[i] = {

                                           name: label,
                                           y: totalData[i],
                                           color: color[statusData[i]]

                                       };

                                   }
                                   $rootScope.rnrStatusTitle = 'Requisition Status Summary for '+params.programName+' ('+params.periodName+' ,'+params.year+' )';

                                   loadPieChart('rnr-status-summary','', $scope.RnRStatusPieChartData);
                                   $scope.rnrStatusPieChartOptionFunction();
                                   $scope.rnrStatusRenderedData = {
                                       status: _.pairs(_.object(_.range(data.length), _.pluck(data, 'status')))

                                   };

                                   bindChartEvent("#rnr-status-report", "plotclick", rnrStatusChartClickHandler);
                                   bindChartEvent("#rnr-status-report", flotChartHoverCursorHandler);

                               } else {
                                   $scope.resetRnRStatusReportData();
                               }
                               $scope.overAllTotal();
                               $scope.paramsChanged($scope.tableParams);

       });




    };


    function loadPieChart(chartId, title,dataV) {
                                   console.log(dataV);
    var chart = new Highcharts.Chart({
            chart: {
                renderTo: chartId,
                type: 'pie'

            },
            credits:{
            enabled:false
            },
            title: {
                text: title
            },
            yAxis: {
                title: {
                    text: ' '
                }
            },
            plotOptions: {
                pie: {
                    shadow: false,
                  allowPointSelect: true,
                  cursor: 'pointer',
                     size: '100%',
                  innerSize: '70%',
                  dataLabels: {
                   enabled: true,
                   format: '<b>  {point.percentage:.0f} %',


                   style: {
                   color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                   fontFamily: '\'Lato\', sans-serif', lineHeight: '18px', fontSize: '17px'
                   }
                   },
                   showInLegend: true


                }
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.point.name +'</b>: '+ this.y +' ';
                }
            },
            exporting: {
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
            series: [{
                name: 'Requisition Status',
                data: dataV,
                //data: [["Firefox",6],["MSIE",4],["Chrome",7]],

                showInLegend:true,
                dataLabels: {
                    enabled: true
                }
            }]
        });




       /*  new Highcharts.Chart({
                    chart: {
                        renderTo: chartId,
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
                        text: 'sub'
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

                                *//*
                                                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                *//*
                                style: {
                                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                    fontFamily: '\'Lato\', sans-serif', lineHeight: '18px', fontSize: '17px'
                                }
                            },
                            showInLegend: true
                        }
                    },
                    series: [dataV]
                });*/


    }



/*    var filterHistory = dashboardFiltersHistoryService.get($scope.$parent.currentTab);

    if (isUndefined(filterHistory)) {
        $scope.formFilter = $scope.filterObject = userPreferredFilters || {};

    } else {
        $scope.formFilter = $scope.filterObject = filterHistory || {};
    }*/

$scope.formFilter = {};
    $scope.formPanel = {openPanel: true};
    $scope.rnrStatus = {openPanel: true};

/*
    $scope.programs = programsList;
    $scope.programs.unshift({'name': formInputValue.programOptionSelect});

    $scope.loadGeoZones = function () {
        UserGeographicZoneTree.get({programId: $scope.formFilter.programId}, function (data) {
            $scope.zones = data.zone;
            if (!isUndefined($scope.zones)) {
                $scope.rootZone = $scope.zones.id;
            }
        });
    };

    FlatGeographicZoneList.get(function (data) {
        $scope.geographicZones = data.zones;
    });

    OperationYears.get(function (data) {
        $scope.startYears = data.years;
        $scope.startYears.unshift(formInputValue.yearOptionAll);
    });

    ReportSchedules.get(function (data) {
        $scope.schedules = data.schedules;
        $scope.schedules.unshift({'name': formInputValue.scheduleOptionSelect});

    });

    $scope.filterProductsByProgram = function () {
        $scope.loadGeoZones();
        if (isUndefined($scope.formFilter.programId)) {

            return;
        }
        $scope.filterObject.programId = $scope.formFilter.programId;
    };

    $scope.processZoneFilter = function () {
        $scope.filterObject.zoneId = $scope.formFilter.zoneId;

    };*/




    $scope.loadRnRStatus = function () {
        if (isUndefined($scope.filterObject.period) || isUndefined($scope.filterObject.program)) {
            $scope.resetRnRStatusData();
            return;
        }

        $scope.totalEmergency = 0;

        $scope.$watch(EmergencyRnRStatusSummary.get({zoneId: $scope.filterObject.zoneId,
                periodId: $scope.filterObject.periodId,
                programId: $scope.filterObject.programId
            },
            function (data) {

                if (!isUndefined(data.emergencyRnrStatus)) {
                    $scope.emergency = [];
                    $scope.emergency = data.emergencyRnrStatus;

                    for (var i = 0; i < $scope.emergency.length; i++) {
                        $scope.totalEmergency += $scope.emergency[i].totalEmergencyRnRStatus;
                    }
                }

                $scope.paramsChanged($scope.tableParams);
            }));

        $scope.analyticData= [];
        ExtraAnalyticDataForRnRStatus.get({zoneId: $scope.filterObject.zoneId,
                periodId: $scope.filterObject.periodId,
                programId: $scope.filterObject.programId
            },
            function (data) {
                $scope.analyticData = data.analyticsData;

            });

        $scope.onDetailClicked = function(feature){
            $scope.currentFeature = feature;
            //console.log($scope.currentFeature.status);
            rnrStatusSummaryDetails($scope.currentFeature.status);

        };

        function rnrStatusSummaryDetails(item) {
            if (item) {
                var status;
                if (!isUndefined( $scope.currentFeature.status)) {
                    status =  $scope.currentFeature.status;
                }
                var rnrDetailPath = '/rnr-status-report/program/' + $scope.filterObject.program + '/period/' + $scope.filterObject.period;
                dashboardMenuService.addTab('menu.header.dashboard.rnr.status.detail', '/public/pages/dashboard/index.html#' + rnrDetailPath, 'RNR-STATUS-DETAIL', true, 8);
                $location.path(rnrDetailPath).search("status=" + status + "&zoneId=" + $scope.filterObject.zoneId);

                $scope.$apply();
            }

        }

    };

    $scope.allTotal = 0;
    $scope.overAllTotal = function () {
    $rootScope.allTotal = $scope.totalEmergency + $scope.total;
    };


    $scope.resetRnRStatusData = function () {
        $scope.rnrStatusRenderedData = null;
        $scope.rnRStatusPieChartOption = null;
        $scope.total = 0;
        $scope.RnRStatusPieChartData = null;
        $scope.dataRows = null;
        $scope.datarows = null;
    };


    var getFilterValues = function () {
        $scope.formFilter.programName = getSelectedItemName($scope.formFilter.programId, $scope.programs);
        $scope.formFilter.periodName = getSelectedItemName($scope.formFilter.periodId, $scope.periods);
        $scope.formFilter.zoneName = getSelectedZoneName($scope.formFilter.zoneId, $scope.zones, $scope.geographicZones);
    };

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


    function flotChartHoverCursorHandler(event, pos, item) {

        if (item && !isUndefined(item.dataIndex)) {
            $(event.target).css('cursor', 'pointer');
        } else {
            $(event.target).css('cursor', 'auto');
        }
    }

    function bindChartEvent(elementSelector, eventType, callback) {
        $(elementSelector).bind(eventType, callback);
    }

    function rnrStatusChartClickHandler(event, pos, item) {
        if (item) {
            var status;
            if (!isUndefined($scope.rnrStatusRenderedData.status)) {
                status = $scope.rnrStatusRenderedData.status[item.seriesIndex][1];
            }
            var rnrDetailPath = '/rnr-status-report/program/' + $scope.filterObject.program + '/period/' + $scope.filterObject.period;
            dashboardMenuService.addTab('menu.header.dashboard.rnr.status.detail', '/public/pages/dashboard/index.html#' + rnrDetailPath, 'RNR-STATUS-DETAIL', true, 8);
            $location.path(rnrDetailPath).search("status=" + status + "&zoneId=" + $scope.filterObject.zone);

            $scope.$apply();
        }

    }

    $scope.resetRnRStatusReportData = function () {
        $scope.RnRStatusPieChartData = null;
        $scope.rnRStatusPieChartOption = null;
        $scope.dataRows = null;
    };


    $scope.loadFacilitiesByRequisition = function () {
        if ($scope.formFilter.rgroupId == "All") {
            $scope.filterObject.rgroupId = -1;
        } else if ($scope.formFilter.rgroupId !== undefined || $scope.formFilter.rgroupId === "") {
            $scope.filterObject.rgroupId = $scope.formFilter.rgroupId;
            $.each($scope.requisitionGroups, function (item, idx) {
                if (idx.id == $scope.formFilter.rgroupId) {
                    $scope.filterObject.rgroup = idx.name;
                }
            });
        } else {
            $scope.filterObject.rgroupId = 0;

        }

    };

    $scope.$on('$routeChangeStart', function () {
        var data = {};
        $scope.filterObject = $scope.formFilter;
        angular.extend(data, $scope.filterObject);
        dashboardFiltersHistoryService.add($scope.$parent.currentTab, data);
    });

    $scope.$on('$viewContentLoaded', function () {
        $timeout(function () {
            $scope.search();

        }, 1000);

    });
    $scope.search = function () {
        getFilterValues();
        if ($scope.rootZone == $scope.formFilter.zoneId) {
            return;
        }
        $scope.loadRnRStatus();
    };
    $scope.$watch('formFilter.programId', function () {
       // $scope.filterProductsByProgram();

    });
    $scope.$watch('formFilter.scheduleId', function () {
       // $scope.changeSchedule();

    });

    $scope.paramsChanged = function (params) {

        // slice array data on pages
        if ($scope.data === undefined) {
            $scope.datarows = [];
        } else {
            var data = $scope.data;
            var orderedData = params.filter ? $filter('filter')(data, params.filter) : data;
            orderedData = params.sorting ? $filter('orderBy')(orderedData, params.orderBy()) : data;

            params.total = orderedData.length;
            $scope.datarows = orderedData.slice((params.page - 1) * params.count, params.page * params.count);
            var i = 0;
            var baseIndex = params.count * (params.page - 1) + 1;
            while (i < $scope.datarows.length) {
                $scope.datarows[i].no = baseIndex + i;
                i++;
            }
        }
    };

    // watch for changes of parameters
    $scope.$watch('tableParams', $scope.paramsChanged, true);


      //Filters


           $scope.OnFilterChanged = function () {

           console.log ('changed');

           var programName = '';
           Program.get({id: parseInt($location.search().program,10)}, function(da){
           programName = da.program.name;

           var periodName = '';
           Period.get({id: parseInt($location.search().period,10)}, function(da){
           periodName = da.period.name;

           $location.search().programName = programName;
           $location.search().periodName = periodName;

           console.log($location.search());


           $scope.$parent.params = $location.search();

           $rootScope.initializeRequisitionSummary($location.search());


           });


           });

           };

}
