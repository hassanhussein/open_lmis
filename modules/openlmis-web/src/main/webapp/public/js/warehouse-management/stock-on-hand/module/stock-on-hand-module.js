/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

var stockOnHandModule = angular.module('stock-on-hand', ['openlmis', 'ui.bootstrap.modal','leaflet-directive', 'ui.bootstrap.dialog', 'ui.bootstrap.dropdownToggle', 'ui.bootstrap.pagination', 'ngDraggable','angular.filter']).
    config(['$routeProvider', function ($routeProvider) {
      $routeProvider.
           when('/stock-on-hand', {controller: StockOnHandController, templateUrl: 'partials/stock-on-hand.html'}).
           when('/stock-ledger/:warehouseId/:productId/:year', {controller: StockLedgerController, templateUrl: 'partials/stock-ledger.html',resolve:StockLedgerController.resolve}).

            otherwise({redirectTo: '/search'});
    }]).run(function ($rootScope, AuthorizationService) {
      $rootScope.warehouseSelected = "selected";
//      AuthorizationService.preAuthorize('MANAGE_LOCATION');
    }).filter('positive', function() {
               return function(input) {
                   if (!input) {
                       return 0;
                   }

                   return Math.abs(input);
               };
           });
