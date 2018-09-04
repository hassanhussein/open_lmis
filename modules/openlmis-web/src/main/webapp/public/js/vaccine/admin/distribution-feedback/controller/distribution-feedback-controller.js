function DistributionFeedbackController($scope,$filter,$window,$location,GetFacilityDistributionNotifications,GeoDistrictTree) {


    $scope.startDate = null;
    $scope.endDate = null;

    $scope.changeDates = function () {
        $scope.startDate = $scope.periodStartDate;
        $scope.endDate = $scope.periodEndDate;
        $scope.distributions = [];
    };

    $scope.searchData= function () {
        $scope.showMessage = false;

        var endDate = (null === $scope.endDate) ? $filter('date')(new Date(), 'yyyy-MM-dd'):$scope.endDate;
       $scope.message = "Please Select District and at-least Start Date";
        if(($scope.startDate === null || $scope.startDate === undefined) || ($scope.districtId === undefined || $scope.districtId===null ) ){
            $scope.showMessage = true;
            return $scope.message;
        }else {
            var params = {districtId:parseInt($scope.districtId,10), startDate: $scope.startDate,endDate: endDate};
            GetFacilityDistributionNotifications.get(params, function (data) {
                $scope.distributions =data.notifications;
            });
        }

    };

    function getDataForDisplay(data) {
        if(data.regionId === null) {
            $scope.periodStartDate = undefined;
            $scope.periodEndDate = undefined;
            $scope.districtId=undefined;
            $scope.distributions = [];
            $scope.changeDates();
            $scope.districtId = data.id;
        }
    }

    $scope.getAllData = function(){


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

    $scope.openDistribution =  function (distribution) {

      var year = $filter('date')(new Date(), 'yyyy');
      var period = parseInt(distribution.periodid,10);
      var zone = parseInt(distribution.district_id,10);
      var url ='/public/pages/reports/main/index.html#/distribution-completeness?zone='+zone+'&program=82&year='+year+'&period='+period;
      $window.open(url, '_blank');

    };
}