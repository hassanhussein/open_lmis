/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */


var receiveModule = angular.module('receive', ['openlmis', 'ui.bootstrap.modal','leaflet-directive', 'ui.bootstrap.dialog', 'ui.bootstrap.dropdownToggle', 'ui.bootstrap.pagination', 'ngDraggable','lr.upload','ngFileUpload','angularFileUpload']).
    config(['$routeProvider', function ($routeProvider) {
      $routeProvider.
          when('/list', {controller: ReceiveSearchController, templateUrl: 'partials/list.html'}).
          when('/create', {controller: ReceiveController, templateUrl: 'partials/create.html', resolve: ReceiveController.resolve}).
          when('/edit/:id', {controller: ReceiveController, templateUrl: 'partials/create.html', resolve: ReceiveController.resolve}).
          otherwise({redirectTo: '/list'});
    }]).run(function ($rootScope, AuthorizationService) {
      $rootScope.receiveSelected = "selected";
//      AuthorizationService.preAuthorize('MANAGE_ASN');
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

