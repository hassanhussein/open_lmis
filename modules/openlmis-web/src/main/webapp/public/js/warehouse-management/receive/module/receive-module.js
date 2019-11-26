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
          when('/view/:id', {controller: ReceiveController, templateUrl: 'partials/create.html', resolve: ReceiveController.resolve}).
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
                       } ]).directive('ngDecimal', function(){
                                                                   return {
                                                                       restrict: 'A',
                                                                       link: function($scope, $element, $attributes){
                                                                           var limit = $attributes.ngDecimal;
                                                                           function caret(node) {
                                                                               if(node.selectionStart) {
                                                                                   return node.selectionStart;
                                                                               }
                                                                               else if(!document.selection) {
                                                                                   return 0;
                                                                               }
                                                                               //node.focus();
                                                                               var c		= "\001";
                                                                               var sel	= document.selection.createRange();
                                                                               var txt	= sel.text;
                                                                               var dul	= sel.duplicate();
                                                                               var len	= 0;
                                                                               try{ dul.moveToElementText(node); }catch(e) { return 0; }
                                                                               sel.text	= txt + c;
                                                                               len		= (dul.text.indexOf(c));
                                                                               sel.moveStart('character',-1);
                                                                               sel.text	= "";
                                                                               return len;
                                                                           }
                                                                           $element.bind('keypress', function(event){
                                                                           	var charCode = (event.which) ? event.which : event.keyCode;
                                                                           	var elem=document.getElementById($element.attr("id"));
                                                                           	if (charCode == 45){
                                                                                   var caretPosition=caret(elem);
                                                                                   if(caretPosition==0){
                                                                                   	if($element.val().charAt(0)!="-" ){
                                                                                   		if($element.val() <=limit){
                                                                                               $element.val("-"+$element.val());
                                                                                           }
                                                                                       }
                                                                                       if($element.val().indexOf("-")!=-1){
                                                                                       	event.preventDefault();
                                                                                           return false;
                                                                                       }
                                                                                   }
                                                                                   else{
                                                                                   	event.preventDefault();
                                                                                   }
                                                                               }
                                                                               if (charCode == 46){
                                                                                   if($element.val().length>limit-1){
                                                                                   	event.preventDefault();
                                                                                   	return false;
                                                                                   }
                                                                                   if ($element.val().indexOf('.') !=-1){
                                                                                   	event.preventDefault();
                                                                                       return false;
                                                                                   }
                                                                                   return true;
                                                                               }
                                                                               if (charCode != 45 && charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)){
                                                                               	event.preventDefault();
                                                                                   return false;
                                                                               }
                                                                               if($element.val().length>limit-1){
                                                                               	event.preventDefault();
                                                                               	return false;
                                                                               }
                                                                               return true;
                                                                           });
                                                                       }
                                                                   };
                                                               });

