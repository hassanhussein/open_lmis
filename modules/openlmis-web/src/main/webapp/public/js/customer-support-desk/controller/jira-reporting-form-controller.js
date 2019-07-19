/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
function JiraReportingFormController($scope, $location, $q, localStorageService, faqArticles, reportIssueOnSD, $timeout) {

    $scope.successMessage = "You have submitted issues successfully";
    $scope.faqArticles = faqArticles;

    $scope.createNewIssue = function() {

        $scope.createNewIssue = function() {
            var deferred = $q.defer();
            $timeout(function() {
                reportIssueOnSD.get({
                    summary: $scope.summary,
                    description: $scope.description,
                    reporterId: localStorageService.get(localStorageKeys.USER_ID)
                }, function(data) {
                    if (data.issueKey) {
                        deferred.resolve(data);
                        $('#myForm').trigger("reset");
                        $scope.successMessagebool = true;
                        $scope.summary = "";
                        $scope.description = "";
                    }
                });

            }, 100);
        };
    };
}

JiraReportingFormController.resolve = {
    faqArticles: function($route, $q, $timeout, searchSDArticles) {
        //var url = document.referrer;
       // var urlArray = url.split('/');
        //var searchKeyword = urlArray[5];
        var deferred = $q.defer();
        $timeout(function() {
            searchSDArticles.get({
                query: "faq"
            }, function(data) {
                deferred.resolve(data);
            });

        }, 100);
        return deferred.promise;
    }
};