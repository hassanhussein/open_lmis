app.directive('dashlet',['$compile', 'dashBoardService', function($compile, dashBoardService){
    return {
        restrict: 'EA',
        controller : ['$scope', function ($scope) {

           var vm = $scope;
           vm.configs = vm.configs || angular.copy(vm.persistedConfigs) || {};

           vm.showConfigForDashlet = function(dashletName) {
                 dashBoardService.dashletConfigShow(dashletName);
           };

           vm.isConfigVisibleForDashlet = function(dashletName) {
                return dashBoardService.dashletConfigIsVisible(dashletName);
           };

           vm.cancelConfigForDashlet = function(dashletName) {
                delete vm.configs[dashletName];
                dashBoardService.dashletConfigHide(dashletName, vm.configs);
           };

           vm.anyDashletConfigVisible = function() {
                return dashBoardService.dashletConfigsGetAllVisible().length > 0;
           };

           vm.closeAllVisibleDashletConfig = function() {
                dashBoardService.dashletConfigsClearAllVisible();
                vm.closeHiddenDashletsShowedForConfiguration();
                vm.configs = {};
           };

           vm.isDashBoardHiddenByUser = function(dashletName) {
              return vm.persistedConfigs[dashletName] !== undefined && vm.persistedConfigs[dashletName].showDashlet === false;
           };

           vm.isDashBoardVisibleForConfiguration = function(dashletName) {
               return vm.persistedConfigs[dashletName].showHiddenDashlet === true;
           };

           vm.isDashBoardVisible  = function(dashletName) {
                return !vm.isDashBoardHiddenByUser(dashletName) || vm.isDashBoardVisibleForConfiguration(dashletName);
           };

           vm.showHiddenDashletsForConfiguration = function() {
                Object.keys(vm.persistedConfigs).forEach(function(dashlet, index) {
                       if(!vm.persistedConfigs[dashlet].showDashlet) {
                            vm.persistedConfigs[dashlet].showHiddenDashlet = true;
                            vm.showConfigForDashlet(dashlet);
                       }
                });
           };

           vm.closeHiddenDashletsShowedForConfiguration = function() {
               Object.keys(vm.persistedConfigs).forEach(function(dashlet, index) {
                     if(vm.persistedConfigs[dashlet].showHiddenDashlet) {
                          vm.persistedConfigs[dashlet].showHiddenDashlet = false;
                          vm.cancelConfigForDashlet(dashlet);
                     }
               });
          };

           $scope.$watch('configs', function (oldValue, newValue, scope) {
                dashBoardService.setDashletConfigs(vm.configs);
           });
        }],
        terminal: true,
        link: function (scope, elm, attr) {

            var dashboardContentSection = angular.element(elm[0].querySelector('.overlay-section'));
            var dashboardConfigSection = angular.element(elm[0].querySelector('.dashlet-config'));

            var loadingOverlay = angular.element('<div class="dashlet-overlay" id="'+attr.dashlet+'"  ng-hide="persistedConfigs.'+attr.dashlet+'.showHiddenDashlet"></div>');
            loadingOverlay.append('<div>Loading...</div>');

            var dashletConfigOverLay = angular.element('<div ng-show="isConfigVisibleForDashlet(\''+attr.dashlet+'\')" class="dashlet-config-overlay" id="'+attr.dashlet+'_config"></div>');
            dashletConfigOverLay.append('<div><h4 style="color: #000;">Change the visibility of this dash-let.</h4><div class="btn-group btn-group-sm" data-toggle="buttons">'+
                                           '<label class="btn btn-default" style="text-transform: none" ng-click="configs.'+attr.dashlet+'.showDashlet = true" ng-class="{\'active\' : configs.'+attr.dashlet+'.showDashlet !== false}">'+
                                             '<input type="radio"  ng-model="configs.'+attr.dashlet+'.showDashlet" value="true"> Show'+
                                           '</label>'+
                                           '<label class="btn btn-default btn-sm" style="text-transform: none" ng-click="configs.'+attr.dashlet+'.showDashlet = false" ng-class="{\'active\' : configs.'+attr.dashlet+'.showDashlet === false}">'+
                                         '<input type="radio" ng-model="configs.'+attr.dashlet+'.showDashlet" value="false"> Hide'+
                                        '</label>'+
                                         '</div>'+
                                         '<hr style="margin: 45px; border: 1px solid #c1c1c1;"/>'+
                                         '<input ng-hide="persistedConfigs.'+attr.dashlet+'.showHiddenDashlet" type="button" class="btn btn-danger btn-sm" value="Cancel" ng-click="cancelConfigForDashlet(\''+attr.dashlet+'\')"/>'+
                                         '</div>');

            var configButton = angular.element('<i class="glyphicon glyphicon-cog" ng-click="showConfigForDashlet(\''+attr.dashlet+'\')"></i>');

            $compile(loadingOverlay)(scope);
            $compile(dashletConfigOverLay)(scope);
            $compile(configButton)(scope);

            dashboardContentSection.append(loadingOverlay);
            dashboardContentSection.append(dashletConfigOverLay);
            dashboardConfigSection.append(configButton);

            if(!elm.attr('ng-if')) {
                //elm.attr('ng-if', 'persistedConfigs.'+attr.dashlet+'.showDashlet !== false');
                elm.attr('ng-if', 'isDashBoardVisible(\''+attr.dashlet+'\')');
                elm.removeAttr('dashlet');
                $compile(elm)(scope);
            }
        }
    };
}]);