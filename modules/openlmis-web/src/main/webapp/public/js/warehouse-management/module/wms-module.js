/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
angular.module('wms', ['openlmis', 'ngTable',  'ui.chart', 'angularCombine' ,'ui.bootstrap', 'nsPopover', 'textAngular','lr.upload','ngFileUpload','angularFileUpload'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/list', {controller:WarehouseManagementController, templateUrl:'partials/list.html',reloadOnSearch:false}).
            when('/create', {controller:WarehouseManagementController, templateUrl:'partials/create.html'}).
            otherwise({redirectTo:'/list'});
    }]).config(function(angularCombineConfigProvider) {
        angularCombineConfigProvider.addConf(/filter-/, '/public/pages/reports/shared/filters.html');
    }).directive('validFile',[function() {
               return {
                 require : 'ngModel',
                 scope : {format: '@', upload : '&upload'},
                 link : function(scope, el, attrs, ngModel) {
                   // change event is fired when file is selected
                   el.bind('change', function(event) {
                     console.log(event.target.files[0]);
                     scope.upload({file:event.target.files[0]});
                     scope.$apply(function() {
                       ngModel.$setViewValue(el.val());
                       ngModel.$render();
                     });
                   });
                 }
               };
             }]).directive('fileModel', [ '$parse', function($parse) {
                    return {
                        restrict : 'A',
                        link : function(scope, element, attrs) {
                            var model = $parse(attrs.fileModel);
                            var modelSetter = model.assign;

                            element.bind('change', function() {
                                scope.$apply(function() {
                                    modelSetter(scope, element[0].files[0]);
                                });
                            });
                        }
                    };
                } ]);
