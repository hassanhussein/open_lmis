services.factory('DashboardStockStatusSummaryData', function ($q, $timeout, $resource,StockStatusSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockStatusSummary.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});


services.factory('StockAvailableForPeriodData', function ($q, $timeout, $resource,StockAvailableForPeriod) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockAvailableForPeriod.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});

services.factory('StockAvailableByProgramAndPeriodData', function ($q, $timeout, $resource,StockAvailableByProgramAndPeriod) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockAvailableByProgramAndPeriod.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});

services.factory('ConsumptionTrendsData', function ($q, $timeout, $resource,ConsumptionTrends) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            ConsumptionTrends.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});

services.factory('RnrPassedQualityCheckData', function ($q, $timeout, $resource,RnrPassedQualityCheck) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            RnrPassedQualityCheck.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});

services.factory('IndexOfAluStockAvailabilityData', function ($q, $timeout, $resource,IndexOfAluStockAvailability) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            IndexOfAluStockAvailability.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});


services.factory('StockStatusSummary', function ($resource) {
    return $resource('/api/dashboard/stock-status-summary.json', {}, {});
});
services.factory('StockAvailableForPeriod', function ($resource) {
    return $resource('/api/dashboard/stock-available-for-period.json', {}, {});
});

services.factory('StockAvailableByProgramAndPeriod', function ($resource) {
    return $resource('/api/dashboard/stock-available-for-program-period.json', {}, {});
});

services.factory('ConsumptionTrends', function ($resource) {
    return $resource('/api/dashboard/consumption-trend-year.json', {}, {});
});

services.factory('RnrPassedQualityCheck', function ($resource) {
    return $resource('/api/dashboard/rnr-passed-quality-check.json', {}, {});
});

services.factory('IndexOfAluStockAvailability', function ($resource) {
    return $resource('/api/dashboard/index-stock-availability.json', {}, {});
});
