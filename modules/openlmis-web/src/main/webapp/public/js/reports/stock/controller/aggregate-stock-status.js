function AggregateStockStatusReportFunction($window,$scope, AggregateStockStatusReport, $state, Periods) {
    var definedObject = {};

    function getRenamedDate(periodListElement) {
        var dateToday = new Date(periodListElement.endDate);
        definedObject = periodListElement;
        var locale = "en-us";
        definedObject.month = dateToday.toLocaleString(locale, {month: "long"}) + ' ' + periodListElement.stringYear;
        return definedObject;
    }

    var headerCellData = function (periodToDisplay) {


        Periods.get({scheduleId: $scope.paramToBeUpdated.schedule}, function (data) {
            var periodList = data.periods;

            var filteredPeriods = [];
            for (var i = 0; i < periodList.length; i++) {
                if (_.contains(periodToDisplay, periodList[i].name)) {
                 /*   if (periodList[i].enableOrder)
                        filteredPeriods.push(getRenamedDate(periodList[i]));
                    else*/
                        filteredPeriods.push(periodList[i]);

                }
            }

            $scope.periodHeaders = filteredPeriods.sort(function (a, b) {
                return (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0);
            });


        });


    };

    $scope.isMOS = false;

    $scope.getFacilityStock = function (facility, period) {
        var f = _.findWhere($scope.data, {facilityName: facility});

        if (f !== undefined)
            p = _.findWhere(f.stocks, {periodName: period});
        if (p !== undefined) {

            return p;

        }
        else
            return null;
    };


  $scope.OnFilterChanged = function () {

    };

  $scope.showIndicators = false;
  $scope.tabToggle = function(data){

  if(data === 'mos')
  {
    $scope.showIndicators = true;
    $scope.searchReport();

  }else if(data === 'soh'){
      $scope.showIndicators = false;
          $scope.searchReport();
  }else{

      $scope.showIndicators = false;

  }

  };


    $scope.searchReport = function () {

       var allParams = angular.extend($scope.filter, $scope.getSanitizedParameter());
        var selectDisggregate = $scope.filter.disaggregated;
        var showIndicators = $scope.showIndicators;
        $scope.stockStatuses =  $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;
        AggregateStockStatusReport.get(allParams, function (data) {

            if (data.pages.rows !== undefined || null !== data.pages) {

           /*   $scope.facilityStocks = data.pages.rows;
              var groupByFacility = _.groupBy(data.pages.rows, function (f) {
                    return f.facility;
                });
    var periodData = _.pluck(data.pages.rows, 'periodName');

                var periodToDisplay = _.uniq(periodData);
                headerCellData(periodToDisplay);

                $scope.data = $.map(groupByFacility, function (value, index) {
                    return [{"facilityName": index, "stocks": value}];
                });*/

                //Adding new functions to pivot the data

                    var output = getPivotData(data.pages.rows, "periodName", "code", selectDisggregate,showIndicators);
                     $scope.data = output.pivotData;

                     $scope.periods = output.periods;

                $scope.paramsChanged($scope.tableParams);
            }
        });
    };

    function getPivotData(dataArray, colName, dataIndex, disaggregated,showIndicators) {

        var newCols = [];
        var pivotData = [];
        for (var i = 0; i < dataArray.length; i++) {
            if (newCols.indexOf(dataArray[i][colName]) < 0) {

                newCols.push(dataArray[i][colName]);
            }
            var pivotRow = {};
            if (utils.isNullOrUndefined(disaggregated) || disaggregated === false || disaggregated === 'false') {
                pivotRow = _.findWhere(pivotData, {code: dataArray[i][dataIndex]});

            } else {

                pivotRow = _.findWhere(pivotData, {
                    facilityId: dataArray[i].facilityId,
                    code: dataArray[i][dataIndex]
                });

            }
            if (pivotRow === null || pivotRow === undefined) {
                pivotRow = {
                    "facility": dataArray[i].facility,
                    "facilityType": dataArray[i].facilityType,
                    "facilityId": dataArray[i].facilityId,
                    "product": dataArray[i].product,
                    "code": dataArray[i].code,
                    "level": dataArray[i].level,
                    "district": dataArray[i].district

                };

                pivotData.push(pivotRow);
            }
            if(!showIndicators) {

            pivotRow[dataArray[i][colName]] = dataArray[i].soh;

            }
            else {

            pivotRow[dataArray[i][colName]] = dataArray[i].mos;

            }


        }

        return {"periods": newCols, "pivotData": pivotData};
    }



/*    $scope.OnFilterChanged = function () {

        if ($scope.filter !== undefined) {

            if ($scope.filter.zone === undefined)
                $scope.filter.zone = 0;
                $scope.filter.max = 1000;


            $scope.paramToBeUpdated = {
                program: parseInt($scope.filter.program, 10),
                schedule: parseInt($scope.filter.schedule, 10),
                product: parseInt($scope.filter.product, 10),
                zone: parseInt($scope.filter.zone, 10),
                periodStart: $scope.filter.periodStart,
                periodEnd: $scope.filter.periodEnd,
                max: $scope.filter.max,
                disaggregated:$scope.filter.disaggregated
            };
        }

    };*/


  $scope.exportReport = function(type) {

    $scope.filter.pdformat = 1;
    var params = jQuery.param($scope.getSanitizedParameter());
    var url = '/reports/download/aggregate_stock_status_report/' + type + '?' + params;
    $window.open(url, '_blank');
  };

}


AggregateStockStatusReportFunction.resolve = {};