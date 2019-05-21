/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
var customerSupportDeskModule = angular.module('customer-support-desk', ['openlmis', 'ui.bootstrap.modal', 'ui.bootstrap.dialog']).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/main-desk', {
        controller: MainDeskController,
        templateUrl: 'partials/main_desk.html',
        resolve: MainDeskController.resolve
    }).
    when('/search-result', {
        controller: SearchResultController,
        templateUrl: 'partials/search_results_desk.html',
        resolve: SearchResultController.resolve
    }).
    when('/jira-reporting-form', {
        controller: JiraReportingFormController,
        templateUrl: 'partials/jira_reporting_form.html',
        resolve: JiraReportingFormController.resolve
    }).
    when('/articles', {
        controller: KnowledgeBaseArticlesController,
        templateUrl: 'partials/knowledge_base_articles.html',
        resolve: {
            topicUrl: ['$route', function($route) {
                return $route.current.params.param;
            }]
        }
    }).
    otherwise({
        redirectTo: '/main-desk'
    });
}]);