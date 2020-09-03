/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *   Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 *   This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('manualTestTypes', ['openlmis', 'ui.bootstrap', 'ui.bootstrap.dropdownToggle', 'ui.bootstrap.pagination', 'ui.bootstrap.modal', 'ui.bootstrap.dialog']).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/create', {
        controller: ManualTestTypeController,
        templateUrl: 'partials/create.html',
        mode: 'NEW'
    }).when('/list', {
        controller: ManualTestTypeController,
        templateUrl: 'partials/list.html',
        mode: 'LIST'
    }).when('/edit/:id', {
        controller: ManualTestTypeController,
        templateUrl: 'partials/create.html',
        mode: 'EDIT'
    }).when('/create-result-category', {
        controller: ManualTestResultCategoryController,
        templateUrl: 'partials/create-result-category.html',
        mode: 'NEW'
    }).when('/list-result-category', {
        controller: ManualTestResultCategoryController,
        templateUrl: 'partials/list-result-category.html',
        mode: 'LIST'
    }).when('/edit-result-category/:id', {
        controller: ManualTestResultCategoryController,
        templateUrl: 'partials/create-result-category.html',
        mode: 'EDIT'
    }).when('/create-result-type', {
        controller: ManualTestResultTypeController,
        templateUrl: 'partials/create-result-type.html',
        mode: 'NEW'
    }).when('/list-result-type', {
        controller: ManualTestResultTypeController,
        templateUrl: 'partials/list-result-type.html',
        mode: 'LIST'
    }).when('/edit-result-type/:id', {
        controller: ManualTestResultTypeController,
        templateUrl: 'partials/create-result-type.html',
        mode: 'EDIT'
    }).when('/chat', {
        controller: ChatController,
        templateUrl: 'partials/chat.html',
        mode: 'chat'
    }).otherwise({redirectTo: '/list'});

}]).run(function($rootScope,$http, $sce) {
    var url = "https://jshz.3cx.co.za:5001/";
    var trust = $sce.trustAsResourceUrl(url);

    $http.jsonp(trust,{params: {format:'jsonp'}})
        .then(function(response) {
            console.log(response);
            $rootScope.response = response.data;
        }).catch(function(response) {
        console.log(response);
        $rootScope.response = 'ERROR: ' + response.status;
    });
});

