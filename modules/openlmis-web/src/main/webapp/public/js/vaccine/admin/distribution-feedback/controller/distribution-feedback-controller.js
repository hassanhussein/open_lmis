function DistributionFeedbackController($scope,$window,$location,GetFacilityDistributionNotifications,GeoDistrictTree) {


    $scope.startDate = null;
    $scope.endDate = null;

    $scope.changeDates = function () {
        $scope.startDate = $scope.periodStartDate;
        $scope.endDate = $scope.periodEndDate;
    };

    function getDataForDisplay(data) {
        if(data.regionId === null)
            $scope.districtId = data.id;
    }

    $scope.getAllData = function(){
    if(($scope.endDate === null || $scope.endDate === undefined) && ($scope.startDate === null || $scope.startDate === undefined) && isUndefined($scope.districtId) ){
        return null;
    }else {
        var params = {districtId:parseInt($scope.districtId,10), startDate: $scope.startDate,endDate: $scope.endDate};

        GetFacilityDistributionNotifications.get(params, function (data) {
            $scope.distributions =data.notifications;
        });
    }

    };

    GeoDistrictTree.get({}, function (data) {

        var data2 = data.regionFacilityTree;

        $('#tree').treeview({
            data: data2,
            levels: 2,
            color: "#398085",
            onhoverColor: 'lightblue',
            onNodeSelected: function (event, data) {
                  getDataForDisplay(data);
            }

        });

    });
  $scope.openDistribution =  function (distributionId) {

      var url = '/public/pages/vaccine/admin/distribution-feedback/index.html#/search/' +  parseInt(distributionId,10);
      $window.open(url, '_blank');


    }
}