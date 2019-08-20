/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
function SearchResultController($scope, $location, searchResults, faqArticles) {
    $scope.searchResults = searchResults;
    $scope.faqArticles = faqArticles;


    $scope.viewContent = function(contentNumber) {
        $location.path($location.$$path.replace(getURLName(), 'articles')).search({
            param: $scope.searchResults.values[contentNumber].content.iframeSrc,
            title: $scope.searchResults.values[contentNumber].title
        });
    };

     $scope.viewFAQContent = function(contentNumber) {
            $location.path($location.$$path.replace(getURLName(), 'articles')).search({
                param: $scope.faqArticles.values[contentNumber].content.iframeSrc,
                title: $scope.faqArticles.values[contentNumber].title
            });
        };

     function getURLName() {
        var urlParts = $location.$$path.split('/');
        return urlParts[urlParts.length - 1];
      }
}

SearchResultController.resolve = {
    searchResults: function($route, $q, $timeout, searchSDArticles) {
        var searchKeyword = $route.current.params.param;
        console.log(searchKeyword);
        var deferred = $q.defer();
        $timeout(function() {
            searchSDArticles.get({
                query: searchKeyword
            }, function(data) {
                deferred.resolve(data);
            });

        }, 100);
        return deferred.promise;
    },
    faqArticles: function($route, $q, $timeout, searchSDArticles) {
        var url = document.referrer;
        var urlArray = url.split('/');
        var searchKeyword = urlArray[5];
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