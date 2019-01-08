function AggregateStockStatusReportFunction($scope, AggregateStockStatusReport, $state, Periods) {
    var definedObject = {};

    function getRenamedDate(periodListElement) {
        var dateToday = new Date(periodListElement.endDate);
        definedObject = periodListElement;
        var locale = "en-us";
        definedObject.month = dateToday.toLocaleString(locale, {month: "long"}) + ' ' + periodListElement.stringYear;
        console.log(definedObject);
        return definedObject;
    }

    var headerCellData = function (periodToDisplay) {

        console.log(periodToDisplay);

        Periods.get({scheduleId: $scope.paramToBeUpdated.schedule}, function (data) {
            var periodList = data.periods;

            var filteredPeriods = [];
            for (var i = 0; i < periodList.length; i++) {
                if (_.contains(periodToDisplay, periodList[i].name)) {
                    if (periodList[i].enableOrder)
                        filteredPeriods.push(getRenamedDate(periodList[i]));
                    else
                        filteredPeriods.push(periodList[i]);

                }
            }

            $scope.periodHeaders = filteredPeriods.sort(function (a, b) {
                return (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0);
            });


        });


    };

    $scope.getFacilityStock = function (facility, period) {
        var f = _.findWhere($scope.stockStatuses, {facilityName: facility});

        if (f !== undefined)
            p = _.findWhere(f.stocks, {periodName: period});
        if (p !== undefined) {

            return p;

        }
        else
            return null;
    };

    $scope.searchReport = function () {
        $scope.stockStatuses = [];
        AggregateStockStatusReport.get($scope.paramToBeUpdated, function (data) {
            if (data.pages.rows !== undefined || null !== data.pages) {
                $scope.facilityStocks = data.pages.rows;
                var groupByFacility = _.groupBy(data.pages.rows, function (f) {
                    return f.facility;
                });

                var periodData = _.pluck(data.pages.rows, 'periodName');

                var periodToDisplay = _.uniq(periodData);
                headerCellData(periodToDisplay);

                $scope.stockStatuses = $.map(groupByFacility, function (value, index) {
                    return [{"facilityName": index, "stocks": value}];
                });
            }
        });
    };
    $scope.OnFilterChanged = function () {

        if ($scope.filter !== undefined) {

            if ($scope.filter.zone === undefined)
                $scope.filter.zone = 0;

            $scope.paramToBeUpdated = {
                program: parseInt($scope.filter.program, 10),
                schedule: parseInt($scope.filter.schedule, 10),
                product: parseInt($scope.filter.product, 10),
                zone: parseInt($scope.filter.zone, 10),
                periodStart: $scope.filter.periodStart,
                periodEnd: $scope.filter.periodEnd
            };
        }

    };

}


AggregateStockStatusReportFunction.resolve = {};