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
function ReportsController($scope,$filter,$window, $location, reports, $routeParams, $timeout, GetReportDataValue,GetSiteList,OperationYears) {
$scope.years=null;
$scope.getYears=function(){
OperationYears.get(function (data) {
                    $scope.years = data.years;

                });

}

$scope.getYears();



$scope.yearSelected=null;

  $scope.categories = ['WMS reports'];
  $scope.report_list = [
                     {'id':1,code:'ssr', name:'Stock Status Report'},
                      {'id':2,code:'dr', name:'Distribution Report'}
                     ];

  var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];




 $scope.getMonths=function(){
 if($scope.yearSelected){
  var currentDate=new Date();
  var currentYear=currentDate.getFullYear();
  var currentMonth=currentDate.getMonth();
  if(parseInt($scope.yearSelected,10)===currentYear){


  $scope.months=months.slice(0,currentMonth+1);
  return;
  }
  $scope.months=months;
 }


 }

  $scope.yearChanged=function(){
    console.log($scope.yearSelected)

     $scope.getMonths()
   }

$scope.getSites=function(){
GetSiteList.get({}, function (data) {
        $scope.sites=data.sites
      });

}

$scope.getSites();




}

ReportsController.resolve = {
  reports: function ($q, $timeout) {
    var deferred = $q.defer();
    $timeout(function () {
     // CustomReportList.get(function (data) {
        deferred.resolve([]);
     // });
    }, 100);
    return deferred.promise;
  }
};
