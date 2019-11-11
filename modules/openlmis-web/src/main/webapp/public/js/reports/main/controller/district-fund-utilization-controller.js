function FacilityFundUtilizationReportController ($scope, $window,FacilityFundUtilizationReport) {

    $scope.OnFilterChanged = function(){
         // clear old data if there was any
         $scope.data = $scope.datarows = [];

         $scope.filter.max = 10000;
        $scope.filter.page = 1;

         FacilityFundUtilizationReport.get($scope.getSanitizedParameter(), function (data) {
         console.log(data);
             if (data.pages !== undefined && data.pages.rows !== undefined) {

              var pivotedData = getPivotData(data.pages.rows, 'sourceofFundName');
              console.log(pivotedData);

                 $scope.data = pivotedData.pivotData;
                 $scope.headers = pivotedData.header;
                // groupDataInColumns($scope.data);
                 console.log(JSON.stringify($scope.data));
                $scope.paramsChanged($scope.tableParams);
             }
         });
     };


     function getPivotData(dataArray, colName) {

            var newCols = [];
            var pivotData = [];
            for (var i = 0; i < dataArray.length; i++) {
                if (newCols.indexOf(dataArray[i][colName]) < 0) {

                  newCols.push(dataArray[i][colName]);

                }
                var pivotRow = {};


                    pivotRow = {
                        "facility": dataArray[i].facilityName,
                        "facilityType": dataArray[i].facilityType,
                        "zone_name": dataArray[i].zone_name,
                        "facilityCode": dataArray[i].facilityCode,
                        "region": dataArray[i].region_name,
                        "district": dataArray[i].district_name,

                    };

                    pivotData.push(pivotRow);


                pivotRow[dataArray[i][colName]] = dataArray[i].quantity;



            }
            return {"header": newCols, "pivotData": pivotData};
        }

     $scope.exportReport = function (type) {
         $scope.filter.pdformat = 1;
         var params = jQuery.param($scope.getSanitizedParameter());
         var url = '/reports/download/district_fund_utilization/' + type + '?' + params;
         $window.open(url);
     };

}