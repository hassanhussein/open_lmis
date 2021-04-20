function RejectedControllerFunction($scope, $location, $routeParams, $rootScope, $stateParams, GetRejectedRnRByZoneReport, $state) {
    "use strict";
    $scope.statuses = [{code: 'INITIATED', name: 'District Rejected'}, {code: 'AUTHORIZED', name: 'Regional Rejected'}];
    $scope.default_status = 'AUTHORIZED';
    $scope.filter = $stateParams;


    function getRejectionRate(rows) {
        var groupByZone = _.groupBy(rows, 'zoneName');
        var data = _.map(groupByZone, function(value, key) {
            var total = 0;
            for (var i = 0; i < value.length; i++) {
                var rejectedCount = value[i].rejectionCount;
                total += (rejectedCount);
            }
            return {'key': key, 'total': total};
        });

        var totalValues = _.pluck(data, 'total');
        var key = _.pluck(data, 'key');
        var maximumValue = Math.max.apply(null, totalValues);
        var array1 = key, array3 = totalValues, result = [], i = -1;

        while (array1[++i]) {
            if (array3[i] === maximumValue)
                result.push({
                    name: array1[i], y: array3[i], sliced: true,
                    selected: true, color: 'red'
                });
            else
                result.push([array1[i], array3[i]]);
        }
        functionalData(result);
    }

    $scope.currentPage = 1;
    $scope.pageSize = 50;

    $scope.OnFilterChanged = function() {

        $scope.filter.max = 10000;
        $scope.filter.page = 1;
        $scope.default_status = 'INITIATED';
        $scope.data = $scope.datarows = [];
        $scope.filter.status = $scope.default_status;

        $scope.filter.max = 10000;
        $scope.status = $scope.getSanitizedParameter().status;
        $scope.program = $scope.getSanitizedParameter().program;

        GetRejectedRnRByZoneReport.get($scope.getSanitizedParameter(), function(data) {
            if (data.pages !== undefined) {
                $scope.data = data.pages.rows;
                $scope.paramsChanged($scope.tableParams);
                getRejectionRate(data.pages.rows);
            }
        });

    };

    $scope.goToRejectionReasons = function() {
        var params = {'zone': "", 'value': ""};
        var params2 = angular.extend(params, $scope.getSanitizedParameter());
        $state.go('rejectionByZoneView', params2);
    };


    var displayEventData = function(event) {
        console.log($scope.getSanitizedParameter());
        var params = {'zone': event.point.name, 'value': event.point.y};
        var params2 = angular.extend(params, $scope.getSanitizedParameter());
        $state.go('rejectionByZoneView', params2);

    };

    var functionalData = function(data) {
        var chart = new Highcharts.Chart({
            chart: {
                renderTo: 'rejected',
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 90
                },
                style: {
                    fontFamily: 'helvetica'
                }
            },
            credits: {enabled: false},
            title: {
                text: 'RnR Rejection by Zone'
            },
            plotOptions: {
                pie: {
                    shadow: false,
                    cursor: 'pointer',
                    slicedOffset: 20
                }
            },
            tooltip: {
                formatter: function() {
                    return '<b>' + this.point.name + '</b>: ' + this.y;
                }
            },
            series: [{
                name: 'zones',
                data: data,
                size: '60%',
                innerSize: '70%',
                showInLegend: false,
                dataLabels: {
                    enabled: true
                }, animation: true,
                point: {
                    events: {
                        click: function(event) {
                            displayEventData(event);

                        }
                    }
                }
            }]
        });
    };

}

RejectedControllerFunction.resolve = {};