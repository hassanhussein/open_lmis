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


services.factory('StockStatusSummary', function ($resource) {
    return $resource('/api/dashboard/stock-status-summary.json', {}, {});
});
