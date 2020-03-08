function AggregateStockStatusReportFunction($window,$scope, AggregateStockStatusReport, $state, Periods

) {
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
  $scope.showConsumption = false;
  $scope.showIndicators = false;
  $scope.tabToggle = function(data){

  if(data === 'mos')
  {
    $scope.showIndicators = true;
    $scope.searchReport();

  }else if(data === 'soh'){
  console.log($scope.filter.vewConsumption);
      $scope.showIndicators = false;
      $scope.searchReport();
  }

  };
$scope.showConsumption = false;

  $scope.searchConsumption = function(){
     console.log($scope.filter.vewConsumption);
        $scope.showIndicators = false;
           $scope.showConsumption= $scope.filter.vewConsumption;
                 $scope.searchReport();
  };


    $scope.searchReport = function () {

       var allParams = angular.extend($scope.filter, $scope.getSanitizedParameter());
        var selectDisggregate = $scope.filter.disaggregated;
        var showIndicators = $scope.showIndicators;
         var showConsumption = $scope.showConsumption;
        $scope.stockStatuses =  $scope.data = $scope.datarows = [];
        $scope.filter.max = 10000;
        AggregateStockStatusReport.get(allParams, function (data) {

            if (data.pages.rows !== undefined || null !== data.pages) {
            console.log(data.pages.rows);

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

                    var output = getPivotData(data.pages.rows, "periodName", "code", selectDisggregate,showIndicators,showConsumption);
                     $scope.data = output.pivotData;

                     $scope.periods = output.periods;

                $scope.paramsChanged($scope.tableParams);
            }
        });
    };
      $scope.ChangeColor = function(data){

                if(2<=data && data <= 4) {

                    return {'background-color':'#009F48','name':'SP'};

                }else if(data > 4) {

                    return {'background-color':'#00AFEE','name':'OS'};

                }else if(data >0 && data < 20) {

                  return {'background-color':'#F1C30E','name':'US'};

                }else if(data === 0) {

                  return {'background-color':'#FE0000','name':'SO'};

                }else {

                  return {'background-color':'#E3E3E3','name':'UK'};

                }


      };

    function getPivotData(dataArray, colName, dataIndex, disaggregated,showIndicators,showConsumption) {

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
                    "district": dataArray[i].district,
                    "minMonthsOfStock": dataArray[i].minMonthsOfStock,
                    "maxMonthsOfStock": dataArray[i].maxMonthsOfStock
                };

                pivotData.push(pivotRow);
            }
            if(!showIndicators && !showConsumption) {

            pivotRow[dataArray[i][colName]] = dataArray[i].soh;

            }else if(!showIndicators && showConsumption){

            pivotRow[dataArray[i][colName]] = dataArray[i].amc;

            } else  {

            pivotRow[dataArray[i][colName]] = dataArray[i].mos;

            }

        }
        return {"periods": newCols, "pivotData": pivotData};
    }


  $scope.exportReport = function(type) {

    $scope.filter.pdformat = 1;
    var params = jQuery.param($scope.getSanitizedParameter());
     var url = '/reports/download/status';
    if($scope.showIndicators !== true && !$scope.showConsumption){
         url = url + (($scope.filter.disaggregated === true) ? '_disaggregated' : '') + '/' + type + '?' + params;
         console.log(url);
    }else if($scope.showIndicators !== true && $scope.showConsumption){
         var url2 = '/reports/download/';
         url = url2 + (($scope.filter.disaggregated === true) ? 'amc_status_disaggregated' :'amc_status')+'/' + type + '?' + params;
    }else{
         url = url + (($scope.filter.disaggregated === true) ? '_mos_disaggregated' :'_mos')+'/' + type + '?' + params;
    }
    window.open(url, '_blank');

  };

}


AggregateStockStatusReportFunction.resolve = {};