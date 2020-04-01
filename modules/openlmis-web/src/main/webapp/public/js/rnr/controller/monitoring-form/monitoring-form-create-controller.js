function MonitoringFormController($dialog,$scope,report,$location,saveMonitoringReport, MonitoringReportSubmit){
  report.columnTemplate = [{name:'stockOnHand',label:'In stock at Council level'}, {name:'quantityRequested', label:'Requested'}];
  var oldCase = report.numberOfCumulativeCases;
  $scope.report  = report;
   console.log(report);

    $scope.report.oldCase  =

    $scope.changeCummulativeCases  = function(report) {
       console.log(oldCase);
       var totalCases  = parseInt(report.patientOnTreatment,10) + oldCase ;
       report.numberOfCumulativeCases = totalCases;

    };


    $scope.submit = function () {
       $scope.message = '';
       $scope.error = '';

     /*  if (!$scope.report.isValid($scope)) {
         $scope.error = 'You are attempting to save invalid values. Please make sure all information is valid. ';
         return;
       }*/

       var callBack = function (result) {
         if (result) {
           MonitoringReportSubmit.update($scope.report, function () {
             $scope.message = "Successiful Submitted";
             $location.path('/create-monitoring-form');
           });
         }
       };
       var options = {
         id: "confirmDialog",
         header: "Confirm Report Submission",
         body: "msg.question.submit.covid.confirmation"
       };
       OpenLmisDialog.newDialog(options, callBack, $dialog);
     };


  /*  $scope.submit = function () {
       $scope.report.status = "SUBMITTED";
       $scope.saveForm();
    };*/


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
          $location.path('/create-monitoring-form');
      };







  console.log(report);

}

MonitoringFormController.resolve = {

    report: function ($q, $timeout, $route, GetMonitoringReport) {
        var deferred = $q.defer();

        $timeout(function () {


         GetMonitoringReport.get({id:parseInt($route.current.pathParams.reportId, 10)},function (data) {
                deferred.resolve(data.report);
            });


        }, 100);
        return deferred.promise;
    }



};