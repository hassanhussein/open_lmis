function MetabaseDashboardConroller($scope, $sce, dashboardUrls) {
    $scope.dashboardUrls = dashboardUrls;
    $scope.trustSrc = function (src) {
        return $sce.trustAsResourceUrl(src);
    };
    $scope.testUrl = {src: $scope.dashboardUrls[0].value };

}

MetabaseDashboardConroller.resolve = {
    dashboardUrls: function ($q, $timeout, SettingsByGroup) {
        var deffered = $q.defer();
        $timeout(function () {
            SettingsByGroup.get({name:"Main Dashboard Dashlets"}, function (data) {
                deffered.resolve(data.settings);

            }, {});
        }, 100);
        return deffered.promise;
    }
};