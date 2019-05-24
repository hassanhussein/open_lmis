/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
function MainDeskController($scope, $location, faqArticles, archive) {

    $scope.faqArticles = faqArticles;
    $scope.archive = archive;

    $scope.viewFAQContent = function(contentNumber) {
        $location.path($location.path().replace(getURLName(), 'articles')).search({
            param: $scope.faqArticles.values[contentNumber].content.iframeSrc
        });
    };

    $scope.viewArchiveContent = function(contentNumber) {
        $location.path($location.path().replace(getURLName(), 'articles')).search({
            param: $scope.archive.values[contentNumber].content.iframeSrc
        });
    };

    function getURLName() {
        var urlParts = $location.path().split('/');
        return urlParts[urlParts.length - 1];
    }


    $scope.search = function() {
        $location.path($location.$$path.replace(getURLName(), 'search-result')).search({
            param: $scope.support.query
        });

    };

    $scope.changeRoute = function(routeName) {
        $location.path($location.$$path.replace(getURLName(), routeName));
    };
}


MainDeskController.resolve = {
    faqArticles: function($route, $q, $timeout, searchSDArticles) {
       //var url = document.referrer;
       //var urlArray = url.split('/');
       // var searchKeyword = urlArray[5];
        var deferred = $q.defer();
        $timeout(function() {
            searchSDArticles.get({
                query: "faq"
            }, function(data) {
                deferred.resolve(data);
            });

        }, 100);
        return deferred.promise;
    },
    archive: function($route, $q, $timeout, searchSDArticles) {
       // var url = document.referrer;
       // var urlArray = url.split('/');
       // var searchKeyword = urlArray[5];
        var deferred = $q.defer();
        $timeout(function() {
            searchSDArticles.get({
                query: "archive"
            }, function(data) {
                deferred.resolve(data);
            });

        }, 100);
        return deferred.promise;
    }
};