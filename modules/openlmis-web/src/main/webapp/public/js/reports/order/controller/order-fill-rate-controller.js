function OrderFillRateControllerFunc($scope,GeoDistrictTree,AggregateItemFillRateSummary) {

$scope.data = 'mamamama';



/*
    GeoDistrictTree.get({}, function (data) {

        var data2 = data.regionFacilityTree;
        console.log(data2);
        $('#tree').treeview({
            data: data2,
            levels: 2,
            color: "#398085",
            onhoverColor: 'lightblue',
            onNodeSelected: function (event, data) {
                getDataForDisplay(data);
            }

        });

    });*/

      var calculateAverageItemFillRate = function(orderList){
      var sum = 0;
      for(var i=0; i< orderList.length;i++) {
       sum += parseInt((orderList[i].receivedQuantity/orderList[i].approvedQuantity )*100,10);
      }
       return sum/ orderList.length;
      };

    var filter = {};

    filter.schedule=2; filter.period=91; filter.year=2019; filter.program=1; filter.max=10000;filter.page=1; filter.limit=10000;

      $scope.OnFilterChanged = function() {

        $scope.data = $scope.datarows = [];
       /* $scope.filter.max = 10000;
        $scope.filter.page = 1;
        $scope.filter.limit = 10000;*/

        AggregateItemFillRateSummary.get(filter, function(data) {


           var groupByFacilityData = _.groupBy(data.openLmisResponse.rows, 'facilityName');

           var mappedData = _.map(groupByFacilityData, function(orders, index){

               return {"facilityName":index,"district":orders[0].district,"region":orders[0].region,"msdZone":orders[0].msdZone,"nodes":orders,"orf":calculateAverageItemFillRate(orders) };

           });

           //    console.log(mappedData);

           $scope.dataRow = mappedData;

                                console.log($scope.dataRow);

                                  $('#tree').treeview({
                                            data: mappedData,
                                            levels: 2,
                                            color: "#398085",
                                            onhoverColor: 'lightblue',
                                            onNodeSelected: function (event, data) {
                                                getDataForDisplay(data);
                                            }

                                        });


          if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {

           $scope.pagination = data.openLmisResponse.pagination;
           $scope.totalItems = 1000;
           $scope.currentPage = $scope.pagination.page;
           //$scope.tableParams.total = $scope.totalItems;
           //check if this is last page and reduce totalItemSize so user can not go to next page
           if (data.openLmisResponse.rows.length !== $scope.pageSize) {
           $scope.totalItems = $scope.pageSize * $scope.page;
           }
           $scope.data = data.openLmisResponse.rows;


          // $scope.paramsChanged($scope.tableParams);
          }
        });
      };

$scope.OnFilterChanged();


}