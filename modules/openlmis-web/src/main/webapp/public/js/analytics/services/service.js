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

services.factory('PercentageWastageData', function ($q, $timeout, $resource,PercentageWastage) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            PercentageWastage.get(params, function (data) {

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

services.factory('StockStatusByProgramData', function ($q, $timeout, $resource,StockStatusByProgram) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockStatusByProgram.get(params, function (data) {

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


services.factory('FullProcessingPeriodData', function ($q, $timeout, $resource,FullProcessingPeriods1) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            FullProcessingPeriods1.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.period;
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

services.factory('DefaultProgramData', function ($q, $timeout, $resource,DefaultProgram1) {
    function get() {

        var deferred = $q.defer();
        $timeout(function () {
            DefaultProgram1.get({}, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.program;
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

services.factory('StockStatusSummaryByPeriodData', function ($q, $timeout, $resource,StockStatusSummaryByPeriod) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockStatusSummaryByPeriod.get(params, function (data) {

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

services.factory('StockStatusByProgramAndYearData', function ($q, $timeout, $resource,StockStatusByProgramAndYear) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockStatusByProgramAndYear.get(params, function (data) {

                var stocks =[];
                if (data !== undefined) {
                    stocks = data.stocks;
                                    console.log(params);
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

services.factory('StockAvailableByLevelData', function ($q, $timeout, $resource,StockAvailableByLevel) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            StockAvailableByLevel.get(params, function (data) {

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

services.factory('TimelinessReportingData', function ($q, $timeout, $resource,TimelinessReporting) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            TimelinessReporting.get(params, function (data) {

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
services.factory('OntimeDeliveryReportData', function ($q, $timeout, $resource,OntimeDeliveryReport) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            OntimeDeliveryReport.get(params, function (data) {

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

services.factory('PercentageWastage', function ($resource) {
    return $resource('/api/dashboard/percentage-wastage.json', {}, {});
});

services.factory('FullProcessingPeriods', function ($resource) {
    return $resource('/full-reported-period/:program.json', {}, {});
});

services.factory('DefaultProgram', function ($resource) {
    return $resource('/default-program.json', {}, {});
});

services.factory('StockStatusByProgram', function ($resource) {
    return $resource('/api/dashboard/stock-status-by-program-and-year.json', {}, {});
});

services.factory('StockStatusSummaryByPeriod', function ($resource) {
    return $resource('/api/dashboard/stock-availability-summary.json', {}, {});
});

services.factory('StockStatusByProgramAndYear', function ($resource) {
    return $resource('/api/dashboard/stock-availability-trends-by-program-and-year.json', {}, {});
});

services.factory('StockAvailableByLevel', function ($resource) {
    return $resource('/api/dashboard/stock-availability-by-level.json', {}, {});
});

services.factory('TimelinessReporting', function ($resource) {
    return $resource('/api/dashboard/dashboard-timeliness-reporting.json', {}, {});
});

services.factory('OntimeDeliveryReport', function ($resource) {
    return $resource('/api/dashboard/on-time-delivery.json', {}, {});
});
