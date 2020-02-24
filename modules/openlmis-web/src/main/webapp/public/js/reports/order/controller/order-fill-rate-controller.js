function OrderFillRateControllerFunc($scope,GeoDistrictTree,AggregateItemFillRateSummary,$timeout,AggregateItemFillRateByOrder) {

      $scope.calculateAverageItemFillRate = function(orderList){
      var sum = 0;
      for(var i=0; i< orderList.length;i++) {
       sum += parseInt((orderList[i].receivedQuantity/orderList[i].approvedQuantity )*100,10);
      }
       var result = sum/ orderList.length;
       return Math.round(result);
      };

    var filter = {};

   // filter.schedule=2; filter.period=91; filter.year=2019; filter.program=1; filter.max=10000;filter.page=1; filter.limit=10000;

     console.log($scope.filter);
     $scope.dataRow =$scope.filteredFacilityData =[];
     //$scope.filter = filter;
      $scope.OnFilterChanged = function() {

        $scope.data = $scope.datarows = [];
       /* $scope.filter.max = 10000;
        $scope.filter.page = 1;
        $scope.filter.limit = 10000;*/

        var filteredFacilityData = [];

        AggregateItemFillRateSummary.get($scope.filter, function(data) {

           filteredFacilityData = data.openLmisResponse.rows;

           var groupByDistrictData = _.groupBy(data.openLmisResponse.rows, 'district');

           var districtMappedData = _.map(groupByDistrictData, function(orders, index){

           var mapFacilities = _.map(orders, function(dt, ind){
              return {'text':dt.facilityName+' : <strong>Aggregate Order Fill Rate</strong>: <strong>'+$scope.calculateAverageItemFillRate([dt])+'%</strong>','orderId':dt.rnrId,'facilityName':dt.facilityName};
           });

           var facilities  = _.each(orders, function(data){
           return {'text':data.facilityName, 'nodes':[]};
           });

           return {'level':1,'text':index+' : <strong>Aggregate Order Fill Rate</strong>:<strong>'+$scope.calculateAverageItemFillRate(facilities)+'%</strong>', 'nodes':mapFacilities, 'region':facilities[0].region,'msdZone':facilities[0].msdZone,'total':$scope.calculateAverageItemFillRate(facilities)};

          });

          var regionData = _.groupBy(districtMappedData,'region');

          var mapRegion = _.map(regionData, function(regions, index) {

               var sum = 0;
               _.each(regions, function(data){
                 sum += data.total;
               });
              return {'level':2,'text':index+' : <strong>Aggregate Order Fill Rate</strong>:<strong>'+Math.round(sum/regions.length)+'%</strong>', 'nodes':regions, 'msdZone':regions[0].msdZone, 'total': Math.round(sum/regions.length)};
          });

          var groupByZone = _.groupBy(mapRegion, 'msdZone');

          var mapZone = _.map(groupByZone, function(zones, index){

                    var sum = 0;
                               _.each(zones, function(data){
                                 sum += data.total;
                               });

               return {'level':3,'text':index+' : <strong>Aggregate Order Fill Rate</strong>:<strong>'+Math.round(sum/zones.length)+'%</strong>', 'nodes':zones, 'total': Math.round(sum/zones.length)};

          });






              console.log(mapZone);

           //$scope.dataRow = mappedData;


                                  $('#tree').treeview({
                                            data: mapZone,
                                            levels: 4,
                                            color: "#398085",
                                            onhoverColor: 'lightblue',
                                            onNodeSelected: function (event, data) {
                                                     $scope.getDataForDisplay(data, filteredFacilityData);
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

        $scope.mappedFacilities = [];

       $scope.getDataForDisplay = function (data, allData){

       $timeout(function(){

           if(data.level === undefined) {


               var filterData = _.where(allData, {facilityName: data.facilityName});

               var groupByFacility = _.groupBy(filterData, 'facilityName');

              var mappedD = _.map(groupByFacility, function(data,index) {

                 return {'facilityName':index, 'district':data[0].district, 'msdZone': data[0].msdZone, 'orderList':data};
               });

              $scope.mappedFacilities = mappedD;
               $scope.$apply();
                        console.log($scope.mappedFacilities);

               }


       },100);



        };



  $scope.getDetails = function(rnr, row){

    console.log($scope.filter);

   var param = angular.extend($scope.filter, {'orderId':parseInt(rnr,10)});

   AggregateItemFillRateByOrder.get(param, function(data) {
     row.items = data.openLmisResponse.rows;
     $scope.allTheData = row;



        if (data.openLmisResponse !== undefined && data.openLmisResponse.rows !== undefined) {
          console.log(data);
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
   $scope.successModal = true;
  };


}