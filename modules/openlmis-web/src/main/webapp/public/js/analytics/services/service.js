services.factory('DashboardStockStatusSummaryData', function($q, $timeout, $resource, StockStatusSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockStatusSummary.get(params, function(data) {

                var stocks = [];
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


services.factory('StockAvailableForPeriodData', function($q, $timeout, $resource, StockAvailableForPeriod) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockAvailableForPeriod.get(params, function(data) {

                var stocks = [];
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

services.factory('StockAvailableByProgramAndPeriodData', function($q, $timeout, $resource, StockAvailableByProgramAndPeriod) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockAvailableByProgramAndPeriod.get(params, function(data) {

                var stocks = [];
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

services.factory('ConsumptionTrendsData', function($q, $timeout, $resource, ConsumptionTrends) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            ConsumptionTrends.get(params, function(data) {

                var stocks = [];
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

services.factory('RnrPassedQualityCheckData', function($q, $timeout, $resource, RnrPassedQualityCheck) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            RnrPassedQualityCheck.get(params, function(data) {

                var stocks = [];
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

services.factory('IndexOfAluStockAvailabilityData', function($q, $timeout, $resource, IndexOfAluStockAvailability) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            IndexOfAluStockAvailability.get(params, function(data) {

                var stocks = [];
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

services.factory('PercentageWastageData', function($q, $timeout, $resource, PercentageWastage) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            PercentageWastage.get(params, function(data) {

                var stocks = [];
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

services.factory('StockStatusByProgramData', function($q, $timeout, $resource, StockStatusByProgram) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockStatusByProgram.get(params, function(data) {

                var stocks = [];
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


services.factory('FullProcessingPeriodData', function($q, $timeout, $resource, FullProcessingPeriods) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            FullProcessingPeriods.get(params, function(data) {

                var stocks = [];
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

services.factory('DefaultProgramData', function($q, $timeout, $resource, DefaultProgram1) {
    function get() {

        var deferred = $q.defer();
        $timeout(function() {
            DefaultProgram1.get({}, function(data) {

                var stocks = [];
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

services.factory('StockStatusSummaryByPeriodData', function($q, $timeout, $resource, StockStatusSummaryByPeriod) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockStatusSummaryByPeriod.get(params, function(data) {

                var stocks = [];
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

services.factory('StockStatusByProgramAndYearData', function($q, $timeout, $resource, StockStatusByProgramAndYear) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockStatusByProgramAndYear.get(params, function(data) {
                var stocks = [];
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

services.factory('StockAvailableByLevelData', function($q, $timeout, $resource, StockAvailableByLevel) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockAvailableByLevel.get(params, function(data) {

                var stocks = [];
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

services.factory('TimelinessReportingData', function($q, $timeout, $resource, TimelinessReporting) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            TimelinessReporting.get(params, function(data) {

                var stocks = [];
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
services.factory('OntimeDeliveryReportData', function($q, $timeout, $resource, OntimeDeliveryReport) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            OntimeDeliveryReport.get(params, function(data) {

                var stocks = [];
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




//API for Zambia Dashboard
services.factory('GetNumberOfEmergencyData', function($q, $timeout, $resource, GetNumberOfEmergency) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetNumberOfEmergency.get(params, function(data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetPercentageOfEmergencyOrderByProgramData', function($q, $timeout, $resource, GetPercentageOfEmergencyOrderByProgram) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetPercentageOfEmergencyOrderByProgram.get(params, function(data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetEmergencyOrderByProgramData', function($q, $timeout, $resource, GetEmergencyOrderByProgram) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetEmergencyOrderByProgram.get(params, function(data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetTrendOfEmergencyOrdersSubmittedPerMonthData', function($q, $timeout, $resource, GetTrendOfEmergencyOrdersSubmittedPerMonth) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetTrendOfEmergencyOrdersSubmittedPerMonth.get(params, function(data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetEmergencyOrderTrendsData', function($q, $timeout, $resource, GetEmergencyOrderTrends) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetEmergencyOrderTrends.get(params, function(data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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


services.factory('RnRStatusSummaryData', function($q, $timeout, $resource, RnRStatusSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {

            RnRStatusSummary.get({
                    zoneId: params.zone,
                    periodId: params.period,
                    programId: params.program
                },
                function(data) {
                    var stocks = {};
                    if (data !== undefined) {
                        stocks = data.rnrStatus;
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
services.factory('ConsumptionTrendSummaryData', function($q, $timeout, $resource, ConsumptionTrendSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {

            ConsumptionTrendSummary.get(params,
                function(data) {
                    var stocks = {};
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

services.factory('GetEmergencyAndRegularRnRTrendsData', function($q, $timeout, $resource, GetEmergencyAndRegularRnRTrends) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {

            GetEmergencyAndRegularRnRTrends.get(params,
                function(data) {
                    var stocks = {};
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

services.factory('GetGeoStockStatusForMapData', function($q, $timeout, $resource, GetGeoStockStatusForMap) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {

            GetGeoStockStatusForMap.get(params,
                function(data) {
                    var stocks = {};
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

services.factory('GetDistrictFundUtilizationData', function($q, $timeout, $resource, GetDistrictFundUtilization) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {

            GetDistrictFundUtilization.get(params,
                function(data) {
                    var stocks = {};
                    if (data !== undefined) {
                        stocks = data.financies;
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

services.factory('GetGeoStockStatusDetailsData', function($q, $timeout, $resource, GetGeoStockStatusDetails) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {

            GetGeoStockStatusDetails.get(params,
                function(data) {
                    var stocks = {};
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

services.factory('GetNumberOfEmergency', function($resource) {
    return $resource('/api//dashboard/getNumberOfEmergency.json', {}, {});
});
services.factory('GetPercentageOfEmergencyOrderByProgram', function($resource) {
    return $resource('/api//dashboard/getPercentageOfEmergencyOrderByProgram.json', {}, {});
});
services.factory('GetEmergencyOrderByProgram', function($resource) {
    return $resource('/api//dashboard/getEmergencyOrderByProgram.json', {}, {});
});
services.factory('GetTrendOfEmergencyOrdersSubmittedPerMonth', function($resource) {
    return $resource('/api//dashboard/getTrendOfEmergencyOrdersSubmittedPerMonth.json', {}, {});
});
services.factory('GetEmergencyOrderTrends', function($resource) {
    return $resource('/api/dashboard/emergencyOrderTrends.json', {}, {});
});



services.factory('StockStatusSummary', function($resource) {
    return $resource('/api/dashboard/stock-status-summary.json', {}, {});
});
services.factory('StockAvailableForPeriod', function($resource) {
    return $resource('/api/dashboard/stock-available-for-period.json', {}, {});
});

services.factory('StockAvailableByProgramAndPeriod', function($resource) {
    return $resource('/api/dashboard/stock-available-for-program-period.json', {}, {});
});

services.factory('ConsumptionTrends', function($resource) {
    return $resource('/api/dashboard/consumption-trend-year.json', {}, {});
});

services.factory('RnrPassedQualityCheck', function($resource) {
    return $resource('/api/dashboard/rnr-passed-quality-check.json', {}, {});
});

services.factory('IndexOfAluStockAvailability', function($resource) {
    return $resource('/api/dashboard/index-stock-availability.json', {}, {});
});

services.factory('PercentageWastage', function($resource) {
    return $resource('/api/dashboard/percentage-wastage.json', {}, {});
});

services.factory('FullProcessingPeriods', function($resource) {
    return $resource('/full-reported-period/:program.json', {}, {});
});

services.factory('DefaultProgram', function($resource) {
    return $resource('/default-program.json', {}, {});
});

services.factory('StockStatusByProgram', function($resource) {
    return $resource('/api/dashboard/stock-status-by-program-and-year.json', {}, {});
});

services.factory('StockStatusSummaryByPeriod', function($resource) {
    return $resource('/api/dashboard/stock-availability-summary.json', {}, {});
});

services.factory('StockStatusByProgramAndYear', function($resource) {
    return $resource('/api/dashboard/stock-availability-trends-by-program-and-year.json', {}, {});
});

services.factory('StockAvailableByLevel', function($resource) {
    return $resource('/api/dashboard/stock-availability-by-level.json', {}, {});
});

services.factory('TimelinessReporting', function($resource) {
    return $resource('/api/dashboard/dashboard-timeliness-reporting.json', {}, {});
});

services.factory('OntimeDeliveryReport', function($resource) {
    return $resource('/api/dashboard/on-time-delivery.json', {}, {});
});

services.factory('RejectionCount', function($resource) {
    return $resource('/api/dashboard/getRejectionCount.json', {}, {});
});

services.factory('DashboardRnrTypes', function($resource) {
    return $resource('/dashboard/rnr-emergency-regular-types.json', {}, {});
});


services.factory("totalRnRCreatedByRequisitionGroup", function($resource) {
    return $resource('/dashboard//RnRCreateForRequisitionGroup', {}, {});
});
services.factory('RnRStatusSummary', function($resource) {
    return $resource('/dashboard/RnRStatus/:zoneId/:periodId/:programId/rnrStatus.json', {}, {});
});

services.factory("EmergencyRnRStatusSummary", function($resource) {
    return $resource('/dashboard/EmergencyRnRStatus/:zoneId/:periodId/:programId/rnrStatus.json', {}, {});
});

services.factory("ConsumptionTrendSummary", function($resource) {
    return $resource('/api/dashboard/getConsumptionSummaryTrends.json', {}, {});
});

services.factory("GetEmergencyAndRegularRnRTrends", function($resource) {
    return $resource('/api/dashboard/getEmergencyAndRegularRnRTrends.json', {}, {});
});

services.factory("GetGeoStockStatusForMap", function($resource) {
    return $resource('/api/dashboard/getGeoStockStatusForMap.json', {}, {});
});

services.factory("GetDistrictFundUtilization", function($resource) {
    return $resource('/api/dashboard/getDistrictFundUtilization.json', {}, {});
});


services.factory('GetSourceOfFundsByLocationData', function($q, $timeout, $resource, GetSourceOfFundsByLocation) {
    function get(params) {
        var deferred = $q.defer();
        $timeout(function() {
            GetSourceOfFundsByLocation.get(params, function(data) {
                var sourceOfFunds = {};
                if (data !== undefined) {
                    sourceOfFunds = data.sourceOfFunds;
                }
                deferred.resolve(sourceOfFunds);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});

services.factory('GetGeoJsonInfoData', function($q, $timeout, $resource, GetGeoJsonInfo) {
    function get(params) {
        var deferred = $q.defer();
        $timeout(function() {
            GetGeoJsonInfo.get(params, function(data) {
                var sourceOfFunds = {};
                if (data !== undefined) {
                    sourceOfFunds = data.stocks;
                }
                deferred.resolve(sourceOfFunds);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});

services.factory('GetRegionalStockStatusSummaryData', function($q, $timeout, $resource, GetRegionalStockStatusSummary) {
    function get(params) {
        var deferred = $q.defer();
        $timeout(function() {
            GetRegionalStockStatusSummary.get(params, function(data) {
                var sourceOfFunds = {};
                if (data !== undefined) {
                    sourceOfFunds = data.stocks;
                }
                deferred.resolve(sourceOfFunds);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };

});


services.factory('GetSourceOfFundsByLocation', function($resource) {
    return $resource('/api//dashboard/getSourceOfFundsByLocation.json', {}, {});
});

services.factory('GetGeoStockStatusDetails', function($resource) {
    return $resource('/api//dashboard/getGeoStockStatusDetails.json', {}, {});
});


services.factory('StockOutRate', function($resource) {
    return $resource('/api/dashboard/getStockOutRate.json', {}, {});
});

services.factory('StockOutRateData', function($q, $timeout, $resource, StockOutRate) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockOutRate.get(params, function(data) {

                var stockOutRates = [];
                if (data !== undefined) {
                    stockOutRates = data.stockOutRates;
                }
                deferred.resolve(stockOutRates);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

services.factory('LatestReportedStockStatusByDistrictForTracer', function($resource) {
    return $resource('/api/dashboard/getLatestReportedStockStatusForAllTracerByDistrict.json', {}, {});
});

services.factory('LatestReportedStockStatusByDistrict', function($resource) {
    return $resource('/api/dashboard/getLatestReportedStockStatusForProductByDistrict.json', {}, {});
});

services.factory('LatestReportedStockStatusByDistrictData', function($q, $timeout, $resource, LatestReportedStockStatusByDistrictForTracer, LatestReportedStockStatusByDistrict) {
    function get(params) {
        var deferred = $q.defer();
        if (params.indicator == 'allTracerProducts' || !params.indicator) {
            $timeout(function() {
                LatestReportedStockStatusByDistrictForTracer.get(params, function(data) {

                    var stockOutRates = [];
                    if (data !== undefined) {
                        stockOutRates = data.commoditiesDetailsByDistrict;
                        //Merge region served by mutiple warehouses
                        String.prototype.replaceAll = function (find, replace) {
                            var str = this;
                            return str.replace(new RegExp(find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), replace);
                        };

                        var originalMsg = JSON.stringify(stockOutRates);
                        console.log(originalMsg);
                        var updatedMsg = originalMsg.replaceAll("Geita(Mwanza Zone)", "Geita")
                        .replaceAll("Geita(Muleba Zone)", "Geita")
                        .replaceAll("Njombe (Iringa Zone)", "Njombe")
                        .replaceAll("Ruvuma (Iringa Zone)", "Ruvuma")
                         .replaceAll("Njombe (Mbeya Zone)", "Njombe")
                         .replaceAll("Ruvuma (Mtwara Zone)", "Ruvuma");

                        stockOutRates = JSON.parse(updatedMsg);
                    }
                    deferred.resolve(stockOutRates);
                });

            }, 100);
        } else {
            $timeout(function() {
                LatestReportedStockStatusByDistrict.get(params, function(data) {

                    var stockOutRates = [];
                    if (data !== undefined) {
                        stockOutRates = data.commoditiesDetailsByDistrict;
                        //Merge region served by mutiple warehouses
                        String.prototype.replaceAll = function (find, replace) {
                            var str = this;
                            return str.replace(new RegExp(find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), replace);
                        };

                        var originalMsg = JSON.stringify(stockOutRates);
                        var updatedMsg = originalMsg.replaceAll("Geita(Mwanza Zone)", "Geita")
                        .replaceAll("Geita(Muleba Zone)", "Geita")
                        .replaceAll("Njombe (Iringa Zone)", "Njombe")
                        .replaceAll("Ruvuma (Iringa Zone)", "Ruvuma")
                         .replaceAll("Njombe (Mbeya Zone)", "Njombe")
                         .replaceAll("Ruvuma (Mtwara Zone)", "Ruvuma");

                        stockOutRates = JSON.parse(updatedMsg);

                    }
                    deferred.resolve(stockOutRates);

                });

            }, 100);
        }
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory('StockStatusByLocation', function($resource) {
    return $resource('/api/dashboard/getAllCommoditiesDetailsByDistrict.json', {}, {});
});

services.factory('StockStatusByLocationData', function($q, $timeout, $resource, StockStatusByLocation) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            StockStatusByLocation.get(params, function(data) {

                var stockOutRates = [];
                if (data !== undefined) {
                    stockOutRates = data.commoditiesDetailsByDistrict;
                }
                deferred.resolve(stockOutRates);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory('GetTzRegionMapData', function($q, $timeout, $resource, GetTzRegionMap) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetTzRegionMap.get(params, function(data) {
                deferred.resolve(data);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

services.factory('GetTzDistrictMap', function($resource) {
    return $resource('/api/dashboard/tz-district.json', {}, {});
});


services.factory('GetTzDistrictMapData', function($q, $timeout, $resource, GetTzDistrictMap) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetTzDistrictMap.get(params, function(data) {

                deferred.resolve(data);

            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});



services.factory('GetTzRegionMap', function($resource) {
    return $resource('/api/dashboard/tz-reg.json', {}, {});
});


services.factory('GetTLEAndTLD', function($resource) {
    return $resource('/api/dashboard/getTLEAndTLDConsumption.json', {}, {});
});



services.factory('GetTLEAndTLDData', function($q, $timeout, $resource, GetTLEAndTLD) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetTLEAndTLD.get(params, function(data) {
                var TLEAndTLD = [];
                if (data !== undefined) {
                    TLEAndTLD = data.TLEAndTLDConsumption;
                }
                deferred.resolve(TLEAndTLD);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory('GetStockOutRateTrendOfTracerProducts', function($resource) {
    return $resource('/api/dashboard/getStockOutRateTrendOfTracerProducts.json', {}, {});
});



services.factory('GetStockOutRateTrendOfTracerProductsData', function($q, $timeout, $resource, GetStockOutRateTrendOfTracerProducts) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetStockOutRateTrendOfTracerProducts.get(params, function(data) {
                var stockOutRateTrendOfTracerProducts = [];
                if (data !== undefined) {
                    stockOutRateTrendOfTracerProducts = data.StockOutRateTrendOfTracerProducts;
                }
                deferred.resolve(stockOutRateTrendOfTracerProducts);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory('GetStockOutRateTrendOfProducts', function($resource) {
    return $resource('/api/dashboard/getStockOutRateTrendOfProducts.json', {}, {});
});



services.factory('GetStockOutRateTrendOfProductsData', function($q, $timeout, $resource, GetStockOutRateTrendOfProducts) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetStockOutRateTrendOfProducts.get(params, function(data) {
                var stockOutRateTrendOfProducts = [];
                if (data !== undefined) {
                    stockOutRateTrendOfProducts = data.StockOutRateTrendOfProducts;
                }
                deferred.resolve(stockOutRateTrendOfProducts);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

services.factory('GetGeoJsonInfo', function($resource) {
    return $resource('/api//dashboard/getGeoJsonInfo.json', {}, {});
});

services.factory('GetRegionalStockStatusSummary', function($resource) {
    return $resource('/api//dashboard/getRegionalStockStatusSummary.json', {}, {});
});


services.factory('GetLatestStockImbalanceReportByDistrictForTracer', function($resource) {
    return $resource('/api/dashboard/getLatestStockImbalanceReportByDistrictForTracer.json', {}, {});
});



services.factory('GetLatestStockImbalanceReportByDistrictForTracerData', function($q, $timeout, $resource, GetLatestStockImbalanceReportByDistrictForTracer) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetLatestStockImbalanceReportByDistrictForTracer.get(params, function(data) {
                var stockImbalanceByDistrict = [];
                if (data !== undefined) {
                    stockImbalanceByDistrict = data.stockImbalanceByDistrict;
                }
                deferred.resolve(stockImbalanceByDistrict);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory('GetLatestStockImbalanceReportByDistrictForProduct', function($resource) {
    return $resource('/api/dashboard/getLatestStockImbalanceReportByDistrictForProduct.json', {}, {});
});

services.factory('GetStockImbalanceSummary', function($resource) {
    return $resource('/api/dashboard/getStockImbalanceSummary.json', {}, {});
});



services.factory('GetLatestStockImbalanceReportByDistrictForProductData', function($q, $timeout, $resource, GetLatestStockImbalanceReportByDistrictForProduct) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetLatestStockImbalanceReportByDistrictForProduct.get(params, function(data) {
                var stockImbalanceByDistrict = [];
                if (data !== undefined) {
                    stockImbalanceByDistrict = data.stockImbalanceByDistrict;
                }
                deferred.resolve(stockImbalanceByDistrict);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

services.factory('GetStockImbalanceSummaryData', function($q, $timeout, $resource, GetStockImbalanceSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetStockImbalanceSummary.get(params, function(data) {
                var stockImbalanceByDistrict = [];
                if (data !== undefined) {
                    stockImbalanceByDistrict = data.imbalances;
                }
                deferred.resolve(stockImbalanceByDistrict);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

services.factory("GetCOVIDStockStatus", function($resource) {
    return $resource('/api/dashboard/getCOVIDStockStatus.json', {}, {});
});

services.factory('GetCOVIDStockStatusData', function($q, $timeout, $resource, GetCOVIDStockStatus) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetCOVIDStockStatus.get(params, function(data) {
                var stockStatus = [];
                if (data !== undefined) {
                    stockStatus = data.COVIDStockStatus;
                }
                deferred.resolve(stockStatus);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory("GetCOVIDReportByFacility", function($resource) {
    return $resource('/api/dashboard/getCOVIDReportByFacility.json', {}, {});
});

services.factory('getCOVIDReportByFacilityData', function($q, $timeout, $resource, GetCOVIDReportByFacility) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetCOVIDReportByFacility.get(params, function(data) {
                var stockStatus = [];
                if (data !== undefined) {
                    stockStatus = data.COVIDReportByFacility;
                }
                deferred.resolve(stockStatus);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});



services.factory("GetInboundReports", function($resource) {
    return $resource('/api/dashboard/getInboundReports.json', {}, {});
});

services.factory('GetInboundReportsData', function($q, $timeout, $resource, GetInboundReports) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetInboundReports.get(params, function(data) {
                var inboundReports = [];
                if (data !== undefined) {
                    inboundReports = data.inboundReports;
                }
                deferred.resolve(inboundReports);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

services.factory("GetCasesPerDesignatedFacilities", function($resource) {
    return $resource('/api/dashboard/getCasesPerDesignatedFacilities.json', {}, {});
});

services.factory('GetCasesPerDesignatedFacilitiesData', function($q, $timeout, $resource, GetCasesPerDesignatedFacilities) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetCasesPerDesignatedFacilities.get(params, function(data) {
                var cases = [];
                if (data !== undefined) {
                    cases = data.cases;
                }
                deferred.resolve(cases);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});


services.factory("GetCummulativeCasesTrend", function($resource) {
    return $resource('/api/dashboard/getCummulativeCasesTrend.json', {}, {});
});

services.factory('GetCummulativeCasesTrendData', function($q, $timeout, $resource, GetCummulativeCasesTrend) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function() {
            GetCummulativeCasesTrend.get(params, function(data) {
                var cases = [];
                if (data !== undefined) {
                    cases = data.cases;
                }
                deferred.resolve(cases);
            });

        }, 100);
        return deferred.promise;
    }
    return {
        get: get
    };
});

