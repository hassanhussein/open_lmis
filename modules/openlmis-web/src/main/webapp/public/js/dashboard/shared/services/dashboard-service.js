services.factory('dashBoardService',function($rootScope,$location){

    var service = {};

    var dashletConfigs = {};
    var visibleConfigsDashlets = [];

    service.dashletConfigShow = function(dashletName) {
        visibleConfigsDashlets.push(dashletName);
    };

    service.dashletConfigIsVisible = function(dashletName){
        return visibleConfigsDashlets.includes(dashletName);
    };

    service.dashletConfigHide = function(dashletName) {
        visibleConfigsDashlets = visibleConfigsDashlets.filter(function(element) {
            return element !== dashletName;
        });
    };

    service.dashletConfigsGetAllVisible = function() {
        return  visibleConfigsDashlets;
    };

    service.dashletConfigsClearAllVisible = function() {
         visibleConfigsDashlets = [];
    };

    service.dashletConfigShowAllConfigValues = function () {
        return dashletConfigs;
    };

    service.setDashletConfigs = function(configs) {
        dashletConfigs = configs;
    };

    return service;
});