function ApproveMonitoringFormController($scope, GetApprovalPendingIvds) {


    GetApprovalPendingIvds.get({programId: 1}, function (data) {
      $scope.pending = data.report;
    });




}