function MetabaseDashboardConroller($scope, $sce,metabaseNavService, localStorageService,dashboardUrls) {
    $scope.dashboardUrls = dashboardUrls;
    var a=localStorageService.get("metabasePage");
    $scope.trustSrc = function (src) {
        return $sce.trustAsResourceUrl(src);
    };
    $scope.testUrl = {src: a };
    $('#externalPage').ready(function () {
        $('#loadingMessage').css('display', 'block');
        $scope.inprogress=true;
    });
    $('#externalPage').load(function () {
        $('#loadingMessage').css('display', 'none');
    });
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