function MonitoringFormController($scope,report,$location, saveMonitoringReport){
  report.columnTemplate = [{name:'stockOnHand',label:'In stock at Council level'}, {name:'quantityRequested', label:'Requested'}];
  $scope.report  = report;


    $scope.submit = function () {
       $scope.report.status = "SUBMITTED";
       $scope.saveForm();
    };


    $scope.saveForm = function () {
          $scope.showError = true;
          console.log($scope.report);
          //if($scope.saveMonitoringReport.$valid){
              saveMonitoringReport.update(
              {
                          id: $scope.report.id,
                          operation: "save"
              },
              $scope.report, function () {
                  // success
                  $scope.$parent.message = 'Your changes have been saved';
                  //$location.path('');
              }, function (data) {
                  // error
                  $scope.error = data.messages;
              });
         // }
      };

      $scope.cancel = function () {
          $location.path('');
      };







  console.log(report);

}

MonitoringFormController.resolve = {

    report: function ($q, $timeout, $route, GetInitiatedForm) {
        var deferred = $q.defer();

        $timeout(function () {


         GetInitiatedForm.get({zoneId:parseInt($route.current.pathParams.zoneId, 10), programId:parseInt($route.current.pathParams.program, 10)},function (data) {
                deferred.resolve(data.monitoringData);
            });


        }, 100);
        return deferred.promise;
    }



}