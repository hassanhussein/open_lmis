function VaccineDistributionSummary($scope, distribution) {

    $scope.distributions = {};

    if(distribution !== null){

        $scope.distributions = distribution;

    }

}

VaccineDistributionSummary.resolve = {

    distribution: function ($q, $timeout, $route, GetDistributionByIdData) {
        var deferred = $q.defer();
        $timeout(function () {
            GetDistributionByIdData.get({distributionId:parseInt($route.current.params.id, 10)}).then(function (data) {
                console.log(data);
                deferred.resolve(data);
            });
        }, 100);
        return deferred.promise;
    }  ,

    distribution: function ($q, $timeout, $route, StockCards) {
        var deferred = $q.defer();
        $timeout(function () {
            StockCards.get({distributionId:parseInt($route.current.params.id, 10)}).then(function (data) {
                console.log(data);
                deferred.resolve(data);
            });
        }, 100);
        return deferred.promise;
    }

};