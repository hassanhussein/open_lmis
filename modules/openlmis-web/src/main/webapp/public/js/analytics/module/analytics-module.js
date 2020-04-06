/**
 * Created by hassan on 10/30/17.
 */

/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015 Clinton Health Access Initiative (CHAI)/MoHCDGEC Tanzania.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the GNU Affero General Public License for more details.
 */

var app  = angular.module('analytics', ['openlmis','ui.router','leaflet-directive','google-maps', 'ngGrid', 'ui.bootstrap.dialog', 'ui.bootstrap.accordion',
    'ui.bootstrap.modal','ui.bootstrap.pagination', 'ui.bootstrap.dropdownToggle',
    'angularUtils.directives.uiBreadcrumbs','ng-breadcrumbs','ncy-angular-breadcrumb','angularCombine',
    'ngTable','ui.bootstrap.pagination', 'tree.dropdown','rxDataTable','ngAnimate'
]);
///Start

app.config(function($stateProvider, $urlRouterProvider, $breadcrumbProvider,$httpProvider){

    var states = [
        {
            name: 'home',
            url: '/home',
            templateUrl: 'partials/analytics2.html',
            controller:AnalyticsFunction,
            resolve:AnalyticsFunction.resolve,
            ncyBreadcrumb: {
                label: 'Supply Chain Dashboard'
            }
        } ,
           {
            name: 'emergency',
            url: '/emergency',
            templateUrl: 'partials/analytics_emergency.html',
            controller:AnalyticsFunction,
            resolve:AnalyticsFunction.resolve
        },  {
                      name: 'rejectionByZoneView',
                      url: '/zone:zone/:value/:status/:period/:program',
                      templateUrl: 'partials/rejection-by-zone.html',
                      controller: 'RejectionByZoneControllerFunction',
                      ncyBreadcrumb: {
                          label: 'Rejection of {{zone}}',
                          parent: 'home'
                      }
                  }
    ];

    states.forEach($stateProvider.state);


    $urlRouterProvider.otherwise('/home');
   $httpProvider.interceptors.push('DashBoardResourceLoadingInterceptor');
}).config(function(angularCombineConfigProvider) {
    angularCombineConfigProvider.addConf(/filter-/, '/public/pages/reports/shared/filters.html');
})
    .filter('routeActive', function($state, $breadcrumb) {
        return function(route, steps) {
            for(var i = 0, j = steps.length; i < j; i++) {
                if(steps[i].name === route.name) {
                    return steps[i];
                }
            }

            return false;
        };
    }).filter('positive', function() {
        return function(input) {
            if (!input) {
                return 0;
            }

            return Math.abs(input);
        };
    })
    .controller('ElementCtrl', function($scope, $stateParams){
        $scope.idElement = $stateParams.idElement;
    }).service('DashBoardResourceLoadingInterceptor', ['resourceLoadingConfig', function(resourceLoadingConfig) {
        var service = this;

        this.request = function(config) {
         /*  if(config.params && config.params.associatedDashlets) {
                console.log(config.params.associatedDashlets);
                config.params.associatedDashlets.forEach(function(dashlet) {
                    angular.element('#'+dashlet+'').show();
                });
                config.dashlets = config.params.associatedDashlets;
                delete config.params.associatedDashlets;
           }*/
            return config;
        };

        this.response = function(response) {
      /*      if(response.config.dashlets) {
                response.config.dashlets.forEach(function(dashlet) {
                    angular.element('#'+dashlet+'').hide();
                });
            }*/
           return response;
        };
    }]);


