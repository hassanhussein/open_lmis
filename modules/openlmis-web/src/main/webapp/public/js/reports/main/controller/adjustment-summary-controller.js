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

function AdjustmentSummaryReportController($scope,SettingsByKey, $window,$routeParams, $q, AdjustmentSummaryReport) {

$scope.pageSize = 10;
$scope.currentPage = 1;


  SettingsByKey.get({key: 'ADJUSTMENT_COLOR_THRESHOLD_LOW'}, function (data) {
    $scope.low = data.settings.value;
  });


    SettingsByKey.get({key: 'ADJUSTMENT_COLOR_THRESHOLD_MEDIUM'}, function (data) {
      $scope.medium = data.settings.value;
    });

    SettingsByKey.get({key: 'ADJUSTMENT_COLOR_THRESHOLD_HIGH'}, function (data) {
      $scope.high = data.settings.value;
    });



$scope.colored = ($scope.$routeParams !==undefined)?$scope.$routeParams.color:$scope.$routeParams;
console.log($routeParams.color);

    $scope.updateColor = function (number) {
      var color;
      if(number <= parseInt($scope.low,10)){
      color = 'green';
      } else if(number > parseInt($scope.low, 10) && number <= parseInt( $scope.medium, 10)) {
      color = 'orange';
      } else {
          color =  'red';
      }
     return {'background-color':color};
    };

    $scope.getPercentageTotal = function (adjustment, total){
         var totalV = 0;

         if(total > 0){
          totalV  = parseInt(adjustment,10) /parseInt(total,10) * 100;
         }

        return Math.round(Math.abs(totalV));
    };
    $scope.exportReport = function (type) {

             $scope.filter.limit = 1000;
             $scope.filter.page  = 1;

             var allow = $scope.allPrinting($scope.getSanitizedParameter());

             allow.then(function(){
                $scope.filter.pdformat = 1;
                     var url = '/reports/download/adjustment_summary/' + type + '?' + jQuery.param($scope.getSanitizedParameter());
                     $window.open(url, '_blank');
             });


     };


     $scope.allPrinting = function(params){

                 var deferred = $q.defer();

                   AdjustmentSummaryReport.get(params, function (data) {

                   if(data.openLmisResponse.rows.length  > 0){

                        deferred.resolve();
                   }

                   });


          return deferred.promise;

     };


	$scope.OnFilterChanged = function () {
		// clear old data if there was any
		$scope.data = $scope.datarows = [];
		$scope.filter.max = 10000;
		$scope.filter.limit = $scope.pageSize;
		$scope.filter.page = $scope.page;

		//variable to manage counts on pagination
		$scope.countFactor = $scope.pageSize * ($scope.page - 1);




		AdjustmentSummaryReport.get($scope.getSanitizedParameter(), function (data) {
			if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
				$scope.data = data.openLmisResponse.rows;
				$scope.pagination = data.openLmisResponse.pagination;
				$scope.totalItems = 1000;
				//check if this is last page and reduce totalItemSize so user can not go to next page
				if (data.openLmisResponse.rows.length !== parseInt($scope.filter.limit, 10)) {$scope.totalItems = $scope.pageSize * $scope.page;
				}
				$scope.currentPage = $scope.pagination.page;
				$scope.tableParams.total = $scope.totalItems;
				$scope.paramsChanged($scope.tableParams);
			}
		});
	};


	$scope.$watch('currentPage', function () {
		if ($scope.currentPage > 0) {
			$scope.page = $scope.currentPage;
			$scope.OnFilterChanged();
		}
	});

}