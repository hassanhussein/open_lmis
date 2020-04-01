function ApproveMonitoringFormDetailController($dialog,$scope,report, $location,MonitoringReportApprove){
report.editable = false;
$scope.report = report;


 $scope.approve = function () {
    var callBack = function (result) {
      if (result) {
        MonitoringReportApprove.update($scope.report, function () {
          $scope.message = "Approved Successfully";
          $location.path('/approve-monitoring-form');
        });
      }
    };
    var options = {
      id: "confirmDialog",
      header: "Confirm Approval of the report",
      body: "Confirm Approval of the report?"
    };
    OpenLmisDialog.newDialog(options, callBack, $dialog);
  };

  $scope.cancel = function () {

   $location.path('/approve-monitoring-form');
  };


}

ApproveMonitoringFormDetailController.resolve = {

report: function ($q, $timeout, $route, GetMonitoringReport) {
        var deferred = $q.defer();

        $timeout(function () {

         GetMonitoringReport.get({id:parseInt($route.current.params.id, 10)},function (data) {
                deferred.resolve(data.report);
            });


        }, 100);
        return deferred.promise;
    }



};