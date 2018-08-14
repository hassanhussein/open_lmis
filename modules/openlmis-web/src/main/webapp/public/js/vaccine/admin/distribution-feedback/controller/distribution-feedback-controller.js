function DistributionFeedbackController($scope,$window,$location,GetFacilityDistributionNotifications,GetDistributionByIdData) {

    GetFacilityDistributionNotifications.get({}, function (data) {
        $scope.distributions =data.notifications;
    });

  $scope.openDistribution =  function (distributionId) {

      var url = '/public/pages/vaccine/admin/distribution-feedback/index.html#/search/' +  parseInt(distributionId,10);
      $window.open(url, '_blank');

console.log(distributionId);

    }
}